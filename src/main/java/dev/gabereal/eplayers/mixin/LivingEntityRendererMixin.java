package dev.gabereal.eplayers.mixin;

import dev.gabereal.eplayers.EliminatePlayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "hasLabel", at = @At("HEAD"), cancellable = true)
    private void eliminateplayers$possiblyHideNameTag(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof AbstractClientPlayerEntity player &&
                EliminatePlayers.bannedUuids.contains(player.getUuid())) {

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.cameraEntity != null) {
                Vec3d cameraPos = client.cameraEntity.getCameraPosVec(1.0f);
                Vec3d playerPos = player.getPos().add(0, player.getStandingEyeHeight(), 0);
                Vec3d toPlayer = playerPos.subtract(cameraPos).normalize();
                Vec3d lookVec = client.cameraEntity.getRotationVec(1.0f).normalize();
                double dot = lookVec.dotProduct(toPlayer);

                if (dot > 0.95) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
