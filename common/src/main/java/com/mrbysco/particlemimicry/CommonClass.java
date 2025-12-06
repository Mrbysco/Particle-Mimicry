package com.mrbysco.particlemimicry;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import com.mrbysco.particlemimicry.networking.SetParticleDataPayload;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class CommonClass {

	public static void init() {
		MimicryRegistry.loadClass();
	}

	public static void handleSetParticle(ServerLevel level, ServerPlayer player, SetParticleDataPayload payload) {
		if (level.getBlockEntity(payload.pos()) instanceof ParticleEmitterBlockEntity blockEntity) {
			blockEntity.setData(player, payload.particleType(), payload.offset(),
					payload.specialParameters(), payload.delta(), payload.speed(),
					payload.count(), payload.interval());
			blockEntity.refreshClient();
		}
	}
}