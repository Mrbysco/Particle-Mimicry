package com.mrbysco.particlemimicry.networking.handler;

import com.mrbysco.particlemimicry.CommonClass;
import com.mrbysco.particlemimicry.networking.SetParticleDataPayload;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
	public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

	public static ServerPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleParticleData(final SetParticleDataPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
						MinecraftServer server = context.player().level().getServer();
						var dimensionKey = ResourceKey.create(Registries.DIMENSION, payload.dimension());
						ServerLevel level = server.getLevel(dimensionKey);
						CommonClass.handleSetParticle(level, player, payload);
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("particlemimicry.networking.set_particle_data.failed", e.getMessage()));
					return null;
				});
	}
}
