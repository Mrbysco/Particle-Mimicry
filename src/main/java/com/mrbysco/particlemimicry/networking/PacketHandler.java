package com.mrbysco.particlemimicry.networking;

import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.networking.handler.ServerPayloadHandler;
import com.mrbysco.particlemimicry.networking.message.SetParticleDataPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {
	public static void setupPackets(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(ParticleMimicry.MOD_ID);

		registrar.play(SetParticleDataPayload.ID, SetParticleDataPayload::new, handler -> handler
				.server(ServerPayloadHandler.getInstance()::handleParticleData));
	}
}
