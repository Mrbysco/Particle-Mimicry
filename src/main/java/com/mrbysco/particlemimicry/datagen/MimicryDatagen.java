package com.mrbysco.particlemimicry.datagen;

import com.mrbysco.particlemimicry.datagen.assets.MimicryItemModelProvider;
import com.mrbysco.particlemimicry.datagen.assets.MimicryLanguageProvider;
import com.mrbysco.particlemimicry.datagen.assets.MimicryStateProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryBlockTagProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryItemTagProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryLootProvider;
import com.mrbysco.particlemimicry.datagen.data.MimicryRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MimicryDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new MimicryLootProvider(generator));
			generator.addProvider(true, new MimicryRecipeProvider(generator));
			BlockTagsProvider provider;
			generator.addProvider(true, provider = new MimicryBlockTagProvider(generator, helper));
			generator.addProvider(true, new MimicryItemTagProvider(generator, provider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new MimicryLanguageProvider(generator));
			generator.addProvider(true, new MimicryStateProvider(generator, helper));
			generator.addProvider(true, new MimicryItemModelProvider(generator, helper));
		}
	}
}