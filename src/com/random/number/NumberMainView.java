package com.random.number;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Font;

import javax.swing.JComboBox;

public class NumberMainView {

	private JFrame frame;
	private JTextField textField_numberCount;
	private JScrollPane jPanel_scrollRandomNum;
	private JScrollPane jPanel_scrollInputNum;
	private JScrollPane jPanel_compare;
	private JButton btnStartMemorize;
	private JLabel lblDisplayTheRandom;
	private JLabel lblInputYourMemorized;
	private JLabel lblResultComparison;
	private JLabel lblCostTime;
	private JLabel label_timeCost;
	private JButton btnCompareResult;
	private JTextArea textArea_inputNum;
	private JTextArea textArea_randomNum;
	private JTextPane textPane_compare;	
	
	private int numberCount;
	private String randomNum;
	private long startTime;
	private long endTime;	
	private StringBuilder matchedStringBuilder;
	private StringBuilder unmatchedStringBuilder;
	private JComboBox comboBox_year;
	private JComboBox comboBox_month;
	private String selectedYear;
	private String selectedMonth;

	private String[] years = new String[]{"2018", "2019"};
	private String[] months = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NumberMainView window = new NumberMainView();
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
	public NumberMainView() {
		initialize();		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 920, 675);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnGenerateRandomNumber = new JButton("Generate");
		btnGenerateRandomNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String countStr = textField_numberCount.getText().trim();
				if(countStr.length() > 0) {
					numberCount = Integer.parseInt(countStr); 
					if(numberCount > 0) {
						generateRandomNum();
						textArea_randomNum.setText(randomNum);
						startTime = System.currentTimeMillis();
					}
				}		
				textArea_inputNum.setEditable(false);
				textArea_inputNum.setText("");
				textPane_compare.setText("");
			}
		});
		btnGenerateRandomNumber.setBounds(588, 193, 138, 29);
		frame.getContentPane().add(btnGenerateRandomNumber);								
		
		JLabel lblNumberCount = new JLabel("number count:");
		lblNumberCount.setBounds(325, 198, 99, 16);
		frame.getContentPane().add(lblNumberCount);
		
		textField_numberCount = new JTextField();
		textField_numberCount.setBounds(436, 195, 100, 22);
		frame.getContentPane().add(textField_numberCount);
		textField_numberCount.setColumns(10);
		
		btnStartMemorize = new JButton("End Memorize");
		btnStartMemorize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_randomNum.setText("");
				textField_numberCount.setText("");
				textArea_inputNum.setEditable(true);
				endTime = System.currentTimeMillis();
			}
		});
		btnStartMemorize.setBounds(742, 193, 138, 29);
		frame.getContentPane().add(btnStartMemorize);
		
		lblDisplayTheRandom = new JLabel("Display the random number:");
		lblDisplayTheRandom.setBounds(25, 24, 194, 16);
		frame.getContentPane().add(lblDisplayTheRandom);
		
		lblInputYourMemorized = new JLabel("Input your memorized number:");
		lblInputYourMemorized.setBounds(27, 217, 254, 16);
		frame.getContentPane().add(lblInputYourMemorized);
		
		lblResultComparison = new JLabel("Result Comparison:");
		lblResultComparison.setBounds(25, 406, 211, 16);
		frame.getContentPane().add(lblResultComparison);
		
		lblCostTime = new JLabel("Cost Time:");
		lblCostTime.setBounds(25, 526, 73, 16);
		frame.getContentPane().add(lblCostTime);
		
		label_timeCost = new JLabel("00:00:000");
		label_timeCost.setBounds(93, 526, 73, 16);
		frame.getContentPane().add(label_timeCost);
		
		btnCompareResult = new JButton("Compare Result");
		btnCompareResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputNumber = textArea_inputNum.getText().trim();
				compareNumber(inputNumber, randomNum);
				String timeCost = calculateTimeCost();
				label_timeCost.setText(timeCost);
			}
		});
		btnCompareResult.setBounds(742, 382, 138, 29);
		frame.getContentPane().add(btnCompareResult);
		
		
		textArea_inputNum = new JTextArea();		
		textArea_inputNum.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		textArea_inputNum.setEditable(false);
		textArea_inputNum.setLineWrap(true);
		jPanel_scrollInputNum = new JScrollPane(textArea_inputNum);
		jPanel_scrollInputNum.setBounds(25, 245, 855, 130);
		frame.getContentPane().add(jPanel_scrollInputNum);
		
		textArea_randomNum = new JTextArea();	
		textArea_randomNum.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		textArea_randomNum.setLineWrap(true);
		textArea_randomNum.setEditable(false);
		jPanel_scrollRandomNum = new JScrollPane(textArea_randomNum);		
		jPanel_scrollRandomNum.setBounds(25, 53, 855, 130);
		frame.getContentPane().add(jPanel_scrollRandomNum);														
		
		textPane_compare = new JTextPane() {
			@Override
		    public boolean getScrollableTracksViewportWidth() {
		        return getUI().getPreferredSize(this).width
		                        <= getParent().getSize().width;
		    }
		};
		textPane_compare.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textPane_compare.setEditable(false);
		jPanel_compare = new JScrollPane(textPane_compare);		
		jPanel_compare.setBounds(25, 434, 855, 83);		
		frame.getContentPane().add(jPanel_compare);
		
		
		comboBox_year = new JComboBox(years);
		comboBox_year.setSelectedIndex(0);
		selectedYear = (String) comboBox_year.getSelectedItem();
		comboBox_year.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedYear = (String) ((JComboBox)e.getSource()).getSelectedItem();
			}
		});
		comboBox_year.setBounds(588, 559, 66, 19);
		frame.getContentPane().add(comboBox_year);
		
		comboBox_month = new JComboBox(months);
		comboBox_month.setSelectedIndex(0);
		selectedMonth = (String) comboBox_month.getSelectedItem();
		comboBox_month.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedMonth = (String) ((JComboBox)e.getSource()).getSelectedItem();
			}
		});
		comboBox_month.setBounds(666, 559, 66, 19);
		frame.getContentPane().add(comboBox_month);
		
		JLabel lblShowChartLine = new JLabel("Show Chart Line:");
		lblShowChartLine.setBounds(468, 559, 125, 18);
		frame.getContentPane().add(lblShowChartLine);
		
		JButton btnChartLine = new JButton("Chart Line");
		btnChartLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("output selected year and selected month: "+selectedYear+"/"+selectedMonth);
				new LineChartView(selectedYear, selectedMonth);
			}
		});
		btnChartLine.setBounds(773, 556, 97, 25);
		frame.getContentPane().add(btnChartLine);
		
		
		
	}
	
	public String calculateTimeCost() {
		long timeCost = endTime-startTime;		
		int totalSeconds = (int) (timeCost / 1000);
		
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		int milliSeconds = (int) (timeCost % 1000); 
		
		String timeRes = String.format("%02d:%02d:%03d", minutes, seconds, milliSeconds);
		
		return timeRes;		
	}
	
	public void displayNum(StyledDocument doc, Style style, boolean matchedSb, String addedNum, Color color) {
		if(matchedSb) {
			if(matchedStringBuilder.length() > 0) {
				try {
					StyleConstants.setForeground(style, color);					
					doc.insertString(doc.getLength(), matchedStringBuilder.toString() ,style);	
					matchedStringBuilder = new StringBuilder();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			unmatchedStringBuilder.append(addedNum).append(" ");
		}else {
			if(unmatchedStringBuilder.length() > 0) {
				try {
					StyleConstants.setForeground(style, color);												
					doc.insertString(doc.getLength(), unmatchedStringBuilder.toString() ,style);	
					unmatchedStringBuilder = new StringBuilder();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			matchedStringBuilder.append(addedNum).append(" ");
		}		                          
	}
	
	public void compareNumber(String actualNum, String expectedNum) {
		StyledDocument doc = textPane_compare.getStyledDocument();
		Style style = textPane_compare.addStyle("Style", null);
		
		String[] expectedArr = expectedNum.split(" ");		
		String[] actualArr = new String[actualNum.length()];
		int index = 0;
		for(char charVal : actualNum.toCharArray()) {
			actualArr[index++] = String.valueOf(charVal); 
		}
		int numLength = Math.min(actualArr.length, expectedArr.length);
		
		matchedStringBuilder = new StringBuilder();
		unmatchedStringBuilder = new StringBuilder();
		
		matchedStringBuilder.append(expectedNum).append("\n");
		
		for(int i=0; i<numLength; i++) {
			if(actualArr[i].equals(expectedArr[i])) {
				displayNum(doc, style, false, actualArr[i], Color.RED);
			}else {
				displayNum(doc, style, true, actualArr[i], Color.BLACK);
			}
		}
		if(matchedStringBuilder.length() > 0) {
			displayNum(doc, style, true, "", Color.BLACK);
		}else {
			displayNum(doc, style, false, "", Color.RED);
		}
	}
	
	public void generateRandomNum() {		
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<numberCount; i++) {
			sb.append(random.nextInt(10)).append(" ");
		}
		randomNum = sb.toString();
	}
}
