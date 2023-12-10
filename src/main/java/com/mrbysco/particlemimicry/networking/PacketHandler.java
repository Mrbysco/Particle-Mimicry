package com.mrbysco.particlemimicry.networking;

import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.networking.message.SetParticleEmitterDataMessage;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ParticleMimicry.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = 0;

	public static void init() {
		CHANNEL.registerMessage(id++, SetParticleEmitterDataMessage.class, SetParticleEmitterDataMessage::encode, SetParticleEmitterDataMessage::decode, SetParticleEmitterDataMessage::handle);
	}
}
