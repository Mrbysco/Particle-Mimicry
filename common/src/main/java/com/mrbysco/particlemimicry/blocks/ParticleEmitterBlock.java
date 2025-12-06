package com.mrbysco.particlemimicry.blocks;

import com.mojang.serialization.MapCodec;
import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import com.mrbysco.particlemimicry.registration.MimicryRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ParticleEmitterBlock extends BaseEntityBlock {
	public static final MapCodec<ParticleEmitterBlock> CODEC = simpleCodec(ParticleEmitterBlock::new);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty MIMICING = BooleanProperty.create("mimicing");

	public ParticleEmitterBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE).setValue(MIMICING, Boolean.FALSE));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof ParticleEmitterBlockEntity blockEntity) {
			if (level.isClientSide()) {
				com.mrbysco.particlemimicry.client.screen.ParticleEmitterEditScreen.openScreen(pos, level.dimension().location(),
						blockEntity.particleType, blockEntity.offset, blockEntity.specialParameters, blockEntity.delta,
						blockEntity.speed, blockEntity.count, String.valueOf(blockEntity.interval));
			}

			return InteractionResult.SUCCESS;
		}

		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ParticleEmitterBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (!level.isClientSide() && state.getValue(POWERED)) {
			return createTickerHelper(blockEntityType, MimicryRegistry.PARTICLE_EMITTER_ENTITY.get(), ParticleEmitterBlockEntity::serverTick);
		}
		return null;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		if (state.getValue(MIMICING)) {
			return RenderShape.INVISIBLE;
		}
		return RenderShape.MODEL;
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
		if (!level.isClientSide()) {
			boolean flag = state.getValue(POWERED);
			if (flag != level.hasNeighborSignal(pos)) {
				if (flag) {
					level.scheduleTick(pos, this, 4);
				} else {
					level.setBlock(pos, state.cycle(POWERED), 2);
				}
			}

		}
	}

	@Override
	public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
		if (state.getValue(POWERED) && !serverLevel.hasNeighborSignal(pos)) {
			serverLevel.setBlock(pos, state.cycle(POWERED), 2);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(POWERED, MIMICING);
	}
}
