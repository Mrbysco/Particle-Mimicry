package com.mrbysco.particlemimicry.datagen.assets;

import com.mrbysco.particlemimicry.Constants;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class MimicryLanguageProvider extends LanguageProvider {
	public MimicryLanguageProvider(PackOutput output) {
		super(output, Constants.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addBlock(MimicryRegistry.PARTICLE_EMITTER, "Particle Emitter");

		add("particlemimicry.particle", "Particle Type");
		add("particlemimicry.particle.tooltip", "The ID of the particle");
		add("particlemimicry.setParticle", "Set Particle Type");
		add("particlemimicry.offset", "Offset");
		add("particlemimicry.offset.tooltip", "The offset of the particle from the emitter's position");
		add("particlemimicry.specialParameters", "Special Parameters");
		add("particlemimicry.specialParameters.tooltip", "Special parameters for the particle, such as color or size");
		add("particlemimicry.delta", "Delta");
		add("particlemimicry.delta.tooltip", "The movement delta positions of the particle, dictating which way it moves");
		add("particlemimicry.speed", "Speed");
		add("particlemimicry.speed.tooltip", "The multiplier for the particles delta");
		add("particlemimicry.count", "Count");
		add("particlemimicry.count.tooltip", "The number of particles to spawn each time");
		add("particlemimicry.interval", "Interval");
		add("particlemimicry.interval.tooltip", "The interval between particle spawns");

		add("particlemimicry.networking.set_particle_data.failed", "Failed to set Particle Emitter data %s");
	}
}
