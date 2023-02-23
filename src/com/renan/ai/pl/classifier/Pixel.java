package com.renan.ai.pl.classifier;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pixel {

	private PixelType type;
	private Team team;
	private List<ColourRange> rgbValues;

	public Pixel(PixelType type, Team team, List<ColourRange> rgbValues) {
		this.type = type;
		this.team = team;
		this.rgbValues = rgbValues;
	}

	public PixelType getType() {
		return type;
	}

	public Team getTeam() {
		return team;
	}

	public List<ColourRange> getRgbValues() {
		return rgbValues;
	}

	public boolean match(double red, double green, double blue) {
		Predicate<ColourRange> matchColour = rgb -> (red >= rgb.getMinR() && red <= rgb.getMaxR())
				&& (green >= rgb.getMinG() && green <= rgb.getMaxG())
				&& (blue >= rgb.getMinB() && blue <= rgb.getMaxB());
		boolean match = rgbValues.stream().anyMatch(matchColour);
		if (match) {
			Logger.getLogger(Pixel.class.getName()).log(Level.INFO,
					team.getPrefix() + " " + type + " (" + red + ", " + green + ", " + blue + ")");
		}
		return match;
	}

}
