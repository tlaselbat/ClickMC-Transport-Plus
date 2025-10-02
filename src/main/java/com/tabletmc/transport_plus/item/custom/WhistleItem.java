package com.tabletmc.transport_plus.item.custom;
import com.tabletmc.transport_plus.net.payload.StringPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import static com.tabletmc.transport_plus.ModConstants.summonCooldown;

public class WhistleItem extends Item {
    public WhistleItem(Settings settings) {
        super(settings);
    }
    // 1.21.8: Item#use returns ActionResult instead of TypedActionResult<ItemStack>
    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        // Client-side: send action to server (C2S)
        if (world.isClient) {
            String action = player.hasVehicle() ? "dismount" : "summon";
            ClientPlayNetworking.send(new StringPayload(action));
            return ActionResult.SUCCESS;
        }

        // Server receives via ServerNetworking.registerGlobalReceiver (C2S), no need to send S2C here
        return ActionResult.SUCCESS_SERVER;
    }
}
