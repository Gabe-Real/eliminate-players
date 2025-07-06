package gabereal.amogus.eliminate_players.mixin;

import gabereal.amogus.eliminate_players.EliminatePlayers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract void readCustomDataFromNbt(NbtCompound nbt);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!(source.getAttacker() instanceof PlayerEntity player)) return;
		if (!EliminatePlayers.bannedUuids.contains(player.getUuid())) return;

		Identifier entityId = Registries.ENTITY_TYPE.getId(this.getType());
		if (entityId != null && entityId.equals(new Identifier("masondecor", "soulmould"))) {
			NbtCompound nbt = new NbtCompound();
			this.writeCustomDataToNbt(nbt);

			UUID owner = null;
			if (nbt.contains("Owner")) {
				nbt.remove("ActionState");
				nbt.putInt("ActionState", 1);
				owner = nbt.getUuid("Owner");
				nbt.remove("Owner");
			}

			nbt.putUuid("Owner", player.getUuid());
			this.readCustomDataFromNbt(nbt);

			if (owner != null && getWorld().getPlayerByUuid(owner) != null) {
				LivingEntity living = (LivingEntity) (Object) this;
				if (living instanceof HostileEntity hostile) {
					hostile.setTarget(getWorld().getPlayerByUuid(owner));
				}
			}
		}
	}
}

