package dev.gabereal.eplayers.mixin;

import dev.gabereal.eplayers.EliminatePlayers;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    private static final Identifier GHOST_SKIN_WIDE = new Identifier("eliminateplayers", "entity/removed_wide.png");
    private static final Identifier GHOST_SKIN_SLIM = new Identifier("eliminateplayers", "entity/removed.png");

    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    private void eliminateplayers$useGhostSkin(CallbackInfoReturnable<Identifier> cir) {
        AbstractClientPlayerEntity self = (AbstractClientPlayerEntity) (Object) this;

        if (EliminatePlayers.bannedUuids.contains(self.getUuid())) {
            PlayerListEntry entry = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(self.getUuid());

            if (entry != null && entry.getModel().equals("slim")) {
                cir.setReturnValue(GHOST_SKIN_SLIM);
            } else {
                cir.setReturnValue(GHOST_SKIN_WIDE);
            }
        }
    }
}
