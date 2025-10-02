package com.tabletmc.transport_plus.net;

import com.tabletmc.transport_plus.ModInitializer;
import com.tabletmc.transport_plus.impl.ServerPlayerEntityImpl;
import com.tabletmc.transport_plus.net.payload.StringPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;


public class ServerNetworking extends ModInitializer {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(StringPayload.PACKET_ID, StringPayload.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(StringPayload.PACKET_ID, (StringPayload handler, ServerPlayNetworking.Context context) -> {
            if (handler.stringPayload().equals("summon")) {
                ((ServerPlayerEntityImpl) context.player()).summonMount(true);
                context.player().sendMessage(Text.literal("Horse summoned!"));
            } else if (handler.stringPayload().equals("dismount")) {
                ((ServerPlayerEntityImpl) context.player()).dismountHorse(false);
                context.player().sendMessage(Text.literal("Horse dismounted!"));
            }
        });
    }
}