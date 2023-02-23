package com.renan.ai.pl.classifier;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class PlayerPositionByNaiveBayes {
	public static void main(String[] args) throws Exception {
		StringBuilder featureVectorFile = new StringBuilder();
		StringBuilder featureVectorFileData = new StringBuilder();

		Map<String, Set<String>> possibleValuesForAttributes = new HashMap<>();

		String featureVectorHeader = """
				@relation players\n
				@attribute height  real
				@attribute kit_number  real
				@attribute crossing  real
				@attribute finishing  real
				@attribute heading_accuracy  real
				@attribute short_passing  real
				@attribute volleys  real
				@attribute dribbling  real
				@attribute curve  real
				@attribute fk_accuracy  real
				@attribute long_passing  real
				@attribute ball_control  real
				@attribute acceleration  real
				@attribute sprint_speed  real
				@attribute agility  real
				@attribute reactions  real
				@attribute balance  real
				@attribute shot_power  real
				@attribute jumping  real
				@attribute stamina  real
				@attribute strength  real
				@attribute long_shots  real
				@attribute aggression  real
				@attribute interceptions  real
				@attribute positioning  real
				@attribute vision  real
				@attribute penalties  real
				@attribute composure  real
				@attribute defensive_awareness  real
				@attribute standing_tackle  real
				@attribute sliding_tackle  real
				@attribute gk_diving  real
				@attribute gk_handling  real
				@attribute gk_kicking  real
				@attribute gk_positioning  real
				@attribute gk_reflexes  real
				@attribute position  {}\n
				@data\n""";

		Formatter formatter = new Formatter();

		List<Player> samplePlayers = List.of(
				new Player("W. Saliba", 193, 12, new Attacking(58, 37, 69, 81, 34), new Skill(72, 55, 35, 74, 74),
						new Movement(77, 84, 50, 82, 59), new Power(60, 72, 69, 86, 23),
						new Mentality(82, 84, 43, 62, 47, 79), new Defending(84, 86, 82),
						new Goalkeeping(7, 10, 6, 8, 11), "CB"),

				new Player("M. Hummels", 191, 15, new Attacking(65, 57, 86, 81, 64), new Skill(69, 66, 53, 84, 77),
						new Movement(45, 55, 60, 86, 62), new Power(71, 70, 66, 87, 49),
						new Mentality(71, 87, 57, 80, 68, 89), new Defending(88, 86, 85),
						new Goalkeeping(15, 6, 10, 5, 6), "CB"),

				new Player("Gabriel", 190, 6, new Attacking(38, 31, 85, 78, 28), new Skill(52, 47, 27, 74, 67),
						new Movement(61, 73, 51, 81, 37), new Power(51, 70, 66, 87, 30),
						new Mentality(84, 80, 44, 59, 40, 74), new Defending(83, 84, 81),
						new Goalkeeping(12, 8, 13, 10, 7), "CB"),

				new Player("M. Maignan", 191, 16, new Attacking(18, 15, 17, 57, 16), new Skill(31, 38, 16, 53, 40),
						new Movement(50, 53, 41, 85, 56), new Power(64, 74, 39, 78, 20),
						new Mentality(35, 25, 15, 61, 15, 66), new Defending(23, 16, 19),
						new Goalkeeping(85, 82, 85, 85, 89), "GK"),

				new Player("H. Lloris", 188, 1, new Attacking(13, 10, 9, 52, 15), new Skill(12, 12, 10, 55, 29),
						new Movement(61, 59, 54, 83, 56), new Power(53, 72, 41, 43, 14),
						new Mentality(31, 25, 9, 57, 40, 55), new Defending(24, 12, 18),
						new Goalkeeping(86, 82, 70, 82, 86), "GK"),

				new Player("Kepa", 189, 1, new Attacking(25, 11, 18, 60, 18), new Skill(15, 25, 19, 60, 34),
						new Movement(34, 33, 41, 71, 39), new Power(64, 71, 37, 51, 14),
						new Mentality(18, 21, 15, 61, 24, 63), new Defending(19, 18, 16),
						new Goalkeeping(80, 78, 85, 78, 84), "GK"),

				new Player("M. Ødegaard", 178, 8, new Attacking(84, 76, 52, 89, 76), new Skill(86, 83, 85, 84, 87),
						new Movement(78, 71, 85, 77, 83), new Power(80, 46, 81, 56, 78),
						new Mentality(63, 65, 81, 89, 63, 78), new Defending(65, 49, 51),
						new Goalkeeping(14, 15, 15, 8, 10), "CAM"),

				new Player("Bernardo Silva", 173, 20, new Attacking(84, 78, 51, 88, 78), new Skill(92, 84, 67, 78, 91),
						new Movement(81, 65, 94, 87, 92), new Power(79, 51, 93, 54, 78),
						new Mentality(77, 65, 83, 84, 68, 89), new Defending(66, 73, 58),
						new Goalkeeping(9, 10, 14, 12, 9), "CAM"),

				new Player("Bruno Fernandes", 179, 8, new Attacking(86, 83, 64, 89, 87), new Skill(79, 86, 85, 87, 84),
						new Movement(75, 69, 78, 91, 79), new Power(88, 71, 93, 66, 88),
						new Mentality(82, 67, 85, 90, 90, 79), new Defending(66, 73, 65),
						new Goalkeeping(12, 14, 15, 8, 14), "CAM"),

				new Player("D. Núñez", 187, 27, new Attacking(67, 83, 76, 69, 79), new Skill(76, 75, 61, 64, 76),
						new Movement(88, 91, 78, 85, 67), new Power(86, 78, 89, 87, 75),
						new Mentality(80, 41, 86, 73, 78, 82), new Defending(36, 39, 33),
						new Goalkeeping(14, 10, 12, 10, 13), "ST"),

				new Player("V. Osimhen", 185, 9, new Attacking(62, 90, 81, 72, 76), new Skill(80, 63, 48, 53, 80),
						new Movement(87, 93, 78, 85, 68), new Power(80, 90, 81, 86, 73),
						new Mentality(70, 34, 86, 70, 75, 79), new Defending(36, 45, 22),
						new Goalkeeping(14, 14, 10, 9, 6), "ST"),

				new Player("Morata", 190, 19, new Attacking(72, 84, 85, 76, 79), new Skill(78, 78, 50, 63, 82),
						new Movement(81, 84, 74, 80, 62), new Power(80, 86, 78, 81, 74),
						new Mentality(69, 26, 84, 77, 75, 78), new Defending(37, 14, 20),
						new Goalkeeping(4, 5, 4, 4, 5), "ST"),

				new Player("W. Weghorst", 197, 27, new Attacking(42, 78, 90, 75, 80), new Skill(65, 40, 48, 51, 79),
						new Movement(53, 63, 62, 81, 42), new Power(84, 79, 81, 90, 70),
						new Mentality(85, 34, 80, 67, 71, 81), new Defending(44, 63, 38),
						new Goalkeeping(11, 9, 14, 16, 12), "ST"),

				new Player("S. Haller", 190, 9, new Attacking(42, 83, 86, 76, 83), new Skill(74, 57, 46, 51, 78),
						new Movement(56, 73, 66, 82, 52), new Power(83, 82, 73, 92, 65),
						new Mentality(67, 36, 87, 72, 88, 82), new Defending(40, 61, 56),
						new Goalkeeping(6, 7, 14, 7, 6), "ST"),

				new Player("O. Giroud", 193, 9, new Attacking(64, 86, 91, 78, 86), new Skill(69, 71, 65, 60, 85),
						new Movement(41, 43, 54, 84, 53), new Power(86, 82, 55, 90, 69),
						new Mentality(76, 42, 87, 78, 90, 85), new Defending(37, 37, 20),
						new Goalkeeping(12, 15, 11, 6, 5), "ST"),

				new Player("R. Lewandowski", 185, 9, new Attacking(71, 94, 91, 84, 89), new Skill(85, 79, 85, 70, 89),
						new Movement(76, 75, 77, 93, 82), new Power(91, 85, 76, 87, 84),
						new Mentality(81, 49, 94, 81, 90, 88), new Defending(35, 42, 19),
						new Goalkeeping(15, 6, 12, 8, 10), "ST"),

				new Player("P. Aubameyang", 187, 9, new Attacking(72, 86, 76, 75, 82), new Skill(78, 80, 76, 64, 80),
						new Movement(80, 87, 74, 84, 67), new Power(83, 75, 67, 71, 79),
						new Mentality(43, 48, 87, 74, 82, 80), new Defending(20, 29, 36),
						new Goalkeeping(6, 9, 15, 9, 9), "ST"),

				new Player("F. Wirtz", 176, 27, new Attacking(77, 78, 39, 82, 68), new Skill(86, 76, 54, 82, 88),
						new Movement(76, 79, 85, 80, 82), new Power(67, 52, 79, 50, 70),
						new Mentality(63, 45, 78, 85, 60, 80), new Defending(55, 57, 44),
						new Goalkeeping(14, 14, 13, 6, 10), "CAM"),

				new Player("N. Schlotterbeck", 191, 4, new Attacking(48, 49, 84, 74, 51), new Skill(72, 49, 35, 79, 71),
						new Movement(77, 76, 68, 81, 66), new Power(75, 78, 73, 82, 60),
						new Mentality(81, 82, 56, 67, 38, 76), new Defending(81, 86, 80),
						new Goalkeeping(8, 10, 8, 14, 13), "CB"),

				new Player("J. Pickford", 185, 1, new Attacking(17, 19, 15, 58, 20), new Skill(14, 18, 16, 60, 40),
						new Movement(56, 44, 57, 77, 55), new Power(65, 77, 45, 70, 13),
						new Mentality(45, 24, 18, 69, 45, 65), new Defending(21, 20, 12),
						new Goalkeeping(83, 77, 87, 80, 87), "GK"),
				new Player("Robert Sánchez", 197, 1, new Attacking(12, 14, 15, 51, 15), new Skill(15, 12, 14, 55, 37),
						new Movement(45, 44, 40, 73, 35), new Power(35, 73, 40, 66, 16),
						new Mentality(33, 8, 12, 65, 15, 65), new Defending(18, 13, 15),
						new Goalkeeping(79, 76, 74, 79, 80), "GK"));


		formatter.format("%15s %15s %15s %15s %15s %15s %15s %15s %15s %15s\n", "HEIGHT", "KIT_NUMBER", "ATTACKING",
				"SKILL", "MOVEMENT", "POWER", "MENTALITY", "DEFENDING", "GOALKEEPING", "POSITION");

		samplePlayers.forEach(feature -> {

			fillDistinctValuesForHeader("position", feature.position(), possibleValuesForAttributes);

			featureVectorFileData.append(feature.height() + ",");
			featureVectorFileData.append(feature.kitNumber() + ",");
			
			featureVectorFileData.append(feature.attacking().crossing() + ",");
			featureVectorFileData.append(feature.attacking().finishing() + ",");
			featureVectorFileData.append(feature.attacking().headingAccuracy() + ",");
			featureVectorFileData.append(feature.attacking().shortPassing() + ",");
			featureVectorFileData.append(feature.attacking().volleys() + ",");
			
			featureVectorFileData.append(feature.skill().dribbling() + ",");
			featureVectorFileData.append(feature.skill().curve() + ",");
			featureVectorFileData.append(feature.skill().fkAccuracy() + ",");
			featureVectorFileData.append(feature.skill().longPassing() + ",");
			featureVectorFileData.append(feature.skill().ballControl() + ",");
			
			featureVectorFileData.append(feature.movement().acceleration() + ",");
			featureVectorFileData.append(feature.movement().sprintSpeed() + ",");
			featureVectorFileData.append(feature.movement().agility() + ",");
			featureVectorFileData.append(feature.movement().reactions() + ",");
			featureVectorFileData.append(feature.movement().balance() + ",");
			
			featureVectorFileData.append(feature.power().shotPower() + ",");
			featureVectorFileData.append(feature.power().jumping() + ",");
			featureVectorFileData.append(feature.power().stamina() + ",");
			featureVectorFileData.append(feature.power().strength() + ",");
			featureVectorFileData.append(feature.power().longShots() + ",");
			
			featureVectorFileData.append(feature.mentality().aggression() + ",");
			featureVectorFileData.append(feature.mentality().interceptions() + ",");
			featureVectorFileData.append(feature.mentality().positioning() + ",");
			featureVectorFileData.append(feature.mentality().vision() + ",");
			featureVectorFileData.append(feature.mentality().penalties() + ",");
			featureVectorFileData.append(feature.mentality().composure() + ",");
			
			featureVectorFileData.append(feature.defending().defensiveAwareness() + ",");
			featureVectorFileData.append(feature.defending().standingTackle() + ",");
			featureVectorFileData.append(feature.defending().slidingTackle() + ",");
			
			featureVectorFileData.append(feature.goalkeeping().gkDiving() + ",");
			featureVectorFileData.append(feature.goalkeeping().gkHandling() + ",");
			featureVectorFileData.append(feature.goalkeeping().gkKicking() + ",");
			featureVectorFileData.append(feature.goalkeeping().gkPositioning() + ",");
			featureVectorFileData.append(feature.goalkeeping().gkReflexes() + ",");
			
			featureVectorFileData.append(feature.position());
			
			featureVectorFileData.append("\n");

			formatter.format("%14.7s %14.6s %14.6s %14.6s %14.6s %14.6s %14.6s %14.6s %14.6s %17s\n", feature.height(),
					feature.kitNumber(), feature.attacking(), feature.skill(), feature.movement(), feature.power(),
					feature.mentality(), feature.defending(), feature.goalkeeping(), feature.position());
		});

		for (Map.Entry<String, Set<String>> entry : possibleValuesForAttributes.entrySet()) {
			String attribute = entry.getKey();
			Set<String> attributeValue = entry.getValue();
			featureVectorHeader = featureVectorHeader.replace(attribute + "  {}",
					attribute + "  " + attributeValue.stream().collect(Collectors.joining(",", "{", "}")));

		}

		featureVectorFile.append(featureVectorHeader);
		featureVectorFile.append(featureVectorFileData);

		System.out.println(formatter);

		List<Player> playersToClassify = List.of(new Player("Marc Bartra", 184, 3, new Attacking(57, 55, 79, 81, 43),
				new Skill(70, 66, 51, 79, 78), new Movement(69, 72, 69, 82, 71), new Power(73, 89, 69, 77, 68),
				new Mentality(77, 83, 61, 76, 52, 76), new Defending(79, 81, 82), new Goalkeeping(10, 15, 13, 12, 8),
				"CB"));

		File arffFile = new File("players.arff");
		FileOutputStream outputStream = new FileOutputStream(arffFile);
		outputStream.write(featureVectorFile.toString().getBytes());
		outputStream.close();

		DataSource source = new DataSource("players.arff");
		Instances instances = source.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);
		NaiveBayes naiveBayes = new NaiveBayes();
		naiveBayes.buildClassifier(instances);

		Formatter resultTable = new Formatter();
		resultTable.format("%14.15s %14.6s %14.6s %14.6s %17s\n", "PLAYER", "GK %", "CB %", "CAM %", "ST %");

		playersToClassify.forEach(player -> {

			try {
				Instance instanceToClassify = new DenseInstance(instances.numAttributes());
				instanceToClassify.setDataset(instances);
				
				instanceToClassify.setValue(0, player.height());
				instanceToClassify.setValue(1, player.kitNumber());
				
				instanceToClassify.setValue(2, player.attacking().crossing());
				instanceToClassify.setValue(3, player.attacking().finishing());
				instanceToClassify.setValue(4, player.attacking().headingAccuracy());
				instanceToClassify.setValue(5, player.attacking().shortPassing());
				instanceToClassify.setValue(6, player.attacking().volleys());
				
				instanceToClassify.setValue(7, player.skill().dribbling());
				instanceToClassify.setValue(8, player.skill().curve());
				instanceToClassify.setValue(9, player.skill().fkAccuracy());
				instanceToClassify.setValue(10, player.skill().longPassing());
				instanceToClassify.setValue(11, player.skill().ballControl());
				
				instanceToClassify.setValue(12, player.movement().acceleration());
				instanceToClassify.setValue(13, player.movement().sprintSpeed());
				instanceToClassify.setValue(14, player.movement().agility());
				instanceToClassify.setValue(15, player.movement().reactions());
				instanceToClassify.setValue(16, player.movement().balance());
				
				instanceToClassify.setValue(17, player.power().shotPower());
				instanceToClassify.setValue(18, player.power().jumping());
				instanceToClassify.setValue(19, player.power().stamina());
				instanceToClassify.setValue(20, player.power().strength());
				instanceToClassify.setValue(21, player.power().longShots());
				
				instanceToClassify.setValue(22, player.mentality().aggression());
				instanceToClassify.setValue(23, player.mentality().interceptions());
				instanceToClassify.setValue(24, player.mentality().positioning());
				instanceToClassify.setValue(25, player.mentality().vision());
				instanceToClassify.setValue(26, player.mentality().penalties());
				instanceToClassify.setValue(27, player.mentality().composure());
				
				instanceToClassify.setValue(28, player.defending().defensiveAwareness());
				instanceToClassify.setValue(29, player.defending().standingTackle());
				instanceToClassify.setValue(30, player.defending().slidingTackle());
				
				instanceToClassify.setValue(31, player.goalkeeping().gkDiving());
				instanceToClassify.setValue(32, player.goalkeeping().gkHandling());
				instanceToClassify.setValue(33, player.goalkeeping().gkKicking());
				instanceToClassify.setValue(34, player.goalkeeping().gkPositioning());
				instanceToClassify.setValue(35, player.goalkeeping().gkReflexes());

				double[] result = naiveBayes.distributionForInstance(instanceToClassify);
				DecimalFormat decimalFormat = new DecimalFormat("0.00");

				resultTable.format("%14.15s %14.6s %14.6s %14.6s %17s\n", player.name(),
						decimalFormat.format(result[1]), decimalFormat.format(result[3]),
						decimalFormat.format(result[2]), decimalFormat.format(result[0]));

			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		System.out.println(resultTable);

	}

	private static void fillDistinctValuesForHeader(String attribute, String attributeValue,
			Map<String, Set<String>> possibleValuesForAttributes) {
		possibleValuesForAttributes.merge(attribute, new HashSet<>(Arrays.asList(attributeValue)), (oldSet, newSet) -> {
			newSet.addAll(oldSet);
			return newSet;
		});
	}

	private static String getRange(double featureValue) {
		int largerMultiple = (int) (5 * (Math.ceil(Math.abs(featureValue / 5))));
		int smallerMultiple = largerMultiple - 5;
		String featureOverallRange = smallerMultiple + "-" + largerMultiple;
		return featureOverallRange;
	}
}
