package com.mrbysco.particlemimicry.platform;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import com.mrbysco.particlemimicry.platform.services.IPlatformHelper;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgePlatformHelper implements IPlatformHelper {


	@Override
	public BlockEntityType<ParticleEmitterBlockEntity> createBlockEntityType() {
		return BlockEntityType.Builder.of(ParticleEmitterBlockEntity::new, MimicryRegistry.PARTICLE_EMITTER.get()).build(null);
	}

	@Override
	public void sendPayloadToServer(CustomPacketPayload payload) {
		PacketDistributor.sendToServer(payload);
	}
}
