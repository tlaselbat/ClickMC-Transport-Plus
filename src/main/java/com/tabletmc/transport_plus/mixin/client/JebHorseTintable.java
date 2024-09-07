package com.tabletmc.transport_plus.mixin.client;

import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.tabletmc.transport_plus.utils.RenderUtils.isJeb;

@Mixin(value = HorseEntityRenderer.class, priority = 960)
public class JebHorseTintable {
    @Redirect(method = "getTexture(Lnet/minecraft/entity/passive/HorseEntity;)Lnet/minecraft/util/Identifier;",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/HorseEntity;getVariant()Lnet/minecraft/entity/passive/HorseColor;"))
    HorseColor jebHorseTintable(HorseEntity instance){
        if (isJeb(instance)){
            return HorseColor.WHITE;
        }
        return instance.getVariant();
    }
}
