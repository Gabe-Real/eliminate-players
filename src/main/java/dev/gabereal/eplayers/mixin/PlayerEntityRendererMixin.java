package dev.gabereal.eplayers.mixin;

import dev.gabereal.eplayers.EliminatePlayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void eliminateplayers$maybeHideGhost(
            AbstractClientPlayerEntity player,
            float f,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (!EliminatePlayers.bannedUuids.contains(player.getUuid())) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.cameraEntity == null) return;

        Vec3d cameraPos = client.cameraEntity.getCameraPosVec(tickDelta);
        Vec3d playerPos = player.getPos().add(0, player.getStandingEyeHeight(), 0);

        Vec3d toPlayer = playerPos.subtract(cameraPos).normalize();
        Vec3d lookVec = client.cameraEntity.getRotationVec(tickDelta).normalize();

        double dot = lookVec.dotProduct(toPlayer);

        if (dot > 0.95) {
            ci.cancel();
        }
    }
}