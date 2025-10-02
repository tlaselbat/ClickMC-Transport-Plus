package com.tabletmc.transport_plus;

import com.tabletmc.transport_plus.item.ModItems;
import com.tabletmc.transport_plus.net.ServerNetworking;
import com.tabletmc.transport_plus.net.attach.PlayerAttachments;


public class ModInitializer implements net.fabricmc.api.ModInitializer {

	@Override
	public void onInitialize()
	{
		ModItems.registerModItems();
		// Initialize attachments (registers types)
		PlayerAttachments.init();
		ServerNetworking.init();
		ModConstants.LOGGER.info("Travel System Revamp Initialized.");


		}

	//mTODO: Iron door key
	//TODO: shulker charm
	//TODO: summon mount item
	//TODO:
	//TODO:
	//TODO:
	//TODO:
	//TODO:

}


