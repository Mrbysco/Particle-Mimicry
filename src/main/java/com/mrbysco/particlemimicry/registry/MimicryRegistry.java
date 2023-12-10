package com.mrbysco.particlemimicry.registry;

import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.blocks.ParticleEmitterBlock;
import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MimicryRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ParticleMimicry.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ParticleMimicry.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ParticleMimicry.MOD_ID);

	public static final DeferredBlock<ParticleEmitterBlock> PARTICLE_EMITTER = BLOCKS.register("particle_emitter", () ->
			new ParticleEmitterBlock(Block.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).strength(0.8F).sound(SoundType.METAL).noOcclusion()));

	public static final Supplier<BlockEntityType<ParticleEmitterBlockEntity>> PARTICLE_EMITTER_ENTITY = BLOCK_ENTITIES.register("particle_emitter", () ->
			BlockEntityType.Builder.of(ParticleEmitterBlockEntity::new, PARTICLE_EMITTER.get()).build(null));
	public static final DeferredItem<BlockItem> PARTICLE_EMITTER_ITEM = ITEMS.registerSimpleBlockItem(PARTICLE_EMITTER);
}
