package com.renan.ai.pl.classifier;

import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.javatuples.Triplet;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class EnglishTeamsFeatureExtraction {

	public static synchronized void main(String[] args) throws IOException {
		int teamCategory;
		String teamCategoryStr;
		double red, green, blue;

		String featureVectorFile = """
				@relation teams\n
				@attribute arsenal_main_colour real
				@attribute arsenal_goalkeeper_jersey_colour real
				@attribute city_main_colour real
				@attribute city_goalkeeper_jersey_colour real
				@attribute chelsea_main_colour real
				@attribute chelsea_goalkeeper_jersey_colour real
				@attribute classe {arsenal, chelseafc, mancity}\n
				@data\n""";

		File imageFolder = new File("src\\images");
		File[] imageFiles = imageFolder.listFiles();

		float[][] features = new float[32][7];

		for (int i = 0; i < imageFiles.length; i++) {

			Team team = Team.getByTeamPrefix(imageFiles[i].getName().split("_")[0]);
			teamCategory = team.getTeamCategory();
			teamCategoryStr = team.getPrefix();

			float[] featureArrayForImage = loadImageAndGetFeatureVector(imageFolder.getAbsolutePath() + "\\" + imageFiles[i].getName());

			features[i][0] = featureArrayForImage[0];
			features[i][1] = featureArrayForImage[1];

			features[i][2] = featureArrayForImage[2];
			features[i][3] = featureArrayForImage[3];

			features[i][4] = featureArrayForImage[4];
			features[i][5] = featureArrayForImage[5];

			features[i][6] = teamCategory;

			System.out.println("Arsenal main colour: " + features[i][0] + " - Arsenal goalkeeper jersey colour: "
					+ features[i][1] + " - City main colour: " + features[i][2] + " - City goalkeeper jersey colour: "
					+ features[i][3] + " - Chelsea main colour: " + features[i][4]
					+ " - Chelsea goalkeeper jersey colour: " + features[i][5] + " - Team category: " + features[i][6]);

			featureVectorFile += features[i][0] + "," + features[i][1] + "," + features[i][2] + "," + features[i][3]
					+ "," + features[i][4] + "," + features[i][5] + "," + teamCategoryStr + "\n";

		}
		File arffFile = new File("teams.arff");
		FileOutputStream outputStream = new FileOutputStream(arffFile);
		outputStream.write(featureVectorFile.getBytes());
		outputStream.close();
	}

	public static float[] loadImageAndGetFeatureVector(String imagePath) {
		
		SelectedTeamFeatures selectedTeamFeatures = new SelectedTeamFeatures();
		
		IplImage rawImage = loadPlayerImage(imagePath);
		
		iterateEveryPixelAndSetFeatureValues(selectedTeamFeatures, getFeatureSpecificationForEachTeam(), rawImage);

		setFeaturesPercentage(selectedTeamFeatures, rawImage);
		
		float[] featureArrayForImage = getFeatureArrayForImage(selectedTeamFeatures);
		return featureArrayForImage;
	}

	private static IplImage loadPlayerImage(String imagePath) {
		IplImage rawImage = cvLoadImage(imagePath);
		Logger.getLogger(Pixel.class.getName()).log(Level.INFO, imagePath);
		return rawImage;
	}

	private static void setFeaturesPercentage(SelectedTeamFeatures selectedTeamFeatures, IplImage rawImage) {
		selectedTeamFeatures.setArsenalMainColour(
				(selectedTeamFeatures.getArsenalMainColour() / (rawImage.height() * rawImage.width())) * 100);
		selectedTeamFeatures.setArsenalGoalkeeperJerseyColour(
				(selectedTeamFeatures.getArsenalGoalkeeperJerseyColour() / (rawImage.height() * rawImage.width()))
						* 100);

		selectedTeamFeatures.setCityMainColour(
				(selectedTeamFeatures.getCityMainColour() / (rawImage.height() * rawImage.width())) * 100);
		selectedTeamFeatures.setCityGoalkeeperJerseyColour(
				(selectedTeamFeatures.getCityGoalkeeperJerseyColour() / (rawImage.height() * rawImage.width())) * 100);

		selectedTeamFeatures.setChelseaMainColour(
				(selectedTeamFeatures.getChelseaMainColour() / (rawImage.height() * rawImage.width())) * 100);
		selectedTeamFeatures.setChelseaGoalkeeperJerseyColour(
				(selectedTeamFeatures.getChelseaGoalkeeperJerseyColour() / (rawImage.height() * rawImage.width()))
						* 100);
	}

	private static void iterateEveryPixelAndSetFeatureValues(SelectedTeamFeatures selectedTeamFeatures,
			List<Pixel> allMappedPixels, IplImage rawImage) {
		double red;
		double green;
		double blue;
		for (int height = 0; height < rawImage.height(); height++) {
			for (int width = 0; width < rawImage.width(); width++) {
				CvScalar rgbPixel = cvGet2D(rawImage, height, width);
				blue = rgbPixel.val(0);
				green = rgbPixel.val(1);
				red = rgbPixel.val(2);
				for (Pixel pixel : allMappedPixels) {
					if (pixel.match(red, green, blue)) {
						switch (pixel.getTeam()) {
						case ARSENAL: {
							switch (pixel.getType()) {
							case MAIN_COLOUR: {
								selectedTeamFeatures
										.setArsenalMainColour(selectedTeamFeatures.getArsenalMainColour() + 1);
								break;
							}
							case GOALKEEPER_JERSEY_COLOUR: {
								selectedTeamFeatures.setArsenalGoalkeeperJerseyColour(
										selectedTeamFeatures.getArsenalGoalkeeperJerseyColour() + 1);
								break;
							}
							}
							break;
						}
						case CITY: {
							switch (pixel.getType()) {
							case MAIN_COLOUR: {
								selectedTeamFeatures.setCityMainColour(selectedTeamFeatures.getCityMainColour() + 1);
								break;
							}
							case GOALKEEPER_JERSEY_COLOUR: {
								selectedTeamFeatures.setCityGoalkeeperJerseyColour(
										selectedTeamFeatures.getCityGoalkeeperJerseyColour() + 1);
								break;
							}
							}
							break;
						}
						case CHELSEA: {
							switch (pixel.getType()) {
							case MAIN_COLOUR: {
								selectedTeamFeatures
										.setChelseaMainColour(selectedTeamFeatures.getChelseaMainColour() + 1);
								break;
							}
							case GOALKEEPER_JERSEY_COLOUR: {
								selectedTeamFeatures.setChelseaGoalkeeperJerseyColour(
										selectedTeamFeatures.getChelseaGoalkeeperJerseyColour() + 1);
								break;
							}
							}
							break;
						}
						}
					}
				}
			}
		}
	}

	private static List<Pixel> getFeatureSpecificationForEachTeam() {
		Pixel arsenalMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.ARSENAL,
				List.of(new ColourRange(204, 19, 31), new ColourRange(119, 5, 5), new ColourRange(213, 19, 24),
						new ColourRange(174, 1, 7), new ColourRange(231, 92, 91), new ColourRange(219, 56, 51),
						new ColourRange(236, 35, 43), new ColourRange(104, 3, 12), new ColourRange(210, 27, 44),
						new ColourRange(196, 10, 33)));
		Pixel arsenalGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.ARSENAL,
				List.of(new ColourRange(255, 238, 136), new ColourRange(255, 229, 76)));

		Pixel cityMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.CITY,
				List.of(new ColourRange(179, 231, 255), new ColourRange(163, 215, 255), new ColourRange(125, 178, 240),
						new ColourRange(107, 159, 225), new ColourRange(124, 180, 252), new ColourRange(143, 195, 254),
						new ColourRange(194, 242, 255), new ColourRange(65, 182, 238), new ColourRange(120, 176, 217),
						new ColourRange(105, 162, 205)));

		Pixel cityGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.CITY,
				List.of(new ColourRange(255, 165, 96), new ColourRange(255, 181, 108)));

		Pixel chelseaMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.CHELSEA,
				List.of(new ColourRange(84, 103, 206), new ColourRange(54, 63, 187), new ColourRange(40, 84, 185),
						new ColourRange(41, 76, 168), new ColourRange(74, 81, 160), new ColourRange(66, 79, 162)));

		Pixel chelseaGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.CHELSEA,
				List.of(new ColourRange(255, 223, 159), new ColourRange(253, 170, 94)));
		return List.of(arsenalMainColourPixel, arsenalGoalkeeperJerseyColourPixel, cityMainColourPixel,
				arsenalGoalkeeperJerseyColourPixel, cityGoalkeeperJerseyColourPixel, chelseaMainColourPixel,
				chelseaMainColourPixel, chelseaGoalkeeperJerseyColourPixel);

	}
	
	private static float[] getFeatureArrayForImage(SelectedTeamFeatures selectedTeamFeatures) {
		return new float[] {
				selectedTeamFeatures.getArsenalMainColour(), 
				selectedTeamFeatures.getArsenalGoalkeeperJerseyColour(), 
				
				selectedTeamFeatures.getCityMainColour(), 
				selectedTeamFeatures.getCityGoalkeeperJerseyColour(), 
				
				selectedTeamFeatures.getChelseaMainColour(), 
				selectedTeamFeatures.getChelseaGoalkeeperJerseyColour()
		};
		
	}

}
