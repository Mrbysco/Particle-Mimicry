package com.mrbysco.particlemimicry.mixin;

import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ParticleEmitterBlockEntity.class)
public class ParticleEmitterBlockEntityMixin extends BlockEntity {

	public ParticleEmitterBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
		if (pkt.getTag() != null)
			loadWithComponents(pkt.getTag(), lookupProvider);

		BlockState state = level.getBlockState(getBlockPos());
		level.sendBlockUpdated(getBlockPos(), state, state, 3);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt, level != null ? level.registryAccess() : VanillaRegistries.createLookup());
		return nbt;
	}
}