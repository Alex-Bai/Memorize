package com.bcgkyy.images;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.bcgkyy.db.DBManagement;
import com.bcgkyy.demo.NumberImage;
import javax.swing.JTextField;

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
	private int leftImageIndex = 0;
	private int rightImageIndex = 0;	
	
	private JLabel label_image1;
	private JLabel label_image2;
	private JLabel label_leftNumber;
	private JLabel label_rightNumber;
	private JTextField textField_leftSearch;
	private JTextField textField_rightSearch;
	private JButton btnRightSearch;

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
				if(leftImageIndex == 0) {
					leftImageIndex += numberImages.size();
				}
				NumberImage numberImage = numberImages.get((--leftImageIndex) % numberImages.size());														
				setImageAndNumber(numberImage.getUrls(), numberImage.getNumber(), leftImageIndex, true);
				
			}
		});
		btnPre_image1.setBounds(120, 560, 117, 29);
		frame.getContentPane().add(btnPre_image1);
		
		JButton btnNext_image1 = new JButton("next");
		btnNext_image1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NumberImage numberImage = numberImages.get((++leftImageIndex) % numberImages.size());				
				setImageAndNumber(numberImage.getUrls(), numberImage.getNumber(), leftImageIndex, true);
			}
		});
		btnNext_image1.setBounds(288, 560, 117, 29);
		frame.getContentPane().add(btnNext_image1);
		
		JButton btnPre_image2 = new JButton("pre");
		btnPre_image2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rightImageIndex == 0) {
					rightImageIndex += numberImages.size();
				}
				NumberImage numberImage = numberImages.get((--rightImageIndex) % numberImages.size()); 
				setImageAndNumber(numberImage.getUrls(), numberImage.getNumber(), rightImageIndex, false);
			}
		});
		btnPre_image2.setBounds(596, 560, 117, 29);
		frame.getContentPane().add(btnPre_image2);
		
		JButton btnNext_image2 = new JButton("next");
		btnNext_image2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NumberImage numberImage = numberImages.get((++rightImageIndex) % numberImages.size());					
				setImageAndNumber(numberImage.getUrls(), numberImage.getNumber(), rightImageIndex, false);		
			}
		});
		btnNext_image2.setBounds(764, 560, 117, 29);
		frame.getContentPane().add(btnNext_image2);
		
		NumberImage initialImage = numberImages.get(leftImageIndex);
		
		label_image1 = new JLabel(getScaledImage(initialImage.getUrls().get(IMAGE_LEFT), IMAGE_WIDTH, IMAGE_HEIGHT));
		label_image1.setBounds(40, 108, IMAGE_WIDTH, IMAGE_HEIGHT);			
		frame.getContentPane().add(label_image1);
		
		label_image2 = new JLabel(getScaledImage(initialImage.getUrls().get(IMAGE_LEFT), IMAGE_WIDTH, IMAGE_HEIGHT));
		label_image2.setBounds(522, 108, IMAGE_WIDTH, IMAGE_HEIGHT);
		frame.getContentPane().add(label_image2);		
		
		label_leftNumber = new JLabel(initialImage.getNumber());
		label_leftNumber.setBounds(40, 518, 61, 16);
		frame.getContentPane().add(label_leftNumber);
		
		label_rightNumber = new JLabel(initialImage.getNumber());
		label_rightNumber.setBounds(522, 518, 61, 16);
		frame.getContentPane().add(label_rightNumber);
		
		textField_leftSearch = new JTextField();
		textField_leftSearch.setBounds(40, 41, 130, 26);
		frame.getContentPane().add(textField_leftSearch);
		textField_leftSearch.setColumns(10);
		
		JButton btnLeftSearch = new JButton("left search");
		btnLeftSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchedNumber = textField_leftSearch.getText().trim();
				boolean findRes = false;
				for(int i=0; i<numberImages.size(); i++) {
					NumberImage numberImage = numberImages.get(i);
					if(numberImage.getNumber().equals(searchedNumber)) {	
						findRes = true;
						setImageAndNumber(numberImage.getUrls(), numberImage.getNumber(), i, true);						
					}
				}
				if(!findRes) {
					JOptionPane.showMessageDialog(frame, "nothing find on left side!");
				}
			}
		});
		btnLeftSearch.setBounds(182, 41, 117, 29);
		frame.getContentPane().add(btnLeftSearch);
		
		textField_rightSearch = new JTextField();
		textField_rightSearch.setColumns(10);
		textField_rightSearch.setBounds(522, 41, 130, 26);
		frame.getContentPane().add(textField_rightSearch);
		
		btnRightSearch = new JButton("right_search");
		btnRightSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchedNumber = textField_rightSearch.getText().trim();
				boolean findRes = false;
				for(int i=0; i<numberImages.size(); i++) {
					NumberImage numberImage = numberImages.get(i);
					if(numberImage.getNumber().equals(searchedNumber)) {						
						findRes = true;
						setImageAndNumber(numberImage.getUrls(), numberImage.getNumber(), i, false);					
					}
				}
				if(!findRes) {
					JOptionPane.showMessageDialog(frame, "nothing find on right side!");
				}
			}
		});
		btnRightSearch.setBounds(664, 41, 117, 29);
		frame.getContentPane().add(btnRightSearch);
		
	}		
	
	private void setImageAndNumber(List<String> urls, String number, int imageIndex, boolean first) {
		if(first) {
			label_image1.setIcon(getScaledImage(urls.get(IMAGE_LEFT), IMAGE_WIDTH, IMAGE_HEIGHT));
			label_leftNumber.setText(number);
			leftImageIndex = imageIndex;
		}else {
			
			label_image2.setIcon(getScaledImage(urls.size()==2 ? urls.get(IMAGE_RIGHT) : urls.get(IMAGE_LEFT), IMAGE_WIDTH, IMAGE_HEIGHT));
			label_rightNumber.setText(number);
			rightImageIndex = imageIndex;
		}
		
	}
	
	private ImageIcon getScaledImage(String imagePath, int w, int h){				 
		try {
			//Image srcImg = ImageIO.read(this.getClass().getResource(imagePath));
			Image srcImg = ImageIO.read(new File(imagePath));			
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
		numberImages = new LinkedList<NumberImage>();		
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
