package com.mrbysco.particlemimicry.client.screen;

import com.mrbysco.particlemimicry.networking.SetParticleDataPayload;
import com.mrbysco.particlemimicry.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

public class ParticleEmitterEditScreen extends AbstractParticleEmitterEditScreen {
	private final String oldParticleType;
	private String oldOffset;
	private String oldParameters;
	private String oldDelta;
	private String oldSpeed;
	private String oldCount;
	private String oldInterval;
	private final BlockPos blockPos;
	private final Identifier dimension;

	public ParticleEmitterEditScreen(BlockPos blockPos, Identifier dimension,
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

	public static void openScreen(BlockPos pos, Identifier dimension,
	                              String particleType, String offset, String parameters, String delta, String speed, String count, String interval) {
		Minecraft.getInstance().setScreen(new ParticleEmitterEditScreen(pos, dimension, particleType, offset, parameters, delta, speed, count, interval));
	}

	@Override
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

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		this.enableControls(true);
	}

	@Override
	protected void populateAndSendPacket() {
		Services.PLATFORM.sendPayloadToServer(new SetParticleDataPayload(blockPos, dimension,
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