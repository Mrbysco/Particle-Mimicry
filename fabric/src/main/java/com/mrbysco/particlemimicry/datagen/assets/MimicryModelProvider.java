package com.mrbysco.particlemimicry.datagen.assets;

import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class MimicryModelProvider extends FabricModelProvider {

	public MimicryModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockModels) {
		blockModels.family(MimicryRegistry.PARTICLE_EMITTER.get());
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModels) {
	}
}
