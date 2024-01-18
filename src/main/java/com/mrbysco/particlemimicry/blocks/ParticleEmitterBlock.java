package com.mrbysco.particlemimicry.blocks;

import com.mojang.serialization.MapCodec;
import com.mrbysco.particlemimicry.blocks.entity.ParticleEmitterBlockEntity;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

public class ParticleEmitterBlock extends BaseEntityBlock {
	public static final MapCodec<ParticleEmitterBlock> CODEC = simpleCodec(ParticleEmitterBlock::new);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty MIMICING = BooleanProperty.create("mimicing");

	public ParticleEmitterBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)).setValue(MIMICING, Boolean.valueOf(false)));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player instanceof FakePlayer) return InteractionResult.FAIL;

		if (level.getBlockEntity(pos) instanceof ParticleEmitterBlockEntity blockEntity) {
			if (level.isClientSide) {
				com.mrbysco.particlemimicry.client.screen.ParticleEmitterEditScreen.openScreen(pos, level.dimension().location(),
						blockEntity.particleType, blockEntity.offset, blockEntity.specialParameters, blockEntity.delta,
						blockEntity.speed, blockEntity.count, String.valueOf(blockEntity.interval));
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ParticleEmitterBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (!level.isClientSide && state.getValue(POWERED)) {
			return createTickerHelper(blockEntityType, MimicryRegistry.PARTICLE_EMITTER_ENTITY.get(), ParticleEmitterBlockEntity::serverTick);
		}
		return null;
	}

	public RenderShape getRenderShape(BlockState state) {
		if (state.getValue(MIMICING)) {
			return RenderShape.INVISIBLE;
		}
		return RenderShape.MODEL;
	}

	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide) {
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

	public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
		if (state.getValue(POWERED) && !serverLevel.hasNeighborSignal(pos)) {
			serverLevel.setBlock(pos, state.cycle(POWERED), 2);
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(POWERED, MIMICING);
	}
}
