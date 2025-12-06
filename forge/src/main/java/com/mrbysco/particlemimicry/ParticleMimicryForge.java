package com.mrbysco.particlemimicry;


import com.mrbysco.particlemimicry.networking.PacketHandler;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Constants.MOD_ID)
public class ParticleMimicryForge {

	public ParticleMimicryForge(IEventBus eventBus) {
		CommonClass.init();

		eventBus.addListener(this::buildContents);
		eventBus.addListener(PacketHandler::setupPackets);
	}

	private void buildContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			event.accept(MimicryRegistry.PARTICLE_EMITTER.get());
		}
	}
}