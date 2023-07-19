package com.mrbysco.particlemimicry.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.particlemimicry.client.screen.components.DeltaSuggestions;
import com.mrbysco.particlemimicry.client.screen.components.ParticleSuggestions;
import com.mrbysco.particlemimicry.client.screen.widget.NumberEditBox;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class AbstractParticleEmitterEditScreen extends Screen {
	private static final Component SET_PARTICLE_LABEL = Component.translatable("particlemimicry.setParticle");
	private static final Component COMMAND_LABEL = Component.translatable("particlemimicry.particle");
	protected EditBox particleTypeEdit;
	protected EditBox offsetEdit;
	protected EditBox specialParametersEdit;
	protected EditBox deltaEdit;
	protected NumberEditBox speedEdit;
	protected NumberEditBox countEdit;
	protected Button doneButton;
	protected Button cancelButton;
	ParticleSuggestions particleSuggestions;
	DeltaSuggestions offsetSuggestions;
	DeltaSuggestions deltaSuggestions;

	private static final String typeSuggestion = "Particle Type";
	private static final String offsetSuggestion = "Offset";
	private static final String specialSuggestion = "Particle specific parameters";
	private static final String deltaSuggestion = "Delta";
	private static final String speedSuggestion = "Speed";
	private static final String countSuggestion = "Count";

	public AbstractParticleEmitterEditScreen() {
		super(GameNarrator.NO_TITLE);
	}

	protected void init() {
		this.addRenderableWidget(this.doneButton = Button.builder(CommonComponents.GUI_DONE, (button) -> {
			this.onDone();
		}).bounds(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20).build());

		this.addRenderableWidget(this.cancelButton = Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
			this.onClose();
		}).bounds(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20).build());

		this.particleTypeEdit = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20,
				Component.translatable("particlemimicry.particle")) {
			protected MutableComponent createNarrationMessage() {
				return super.createNarrationMessage().append(AbstractParticleEmitterEditScreen.this.particleSuggestions.getNarrationMessage());
			}
		};
		this.particleTypeEdit.setMaxLength(100);
		this.particleTypeEdit.setResponder(this::onEdited);
		this.addWidget(this.particleTypeEdit);
		this.setInitialFocus(this.particleTypeEdit);
		this.particleTypeEdit.setFocus(true);
		this.particleSuggestions = new ParticleSuggestions(this.minecraft, this, this.particleTypeEdit, this.font);
		this.particleSuggestions.setAllowSuggestions(true);
		this.particleSuggestions.updateCommandInfo();

		this.offsetEdit = new EditBox(this.font, this.width / 2 - 150, 80, 300, 20,
				Component.translatable("particlemimicry.offset")) {
			protected MutableComponent createNarrationMessage() {
				return super.createNarrationMessage().append(AbstractParticleEmitterEditScreen.this.offsetSuggestions.getNarrationMessage());
			}
		};
		this.offsetEdit.setValue("~ ~ ~");
		this.offsetEdit.setMaxLength(30);
		this.offsetEdit.setSuggestion(offsetSuggestion);
		this.offsetEdit.setResponder(this::onOffsetEdited);
		this.addWidget(this.offsetEdit);
		this.offsetSuggestions = new DeltaSuggestions(this.minecraft, this, this.offsetEdit, this.font, true);
		this.offsetSuggestions.setAllowSuggestions(true);
		this.offsetSuggestions.updateCommandInfo();

		this.specialParametersEdit = new EditBox(this.font, this.width / 2 - 150, 110, 300, 20,
				Component.translatable("particlemimicry.specialParameters"));
		this.specialParametersEdit.setMaxLength(200);
		this.specialParametersEdit.setSuggestion(specialSuggestion);
		this.addWidget(this.specialParametersEdit);

		this.deltaEdit = new EditBox(this.font, this.width / 2 - 150, 140, 300, 20,
				Component.translatable("particlemimicry.delta")) {
			protected MutableComponent createNarrationMessage() {
				return super.createNarrationMessage().append(AbstractParticleEmitterEditScreen.this.deltaSuggestions.getNarrationMessage());
			}
		};
		this.deltaEdit.setValue("0 0 0");
		this.deltaEdit.setMaxLength(30);
		this.deltaEdit.setSuggestion(deltaSuggestion);
		this.deltaEdit.setResponder(this::onDeltaEdited);
		this.addWidget(this.deltaEdit);
		this.deltaSuggestions = new DeltaSuggestions(this.minecraft, this, this.deltaEdit, this.font, false);
		this.deltaSuggestions.setAllowSuggestions(true);
		this.deltaSuggestions.updateCommandInfo();

		this.speedEdit = new NumberEditBox(this.font, this.width / 2 - 150, 170, 120, 20,
				Component.translatable("particlemimicry.speed"), 4) {
		};
		this.speedEdit.setMaxLength(5);
		this.deltaEdit.setSuggestion(speedSuggestion);
		this.speedEdit.setValue("0");
		this.addWidget(this.speedEdit);

		this.countEdit = new NumberEditBox(this.font, this.width / 2 + 30, 170, 120, 20,
				Component.translatable("particlemimicry.count"), 0) {
		};
		this.countEdit.setMaxLength(5);
		this.countEdit.setSuggestion(countSuggestion);
		this.countEdit.setValue("0");
		this.addWidget(this.countEdit);
	}

	public void tick() {
		this.particleTypeEdit.tick();
		this.particleSuggestions.tick();
		this.offsetEdit.tick();
		this.offsetSuggestions.tick();

		this.specialParametersEdit.tick();
		this.deltaEdit.tick();
		this.deltaSuggestions.tick();

		if (!particleTypeEdit.isFocused() && particleTypeEdit.suggestion != null)
			particleSuggestions.hide();

		if (!offsetEdit.isFocused() && offsetEdit.suggestion != null)
			offsetSuggestions.hide();

		if (!deltaEdit.isFocused() && deltaEdit.suggestion != null)
			deltaSuggestions.hide();

		updateSuggestion(particleTypeEdit, typeSuggestion);
		updateSuggestion(offsetEdit, offsetSuggestion);
		updateSuggestion(specialParametersEdit, specialSuggestion);
		updateSuggestion(deltaEdit, deltaSuggestion);
		updateSuggestion(speedEdit, speedSuggestion);
		updateSuggestion(countEdit, countSuggestion);
	}

	private void updateSuggestion(EditBox box, String suggestion) {
		if (!box.getValue().isEmpty()) {
			box.setSuggestion(null);
		} else {
			box.setSuggestion(suggestion);
		}
	}

	public void resize(Minecraft mc, int width, int height) {
		this.init(mc, width, height);
		this.particleTypeEdit.setValue(this.particleTypeEdit.getValue());
		this.particleSuggestions.updateCommandInfo();
		this.offsetEdit.setValue(this.offsetEdit.getValue());
		this.offsetSuggestions.updateCommandInfo();

		this.specialParametersEdit.setValue(this.specialParametersEdit.getValue());
		this.deltaEdit.setValue(this.deltaEdit.getValue());
		this.deltaSuggestions.updateCommandInfo();
		this.speedEdit.setValue(this.speedEdit.getValue());
		this.countEdit.setValue(this.countEdit.getValue());
	}

	protected void onDone() {
		this.populateAndSendPacket();

		this.minecraft.setScreen((Screen) null);
	}

	public void removed() {

	}

	protected abstract void populateAndSendPacket();

	private void onEdited(String particleType) {
		this.particleSuggestions.updateCommandInfo();
	}

	private void onOffsetEdited(String particleType) {
		this.offsetSuggestions.updateCommandInfo();
	}

	private void onDeltaEdited(String particleType) {
		this.deltaSuggestions.updateCommandInfo();
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (particleTypeEdit.isFocused() && this.particleSuggestions.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (offsetEdit.isFocused() && this.offsetSuggestions.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (deltaEdit.isFocused() && this.deltaSuggestions.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (keyCode != 257 && keyCode != 335) {
			return false;
		} else {
			this.onDone();
			return true;
		}
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (particleTypeEdit.isFocused()) {
			return this.particleSuggestions.mouseScrolled(delta) ? true : super.mouseScrolled(mouseX, mouseY, delta);
		} else if (offsetEdit.isFocused()) {
			return this.offsetSuggestions.mouseScrolled(delta) ? true : super.mouseScrolled(mouseX, mouseY, delta);
		} else if (deltaEdit.isFocused()) {
			return this.deltaSuggestions.mouseScrolled(delta) ? true : super.mouseScrolled(mouseX, mouseY, delta);
		}
		return super.mouseScrolled(mouseX, mouseY, delta);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int delta) {
		// The ugly setFocus calls are to stop multiple edit boxes from being focused at once
		if (particleTypeEdit.isFocused() && this.particleSuggestions.mouseClicked(mouseX, mouseY, delta)) {
			offsetEdit.setFocus(false);
			deltaEdit.setFocus(false);
			specialParametersEdit.setFocus(false);
			speedEdit.setFocus(false);
			countEdit.setFocus(false);
			return true;
		} else if (offsetEdit.isFocused() && this.offsetSuggestions.mouseClicked(mouseX, mouseY, delta)) {
			particleTypeEdit.setFocus(false);
			deltaEdit.setFocus(false);
			specialParametersEdit.setFocus(false);
			speedEdit.setFocus(false);
			countEdit.setFocus(false);
			return true;
		} else if (deltaEdit.isFocused() && this.deltaSuggestions.mouseClicked(mouseX, mouseY, delta)) {
			particleTypeEdit.setFocus(false);
			offsetEdit.setFocus(false);
			specialParametersEdit.setFocus(false);
			speedEdit.setFocus(false);
			countEdit.setFocus(false);
			return true;
		} else {
			particleTypeEdit.setFocus(false);
			offsetEdit.setFocus(false);
			specialParametersEdit.setFocus(false);
			deltaEdit.setFocus(false);
			speedEdit.setFocus(false);
			countEdit.setFocus(false);
			return super.mouseClicked(mouseX, mouseY, delta);
		}
	}

	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(poseStack);
		drawCenteredString(poseStack, this.font, SET_PARTICLE_LABEL, this.width / 2, 20, 16777215);
		drawString(poseStack, this.font, COMMAND_LABEL, this.width / 2 - 150, 40, 10526880);
		this.particleTypeEdit.render(poseStack, mouseX, mouseY, partialTick);
		this.offsetEdit.render(poseStack, mouseX, mouseY, partialTick);
		this.specialParametersEdit.render(poseStack, mouseX, mouseY, partialTick);
		this.deltaEdit.render(poseStack, mouseX, mouseY, partialTick);
		this.speedEdit.render(poseStack, mouseX, mouseY, partialTick);
		this.countEdit.render(poseStack, mouseX, mouseY, partialTick);

		super.render(poseStack, mouseX, mouseY, partialTick);
		if (particleTypeEdit.isFocused())
			this.particleSuggestions.render(poseStack, mouseX, mouseY);

		if (offsetEdit.isFocused()) {
			poseStack.translate(0, 40, 0);
			this.offsetSuggestions.render(poseStack, mouseX, mouseY);
		}

		if (deltaEdit.isFocused()) {
			poseStack.translate(0, 80, 0);
			this.deltaSuggestions.render(poseStack, mouseX, mouseY);
		}
	}
}