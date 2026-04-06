package com.mrbysco.particlemimicry;

import com.mrbysco.particlemimicry.networking.SetParticleDataPayload;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTabs;

public class ParticleMimicryFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		CommonClass.init();

		PayloadTypeRegistry.serverboundPlay().register(SetParticleDataPayload.ID, SetParticleDataPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SetParticleDataPayload.ID, (payload, context) -> {
			context.server().execute(() -> {
				if (context.player() instanceof ServerPlayer player) {
					MinecraftServer server = context.server();
					var dimensionKey = ResourceKey.create(Registries.DIMENSION, payload.dimension());
					ServerLevel level = server.getLevel(dimensionKey);
					CommonClass.handleSetParticle(level, player, payload);
				}
			});
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
			entries.accept(MimicryRegistry.PARTICLE_EMITTER.get());
		});
	}
}
