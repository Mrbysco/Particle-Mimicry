package com.mrbysco.particlemimicry.datagen.assets;

import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MimicryItemModelProvider extends ItemModelProvider {
	public MimicryItemModelProvider(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, ParticleMimicry.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		ResourceLocation location = MimicryRegistry.PARTICLE_EMITTER.getId();
		withExistingParent(location.getPath(), modLoc("block/particle_emitter"));
	}
}
