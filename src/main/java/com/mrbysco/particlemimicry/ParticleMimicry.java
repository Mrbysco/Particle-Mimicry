package com.mrbysco.particlemimicry;

import com.mojang.logging.LogUtils;
import com.mrbysco.particlemimicry.networking.PacketHandler;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(ParticleMimicry.MOD_ID)
public class ParticleMimicry {
	public static final String MOD_ID = "particlemimicry";
	private static final Logger LOGGER = LogUtils.getLogger();

	public ParticleMimicry(IEventBus eventBus) {
		eventBus.addListener(this::buildContents);
		eventBus.addListener(PacketHandler::setupPackets);

		MimicryRegistry.BLOCKS.register(eventBus);
		MimicryRegistry.BLOCK_ENTITIES.register(eventBus);
		MimicryRegistry.ITEMS.register(eventBus);
	}

	private void buildContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			event.accept(MimicryRegistry.PARTICLE_EMITTER.get());
		}
	}
}
