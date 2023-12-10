package com.mrbysco.particlemimicry.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.particlemimicry.client.screen.components.DeltaSuggestions;
import com.mrbysco.particlemimicry.client.screen.components.ParticleSuggestions;
import com.mrbysco.particlemimicry.client.screen.widget.NumberEditBox;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
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
	protected NumberEditBox intervalEdit;
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
	private static final String intervalSuggestion = "Interval";

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
		this.particleTypeEdit.setFocused(true);
		this.particleSuggestions = new ParticleSuggestions(this.minecraft, this, this.particleTypeEdit, this.font);
		this.particleSuggestions.setAllowSuggestions(true);
		this.particleSuggestions.updateCommandInfo();
		this.particleTypeEdit.setTooltip(Tooltip.create(Component.translatable(typeSuggestion)));

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
		this.offsetEdit.setTooltip(Tooltip.create(Component.translatable(offsetSuggestion)));

		this.specialParametersEdit = new EditBox(this.font, this.width / 2 - 150, 110, 300, 20,
				Component.translatable("particlemimicry.specialParameters"));
		this.specialParametersEdit.setMaxLength(200);
		this.specialParametersEdit.setSuggestion(specialSuggestion);
		this.addWidget(this.specialParametersEdit);
		this.specialParametersEdit.setTooltip(Tooltip.create(Component.translatable(specialSuggestion)));

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
		this.deltaEdit.setTooltip(Tooltip.create(Component.translatable(deltaSuggestion)));

		this.speedEdit = new NumberEditBox(this.font, this.width / 2 - 150, 170, 90, 20,
				Component.translatable("particlemimicry.speed"), 4) {
		};
		this.speedEdit.setMaxLength(5);
		this.deltaEdit.setSuggestion(speedSuggestion);
		this.speedEdit.setValue("0");
		this.speedEdit.setTooltip(Tooltip.create(Component.translatable(speedSuggestion)));
		this.addWidget(this.speedEdit);

		this.countEdit = new NumberEditBox(this.font, this.width / 2 - 45, 170, 90, 20,
				Component.translatable("particlemimicry.count"), 0) {
		};
		this.countEdit.setMaxLength(5);
		this.countEdit.setSuggestion(countSuggestion);
		this.countEdit.setValue("0");
		;
		this.countEdit.setTooltip(Tooltip.create(Component.translatable(countSuggestion)));
		this.addWidget(this.countEdit);

		this.intervalEdit = new NumberEditBox(this.font, this.width / 2 + 60, 170, 90, 20,
				Component.translatable("particlemimicry.interval"), 0) {
		};
		this.intervalEdit.setMaxLength(5);
		this.intervalEdit.setSuggestion(intervalSuggestion);
		this.intervalEdit.setValue("20");
		this.intervalEdit.setTooltip(Tooltip.create(Component.translatable(intervalSuggestion)));
		this.addWidget(this.intervalEdit);
	}

	public void tick() {
		this.particleSuggestions.tick();
		this.offsetSuggestions.tick();

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
		updateSuggestion(intervalEdit, intervalSuggestion);
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
		this.intervalEdit.setValue(this.intervalEdit.getValue());
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

	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (particleTypeEdit.isFocused()) {
			return this.particleSuggestions.mouseScrolled(scrollX) ? true : super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		} else if (offsetEdit.isFocused()) {
			return this.offsetSuggestions.mouseScrolled(scrollX) ? true : super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		} else if (deltaEdit.isFocused()) {
			return this.deltaSuggestions.mouseScrolled(scrollX) ? true : super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int delta) {
		// The ugly setFocused calls are to stop multiple edit boxes from being focused at once
		if (particleTypeEdit.isFocused() && this.particleSuggestions.mouseClicked(mouseX, mouseY, delta)) {
			offsetEdit.setFocused(false);
			deltaEdit.setFocused(false);
			specialParametersEdit.setFocused(false);
			speedEdit.setFocused(false);
			countEdit.setFocused(false);
			intervalEdit.setFocused(false);
			return true;
		} else if (offsetEdit.isFocused() && this.offsetSuggestions.mouseClicked(mouseX, mouseY, delta)) {
			particleTypeEdit.setFocused(false);
			deltaEdit.setFocused(false);
			specialParametersEdit.setFocused(false);
			speedEdit.setFocused(false);
			countEdit.setFocused(false);
			intervalEdit.setFocused(false);
			return true;
		} else if (deltaEdit.isFocused() && this.deltaSuggestions.mouseClicked(mouseX, mouseY, delta)) {
			particleTypeEdit.setFocused(false);
			offsetEdit.setFocused(false);
			specialParametersEdit.setFocused(false);
			speedEdit.setFocused(false);
			countEdit.setFocused(false);
			intervalEdit.setFocused(false);
			return true;
		} else {
			particleTypeEdit.setFocused(false);
			offsetEdit.setFocused(false);
			specialParametersEdit.setFocused(false);
			deltaEdit.setFocused(false);
			speedEdit.setFocused(false);
			countEdit.setFocused(false);
			intervalEdit.setFocused(false);
			return super.mouseClicked(mouseX, mouseY, delta);
		}
	}

	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.drawCenteredString(this.font, SET_PARTICLE_LABEL, this.width / 2, 20, 16777215);
		guiGraphics.drawString(this.font, COMMAND_LABEL, this.width / 2 - 150, 40, 10526880, false);
		this.particleTypeEdit.render(guiGraphics, mouseX, mouseY, partialTick);
		this.offsetEdit.render(guiGraphics, mouseX, mouseY, partialTick);
		this.specialParametersEdit.render(guiGraphics, mouseX, mouseY, partialTick);
		this.deltaEdit.render(guiGraphics, mouseX, mouseY, partialTick);
		this.speedEdit.render(guiGraphics, mouseX, mouseY, partialTick);
		this.countEdit.render(guiGraphics, mouseX, mouseY, partialTick);
		this.intervalEdit.render(guiGraphics, mouseX, mouseY, partialTick);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
		if (particleTypeEdit.isFocused())
			this.particleSuggestions.render(guiGraphics, mouseX, mouseY);

		PoseStack poseStack = guiGraphics.pose();
		if (offsetEdit.isFocused()) {
			poseStack.translate(0, 40, 0);
			this.offsetSuggestions.render(guiGraphics, mouseX, mouseY);
		}

		if (deltaEdit.isFocused()) {
			poseStack.translate(0, 80, 0);
			this.deltaSuggestions.render(guiGraphics, mouseX, mouseY);
		}
	}
}