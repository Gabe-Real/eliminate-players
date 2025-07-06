/*package gabereal.amogus.eliminate_players.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gabereal.amogus.eliminate_players.EliminatePlayers;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.component.TranslatableComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Shadow @Final private List<ServerPlayerEntity> players;

	// Inject directly into onPlayerConnect to suppress the join message
	@Inject(method = "onPlayerConnect", at = @At(value = "HEAD"))
	private void eplayers$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		if (EliminatePlayers.bannedUuids.contains(player.getUuid())) {
			// Remove join message by setting player silently, or suppress it in another mixin
			player.sendMessage(Text.literal("You joined silently."), false); // Optional
		}
	}

	@Inject(method = "broadcastSystemMessage", at = @At("HEAD"), cancellable = true)
	private void eplayers$actuallyDontBroadcast(Text message, boolean overlay, CallbackInfo ci) {
		if (message instanceof TranslatableComponent transCon) {
			String key = transCon.getKey();
			Optional<Object> texts = Arrays.stream(transCon.getArgs()).filter(obj -> obj instanceof Text).findFirst();
			if ("multiplayer.player.left".equals(key) && texts.isPresent() && texts.get().toString().contains("Mouthpiece")) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
	private void eplayers$noBroadcasty(net.minecraft.network.message.SignedChatMessage message, @Nullable ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci) {
		Text rawMessage = Text.of(message.getContent());
		if (rawMessage.getString().startsWith("/")) return;
		if (sender == null) return;
		if (EliminatePlayers.bannedUuids.contains(sender.getUuid())) {
			ci.cancel();
		}
	}

	@ModifyReturnValue(method = "getPlayerNames", at = @At("RETURN"))
	private String[] eplayers$dontGetAllPlayersArgType(String[] original) {
		// Filter out banned players from the returned player names
		return players.stream()
			.filter(player -> !EliminatePlayers.bannedUuids.contains(player.getUuid()))
			.map(player -> player.getGameProfile().getName())
			.toArray(String[]::new);
	}
}*/
