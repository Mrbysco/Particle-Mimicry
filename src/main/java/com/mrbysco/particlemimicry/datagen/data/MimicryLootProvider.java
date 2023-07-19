package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MimicryLootProvider extends LootTableProvider {
	public MimicryLootProvider(PackOutput output) {
		super(output, Set.of(), List.of(
				new SubProviderEntry(ChunkyBlockLoot::new, LootContextParamSets.BLOCK)
		));
	}

	public static class ChunkyBlockLoot extends BlockLootSubProvider {

		protected ChunkyBlockLoot() {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		protected void generate() {
			dropSelf(MimicryRegistry.PARTICLE_EMITTER.get());
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return (Iterable<Block>) MimicryRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
		}
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
		map.forEach((name, table) -> table.validate(validationContext));
	}
}
