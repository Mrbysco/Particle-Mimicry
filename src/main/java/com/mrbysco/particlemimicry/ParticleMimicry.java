package com.mrbysco.particlemimicry;

import com.mojang.logging.LogUtils;
import com.mrbysco.particlemimicry.networking.PacketHandler;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ParticleMimicry.MOD_ID)
public class ParticleMimicry {
	public static final String MOD_ID = "particlemimicry";
	private static final Logger LOGGER = LogUtils.getLogger();

	public ParticleMimicry() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::setup);

		MimicryRegistry.BLOCKS.register(eventBus);
		MimicryRegistry.BLOCK_ENTITIES.register(eventBus);
		MimicryRegistry.ITEMS.register(eventBus);
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}
}
