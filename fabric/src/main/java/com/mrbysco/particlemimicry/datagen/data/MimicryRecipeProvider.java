package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MimicryRecipeProvider extends FabricRecipeProvider {
	public MimicryRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@NotNull
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		return new Provider(provider, recipeOutput);
	}

	@NotNull
	@Override
	public String getName() {
		return "Particle Mimicry recipes";
	}

	public static class Provider extends RecipeProvider {

		protected Provider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			super(provider, recipeOutput);
		}

		@Override
		public void buildRecipes() {
			shaped(RecipeCategory.REDSTONE, MimicryRegistry.PARTICLE_EMITTER.get())
					.pattern("CCC")
					.pattern("RVR")
					.pattern("GCG")
					.define('C', ItemTags.LEAVES)
					.define('R', ConventionalItemTags.REDSTONE_DUSTS)
					.define('V', ConventionalItemTags.STORAGE_BLOCKS_LAPIS)
					.define('G', ConventionalItemTags.GLOWSTONE_DUSTS)
					.unlockedBy("has_leaves", has(ItemTags.LEAVES))
					.unlockedBy("has_lapis_block", has(ConventionalItemTags.STORAGE_BLOCKS_LAPIS))
					.unlockedBy("has_redstone_dust", has(ConventionalItemTags.REDSTONE_DUSTS))
					.unlockedBy("has_glowstone_dust", has(ConventionalItemTags.GLOWSTONE_DUSTS))
					.save(output);
		}
	}

}
