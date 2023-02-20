package com.renan.ai.pl.classifier;

public enum Team {

	ARSENAL("arsenal", 1), CITY("mancity", 2), MANCHESTER_UNITED("manutd", 3), CHELSEA("chelseafc", 10);

	private String prefix;
	private int teamCategory;

	private Team(String prefix, int teamCategory) {
		this.prefix = prefix;
		this.teamCategory = teamCategory;
	}

	public int getTeamCategory() {
		return teamCategory;
	}

	public String getPrefix() {
		return prefix;
	}

	public static Team getByTeamPrefix(String prefix) {
		return switch (prefix) {
		case "arsenal" -> ARSENAL;
		case "mancity" -> CITY;
		case "manutd" -> MANCHESTER_UNITED;
		case "chelseafc" -> CHELSEA;
		default -> null;
		};
	}

}
