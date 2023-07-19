package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.ParticleMimicry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class MimicryItemTagProvider extends ItemTagsProvider {
	public MimicryItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
								  TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagProvider.contentsGetter(), ParticleMimicry.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

	}
}