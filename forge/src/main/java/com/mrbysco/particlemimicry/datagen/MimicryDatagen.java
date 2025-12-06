package com.mrbysco.particlemimicry.datagen;

import com.mrbysco.particlemimicry.datagen.assets.MimicryLanguageProvider;
import com.mrbysco.particlemimicry.datagen.assets.MimicryModelProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryBlockTagProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryLootProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class MimicryDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new MimicryLootProvider(packOutput, lookupProvider));
		generator.addProvider(true, new MimicryRecipeProvider.Runner(packOutput, lookupProvider));
		generator.addProvider(true, new MimicryBlockTagProvider(packOutput, lookupProvider));

		generator.addProvider(true, new MimicryLanguageProvider(packOutput));
		generator.addProvider(true, new MimicryModelProvider(packOutput));

	}
}