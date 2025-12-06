package com.mrbysco.particlemimicry.platform;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import com.mrbysco.particlemimicry.platform.services.IPlatformHelper;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class NeoForgePlatformHelper implements IPlatformHelper {


	@Override
	public BlockEntityType<ParticleEmitterBlockEntity> createBlockEntityType() {
		return new BlockEntityType<>(ParticleEmitterBlockEntity::new, MimicryRegistry.PARTICLE_EMITTER.get());
	}

	@Override
	public void sendPayloadToServer(CustomPacketPayload payload) {
		ClientPacketDistributor.sendToServer(payload);
	}
}
