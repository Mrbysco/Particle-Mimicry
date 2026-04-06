package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MimicryLootProvider extends LootTableProvider {
	public MimicryLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, Set.of(), List.of(
				new SubProviderEntry(ChunkyBlockLoot::new, LootContextParamSets.BLOCK)
		), lookupProvider);
	}

	public static class ChunkyBlockLoot extends BlockLootSubProvider {

		protected ChunkyBlockLoot(HolderLookup.Provider provider) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
		}

		@Override
		protected void generate() {
			dropSelf(MimicryRegistry.PARTICLE_EMITTER.get());
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return (Iterable<Block>) MimicryRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
		}
	}
}
