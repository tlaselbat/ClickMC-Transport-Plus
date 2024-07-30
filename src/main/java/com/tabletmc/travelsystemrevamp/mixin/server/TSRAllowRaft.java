package com.tabletmc.travelsystemrevamp.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatEntity.class)
public abstract class TSRAllowRaft {
    @ModifyReturnValue(method = "isSmallerThanBoat", at = @At("RETURN"))
    private boolean allowRaft(boolean original, @Local(argsOnly = true)Entity entity){
        if (entity instanceof AbstractHorseEntity && hb$thiz().getVariant() == BoatEntity.Type.BAMBOO)
            return true;
        return original;
    }

    @Unique
    private BoatEntity hb$thiz() {
        return ((BoatEntity)(Object)this);
    }
}
