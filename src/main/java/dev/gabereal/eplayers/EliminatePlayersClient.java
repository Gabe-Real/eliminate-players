package dev.gabereal.eplayers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EliminatePlayersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(EliminatePlayers.CONFIG_CHECK_PACKET, (client, handler, buf, responseSender) -> {
            EliminatePlayers.LOGGER.info("Received config check from server");

            int count = buf.readInt();
            List<String> serverUuids = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                serverUuids.add(buf.readString());
            }

            EliminatePlayers.LOGGER.info("Server sent {} UUIDs", count);

            if (EliminatePlayers.bannedUuids.isEmpty()) {
                EliminatePlayers.LOGGER.warn("Client config appears to be empty, this will cause a mismatch");
            }

            boolean matches = compareConfigs(serverUuids);

            EliminatePlayers.LOGGER.info("Config comparison result: {}", matches ? "MATCH" : "MISMATCH");

            PacketByteBuf response = PacketByteBufs.create();
            response.writeBoolean(matches);

            ClientPlayNetworking.send(EliminatePlayers.CONFIG_RESPONSE_PACKET, response);
        });
    }

    private boolean compareConfigs(List<String> serverUuids) {
        List<String> clientUuids = new ArrayList<>();
        for (UUID uuid : EliminatePlayers.bannedUuids) {
            clientUuids.add(uuid.toString().toLowerCase());
        }

        List<String> normalizedServerUuids = new ArrayList<>();
        for (String uuid : serverUuids) {
            normalizedServerUuids.add(uuid.toLowerCase());
        }

        EliminatePlayers.LOGGER.info("Client UUIDs: {}", clientUuids);
        EliminatePlayers.LOGGER.info("Server UUIDs: {}", normalizedServerUuids);

        if (normalizedServerUuids.size() != clientUuids.size()) {
            EliminatePlayers.LOGGER.warn("Size mismatch - Server: {}, Client: {}",
                    normalizedServerUuids.size(), clientUuids.size());
            return false;
        }

        for (String serverUuid : normalizedServerUuids) {
            if (!clientUuids.contains(serverUuid)) {
                EliminatePlayers.LOGGER.warn("Server UUID not found in client config: {}", serverUuid);
                return false;
            }
        }

        EliminatePlayers.LOGGER.info("Config comparison successful - UUIDs match!");
        return true;
    }
}