package com.random.number;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.Font;

public class NumberMainView {

	private JFrame frame;
	private JTextField textField_numberCount;
	private JScrollPane jPanel_scrollRandomNum;
	private JScrollPane jPanel_scrollInputNum;
	private JScrollPane jPanel_scrollCompareNum;
	private JButton btnStartMemorize;
	private JLabel lblDisplayTheRandom;
	private JLabel lblInputYourMemorized;
	private JLabel lblResultComparison;
	private JLabel lblCostTime;
	private JLabel label_timeCost;
	private JButton btnCompareResult;
	private JTextArea textArea_inputNum;
	private JTextArea textArea_compare;
	private JTextArea textArea_randomNum;
	
	private int numberCount;
	private String randomNum;
	private long startTime;
	private long endTime;
	private JLabel lblTest;

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
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnGenerateRandomNumber = new JButton("Generate");
		btnGenerateRandomNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numberCount = Integer.parseInt(textField_numberCount.getText().trim()); 
				if(numberCount != 0) {
					generateRandomNum();
					textArea_randomNum.setText(randomNum);
					startTime = System.currentTimeMillis();
				}
			}
		});
		btnGenerateRandomNumber.setBounds(581, 160, 138, 29);
		frame.getContentPane().add(btnGenerateRandomNumber);								
		
		JLabel lblNumberCount = new JLabel("number count:");
		lblNumberCount.setBounds(324, 165, 99, 16);
		frame.getContentPane().add(lblNumberCount);
		
		textField_numberCount = new JTextField();
		textField_numberCount.setBounds(435, 162, 100, 22);
		frame.getContentPane().add(textField_numberCount);
		textField_numberCount.setColumns(10);
		
		btnStartMemorize = new JButton("End Memorize");
		btnStartMemorize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_randomNum.setText("");
				endTime = System.currentTimeMillis();
			}
		});
		btnStartMemorize.setBounds(742, 160, 138, 29);
		frame.getContentPane().add(btnStartMemorize);
		
		lblDisplayTheRandom = new JLabel("Display the random number:");
		lblDisplayTheRandom.setBounds(25, 24, 194, 16);
		frame.getContentPane().add(lblDisplayTheRandom);
		
		lblInputYourMemorized = new JLabel("Input your memorized number:");
		lblInputYourMemorized.setBounds(25, 188, 254, 16);
		frame.getContentPane().add(lblInputYourMemorized);
		
		lblResultComparison = new JLabel("Result Comparison:");
		lblResultComparison.setBounds(25, 349, 211, 16);
		frame.getContentPane().add(lblResultComparison);
		
		lblCostTime = new JLabel("Cost Time:");
		lblCostTime.setBounds(732, 535, 73, 16);
		frame.getContentPane().add(lblCostTime);
		
		label_timeCost = new JLabel("00:00:000");
		label_timeCost.setBounds(807, 535, 73, 16);
		frame.getContentPane().add(label_timeCost);
		
		btnCompareResult = new JButton("Compare Result");
		btnCompareResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputNumber = textArea_inputNum.getText().trim();
				String compareRes = compareNumber(inputNumber, randomNum);
				textArea_compare.setText(compareRes);
				String timeCost = calculateTimeCost();
				label_timeCost.setText(timeCost);
			}
		});
		btnCompareResult.setBounds(742, 327, 138, 29);
		frame.getContentPane().add(btnCompareResult);
		
		
		textArea_inputNum = new JTextArea();		
		textArea_inputNum.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		textArea_inputNum.setLineWrap(true);
		jPanel_scrollInputNum = new JScrollPane(textArea_inputNum);
		jPanel_scrollInputNum.setBounds(25, 215, 855, 100);
		frame.getContentPane().add(jPanel_scrollInputNum);				
		
		textArea_compare = new JTextArea();						
		textArea_compare.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		textArea_compare.setLineWrap(true);
		textArea_compare.setEditable(false);
		jPanel_scrollCompareNum = new JScrollPane(textArea_compare);
		jPanel_scrollCompareNum.setBounds(25, 380, 855, 143);
		frame.getContentPane().add(jPanel_scrollCompareNum);		
		
		textArea_randomNum = new JTextArea();	
		textArea_randomNum.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		textArea_randomNum.setLineWrap(true);
		textArea_randomNum.setEditable(false);
		jPanel_scrollRandomNum = new JScrollPane(textArea_randomNum);		
		jPanel_scrollRandomNum.setBounds(25, 53, 855, 100);
		frame.getContentPane().add(jPanel_scrollRandomNum);										
		
		lblTest = new JLabel("<html><center><font color=#ff0000>middle button</font>");
		Font font = lblTest.getFont().deriveFont(Font.PLAIN);
		lblTest.setFont(font);
		lblTest.setBounds(25, 535, 241, 37);
		frame.getContentPane().add(lblTest);
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
	
	public String compareNumber(String actualNum, String expectedNum) {
		//String a = actualNum+"\n"+expectedNum;
		String a = "<html><center><font color=#ff0000>middle button</font>";
		return a;
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
