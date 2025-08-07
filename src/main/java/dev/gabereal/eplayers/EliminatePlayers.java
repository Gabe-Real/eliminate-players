package dev.gabereal.eplayers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EliminatePlayers implements ModInitializer {
	public static final String MOD_ID = "eliminateplayers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier CONFIG_CHECK_PACKET = new Identifier(MOD_ID, "config_check");
	public static final Identifier CONFIG_RESPONSE_PACKET = new Identifier(MOD_ID, "config_response");

	public static final ArrayList<UUID> bannedUuids = new ArrayList<>();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("eliminateplayers.json");

	@Override
	public void onInitialize() {
		loadConfig();
		LOGGER.info("Loaded {} banned UUIDs on {}", bannedUuids.size(),
				FabricLoader.getInstance().getEnvironmentType());

		if (FabricLoader.getInstance().getEnvironmentType().name().equals("SERVER")) {
			setupServerHandlers();
		}
	}

	private void setupServerHandlers() {
		// When a player joins, request their config (with delay to ensure they're ready)
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			LOGGER.info("Player joined, will check config in 1 second: {}", player.getName().getString());

			// Wait 1 second before sending packet to ensure player is fully connected
			server.execute(() -> {
				try {
					Thread.sleep(1000); // 1 second delay

					if (!player.networkHandler.isConnectionOpen()) {
						return; // Player disconnected
					}

					LOGGER.info("Sending config check to player: {}", player.getName().getString());

					// Send server's banned UUIDs to client for comparison
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(bannedUuids.size());
					for (UUID uuid : bannedUuids) {
						buf.writeString(uuid.toString());
					}

					ServerPlayNetworking.send(player, CONFIG_CHECK_PACKET, buf);

					// Give client 15 seconds to respond (total)
					Thread.sleep(15000);
					if (player.networkHandler.isConnectionOpen()) {
						// If still connected after timeout, assume they don't have the mod
						kickPlayerWithInstructions(player, "No response to config check - mod may not be installed");
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (Exception e) {
					LOGGER.error("Error during config check for player {}: {}", player.getName().getString(), e.getMessage());
				}
			});
		});

		// Handle client responses
		ServerPlayNetworking.registerGlobalReceiver(CONFIG_RESPONSE_PACKET, (server, player, handler, buf, responseSender) -> {
			boolean configMatches = buf.readBoolean();

			if (!configMatches) {
				server.execute(() -> kickPlayerWithInstructions(player, "Config mismatch - banned UUIDs don't match server"));
			} else {
				LOGGER.info("Player {} has correct config - allowing connection", player.getName().getString());
			}
		});
	}

	private void kickPlayerWithInstructions(ServerPlayerEntity player, String reason) {
		StringBuilder instructions = new StringBuilder();
		instructions.append("§c§lCONFIG MISMATCH§r\n\n");
		instructions.append("§eYou need to update your EliminatePlayers config!\n\n");
		instructions.append("§6Required banned UUIDs:\n");

		for (UUID uuid : bannedUuids) {
			instructions.append("§7- ").append(uuid.toString()).append("\n");
		}

		instructions.append("\n§eSteps to fix:\n");
		instructions.append("§71. Go to your .minecraft/config/ folder\n");
		instructions.append("§72. Open eliminateplayers.json\n");
		instructions.append("§73. Update the bannedUuids list to match above\n");
		instructions.append("§74. Restart your client and rejoin\n\n");
		instructions.append("§cReason: ").append(reason);

		player.networkHandler.disconnect(Text.literal(instructions.toString()));
		LOGGER.info("Kicked player {} due to config mismatch: {}", player.getName().getString(), reason);
	}

	private void loadConfig() {
		if (!Files.exists(CONFIG_PATH)) {
			createDefaultConfig();
			return;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			Config config = GSON.fromJson(reader, Config.class);

			if (config != null && config.bannedUuids != null) {
				for (String uuidString : config.bannedUuids) {
					try {
						bannedUuids.add(UUID.fromString(uuidString));
					} catch (IllegalArgumentException e) {
						LOGGER.warn("Invalid UUID in config: {}", uuidString);
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("Failed to load config", e);
			createDefaultConfig();
		}
	}

	private void createDefaultConfig() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());

			Config defaultConfig = new Config();
			defaultConfig.bannedUuids = List.of("d0815131-c51a-4831-b973-f69da01e6326");

			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(defaultConfig, writer);
			}

			// Load the default config
			bannedUuids.add(UUID.fromString("d0815131-c51a-4831-b973-f69da01e6326"));

			LOGGER.info("Created default config at {}", CONFIG_PATH);
		} catch (IOException e) {
			LOGGER.error("Failed to create default config", e);
		}
	}

	public static class Config {
		public List<String> bannedUuids = new ArrayList<>();
	}
}