package com.mrbysco.particlemimicry.networking.message;

import com.mrbysco.particlemimicry.ParticleMimicry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetParticleDataPayload(BlockPos pos, ResourceLocation dimension, String particleType, String offset,
									 String specialParameters,
									 String delta, String speed, String count,
									 String interval) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(ParticleMimicry.MOD_ID, "set_particle_data");

	public SetParticleDataPayload(final FriendlyByteBuf packetBuffer) {
		this(packetBuffer.readBlockPos(), packetBuffer.readResourceLocation(), packetBuffer.readUtf(),
				packetBuffer.readUtf(), packetBuffer.readUtf(), packetBuffer.readUtf(),
				packetBuffer.readUtf(), packetBuffer.readUtf(), packetBuffer.readUtf()
		);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(dimension);
		buf.writeUtf(particleType);
		buf.writeUtf(offset);
		buf.writeUtf(specialParameters);
		buf.writeUtf(delta);
		buf.writeUtf(speed);
		buf.writeUtf(count);
		buf.writeUtf(interval);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
