package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class MimicryBlockTagProvider extends FabricTagsProvider.FabricIntrinsicHolderTagsProvider<Block> {

	public MimicryBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.BLOCK, registriesFuture, block -> block.builtInRegistryHolder().key());
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(MimicryRegistry.PARTICLE_EMITTER.get());
	}
}