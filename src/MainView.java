
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainView {

	private JFrame frame;
	private JTextField txtShowWord;
	private Random random;
	private List<String> wordList;
	private Map<String, String> wordMap; 
	private JTextField txtTranslate;
	private int index = -1;
	private JTextField txtEnglishWord;
	private JTextField txtTranslateWord;
	private DBManagement dbManagement;		
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {										
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
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
	public MainView() {		
		initialize();
		catchData();
	}

	public void catchData() {
		
		wordMap = new HashMap<String, String>();
		wordList = new LinkedList<String>();
		dbManagement = new DBManagement();
		
		List<String> dbRecords;
		try {
			dbRecords = dbManagement.getRecords(DBManagement.TABLE_NAME);
			for(String record : dbRecords) {
				String[] recordArr = record.split(DBManagement.SPLIT);
				wordList.add(recordArr[0]);
				if(recordArr.length==2 && !recordArr[0].equals("") && !recordArr[1].equals("")) {
					wordMap.put(recordArr[0], recordArr[1]);
				}				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void setTextEmpty() {
		txtEnglishWord.setText("");
		txtShowWord.setText("");
		txtTranslateWord.setText("");
		txtTranslate.setText("");
	}
	
	public void nextWordAction() {
		String inputWord = txtEnglishWord.getText().trim();
		String inputTranslate = txtTranslateWord.getText().trim();
		if(inputWord.isEmpty() || inputTranslate.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "one of the fields is empty");
		}
		try {
			boolean insertRes = dbManagement.insertRecord(new String[]{inputWord, inputTranslate}, DBManagement.TABLE_NAME);
			if(insertRes) {
				catchData();
			}else{						
				JOptionPane.showMessageDialog(frame, "insert data failed, the English word may already exits");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		txtEnglishWord.setText("");
		txtTranslateWord.setText("");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {			
		random = new Random();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtShowWord = new JTextField();
		txtShowWord.setHorizontalAlignment(SwingConstants.CENTER);
		txtShowWord.setFont(new Font("Dialog", Font.ITALIC, 28));
		txtShowWord.setEditable(false);
		txtShowWord.setText("click next to start");
		txtShowWord.setBounds(12, 50, 286, 54);
		frame.getContentPane().add(txtShowWord);
		txtShowWord.setColumns(10);
		
		JButton btnNext = new JButton("next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtTranslate.setText("");
				txtTranslate.setVisible(false);
				if(wordList.size()==0) {
					JOptionPane.showMessageDialog(frame, "no data in the dictionary");
				}else{
					index = random.nextInt(wordMap.size());
					txtShowWord.setText(wordList.get(index));
				}								
			}
		});
		btnNext.setBounds(106, 146, 97, 25);
		frame.getContentPane().add(btnNext);
						
		JButton btnAdd = new JButton("add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				nextWordAction();
			}
		});
		btnAdd.setBounds(392, 287, 97, 25);
		frame.getContentPane().add(btnAdd);
		
		JButton btnTranslate = new JButton("translate");
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(index>=0) {
					txtTranslate.setVisible(true);
					txtTranslate.setText(wordMap.get(wordList.get(index)));
				}				
			}
		});
		btnTranslate.setBounds(392, 146, 97, 25);
		frame.getContentPane().add(btnTranslate);
		
		txtTranslate = new JTextField();
		txtTranslate.setFont(new Font("Tahoma", Font.PLAIN, 22));
		txtTranslate.setEditable(false);
		txtTranslate.setHorizontalAlignment(SwingConstants.CENTER);
		txtTranslate.setBounds(310, 56, 260, 48);
		frame.getContentPane().add(txtTranslate);
		txtTranslate.setColumns(10);
		
		JLabel lblEnglish = new JLabel("English");
		lblEnglish.setBounds(26, 214, 56, 16);
		frame.getContentPane().add(lblEnglish);
		
		txtEnglishWord = new JTextField();
		txtEnglishWord.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtEnglishWord.setBounds(81, 211, 182, 22);
		frame.getContentPane().add(txtEnglishWord);
		txtEnglishWord.setColumns(10);					
				
		JLabel lblTranslate = new JLabel("translate");
		lblTranslate.setBounds(287, 214, 56, 16);
		frame.getContentPane().add(lblTranslate);
		
		txtTranslateWord = new JTextField();
		txtTranslateWord.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtTranslateWord.setBounds(355, 211, 196, 22);
		frame.getContentPane().add(txtTranslateWord);
		txtTranslateWord.setColumns(10);
		
		JButton btnNewButton = new JButton("delete");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int confirmRes = JOptionPane.showConfirmDialog(frame, "are you sure to delete the record", "confirmation", JOptionPane.YES_NO_OPTION);
				if(JOptionPane.OK_OPTION==confirmRes) {
					try {																		
						boolean deleteRes = dbManagement.deleteRecord(txtShowWord.getText().trim(), DBManagement.TABLE_NAME);
						setTextEmpty();
						if(deleteRes) {
							catchData();							
							JOptionPane.showMessageDialog(frame, "delete data successfully");								
						}else {
							JOptionPane.showMessageDialog(frame, "delete data failed, the English word may not exits");
						}						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}								
			}
		});
		btnNewButton.setBounds(106, 287, 97, 25);
		frame.getContentPane().add(btnNewButton);
		txtTranslate.setVisible(false);			
	}
}

