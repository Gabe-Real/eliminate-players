package dev.gabereal.eplayers;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

public class EliminatePlayers implements ModInitializer {
	public static final String MOD_ID = "eliminateplayers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ArrayList<UUID> bannedUuids = new ArrayList<>();

	@Override
	public void onInitialize() {
		bannedUuids.add(UUID.fromString("d0815131-c51a-4831-b973-f69da01e6326")); // Fix later
	}
}