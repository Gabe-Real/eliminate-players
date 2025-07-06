package gabereal.amogus.eliminate_players.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
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
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Shadow @Final private List<ServerPlayerEntity> players;

	@WrapWithCondition(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastSystemMessage(Lnet/minecraft/text/Text;Z)V"))
	private boolean eplayers$playerLeave(PlayerManager manager, Text text, boolean bl, ClientConnection connect, ServerPlayerEntity player) {
		return !EliminatePlayers.bannedUuids.contains(player.getUuid());
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
		for (int i = 0; i < this.players.size(); ++i) {
			if (!EliminatePlayers.bannedUuids.contains(this.players.get(i).getGameProfile().getId())) {
				original[i] = this.players.get(i).getGameProfile().getName();
			}
		}
		return original;
	}
}
