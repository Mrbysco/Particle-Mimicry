package com.mrbysco.particlemimicry.platform;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import com.mrbysco.particlemimicry.platform.services.IPlatformHelper;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public BlockEntityType<ParticleEmitterBlockEntity> createBlockEntityType() {
		return BlockEntityType.Builder.of(ParticleEmitterBlockEntity::new, MimicryRegistry.PARTICLE_EMITTER.get()).build();
	}

	@Override
	public void sendPayloadToServer(CustomPacketPayload payload) {
		ClientPlayNetworking.send(payload);
	}
}
