package com.mrbysco.particlemimicry.platform.services;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IPlatformHelper {

	/**
	 * Creates a BlockEntityType for the ParticleEmitterBlockEntity.
	 * @return the block entity type
	 */
	BlockEntityType<ParticleEmitterBlockEntity> createBlockEntityType();

    /**
     * Sends a custom packet payload to the server.
     *
     * @param payload The payload to send to the server.
     */
    void sendPayloadToServer(CustomPacketPayload payload);

}
