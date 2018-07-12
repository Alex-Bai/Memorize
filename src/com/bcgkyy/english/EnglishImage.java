package com.bcgkyy.english;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.bcgkyy.db.DBManagement;

public class EnglishImage {

	private JFrame frame;
	private JTextField textField_word;
	private JLabel label_image;
	private DBManagement dbManagement;
	private final String outputImageFolder = "EnglishImages";
	private final String dbTableName = "wordImage.xlsx";
	private final String dbSheetName = "wordImage";
	private String outputFileName;
	private String action = "study";	
	private final int IMAGE_WIDTH = 693;
	private final int IMAGE_HEIGHT = 527;
	private int imageIndex;
	private List<String> wordImages;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EnglishImage window = new EnglishImage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EnglishImage() {
		dbManagement = new DBManagement(dbTableName, dbSheetName);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField_word = new JTextField();
		textField_word.setBounds(126, 46, 130, 26);
		frame.getContentPane().add(textField_word);
		textField_word.setColumns(10);
		
		JLabel lblInputWord = new JLabel("Input Word:");
		lblInputWord.setBounds(35, 51, 79, 16);
		frame.getContentPane().add(lblInputWord);
							
		JButton btnUploadImage = new JButton("Upload Image");
		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				int returnVal = jfc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = jfc.getSelectedFile();
		            outputFileName = outputImageFolder+"/"+file.getName();
		            writeFile(file, outputFileName);		            
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		btnUploadImage.setBounds(268, 47, 137, 26);
		frame.getContentPane().add(btnUploadImage);
		
		JButton btnSaveRecord = new JButton("Save Record");
		btnSaveRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputWord = textField_word.getText().trim();
				String[] record = new String[] {inputWord, outputFileName};
				try {
					dbManagement.insertRecord(record);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSaveRecord.setBounds(429, 46, 117, 29);
		frame.getContentPane().add(btnSaveRecord);
		
		label_image = new JLabel("");
		label_image.setBounds(35, 89, IMAGE_WIDTH, IMAGE_HEIGHT);
		frame.getContentPane().add(label_image);
		
		JButton btnPre = new JButton("pre");
		btnPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPre.setBounds(377, 639, 117, 29);
		frame.getContentPane().add(btnPre);
		
		JButton btnNext = new JButton("next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNext.setBounds(540, 639, 117, 29);
		frame.getContentPane().add(btnNext);
		
		JRadioButton rdbtnStudy = new JRadioButton("study");
		rdbtnStudy.setSelected(true);
		rdbtnStudy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = e.getActionCommand();
			}
		});
		rdbtnStudy.setBounds(35, 637, 79, 26);
		frame.getContentPane().add(rdbtnStudy);
		
		JRadioButton rdbtnReview = new JRadioButton("review");
		rdbtnReview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = e.getActionCommand();
			}
		});
		rdbtnReview.setBounds(126, 639, 92, 26);
		frame.getContentPane().add(rdbtnReview);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnStudy);
		group.add(rdbtnReview);
		
		JLabel labelWord = new JLabel("");
		labelWord.setBounds(750, 344, 117, 52);
		frame.getContentPane().add(labelWord);
		
		JButton btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					wordImages = dbManagement.getRecords();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(action.equals("study")) {
					imageIndex = 0;
					String[] wordImage = wordImages.get(imageIndex++).split(",", 2);
					String imageUrl = wordImage[1];
					label_image.setIcon(getScaledImage(imageUrl));
					labelWord.setText(wordImage[0]);
				}else if(action.equals("review")) {
					
				}
			}
		});
		btnStart.setBounds(217, 639, 117, 29);
		frame.getContentPane().add(btnStart);
		
		File directory = new File(outputImageFolder);
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}
	
	private ImageIcon getScaledImage(String imagePath){				 
		try {
			//Image srcImg = ImageIO.read(this.getClass().getResource(imagePath));
			Image srcImg = ImageIO.read(new File(imagePath));			
			BufferedImage resizedImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resizedImg.createGraphics();

		    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2.drawImage(srcImg, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
		    g2.dispose();
		    return new ImageIcon(resizedImg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	    
	}
	
	public void readDataFromDB() {
		try {
			List<String> wordImages = dbManagement.getRecords();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeFile(File srcFile, String outputFilePath) {
		BufferedImage srcImg;
		try {
			srcImg = ImageIO.read(srcFile);
			File outputFile = new File(outputFilePath);
		    ImageIO.write(srcImg, "png", outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
	}
}
