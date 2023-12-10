package com.mrbysco.particlemimicry.datagen;

import com.mrbysco.particlemimicry.datagen.assets.MimicryItemModelProvider;
import com.mrbysco.particlemimicry.datagen.assets.MimicryLanguageProvider;
import com.mrbysco.particlemimicry.datagen.assets.MimicryStateProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryBlockTagProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryItemTagProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryLootProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MimicryDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new MimicryLootProvider(packOutput));
			generator.addProvider(true, new MimicryRecipeProvider(packOutput, lookupProvider));
			BlockTagsProvider provider;
			generator.addProvider(true, provider = new MimicryBlockTagProvider(packOutput, lookupProvider, helper));
			generator.addProvider(true, new MimicryItemTagProvider(packOutput, lookupProvider, provider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new MimicryLanguageProvider(packOutput));
			generator.addProvider(true, new MimicryStateProvider(packOutput, helper));
			generator.addProvider(true, new MimicryItemModelProvider(packOutput, helper));
		}
	}
}