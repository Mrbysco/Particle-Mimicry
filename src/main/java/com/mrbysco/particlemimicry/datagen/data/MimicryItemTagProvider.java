package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.ParticleMimicry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MimicryItemTagProvider extends ItemTagsProvider {
	public MimicryItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper fileHelper) {
		super(dataGenerator, blockTagsProvider, ParticleMimicry.MOD_ID, fileHelper);
	}

	@Override
	protected void addTags() {

	}
}