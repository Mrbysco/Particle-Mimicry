package com.mrbysco.particlemimicry.client.screen;

import com.mrbysco.particlemimicry.client.screen.components.DeltaSuggestions;
import com.mrbysco.particlemimicry.client.screen.components.ParticleSuggestions;
import com.mrbysco.particlemimicry.client.screen.widget.NumberEditBox;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

public abstract class AbstractParticleEmitterEditScreen extends Screen {
	private static final Component SET_PARTICLE_LABEL = Component.translatable("particlemimicry.setParticle");

	private static final Component PARTICLE_LABEL = Component.translatable("particlemimicry.particle");
	private static final Component SPECIAL_LABEL = Component.translatable("particlemimicry.specialParameters");
	private static final Component OFFSET_LABEL = Component.translatable("particlemimicry.offset");
	private static final Component DELTA_LABEL = Component.translatable("particlemimicry.delta");
	private static final Component SPEED_LABEL = Component.translatable("particlemimicry.speed");
	private static final Component COUNT_LABEL = Component.translatable("particlemimicry.count");
	private static final Component INTERVAL_LABEL = Component.translatable("particlemimicry.interval");

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

	@Override
	protected void init() {
		this.addRenderableWidget(this.doneButton = Button.builder(CommonComponents.GUI_DONE, (button) -> {
			this.onDone();
		}).bounds(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20).build());

		this.addRenderableWidget(this.cancelButton = Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
			this.onClose();
		}).bounds(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20).build());

		// Particle Type
		this.particleTypeEdit = new EditBox(this.font, this.width / 2 - 151, 50, 150, 20,
				Component.literal(typeSuggestion)) {
			@Override
			@NotNull
			protected MutableComponent createNarrationMessage() {
				return super.createNarrationMessage().append(AbstractParticleEmitterEditScreen.this.particleSuggestions.getNarrationMessage());
			}
		};
		this.particleTypeEdit.setMaxLength(100);
		this.particleTypeEdit.setResponder(this::onEdited);
		this.particleTypeEdit.setTooltip(Tooltip.create(Component.translatable("particlemimicry.particle.tooltip")));
		this.addRenderableWidget(this.particleTypeEdit);
		this.setInitialFocus(this.particleTypeEdit);
		this.particleTypeEdit.setFocused(true);
		this.particleSuggestions = new ParticleSuggestions(this.minecraft, this, this.particleTypeEdit, this.font);
		this.particleSuggestions.setAllowSuggestions(true);
		this.particleSuggestions.updateCommandInfo();

		// Special Parameters
		this.specialParametersEdit = new EditBox(this.font, this.width / 2 + 1, 50, 150, 20,
				Component.literal(specialSuggestion));
		this.specialParametersEdit.setMaxLength(200);
		this.specialParametersEdit.setTooltip(Tooltip.create(Component.translatable("particlemimicry.specialParameters.tooltip")));
		this.addRenderableWidget(this.specialParametersEdit);

		// Offset
		this.offsetEdit = new EditBox(this.font, this.width / 2 - 151, 84, 74, 20,
				Component.literal(offsetSuggestion)) {
			@Override
			@NotNull
			protected MutableComponent createNarrationMessage() {
				return super.createNarrationMessage().append(AbstractParticleEmitterEditScreen.this.offsetSuggestions.getNarrationMessage());
			}
		};
		this.offsetEdit.setValue("~ ~ ~");
		this.offsetEdit.setMaxLength(30);
		this.offsetEdit.setResponder(this::onOffsetEdited);
		this.offsetEdit.setTooltip(Tooltip.create(Component.translatable("particlemimicry.offset.tooltip")));
		this.addRenderableWidget(this.offsetEdit);
		this.offsetSuggestions = new DeltaSuggestions(this.minecraft, this, this.offsetEdit, this.font, true);
		this.offsetSuggestions.setAllowSuggestions(true);
		this.offsetSuggestions.updateCommandInfo();

		// Delta
		this.deltaEdit = new EditBox(this.font, this.width / 2 - 75, 84, 74, 20,
				Component.literal(deltaSuggestion)) {
			@Override
			@NotNull
			protected MutableComponent createNarrationMessage() {
				return super.createNarrationMessage().append(AbstractParticleEmitterEditScreen.this.deltaSuggestions.getNarrationMessage());
			}
		};
		this.deltaEdit.setValue("0 0 0");
		this.deltaEdit.setMaxLength(30);
		this.deltaEdit.setResponder(this::onDeltaEdited);
		this.addRenderableWidget(this.deltaEdit);
		this.deltaSuggestions = new DeltaSuggestions(this.minecraft, this, this.deltaEdit, this.font, false);
		this.deltaSuggestions.setAllowSuggestions(true);
		this.deltaSuggestions.updateCommandInfo();
		this.deltaEdit.setTooltip(Tooltip.create(Component.translatable("particlemimicry.delta.tooltip")));

		// Speed
		this.speedEdit = new NumberEditBox(this.font, this.width / 2 + 1, 84, 48, 20,
				Component.translatable("particlemimicry.speed"), 4) {
		};
		this.speedEdit.setMaxLength(5);
		this.speedEdit.setValue("0");
		this.speedEdit.setTooltip(Tooltip.create(Component.translatable("particlemimicry.speed.tooltip")));
		this.addRenderableWidget(this.speedEdit);

		// Speed
		this.countEdit = new NumberEditBox(this.font, this.width / 2 + 52, 84, 48, 20,
				Component.translatable("particlemimicry.count"), 0) {
		};
		this.countEdit.setMaxLength(5);
		this.countEdit.setValue("0");
		this.countEdit.setTooltip(Tooltip.create(Component.translatable("particlemimicry.count.tooltip")));
		this.addRenderableWidget(this.countEdit);

		// Interval
		this.intervalEdit = new NumberEditBox(this.font, this.width / 2 + 103, 84, 48, 20,
				Component.translatable("particlemimicry.interval"), 0) {
		};
		this.intervalEdit.setMaxLength(5);
		this.intervalEdit.setValue("20");
		this.intervalEdit.setTooltip(Tooltip.create(Component.translatable("particlemimicry.interval.tooltip")));
		this.addRenderableWidget(this.intervalEdit);
	}

	@Override
	public void tick() {
		if (this.particleTypeEdit.isFocused())
			this.particleSuggestions.tick();
		if (this.offsetEdit.isFocused())
			this.offsetSuggestions.tick();
		if (this.deltaEdit.isFocused())
			this.deltaSuggestions.tick();

		updateSuggestion(particleTypeEdit, typeSuggestion);
		updateSuggestion(offsetEdit, offsetSuggestion);
		updateSuggestion(specialParametersEdit, specialSuggestion);
		updateSuggestion(deltaEdit, deltaSuggestion);
		updateSuggestion(speedEdit, speedSuggestion);
		updateSuggestion(countEdit, countSuggestion);
		updateSuggestion(intervalEdit, intervalSuggestion);

		if (!particleTypeEdit.isFocused() && particleTypeEdit.suggestion != null)
			particleSuggestions.hide();

		if (!offsetEdit.isFocused() && offsetEdit.suggestion != null)
			offsetSuggestions.hide();

		if (!deltaEdit.isFocused() && deltaEdit.suggestion != null)
			deltaSuggestions.hide();
	}

	private void updateSuggestion(EditBox box, String suggestion) {
		if (!box.getValue().isEmpty()) {
			box.setSuggestion(null);
		} else {
			box.setSuggestion(suggestion);
		}
	}

	@Override
	public void resize(int width, int height) {
		this.init(width, height);
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

	@Override
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

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (particleTypeEdit.isFocused() && this.particleSuggestions.keyPressed(event)) {
			return true;
		} else if (offsetEdit.isFocused() && this.offsetSuggestions.keyPressed(event)) {
			return true;
		} else if (deltaEdit.isFocused() && this.deltaSuggestions.keyPressed(event)) {
			return true;
		} else if (super.keyPressed(event)) {
			return true;
		} else if (event.key() != 257 && event.key() != 335) {
			return false;
		} else {
			this.onDone();
			return true;
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (particleTypeEdit.isFocused()) {
			return this.particleSuggestions.mouseScrolled(scrollX) || super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		} else if (offsetEdit.isFocused()) {
			return this.offsetSuggestions.mouseScrolled(scrollX) || super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		} else if (deltaEdit.isFocused()) {
			return this.deltaSuggestions.mouseScrolled(scrollX) || super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		// The ugly setFocused calls are to stop multiple edit boxes from being focused at once
		if (particleTypeEdit.isFocused() && this.particleSuggestions.mouseClicked(event)) {
			offsetEdit.setFocused(false);
			deltaEdit.setFocused(false);
			specialParametersEdit.setFocused(false);
			speedEdit.setFocused(false);
			countEdit.setFocused(false);
			intervalEdit.setFocused(false);
			return true;
		} else if (offsetEdit.isFocused() && this.offsetSuggestions.mouseClicked(event)) {
			particleTypeEdit.setFocused(false);
			deltaEdit.setFocused(false);
			specialParametersEdit.setFocused(false);
			speedEdit.setFocused(false);
			countEdit.setFocused(false);
			intervalEdit.setFocused(false);
			return true;
		} else if (deltaEdit.isFocused() && this.deltaSuggestions.mouseClicked(event)) {
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
			return super.mouseClicked(event, doubleClick);
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTicks);

		graphics.centeredText(this.font, SET_PARTICLE_LABEL, this.width / 2, 20, ARGB.opaque(16777215));
		graphics.text(this.font, PARTICLE_LABEL, this.width / 2 - 151, 40, ARGB.opaque(10526880), false);
		graphics.text(this.font, SPECIAL_LABEL, this.width / 2 + 1, 40, ARGB.opaque(10526880), false);

		graphics.text(this.font, OFFSET_LABEL, this.width / 2 - 150, 74, ARGB.opaque(10526880), false);
		graphics.text(this.font, DELTA_LABEL, this.width / 2 - 75, 74, ARGB.opaque(10526880), false);

		graphics.text(this.font, SPEED_LABEL, this.width / 2 + 1, 74, ARGB.opaque(10526880), false);
		graphics.text(this.font, COUNT_LABEL, this.width / 2 + 52, 74, ARGB.opaque(10526880), false);
		graphics.text(this.font, INTERVAL_LABEL, this.width / 2 + 103, 74, ARGB.opaque(10526880), false);


		if (particleTypeEdit.isFocused())
			this.particleSuggestions.extractRenderState(graphics, mouseX, mouseY);

		Matrix3x2fStack poseStack = graphics.pose();
		if (offsetEdit.isFocused()) {
			poseStack.pushMatrix();
			poseStack.translate(0, 30);
			this.offsetSuggestions.extractRenderState(graphics, mouseX, mouseY);
			poseStack.popMatrix();
		}

		if (deltaEdit.isFocused()) {
			poseStack.pushMatrix();
			poseStack.translate(0, 90);
			this.deltaSuggestions.extractRenderState(graphics, mouseX, mouseY);
			poseStack.popMatrix();
		}
	}
}