import java.awt.EventQueue;

import javax.swing.JFrame;

import com.bcgkyy.english.EnglishImage;
import com.bcgkyy.images.ImageMainView;
import com.random.number.NumberMainView;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainView {

	private JFrame frame;

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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNumberView = new JButton("Number View");
		btnNumberView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NumberMainView();
			}
		});
		btnNumberView.setBounds(85, 127, 217, 45);
		frame.getContentPane().add(btnNumberView);
		
		JButton btnNumberImageView = new JButton("Number Image View");
		btnNumberImageView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ImageMainView();
			}
		});
		btnNumberImageView.setBounds(85, 228, 217, 45);
		frame.getContentPane().add(btnNumberImageView);
		
		JButton btnWordImageView = new JButton("Word Image View");
		btnWordImageView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EnglishImage();
			}
		});
		btnWordImageView.setBounds(85, 326, 217, 45);
		frame.getContentPane().add(btnWordImageView);
	}

}
