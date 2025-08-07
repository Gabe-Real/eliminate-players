package dev.gabereal.eplayers.mixin;

import dev.gabereal.eplayers.EliminatePlayers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

    public LivingEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "damage", at = @At("HEAD"))
    private void eplayers$giveMeYourSoulmoldsBish(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // "nothing personal, kid"
        if (!(source.getAttacker() instanceof PlayerEntity player)) return;
        if (!EliminatePlayers.bannedUuids.contains(player.getUuid())) return;

        var typeId = Registries.ENTITY_TYPE.getId(this.getType());if (typeId != null && typeId.getPath().contains("soulmould")) {
            NbtCompound nbt = new NbtCompound();
            this.writeCustomDataToNbt(nbt);
            UUID owner = null;
            if(nbt.contains("Owner")) {
                nbt.remove("ActionState");
                nbt.putInt("ActionState", 1);
                owner = nbt.getUuid("Owner");
                nbt.remove("Owner");
            }
            nbt.putUuid("Owner", player.getUuid());
            this.readCustomDataFromNbt(nbt);
            if(this.getWorld().getPlayerByUuid(owner) != null && ((LivingEntity)(Object)this) instanceof HostileEntity h) {
                h.setTarget(this.getWorld().getPlayerByUuid(owner));
            }

        }
    }
}