package com.mrbysco.particlemimicry.networking.message;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class SetParticleEmitterDataMessage {
	public BlockPos pos;
	public ResourceLocation dimension;
	public String particleType, offset, specialParameters, delta, speed, count, interval;

	public SetParticleEmitterDataMessage(BlockPos pos, ResourceLocation dimension, String particleType, String offset, String specialParameters,
										 String delta, String speed, String count, String interval) {
		this.pos = pos;
		this.dimension = dimension;
		this.particleType = particleType.trim();
		this.offset = offset.trim();
		this.specialParameters = specialParameters.trim();
		this.delta = delta.trim();
		this.speed = speed.trim();
		this.count = count.trim();
		this.interval = interval.trim();
	}

	public void encode(FriendlyByteBuf buf) {
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

	public static SetParticleEmitterDataMessage decode(final FriendlyByteBuf packetBuffer) {
		return new SetParticleEmitterDataMessage(packetBuffer.readBlockPos(), packetBuffer.readResourceLocation(), packetBuffer.readUtf(),
				packetBuffer.readUtf(), packetBuffer.readUtf(), packetBuffer.readUtf(),
				packetBuffer.readUtf(), packetBuffer.readUtf(), packetBuffer.readUtf()
		);
	}

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				MinecraftServer server = ctx.getSender().getServer();
				var dimensionKey = ResourceKey.create(Registries.DIMENSION, dimension);
				ServerLevel level = server.getLevel(dimensionKey);

				if (level.getBlockEntity(pos) instanceof ParticleEmitterBlockEntity blockEntity) {
					blockEntity.setData(particleType, offset, specialParameters, delta, speed, count, interval);
					blockEntity.refreshClient();
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
