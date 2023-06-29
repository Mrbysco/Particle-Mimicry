package com.mrbysco.particlemimicry.client.screen.components;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.LocalCoordinates;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeltaSuggestions extends CommandSuggestions {
	private EditBox textBox;
	private String previous = "";
	private Font font;
	private boolean active;
	private List<Suggestion> currentSuggestions;
	private boolean centerCorrect;

	public DeltaSuggestions(Minecraft minecraft, Screen screen, EditBox editBox, Font font, boolean centerCorrect) {
		super(minecraft, screen, editBox, font, true, true, 0, 7, false, Integer.MIN_VALUE);
		this.textBox = editBox;
		this.font = font;
		this.currentSuggestions = new ArrayList<>();
		this.centerCorrect = centerCorrect;
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

		SuggestionsBuilder builder = new SuggestionsBuilder(value, 0);
		var suggestionProvider = listSuggestions(builder);

		try {
			currentSuggestions = suggestionProvider.get().getList();
		} catch (Exception e) {

		}
		showSuggestions(false);
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(SuggestionsBuilder suggestionsBuilder) {
		String s = suggestionsBuilder.getRemaining();
		Collection<SharedSuggestionProvider.TextCoordinates> collection;

		if (!s.isEmpty() && s.charAt(0) == '^') {
			collection = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
		} else {
			collection = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_GLOBAL);
		}
		return SharedSuggestionProvider.suggestCoordinates(s, collection, suggestionsBuilder, Commands.createValidator(this::parse));
	}

	public Coordinates parse(StringReader reader) throws CommandSyntaxException {
		return (Coordinates) (reader.canRead() && reader.peek() == '^' ? LocalCoordinates.parse(reader) :
				WorldCoordinates.parseDouble(reader, centerCorrect));
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
		suggestions = new SuggestionsList(x, 72, width, currentSuggestions, false);
	}
}
