package dev.gabereal.eplayers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.gabereal.eplayers.EliminatePlayers;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("deprecated")
@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow @Final private List<ServerPlayerEntity> players;

    @WrapWithCondition(
            method = "onPlayerConnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
            )
    )
    private boolean eplayers$playerLeave(PlayerManager manager, Text text, boolean bl, ClientConnection connection, ServerPlayerEntity player) {
        return !EliminatePlayers.bannedUuids.contains(player.getUuid());
    }

    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Z)V", at = @At("HEAD"), cancellable = true)
    private void eplayers$actuallyDontBroadcast(Text message, Function<ServerPlayerEntity, Text> playerMessageFactory, boolean overlay, CallbackInfo ci) {
        if (message.getContent() instanceof TranslatableTextContent translatable) {
            String key = translatable.getKey();
            Optional<Object> maybeText = Arrays.stream(translatable.getArgs())
                    .filter(arg -> arg instanceof Text)
                    .findFirst();

            if ("multiplayer.player.left".equals(key) &&
                    maybeText.isPresent() &&
                    ((Text) maybeText.get()).getString().contains("Mouthpiece")) {
                ci.cancel();
            }
        }
    }

    /*@Inject(method = "broadcast",
            at = @At("HEAD"), cancellable = true)
    private void eplayers$noBroadcasty(Text message, Predicate<ServerPlayerEntity> filter, @Nullable ServerPlayerEntity sender,
                                       MessageType.Parameters params, CallbackInfo ci) {
        if (message.getString().startsWith("/")) return;
        if (sender == null) return;
        if (EliminatePlayers.bannedUuids.contains(sender.getUuid())) {
            ci.cancel();
        }
    }*/

    // Filter banned players from getPlayerNames()
    @ModifyReturnValue(method = "getPlayerNames", at = @At("RETURN"))
    private String[] eplayers$dontGetAllPlayersArgType(String[] original) {
        List<String> filtered = new ArrayList<>();
        for (ServerPlayerEntity player : this.players) {
            if (!EliminatePlayers.bannedUuids.contains(player.getGameProfile().getId())) {
                filtered.add(player.getGameProfile().getName());
            }
        }
        return filtered.toArray(new String[0]);
    }
}
