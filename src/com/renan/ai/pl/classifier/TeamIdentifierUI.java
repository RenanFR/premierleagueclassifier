package com.renan.ai.pl.classifier;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

public class TeamIdentifierUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtImagePath;
	private JLabel lblTeamImage;

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
					lblTeamImage.setIcon(new ImageIcon(icon.getImage().getScaledInstance(lblTeamImage.getWidth(), lblTeamImage.getHeight(), Image.SCALE_DEFAULT)));
				}
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
	}
}
