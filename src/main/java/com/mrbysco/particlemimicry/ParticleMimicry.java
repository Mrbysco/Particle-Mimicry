package com.mrbysco.particlemimicry;

import com.mojang.logging.LogUtils;
import com.mrbysco.particlemimicry.networking.PacketHandler;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ParticleMimicry.MOD_ID)
public class ParticleMimicry {
	public static final String MOD_ID = "particlemimicry";
	private static final Logger LOGGER = LogUtils.getLogger();

	public ParticleMimicry() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::setup);
		eventBus.addListener(this::buildContents);

		MimicryRegistry.BLOCKS.register(eventBus);
		MimicryRegistry.BLOCK_ENTITIES.register(eventBus);
		MimicryRegistry.ITEMS.register(eventBus);
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}

	private void buildContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			event.accept(MimicryRegistry.PARTICLE_EMITTER.get());
		}
	}
}
