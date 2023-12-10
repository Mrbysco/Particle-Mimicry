package com.mrbysco.particlemimicry.client.screen.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class NumberEditBox extends EditBox {
	public final int decimalPoints;

	public NumberEditBox(Font font, int x, int y, int width, int height, Component defaultValue, int decimalPoints) {
		super(font, x, y, width, height, defaultValue);
		this.decimalPoints = decimalPoints;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void insertText(String textToWrite) {
		String newValue = textToWrite.replaceAll("[a-zA-Z]", "");
		super.insertText(newValue);
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(String value) {
		if (value.isEmpty()) {
			super.setValue("0");
		} else {
			try {
				super.setValue(String.format("%." + decimalPoints + "f", Float.parseFloat(value)));
			} catch (NumberFormatException e) {
				super.setValue("0");
			}
		}
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		if (!focused) {
			this.setHighlightPos(this.getValue().length());
			this.moveCursorToEnd(false);
		}
	}
}
