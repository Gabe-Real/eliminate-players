package gabereal.amogus.eliminate_players.mixin;

import gabereal.amogus.eliminate_players.EliminatePlayers;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayerNetworkHandlerMixin {

	@Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
	private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
		ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
		UUID playerUuid = player.getUuid();

		if (EliminatePlayers.bannedUuids.contains(playerUuid)) {
			ci.cancel();
		}
	}
}
