package com.tabletmc.transport_plus.mixin.server;

import com.tabletmc.transport_plus.impl.EntityMixinImpl;
import com.tabletmc.transport_plus.impl.NetheriteArmorImpl;
import com.tabletmc.transport_plus.impl.ServerPlayerEntityImpl;
import com.tabletmc.transport_plus.net.attach.PlayerAttachments;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.entity.SpawnReason;
import net.minecraft.predicate.NbtPredicate;
import java.util.function.Function;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin implements ServerPlayerEntityImpl {
    @Shadow public abstract void sendMessage(Text message, boolean actionBar);
    @Unique ServerPlayerMixin playerEntity = this;
    @Unique AnimalEntity storedHorse = playerEntity.getHorse();
    @Unique private boolean tpWasRiding = false;
    /**
     * Summons a horse entity for the player, optionally mounting the player on the horse.
     *
     * @param mountPlayer Whether to mount the player on the horse
     */
    public void summonMount(boolean mountPlayer) {
        if (storedHorse == null) {
            // Attempt to reconstruct from attachment persistence
            NbtCompound saved = ((AttachmentTarget)(Object)this).getAttached(PlayerAttachments.STORED_HORSE);
            if (saved != null && !saved.isEmpty()) {
                Entity loaded = EntityType.loadEntityWithPassengers(
                        saved,
                        ((ServerPlayerEntity) (Object) this).getWorld(),
                        SpawnReason.LOAD,
                        Function.identity()
                );
                if (loaded instanceof AnimalEntity animal) {
                    storedHorse = animal;
                }
            }
            if (storedHorse == null) {
                sendMessage(Text.of("No Horse Found!"), true);
                return;
            }
        }

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        storedHorse.fallDistance = player.fallDistance;
        storedHorse.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());

        if (mountPlayer) {
            player.startRiding(storedHorse, true);
        }

        storedHorse.setVelocity(player.getVelocity());
        player.getWorld().spawnEntity(storedHorse);

        if (!mountPlayer) {
            storedHorse.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0, false, false));
        }
    }

    public void dismountHorse(boolean mountPlayer) {
        if (storedHorse == null) {
            return; // No horse to dismount
        }
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;


        // Remove the horse from the player's riding entity
        if (player.getRootVehicle() != null) {
            player.stopRiding();
            player.getRootVehicle().dismountVehicle();
        }

    }

    /**
     * Stores a horse entity in the player's data.
     *
     * @param mount The horse entity to store.
     */
    @Override
    public void storeMount(AnimalEntity mount) {
        if (storedHorse != null && !storedHorse.getUuid().equals(mount.getUuid())) {
            sendMessage(Text.of("[transport_plus]: Replaced Old Horse"), false);
            summonMount(false);
        }

        if (mount.getRemovalReason() != null) {
            storedHorse = null;
            // Clear persistence
            ((AttachmentTarget)(Object)this).setAttached(PlayerAttachments.STORED_HORSE, new NbtCompound());
        } else {
            storedHorse = mount;
            // Persist to attachment
            NbtCompound tag = NbtPredicate.entityToNbt(storedHorse);
            ((AttachmentTarget)(Object)this).setAttached(PlayerAttachments.STORED_HORSE, tag);
        }
    }


//    @Override
//    public void storeMount(LivingEntity mount) {
//        if (storedMount != null && !storedMount.getUuid().equals(mount.getUuid())) {
//            sendMessage(Text.of("[transport_plus]: Replaced Old Mount"), false);
//            summonMount(false);
//        }
//
//        if (mount.getType() == EntityType.HORSE || mount.getType() == EntityType.CAMEL) {
//            if (((TameableEntity) mount).getRemovalReason() != null) {
//                storedMount = null;
//            } else {
//                storedMount = mount;
//            }
//        }
//    }




    // NBT persistence removed for 1.21.8. Will migrate to Fabric Data Attachment API.

    /**
     * Injects a method that is called when the player starts riding an entity.
     *
     * @param entity The entity that the player is riding.
     * @param force  Whether the player is forced to start riding.
     * @param cir    The callback info for the method injection.
     */
    @Inject(method = "startRiding", at = @At("TAIL"), require = 0)
    public void startRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof AnimalEntity horse && ((NetheriteArmorImpl) horse).hasNetheriteArmor()) {
            storeMount(horse);
        }
    }

    // stopRiding is now handled in EntityMixin to avoid descriptor issues on ServerPlayerEntity.

    // Replace stopRiding hook with a stable tick-based transition detector to avoid recursion
    @Inject(method = "tick", at = @At("TAIL"), require = 0)
    private void transport_plus$afterTick(CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        boolean nowRiding = self.hasVehicle();
        if (tpWasRiding && !nowRiding) {
            // We just dismounted: perform cleanup without calling stopRiding again
            if (storedHorse != null) {
                if (!((NetheriteArmorImpl) storedHorse).hasNetheriteArmor() || storedHorse.getRemovalReason() != null) {
                    storedHorse = null;
                } else {
                    storedHorse.remove(Entity.RemovalReason.DISCARDED);
                    ((EntityMixinImpl) storedHorse).undoRemove();
                    self.fallDistance = storedHorse.fallDistance;
                }
            }
        }
        tpWasRiding = nowRiding;
    }
}
