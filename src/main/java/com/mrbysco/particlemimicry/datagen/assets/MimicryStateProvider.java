package com.mrbysco.particlemimicry.datagen.assets;

import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MimicryStateProvider extends BlockStateProvider {
	public MimicryStateProvider(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, ParticleMimicry.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		ResourceLocation location = MimicryRegistry.PARTICLE_EMITTER.getId();
		ModelFile model = models().cubeAll(location.getPath(), modLoc("block/" + location.getPath()));
		getVariantBuilder(MimicryRegistry.PARTICLE_EMITTER.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
	}
}
