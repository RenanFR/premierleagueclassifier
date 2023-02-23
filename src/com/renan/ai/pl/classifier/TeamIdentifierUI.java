package com.renan.ai.pl.classifier;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TeamIdentifierUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtImagePath;
	private JLabel lblTeamImage;
	private JTextField txtArsenalMainColour;
	private JTextField txtArsenalGoalkeeperJerseyColour;
	private JTextField txtCityMainColour;
	private JTextField txtCityGoalkeeperJerseyColour;
	private JTextField txtChelseaMainColour;
	private JTextField txtChelseaGoalkeeperJerseyColour;
	private JTextField txtNaiveBayesArsenal;
	private JTextField txtNaiveBayesCity;
	private JTextField txtNaiveBayesChelsea;
	private JLabel lblNaiveBayesArsenal;
	private JLabel lblNaiveBayesCity;
	private JLabel lblNaiveBayesChelsea;

	private Instances instances;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TeamIdentifierUI frame = new TeamIdentifierUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TeamIdentifierUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 985, 695);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnLoadTeamImage = new JButton("Carregar imagem do time");
		btnLoadTeamImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(".\\src\\images"));
				int dialogUserActionResult = jfc.showDialog(contentPane, "Carregar imagem do time");
				if (dialogUserActionResult == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					txtImagePath.setText(selectedFile.getAbsolutePath());
					BufferedImage image = null;
					try {
						image = ImageIO.read(selectedFile);
					} catch (IOException ioException) {
						Logger.getLogger(TeamIdentifierUI.class.getName()).log(Level.SEVERE, null, ioException);
					}
					ImageIcon icon = new ImageIcon(image);
					lblTeamImage.setIcon(new ImageIcon(icon.getImage().getScaledInstance(lblTeamImage.getWidth(),
							lblTeamImage.getHeight(), Image.SCALE_DEFAULT)));
				}
				float[] featureArrayForImage = EnglishTeamsFeatureExtraction
						.loadImageAndGetFeatureVector(txtImagePath.getText());
				txtArsenalMainColour.setText(String.valueOf(featureArrayForImage[0]));
				txtArsenalGoalkeeperJerseyColour.setText(String.valueOf(featureArrayForImage[1]));

				txtCityMainColour.setText(String.valueOf(featureArrayForImage[2]));
				txtCityGoalkeeperJerseyColour.setText(String.valueOf(featureArrayForImage[3]));

				txtChelseaMainColour.setText(String.valueOf(featureArrayForImage[4]));
				txtChelseaGoalkeeperJerseyColour.setText(String.valueOf(featureArrayForImage[5]));
			}
		});
		btnLoadTeamImage.setBounds(10, 10, 220, 21);
		contentPane.add(btnLoadTeamImage);

		txtImagePath = new JTextField();
		txtImagePath.setBounds(255, 11, 400, 19);
		contentPane.add(txtImagePath);
		txtImagePath.setColumns(10);

		lblTeamImage = new JLabel("Bianca Costa - Ounana");
		lblTeamImage.setBounds(10, 51, 500, 500);
		contentPane.add(lblTeamImage);

		JLabel lblArsenalMainColour = new JLabel("Arsenal main colour");
		lblArsenalMainColour.setBounds(540, 75, 170, 13);
		contentPane.add(lblArsenalMainColour);

		JLabel lblArsenalGoalkeeperJerseyColour = new JLabel("Arsenal goalkeeper jersey colour");
		lblArsenalGoalkeeperJerseyColour.setBounds(540, 100, 170, 13);
		contentPane.add(lblArsenalGoalkeeperJerseyColour);

		JLabel lblCityMainColour = new JLabel("City main colour");
		lblCityMainColour.setBounds(540, 125, 170, 13);
		contentPane.add(lblCityMainColour);

		JLabel lblCityGoalkeeperJerseyColour = new JLabel("City goalkeeper jersey colour");
		lblCityGoalkeeperJerseyColour.setBounds(540, 150, 170, 13);
		contentPane.add(lblCityGoalkeeperJerseyColour);

		JLabel lblChelseaMainColour = new JLabel("Chelsea main colour");
		lblChelseaMainColour.setBounds(540, 175, 170, 13);
		contentPane.add(lblChelseaMainColour);

		JLabel lblChelseaGoalkeeperJerseyColour = new JLabel("Chelsea goalkeeper jerseycolour");
		lblChelseaGoalkeeperJerseyColour.setBounds(540, 200, 170, 13);
		contentPane.add(lblChelseaGoalkeeperJerseyColour);

		txtArsenalMainColour = new JTextField();
		txtArsenalMainColour.setBounds(720, 72, 96, 19);
		contentPane.add(txtArsenalMainColour);
		txtArsenalMainColour.setColumns(10);

		txtArsenalGoalkeeperJerseyColour = new JTextField();
		txtArsenalGoalkeeperJerseyColour.setColumns(10);
		txtArsenalGoalkeeperJerseyColour.setBounds(720, 97, 96, 19);
		contentPane.add(txtArsenalGoalkeeperJerseyColour);

		txtCityMainColour = new JTextField();
		txtCityMainColour.setColumns(10);
		txtCityMainColour.setBounds(720, 122, 96, 19);
		contentPane.add(txtCityMainColour);

		txtCityGoalkeeperJerseyColour = new JTextField();
		txtCityGoalkeeperJerseyColour.setColumns(10);
		txtCityGoalkeeperJerseyColour.setBounds(720, 147, 96, 19);
		contentPane.add(txtCityGoalkeeperJerseyColour);

		txtChelseaMainColour = new JTextField();
		txtChelseaMainColour.setColumns(10);
		txtChelseaMainColour.setBounds(720, 172, 96, 19);
		contentPane.add(txtChelseaMainColour);

		txtChelseaGoalkeeperJerseyColour = new JTextField();
		txtChelseaGoalkeeperJerseyColour.setColumns(10);
		txtChelseaGoalkeeperJerseyColour.setBounds(720, 197, 96, 19);
		contentPane.add(txtChelseaGoalkeeperJerseyColour);

		JLabel lblNaiveBayes = new JLabel("Naive Bayes");
		lblNaiveBayes.setBounds(665, 269, 76, 13);
		contentPane.add(lblNaiveBayes);

		txtNaiveBayesChelsea = new JTextField();
		txtNaiveBayesChelsea.setBounds(645, 350, 96, 19);
		contentPane.add(txtNaiveBayesChelsea);
		txtNaiveBayesChelsea.setColumns(10);

		txtNaiveBayesArsenal = new JTextField();
		txtNaiveBayesArsenal.setColumns(10);
		txtNaiveBayesArsenal.setBounds(645, 292, 96, 19);
		contentPane.add(txtNaiveBayesArsenal);

		txtNaiveBayesCity = new JTextField();
		txtNaiveBayesCity.setColumns(10);
		txtNaiveBayesCity.setBounds(645, 321, 96, 19);
		contentPane.add(txtNaiveBayesCity);

		lblNaiveBayesArsenal = new JLabel("Arsenal %");
		lblNaiveBayesArsenal.setBounds(559, 295, 76, 13);
		contentPane.add(lblNaiveBayesArsenal);

		lblNaiveBayesCity = new JLabel("City %");
		lblNaiveBayesCity.setBounds(559, 324, 76, 13);
		contentPane.add(lblNaiveBayesCity);

		lblNaiveBayesChelsea = new JLabel("Chelsea %");
		lblNaiveBayesChelsea.setBounds(559, 353, 76, 13);
		contentPane.add(lblNaiveBayesChelsea);

		JButton btnClassificar = new JButton("Classificar");
		btnClassificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					loadLearningDatabase();
					classifyPlayer();

				} catch (Exception exception) {
					Logger.getLogger(TeamIdentifierUI.class.getName()).log(Level.SEVERE, null, exception);
				}
			}


		});
		btnClassificar.setBounds(665, 10, 220, 21);
		contentPane.add(btnClassificar);
	}

	private void loadLearningDatabase() throws Exception {
		DataSource source = new DataSource("teams.arff");
		instances = source.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);
	}
	
	private void classifyPlayer() throws Exception {
		NaiveBayes naiveBayes = new NaiveBayes();
		naiveBayes.buildClassifier(instances);

		Instance instanceToClassify = new DenseInstance(instances.numAttributes());
		instanceToClassify.setDataset(instances);
		instanceToClassify.setValue(0, Float.parseFloat(txtArsenalMainColour.getText()));
		instanceToClassify.setValue(1, Float.parseFloat(txtArsenalGoalkeeperJerseyColour.getText()));
		instanceToClassify.setValue(2, Float.parseFloat(txtCityMainColour.getText()));
		instanceToClassify.setValue(3, Float.parseFloat(txtCityGoalkeeperJerseyColour.getText()));
		instanceToClassify.setValue(4, Float.parseFloat(txtChelseaMainColour.getText()));
		instanceToClassify.setValue(5, Float.parseFloat(txtChelseaGoalkeeperJerseyColour.getText()));

		double[] result = naiveBayes.distributionForInstance(instanceToClassify);
		DecimalFormat decimalFormat = new DecimalFormat("#,###.0000");

		txtNaiveBayesArsenal.setText(decimalFormat.format(result[0]));
		txtNaiveBayesCity.setText(decimalFormat.format(result[2]));
		txtNaiveBayesChelsea.setText(decimalFormat.format(result[1]));
	}
}
