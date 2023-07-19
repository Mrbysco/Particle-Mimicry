package com.mrbysco.particlemimicry.registry;

import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.blocks.ParticleEmitterBlock;
import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MimicryRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ParticleMimicry.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ParticleMimicry.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ParticleMimicry.MOD_ID);

	public static final RegistryObject<Block> PARTICLE_EMITTER = BLOCKS.register("particle_emitter", () ->
			new ParticleEmitterBlock(Block.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).strength(0.8F).sound(SoundType.METAL).noOcclusion()));

	public static final RegistryObject<BlockEntityType<ParticleEmitterBlockEntity>> PARTICLE_EMITTER_ENTITY = BLOCK_ENTITIES.register("particle_emitter", () ->
			BlockEntityType.Builder.of(ParticleEmitterBlockEntity::new, PARTICLE_EMITTER.get()).build(null));
	public static final RegistryObject<Item> PARTICLE_EMITTER_ITEM = ITEMS.register("particle_emitter", () -> new BlockItem(PARTICLE_EMITTER.get(),
			new Item.Properties()));
}
