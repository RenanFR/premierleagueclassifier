package com.renan.ai.pl.classifier;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.javatuples.Triplet;

public class Pixel {

	private PixelType type;
	private Team team;
	private List<Triplet<Integer, Integer, Integer>> rgbValues;

	public Pixel(PixelType type, Team team, List<Triplet<Integer, Integer, Integer>> rgbValues) {
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

	public List<Triplet<Integer, Integer, Integer>> getRgbValues() {
		return rgbValues;
	}

	public boolean match(double red, double green, double blue) {
		boolean match = rgbValues.stream()
				.anyMatch(rgb -> rgb.getValue0() == red && rgb.getValue1() == green && rgb.getValue2() == blue);
		if (match) {
//			Logger.getLogger(Pixel.class.getName()).log(Level.INFO,
//					team.getPrefix() + " " + type + " (" + red + ", " + green + ", " + blue + ")");
		}
		return match;
	}

}
