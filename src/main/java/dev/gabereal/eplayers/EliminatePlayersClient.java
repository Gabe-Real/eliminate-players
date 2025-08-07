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
            int count = buf.readInt();
            List<String> serverUuids = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                serverUuids.add(buf.readString());
            }

            boolean matches = compareConfigs(serverUuids);

            PacketByteBuf response = PacketByteBufs.create();
            response.writeBoolean(matches);

            ClientPlayNetworking.send(EliminatePlayers.CONFIG_RESPONSE_PACKET, response);
        });
    }

    private boolean compareConfigs(List<String> serverUuids) {
        List<String> clientUuids = new ArrayList<>();
        for (UUID uuid : EliminatePlayers.bannedUuids) {
            clientUuids.add(uuid.toString());
        }

        if (serverUuids.size() != clientUuids.size()) {
            return false;
        }

        for (String serverUuid : serverUuids) {
            if (!clientUuids.contains(serverUuid)) {
                return false;
            }
        }

        return true;
    }
}