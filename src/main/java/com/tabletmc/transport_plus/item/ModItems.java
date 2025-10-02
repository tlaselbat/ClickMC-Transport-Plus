package com.tabletmc.transport_plus.item;

import com.tabletmc.transport_plus.ModConstants;
import com.tabletmc.transport_plus.item.custom.WhistleItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

// import static net.minecraft.item.ArmorMaterials.NETHERITE; // 1.21.8 mapping changes; will re-add with correct symbol


public class ModItems  {

//TODO: add trinket item for horse storage

    // 1.21.8: Component-based equestrian armor using EQUIPPABLE on BODY (animal armor)
    public static final Item NETHERITE_HORSE_ARMOR = registerModItems("netherite_horse_armor",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, ModConstants.Id("netherite_horse_armor")))
                    .equippable(EquipmentSlot.BODY)
                    .maxCount(1)
                    .fireproof()
                    .translationKey("item.transport_plus.netherite_horse_armor")
            )
    );
	public static final Item WHISTLE = registerModItems("whistle",
			new WhistleItem(
					new Item.Settings()
						.registryKey(RegistryKey.of(RegistryKeys.ITEM, ModConstants.Id("whistle")))
						.maxCount(1)
						.fireproof()
						.translationKey("item.transport_plus.whistle")
			)
	);

    private static void addItemsToItemGroup(FabricItemGroupEntries entries) {
		// Add the Netherite Horse Armor item to the item group
        entries.add(NETHERITE_HORSE_ARMOR);
		// Add the Whistle item to the item group
		entries.add(WHISTLE);
	}

	private static Item registerModItems(String itemName, Item item) {
		// Register the item with the game's registry
		return Registry.register(Registries.ITEM, ModConstants.Id(itemName), item);
	}

	public static void registerModItems() {
		// Modify the TOOLS item group to include our mod's items
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
				.register(ModItems::addItemsToItemGroup);
	}
}