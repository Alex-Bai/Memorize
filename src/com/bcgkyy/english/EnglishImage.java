package com.bcgkyy.english;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.bcgkyy.db.DerbyDBManagement;

public class EnglishImage {

	private JFrame frame;
	private JLabel label_image;
	private JButton btnStart;
	private JButton btnPre;
	private JButton btnNext;
	
	private DerbyDBManagement dbManagement;
	private final String SHOW_WORD_BTN_MSG = "show word";
	private final String outputImageFolder = "EnglishImages";
	private String action = "study";	
	private final int IMAGE_WIDTH = 374;
	private final int IMAGE_HEIGHT = 285;	
	private int imageIndex;
	private List<List<String>> wordImages;
	private JButton btn_showWord;
	private String englishWord;
	private int showWordBtnClickTime;	
	private List<Integer> randomList;
	private JComboBox comboBox_year;
	private JComboBox comboBox_month;
	private JComboBox comboBox_day;
	private String selectedYear;
	private String selectedMonth;
	private String selectedDay;
	private String[] years = new String[] {"year", "2018","2019"};
	private String[] months = new String[]{"month","01","02","03","04","05","06","07","08","09","10","11","12"};
	private String[] days = new String[] {"day"};
	private JButton btnGo;
	private JTextArea txt_explain;
	private JScrollPane jPanel_scrollInputExplain;
	private JButton btn_saveExplain;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EnglishImage window = new EnglishImage();					
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {			
		
		dbManagement = new DerbyDBManagement();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 600);		
		frame.getContentPane().setLayout(null);
		
