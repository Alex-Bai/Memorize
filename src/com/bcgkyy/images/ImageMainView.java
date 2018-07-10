package com.bcgkyy.images;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.bcgkyy.db.DBManagement;
import com.bcgkyy.demo.NumberImage;

public class ImageMainView {

	private JFrame frame;
	private String dbTableName = "imageNumber.xlsx";
	private String dbSheetName = "imageMapping";
	private DBManagement dbManagement;
	private List<NumberImage> numberImages;
	private final int IMAGE_WIDTH = 449;
	private final int IMAGE_HEIGHT = 384;
	private final int IMAGE_LEFT = 0;
	private final int IMAGE_RIGHT = 1;
	private int firstImageIndex = 0;
	private int secondImageIndex = 0;	
	
	private JLabel label_image1;
	private JLabel label_image2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageMainView window = new ImageMainView();
					window.readImagesFromDB();
					window.initialize();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ImageMainView() {
		dbManagement = new DBManagement(dbTableName, dbSheetName);
		numberImages = new LinkedList<NumberImage>();
	}
	
	/**
	 * Create the application.
	 */
	public ImageMainView(String dbTableName, String dbSheetName) {
		this.dbTableName = dbTableName;
		this.dbSheetName = dbSheetName;
		dbManagement = new DBManagement(dbTableName, dbSheetName);
		numberImages = new LinkedList<NumberImage>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		numberImages = readImagesFromDB();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JButton btnPre_image1 = new JButton("pre");
		btnPre_image1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(firstImageIndex == 0) {
					firstImageIndex += numberImages.size();
				}
				String firstImageUrl = numberImages.get((--firstImageIndex) % numberImages.size()).getUrls().get(IMAGE_LEFT);
				label_image1.setIcon(getScaledImage(firstImageUrl, IMAGE_WIDTH, IMAGE_HEIGHT));
				
			}
		});
		btnPre_image1.setBounds(122, 473, 117, 29);
		frame.getContentPane().add(btnPre_image1);
		
		JButton btnNext_image1 = new JButton("next");
		btnNext_image1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstImageUrl = numberImages.get((++firstImageIndex) % numberImages.size()).getUrls().get(IMAGE_LEFT);
				label_image1.setIcon(getScaledImage(firstImageUrl, IMAGE_WIDTH, IMAGE_HEIGHT));
			}
		});
		btnNext_image1.setBounds(290, 473, 117, 29);
		frame.getContentPane().add(btnNext_image1);
		
		JButton btnPre_image2 = new JButton("pre");
		btnPre_image2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(secondImageIndex == 0) {
					secondImageIndex += numberImages.size();
				}
				String secondImageUrl = numberImages.get((--secondImageIndex) % numberImages.size()).getUrls().get(IMAGE_RIGHT);
				label_image2.setIcon(getScaledImage(secondImageUrl, IMAGE_WIDTH, IMAGE_HEIGHT));
			}
		});
		btnPre_image2.setBounds(596, 473, 117, 29);
		frame.getContentPane().add(btnPre_image2);
		
		JButton btnNext_image2 = new JButton("next");
		btnNext_image2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String secondImageUrl = numberImages.get((++secondImageIndex) % numberImages.size()).getUrls().get(IMAGE_RIGHT);
				label_image2.setIcon(getScaledImage(secondImageUrl, IMAGE_WIDTH, IMAGE_HEIGHT));
			}
		});
		btnNext_image2.setBounds(762, 473, 117, 29);
		frame.getContentPane().add(btnNext_image2);
		
		String firstImageUrl = numberImages.get(0).getUrls().get(0);
		
		label_image1 = new JLabel(getScaledImage(firstImageUrl, IMAGE_WIDTH, IMAGE_HEIGHT));
		label_image1.setBounds(40, 52, IMAGE_WIDTH, IMAGE_HEIGHT);			
		frame.getContentPane().add(label_image1);
		
		label_image2 = new JLabel(getScaledImage(firstImageUrl, IMAGE_WIDTH, IMAGE_HEIGHT));
		label_image2.setBounds(522, 52, IMAGE_WIDTH, IMAGE_HEIGHT);
		frame.getContentPane().add(label_image2);		
		
	}
	
	private ImageIcon getScaledImage(String imagePath, int w, int h){				 
		try {
			Image srcImg = ImageIO.read(this.getClass().getResource(imagePath));
			BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resizedImg.createGraphics();

		    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2.drawImage(srcImg, 0, 0, w, h, null);
		    g2.dispose();
		    return new ImageIcon(resizedImg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	    
	}
	
	public List<NumberImage> readImagesFromDB() {
		List<NumberImage> numberImages = new LinkedList<NumberImage>();		
		try {			
			List<String> imageNumbers = dbManagement.getRecords();
			if(!imageNumbers.isEmpty()) {
				for(String str : imageNumbers) {
					String[] imageStrArr = str.split(",", 2);		
					if(imageStrArr.length == 2) {
						NumberImage numberImage = new NumberImage(imageStrArr[0], imageStrArr[1]);
						numberImages.add(numberImage);
					}				
				}
			}					
		} catch (IOException e) {
			e.printStackTrace();
		}
		numberImages.sort(new Comparator<NumberImage>() {
			@Override
			public int compare(NumberImage ni1, NumberImage ni2) {
				return ni1.getNumber().compareTo(ni2.getNumber());
			}			
		});
		return numberImages;
	}
}
