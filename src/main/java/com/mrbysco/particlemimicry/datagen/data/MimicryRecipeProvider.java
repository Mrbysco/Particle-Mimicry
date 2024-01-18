package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class MimicryRecipeProvider extends RecipeProvider {
	public MimicryRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, MimicryRegistry.PARTICLE_EMITTER.get())
				.pattern("CCC")
				.pattern("RVR")
				.pattern("GCG")
				.define('C', ItemTags.LEAVES)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('V', Tags.Items.STORAGE_BLOCKS_LAPIS)
				.define('G', Tags.Items.DUSTS_GLOWSTONE)
				.unlockedBy("has_leaves", has(ItemTags.LEAVES))
				.unlockedBy("has_lapis_block", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.unlockedBy("has_redstone_dust", has(Tags.Items.DUSTS_REDSTONE))
				.unlockedBy("has_glowstone_dust", has(Tags.Items.DUSTS_GLOWSTONE))
				.save(recipeOutput);
	}
}
