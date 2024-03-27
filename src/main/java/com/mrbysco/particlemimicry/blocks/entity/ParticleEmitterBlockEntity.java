package com.mrbysco.particlemimicry.blocks.entity;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.particlemimicry.ParticleMimicry;
import com.mrbysco.particlemimicry.registry.MimicryRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ParticleEmitterBlockEntity extends BlockEntity {
	public String particleType, offset, specialParameters, delta, speed, count;
	public String particleCommand;
	public int interval;

	public ParticleEmitterBlockEntity(BlockPos pos, BlockState state) {
		super(MimicryRegistry.PARTICLE_EMITTER_ENTITY.get(), pos, state);
		setData(null, "", "", "", "", "", "", "20");
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ParticleEmitterBlockEntity blockEntity) {
		if (blockEntity.interval <= 0) {
			blockEntity.interval = 20;
		}
		if (level.getGameTime() % blockEntity.interval == 0) {
			MinecraftServer server = level.getServer();
			blockEntity.constructCommand();

			if (!StringUtil.isNullOrEmpty(blockEntity.particleCommand)) {
				try {
					CommandSourceStack commandsourcestack = server.createCommandSourceStack().withPosition(Vec3.atCenterOf(pos)).withSuppressedOutput().withLevel((ServerLevel) level);
					server.getCommands().performPrefixedCommand(commandsourcestack, blockEntity.particleCommand);
				} catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.forThrowable(throwable, "Executing particle emitter block");
					CrashReportCategory crashreportcategory = crashreport.addCategory("Particle command to be executed");
					crashreportcategory.setDetail("Command", blockEntity.particleCommand);
					crashreportcategory.setDetail("Name", () -> {
						return state.getBlock().getName().getString();
					});
					throw new ReportedException(crashreport);
				}
			}
		}
	}

	public void setData(@Nullable Player player, String particleType, String offset, String specialParameters, String delta, String speed, String count, String interval) {
		this.particleType = particleType;
		this.offset = checkOffset(player, offset);
		this.specialParameters = specialParameters;
		this.delta = delta;
		this.speed = speed;
		this.count = count;
		int value = 20;
		try {
			int parsedValue = Integer.parseInt(interval);
			value = parsedValue > 0 ? parsedValue : 20;
		} catch (NumberFormatException e) {
			//Nope
		}
		this.interval = value;
	}

	private String checkOffset(@Nullable Player player, String offset) {
		String offsetString = offset;
		if (!offsetString.isEmpty()) {
			//Check the offset against the position of the Particle Emitter to see if it's within 5 blocks of the block
			try {
				MinecraftServer server = level.getServer();
				WorldCoordinates worldcoordinates = WorldCoordinates.parseInt(new StringReader(offsetString));
				Vec3 centerPos = Vec3.atCenterOf(this.getBlockPos());
				CommandSourceStack sourceStack = server.createCommandSourceStack().withPosition(centerPos).withSuppressedOutput().withLevel((ServerLevel) level);
				Vec3 position = worldcoordinates.getPosition(sourceStack);
				if (position != null) {
					if (!position.closerThan(centerPos, 5)) {
						if (player != null) {
							player.sendSystemMessage(Component.literal("Offset is too far away from the Particle Emitter. Resetting to ~ ~1 ~").withStyle(ChatFormatting.RED));
						} else {
							ParticleMimicry.LOGGER.debug("Offset is too far away from the Particle Emitter. Resetting to ~ ~1 ~");
						}
						return "~ ~1 ~";
					}
				}
			} catch (CommandSyntaxException e) {
				//Nope
			}
			return offsetString;
		}

		return offsetString;
	}

	public boolean isNumeric(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void constructCommand() {
		//Construct command string
		StringBuilder commandBuilder = new StringBuilder("particle");
		//Add the particle type to the command
		commandBuilder.append(" ").append(particleType);
		//Add the offset to the command
		if (!offset.isEmpty()) {
			commandBuilder.append(" ").append(offset);
		} else {
			commandBuilder.append(" ~ ~ ~");
		}
		//Add the special parameters to the command if they exist
		if (!specialParameters.isEmpty()) {
			commandBuilder.append(" ").append(specialParameters);
		} else {
			if (particleType.equalsIgnoreCase("block")) {
				commandBuilder.append(" ").append(BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()));
			}
		}
		//Add the delta to the command
		if (!delta.isEmpty()) {
			commandBuilder.append(" ").append(delta);
		} else {
			commandBuilder.append(" 0 0 0");
		}
		//Add the speed to the command
		if (!speed.isEmpty()) {
			commandBuilder.append(" ").append(speed);
		} else {
			commandBuilder.append(" 0");
		}
		//Add the count to the command
		if (!count.isEmpty()) {
			commandBuilder.append(" ").append(count);
		} else {
			commandBuilder.append(" 1");
		}
		this.particleCommand = commandBuilder.toString();
	}

	public void load(CompoundTag tag) {
		super.load(tag);
		this.particleType = tag.getString("ParticleType");
		this.offset = tag.getString("Offset");
		this.specialParameters = tag.getString("SpecialParameters");
		this.delta = tag.getString("Delta");
		this.speed = tag.getString("Speed");
		this.count = tag.getString("Count");
		this.constructCommand();
	}

	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putString("ParticleType", particleType);
		tag.putString("Offset", offset);
		tag.putString("SpecialParameters", specialParameters);
		tag.putString("Delta", delta);
		tag.putString("Speed", speed);
		tag.putString("Count", count);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		if (pkt.getTag() != null)
			load(pkt.getTag());

		BlockState state = level.getBlockState(getBlockPos());
		level.sendBlockUpdated(getBlockPos(), state, state, 3);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	/**
	 * Update the client whenever the chunk loader is modified
	 */
	public void refreshClient() {
		setChanged();
		BlockState state = level.getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, 2);
	}
}