		JButton btnDatabaseUpdate = new JButton("Update Database");
		btnDatabaseUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setButtonVisible(false, false);				
				String deleteSqlStr = "delete from english_images";
				if(dbManagement.deleteRecords(deleteSqlStr, null)) {
					updateImageInfor();					
				}else {
					JOptionPane.showMessageDialog(frame, "update table failed");
				}
			}
		});
		btnDatabaseUpdate.setBounds(37, 19, 179, 29);
		frame.getContentPane().add(btnDatabaseUpdate);
		
		label_image = new JLabel("");
		label_image.setBounds(141, 98, 374, 285);
		frame.getContentPane().add(label_image);
		
		btnPre = new JButton("pre");		
		btnPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				btn_showWord.setEnabled(false);
				if(imageIndex == 0) {
					imageIndex += wordImages.size();
				}
				List<String> wordImage = wordImages.get((--imageIndex)%wordImages.size());
				String imageUrl = wordImage.get(1);				
				label_image.setIcon(getScaledImage(imageUrl));				
				btn_showWord.setText(wordImage.get(0));
				txt_explain.setText(wordImage.size()<3 ? "" : (wordImage.get(2)==null ? "" : wordImage.get(2)));
			}
		});
		btnPre.setBounds(141, 531, 117, 29);
		frame.getContentPane().add(btnPre);
		
		btnNext = new JButton("next");		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetShowMsgBtn();
				List<String> wordImage = wordImages.get(0);
				if(action.equals("study")) {
					btn_showWord.setEnabled(false);					
					wordImage = wordImages.get((++imageIndex)%wordImages.size());
					btn_showWord.setText(wordImage.get(0));
					txt_explain.setText(wordImage.size()<3 ? "" : (wordImage.get(2)==null ? "" : wordImage.get(2)));
				}else if(action.equals("review")) {
					if((imageIndex+1) >= randomList.size()) {						
						generateRandomArrar();
					}
					wordImage = wordImages.get(randomList.get(++imageIndex));
					englishWord = wordImage.get(0);
				}				
				String imageUrl = wordImage.get(1);
				label_image.setIcon(getScaledImage(imageUrl));				
			}
		});
		btnNext.setBounds(377, 531, 117, 29);
		frame.getContentPane().add(btnNext);
		
		setButtonVisible(false, false);
		
		JRadioButton rdbtnStudy = new JRadioButton("study");
		rdbtnStudy.setSelected(true);
		rdbtnStudy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = e.getActionCommand();
				setButtonVisible(false, false);
			}
		});
		rdbtnStudy.setBounds(315, 60, 79, 26);
		frame.getContentPane().add(rdbtnStudy);
		
		JRadioButton rdbtnReview = new JRadioButton("review");
		rdbtnReview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = e.getActionCommand();
				setButtonVisible(false, false);
			}
		});
		rdbtnReview.setBounds(402, 60, 92, 26);
		frame.getContentPane().add(rdbtnReview);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnStudy);
		group.add(rdbtnReview);
				
		
		btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				if(selectedYear != null && !selectedYear.isEmpty() && !selectedYear.equals(years[0])
						&& selectedMonth != null && !selectedMonth.isEmpty() && !selectedMonth.equals(months[0])
						&& selectedDay != null && !selectedDay.isEmpty() && !selectedDay.equals(days[0])) {
					btn_showWord.setVisible(true);
					jPanel_scrollInputExplain.setVisible(true);
					btn_saveExplain.setVisible(true);					
					
					loadDBData();
					
					generateRandomArrar();
					if(wordImages==null || wordImages.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "today has no English words");
					}else {
						List<String> wordImage = wordImages.get(0);
						if(action.equals("study")) {
							setButtonVisible(true, true);
							btn_showWord.setEnabled(false);
							imageIndex=0;
							btn_showWord.setText(wordImage.get(0));
							txt_explain.setText(wordImage.size()<3 ? "" : (wordImage.get(2)==null ? "" : wordImage.get(2)));
						}else if(action.equals("review")) {
							resetShowMsgBtn();
							setButtonVisible(false, true);		
							btn_showWord.setEnabled(true);
							if((imageIndex+1) >= randomList.size()) {						
								generateRandomArrar();
							}
							txt_explain.setText(wordImage.size()<3 ? "" : (wordImage.get(2)==null ? "" : wordImage.get(2)));	
							englishWord = wordImage.get(0);
						}				
						String imageUrl = wordImage.get(1);
						label_image.setIcon(getScaledImage(imageUrl));
					}					
				}else {
					JOptionPane.showMessageDialog(frame, "please select valid year month and date");
				}								
			}
		});
		btnStart.setBounds(493, 60, 117, 29);
		frame.getContentPane().add(btnStart);
		
		btn_showWord = new JButton(SHOW_WORD_BTN_MSG);
		btn_showWord.setVisible(false);
		btn_showWord.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		btn_showWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if((++showWordBtnClickTime)%2 == 0) {
					btn_showWord.setText(SHOW_WORD_BTN_MSG);
				}else {
					btn_showWord.setText(englishWord);
				}
				
			}
		});
		btn_showWord.setBounds(196, 477, 264, 42);
		frame.getContentPane().add(btn_showWord);
		
		comboBox_year = new JComboBox(years);
		comboBox_year.setSelectedIndex(0);
		selectedYear = (String) comboBox_year.getSelectedItem();
		comboBox_year.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedYear = (String) ((JComboBox)e.getSource()).getSelectedItem();
			}
		});
		comboBox_year.setBounds(305, 19, 100, 29);
		frame.getContentPane().add(comboBox_year);
		
		comboBox_month = new JComboBox(months);
		comboBox_month.setSelectedIndex(0);
		selectedMonth = (String) comboBox_month.getSelectedItem();
		comboBox_month.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedMonth = (String) ((JComboBox)e.getSource()).getSelectedItem();
				if(selectedYear != null && !selectedYear.isEmpty() && !selectedYear.equals(years[0]) && !selectedMonth.equals(months[0])) {
					getDayOfMonth(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth)-1);
					comboBox_day.removeAllItems();
					for(String day : days) {
						comboBox_day.addItem(day);
					}					
				}
			}
		});
		comboBox_month.setBounds(417, 19, 100, 29);
		frame.getContentPane().add(comboBox_month);
		
		comboBox_day = new JComboBox(days);
		comboBox_day.setSelectedIndex(0);
		selectedDay = (String) comboBox_month.getSelectedItem();
		comboBox_day.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDay = (String) ((JComboBox)e.getSource()).getSelectedItem();				
			}
		});
		comboBox_day.setBounds(529, 19, 100, 29);
		frame.getContentPane().add(comboBox_day);									
		
		
		txt_explain = new JTextArea();		
		txt_explain.setFont(new Font("Lucida Grande", Font.PLAIN, 20));		
		txt_explain.setLineWrap(true);
		jPanel_scrollInputExplain = new JScrollPane(txt_explain);
		jPanel_scrollInputExplain.setBounds(141, 395, 324, 70);
		jPanel_scrollInputExplain.setVisible(false);
		frame.getContentPane().add(jPanel_scrollInputExplain);
		
		btn_saveExplain = new JButton("save");
		btn_saveExplain.setVisible(false);
		btn_saveExplain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				String wordExplanation = txt_explain.getText().trim();
				System.out.println("output the explain text: "+wordExplanation);
				String btnWord = btn_showWord.getText().trim();
												
				String updateSqlStr = "update english_images set explanation=? where word=?";
				if(dbManagement.updateRecords(updateSqlStr, Arrays.asList(wordExplanation, btnWord))) {
					loadDBData();
					JOptionPane.showMessageDialog(frame, "database update succeed");
				}else {
					JOptionPane.showMessageDialog(frame, "database update failed");
				}							
			}
		});
		btn_saveExplain.setBounds(464, 395, 51, 70);
		frame.getContentPane().add(btn_saveExplain);
				
		
		File directory = new File(outputImageFolder);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		
		frame.setVisible(true);
	}
	
	private void updateImageInfor() {
						
		File imageFolder = new File(outputImageFolder);
		
		if(imageFolder.exists()) {	
			boolean tableUpdate = true;
			for(File file : imageFolder.listFiles()) {
				if(file.isDirectory()) {
					//2018-07-20
					String dateStr = file.getName();					
					List<String[]> imageList = new ArrayList<String[]>();
					for(File dayFile : file.listFiles()) {
						String fileName = dayFile.getName();
						if(fileName.contains("png")) {
							String word = fileName.split("\\.")[0];									
							String imageUrl = outputImageFolder+"/"+dateStr+"/"+fileName;
							String dateArr[] = dateStr.split("-");							
							imageList.add(new String[]{word, imageUrl, "", dateArr[0], dateArr[1], dateArr[2]});
						}	
					}
					if(imageList.size() > 0) {
						String insertSqlStr = "insert into english_images values (?, ?, ?, ?, ?, ?)";
						if(!dbManagement.insertRecords(insertSqlStr, imageList)) {
							tableUpdate = false;
							break;
						}						
					}					
				}													
			}
			if(tableUpdate) {
				JOptionPane.showMessageDialog(frame, "update table succeed");
			}else {
				JOptionPane.showMessageDialog(frame, "update table failed");
			}
		}
	}
	
	private void resetShowMsgBtn() {
		showWordBtnClickTime = 0;
		btn_showWord.setText(SHOW_WORD_BTN_MSG);
	}
	
	private ImageIcon getScaledImage(String imagePath){				 
		try {
			Image srcImg = ImageIO.read(new File(imagePath));			
			BufferedImage resizedImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resizedImg.createGraphics();

		    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2.drawImage(srcImg, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
		    g2.dispose();
		    return new ImageIcon(resizedImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;	    
	}
	
	private void writeFile(File srcFile, String outputFilePath) {
		BufferedImage srcImg;
		try {
			srcImg = ImageIO.read(srcFile);
			File outputFile = new File(outputFilePath);
		    ImageIO.write(srcImg, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}
	
	private void setButtonVisible(boolean firstVisible, boolean secondVisible) {
		btnPre.setVisible(firstVisible);
		btnNext.setVisible(secondVisible);
	}
	
	private void generateRandomArrar() {
		imageIndex = -1;
		randomList = new LinkedList<Integer>();
		for(int i=0; i<wordImages.size(); i++) {
			randomList.add(i);
		}
		Collections.shuffle(randomList);			
	}
	
	public void getDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, month);
	    int maxDay = cal.getActualMaximum(cal.DAY_OF_MONTH);
	    days = new String[maxDay+1];
	    days[0] = "day";
	    for(int i=1; i<=maxDay; i++) {
	    	days[i] = String.valueOf(i);
	    }
	}
	
	private void loadDBData() {
		String queryStr = "select word, image_url, explanation from english_images where date_year=? and date_month=? and date_day=?";
		wordImages = dbManagement.searchRecord(queryStr, Arrays.asList(selectedYear, selectedMonth, selectedDay));		
	} 		
}
