package com.mrbysco.particlemimicry.networking.handler;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import com.mrbysco.particlemimicry.networking.message.SetParticleDataPayload;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ServerPayloadHandler {
	public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

	public static ServerPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleParticleData(final SetParticleDataPayload syncData, final PlayPayloadContext context) {
		// Do something with the data, on the main thread
		context.workHandler().submitAsync(() -> {
					context.player().ifPresent(player -> {
						MinecraftServer server = player.getServer();
						var dimensionKey = ResourceKey.create(Registries.DIMENSION, syncData.dimension());
						ServerLevel level = server.getLevel(dimensionKey);

						if (level.getBlockEntity(syncData.pos()) instanceof ParticleEmitterBlockEntity blockEntity) {
							blockEntity.setData(player, syncData.particleType(), syncData.offset(), syncData.specialParameters(), syncData.delta(), syncData.speed(), syncData.count(), syncData.interval());
							blockEntity.refreshClient();
						}
					});
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("particlemimicry.networking.set_particle_data.failed", e.getMessage()));
					return null;
				});
	}
}
