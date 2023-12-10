package com.mrbysco.particlemimicry.client.screen.components;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class ParticleSuggestions extends CommandSuggestions {
	private EditBox textBox;
	private String previous = "";
	private Font font;
	private boolean active;
	private List<Suggestion> currentSuggestions;

	public ParticleSuggestions(Minecraft minecraft, Screen screen, EditBox editBox, Font font) {
		super(minecraft, screen, editBox, font, true, true, 0, 7, false, Integer.MIN_VALUE);
		this.textBox = editBox;
		this.font = font;
		this.currentSuggestions = new ArrayList<>();
	}

	public void tick() {
		if (suggestions == null)
			textBox.setSuggestion("");
		if (active == textBox.isFocused())
			return;
		active = textBox.isFocused();
		updateCommandInfo();
	}

	@Override
	public void updateCommandInfo() {
		String value = this.textBox.getValue();
		if (value.equals(previous))
			return;
		if (!active) {
			suggestions = null;
			return;
		}
		previous = value;
		List<String> particleTypes = new ArrayList<>(BuiltInRegistries.PARTICLE_TYPE.keySet().stream().map(it ->
				it.getNamespace().equals("minecraft") ? it.getPath() : it.toString()).toList());

		if (value.length() > 0)
			particleTypes.removeIf(it -> !it.contains(value));

		SuggestionsBuilder builder = new SuggestionsBuilder(value, 0);
		var suggestionProvider = SharedSuggestionProvider.suggest(particleTypes, builder);

		try {
			currentSuggestions = suggestionProvider.get().getList();
		} catch (Exception e) {

		}
		showSuggestions(false);
	}

	public void showSuggestions(boolean narrateFirstSuggestion) {
		if (currentSuggestions.isEmpty()) {
			suggestions = null;
			return;
		}
		int width = 0;
		for (Suggestion suggestion : currentSuggestions)
			width = Math.max(width, this.font.width(suggestion.getText()));
		int x = Mth.clamp(textBox.getScreenX(0), 0, textBox.getScreenX(0) + textBox.getInnerWidth() - width);
		suggestions = new CommandSuggestions.SuggestionsList(x, 72, width, currentSuggestions, false);
	}
}
