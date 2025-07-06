package gabereal.amogus.eliminate_players.mixin;

import gabereal.amogus.eliminate_players.EliminatePlayers;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
	private static final Identifier GHOST_SKIN = new Identifier("eliminate_players", "textures/entity/removed.png");

	@Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
	private void eliminateplayers$useGhostSkin(CallbackInfoReturnable<Identifier> cir) {
		AbstractClientPlayerEntity self = (AbstractClientPlayerEntity) (Object) this;

		if (EliminatePlayers.bannedUuids.contains(self.getUuid())) {
			cir.setReturnValue(GHOST_SKIN);
		}
	}
}
