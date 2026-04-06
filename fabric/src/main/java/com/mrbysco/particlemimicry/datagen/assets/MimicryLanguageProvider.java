package com.mrbysco.particlemimicry.datagen.assets;

import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class MimicryLanguageProvider extends FabricLanguageProvider {
	public MimicryLanguageProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add(MimicryRegistry.PARTICLE_EMITTER.get(), "Particle Emitter");

		translationBuilder.add("particlemimicry.particle", "Particle Type");
		translationBuilder.add("particlemimicry.particle.tooltip", "The ID of the particle");
		translationBuilder.add("particlemimicry.setParticle", "Set Particle Type");
		translationBuilder.add("particlemimicry.offset", "Offset");
		translationBuilder.add("particlemimicry.offset.tooltip", "The offset of the particle from the emitter's position");
		translationBuilder.add("particlemimicry.specialParameters", "Special Parameters");
		translationBuilder.add("particlemimicry.specialParameters.tooltip", "Special parameters for the particle, such as color or size");
		translationBuilder.add("particlemimicry.delta", "Delta");
		translationBuilder.add("particlemimicry.delta.tooltip", "The movement delta positions of the particle, dictating which way it moves");
		translationBuilder.add("particlemimicry.speed", "Speed");
		translationBuilder.add("particlemimicry.speed.tooltip", "The multiplier for the particles delta");
		translationBuilder.add("particlemimicry.count", "Count");
		translationBuilder.add("particlemimicry.count.tooltip", "The number of particles to spawn each time");
		translationBuilder.add("particlemimicry.interval", "Interval");
		translationBuilder.add("particlemimicry.interval.tooltip", "The interval between particle spawns");

		translationBuilder.add("particlemimicry.networking.set_particle_data.failed", "Failed to set Particle Emitter data %s");
	}
}
