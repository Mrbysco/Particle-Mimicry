package com.mrbysco.particlemimicry.client.screen;

import com.mrbysco.particlemimicry.networking.PacketHandler;
import com.mrbysco.particlemimicry.networking.message.SetParticleEmitterDataMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class ParticleEmitterEditScreen extends AbstractParticleEmitterEditScreen {
	private String oldParticleType, oldOffset, oldParameters, oldDelta, oldSpeed, oldCount, oldInterval;
	private final BlockPos blockPos;
	private final ResourceLocation dimension;

	public ParticleEmitterEditScreen(BlockPos blockPos, ResourceLocation dimension,
									 String particleType, String offset, String parameters, String delta, String speed, String count, String interval) {
		this.blockPos = blockPos;
		this.dimension = dimension;
		this.oldParticleType = particleType;
		this.oldOffset = offset;
		this.oldParameters = parameters;
		this.oldDelta = delta;
		this.oldSpeed = speed;
		this.oldCount = count;
		this.oldInterval = interval;
	}

	public static void openScreen(BlockPos pos, ResourceLocation dimension,
								  String particleType, String offset, String parameters, String delta, String speed, String count, String interval) {
		Minecraft.getInstance().setScreen(new ParticleEmitterEditScreen(pos, dimension, particleType, offset, parameters, delta, speed, count, interval));
	}

	protected void init() {
		super.init();
		this.particleTypeEdit.setValue(this.oldParticleType);
		this.offsetEdit.setValue(this.oldOffset);
		this.specialParametersEdit.setValue(this.oldParameters);
		this.deltaEdit.setValue(this.oldDelta);
		this.speedEdit.setValue(this.oldSpeed);
		this.countEdit.setValue(this.oldCount);
		this.intervalEdit.setValue(this.oldInterval);
	}

	private void enableControls(boolean value) {
		this.doneButton.active = value;
	}

	public void resize(Minecraft mc, int width, int height) {
		super.resize(mc, width, height);
		this.enableControls(true);
	}

	protected void populateAndSendPacket() {
		PacketHandler.CHANNEL.sendToServer(new SetParticleEmitterDataMessage(blockPos, dimension,
				particleTypeEdit.getValue(),
				offsetEdit.getValue(),
				specialParametersEdit.getValue(),
				deltaEdit.getValue(),
				speedEdit.getValue(),
				countEdit.getValue(),
				intervalEdit.getValue()
		));
	}
}