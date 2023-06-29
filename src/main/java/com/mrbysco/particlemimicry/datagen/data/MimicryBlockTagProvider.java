package com.mrbysco.particlemimicry.datagen.data;

import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class MimicryBlockTagProvider extends BlockTagsProvider {
	public MimicryBlockTagProvider(DataGenerator generator, @Nullable ExistingFileHelper fileHelper) {
		super(generator, ParticleMimicry.MOD_ID, fileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(MimicryRegistry.PARTICLE_EMITTER.get());
	}
}