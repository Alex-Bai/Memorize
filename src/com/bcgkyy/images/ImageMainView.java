package com.bcgkyy.images;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageMainView {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageMainView window = new ImageMainView();
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
	public ImageMainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JButton btnPre_image1 = new JButton("pre");
		btnPre_image1.setBounds(122, 473, 117, 29);
		frame.getContentPane().add(btnPre_image1);
		
		JButton btnNext_image1 = new JButton("next");
		btnNext_image1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNext_image1.setBounds(290, 473, 117, 29);
		frame.getContentPane().add(btnNext_image1);
		
		JButton btnPre_image2 = new JButton("pre");
		btnPre_image2.setBounds(596, 473, 117, 29);
		frame.getContentPane().add(btnPre_image2);
		
		JButton btnNext_image2 = new JButton("next");
		btnNext_image2.setBounds(762, 473, 117, 29);
		frame.getContentPane().add(btnNext_image2);
		
		try {			
			
			BufferedImage image1 = ImageIO.read(this.getClass().getResource("/image1.jpg"));
			BufferedImage image2 = ImageIO.read(this.getClass().getResource("/image2.jpeg"));
			
			JLabel label_image1 = new JLabel(new ImageIcon(getScaledImage(image1, 449, 384)));
			label_image1.setBounds(40, 52, 449, 384);			
			frame.getContentPane().add(label_image1);
			
			JLabel label_image2 = new JLabel(new ImageIcon(getScaledImage(image2, 449, 384)));
			label_image2.setBounds(522, 52, 449, 384);
			frame.getContentPane().add(label_image2);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
}
