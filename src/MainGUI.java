import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainGUI {

	private JFrame frmArtificialIntelligenceProject;
	private JTextField mazeSizeText;
	private JTextField generateSpeedText;
	private JTextField solveSpeedText;
	private JComboBox<String> solveAlgorithm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frmArtificialIntelligenceProject.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmArtificialIntelligenceProject = new JFrame();
		frmArtificialIntelligenceProject.setAlwaysOnTop(true);
		frmArtificialIntelligenceProject.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"./MazeIcon128.jpg"));
		frmArtificialIntelligenceProject.setResizable(false);
		frmArtificialIntelligenceProject.setTitle("Project Spring - 2015");
		frmArtificialIntelligenceProject.setBounds(100, 100, 422, 212);
		frmArtificialIntelligenceProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmArtificialIntelligenceProject.getContentPane().setLayout(null);

		JLabel label = new JLabel();
		label.setText("Maze Generator & Solver");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Monotype Corsiva", Font.PLAIN, 36));
		label.setBounds(22, 11, 372, 41);
		frmArtificialIntelligenceProject.getContentPane().add(label);

		JLabel label_1 = new JLabel();
		label_1.setText("Maze Size");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(22, 63, 57, 17);
		frmArtificialIntelligenceProject.getContentPane().add(label_1);

		mazeSizeText = new JTextField();
		mazeSizeText.setHorizontalAlignment(SwingConstants.CENTER);
		mazeSizeText.setText("10");
		mazeSizeText.setName("mazeSize");
		mazeSizeText.setBounds(144, 60, 144, 20);
		frmArtificialIntelligenceProject.getContentPane().add(mazeSizeText);

		JButton btnGenerateAndSolve = new JButton();
		btnGenerateAndSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int mazeSize = Integer.parseInt(mazeSizeText.getText());
					int solveSpeed = Integer.parseInt(solveSpeedText.getText());
					int generateSpeed = Integer.parseInt(generateSpeedText.getText());
					if ((mazeSize >= 4 && mazeSize <= 100) && (solveSpeed >= 0 && solveSpeed <= 100) && (generateSpeed >= 0 && generateSpeed <= 100)) {
						@SuppressWarnings("unused")
						Thread thread = new Thread(new MazeThread(mazeSize, solveSpeed, generateSpeed, solveAlgorithm.getSelectedIndex()));
					} else
						throw new Exception();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(new Frame(),
							"Maze Size Should be in between 4 and 100 (inclusive)\nSolve & Generation Speed Should be in between 0 and 100 (inclusive)",
							"Invalid Maze Size", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnGenerateAndSolve.setText("<html>Generate<p align=center>&<p align=center>Solve</html>");
		btnGenerateAndSolve.setBounds(310, 60, 96, 107);
		frmArtificialIntelligenceProject.getContentPane().add(btnGenerateAndSolve);

		JLabel label_2 = new JLabel();
		label_2.setText("Generate Speed");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_2.setBounds(22, 93, 97, 17);
		frmArtificialIntelligenceProject.getContentPane().add(label_2);

		generateSpeedText = new JTextField();
		generateSpeedText.setHorizontalAlignment(SwingConstants.CENTER);
		generateSpeedText.setText("0");
		generateSpeedText.setName("solveSpeed");
		generateSpeedText.setBounds(144, 90, 144, 20);
		frmArtificialIntelligenceProject.getContentPane().add(generateSpeedText);

		JLabel label_3 = new JLabel();
		label_3.setText("Solve Speed");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_3.setBounds(22, 123, 75, 17);
		frmArtificialIntelligenceProject.getContentPane().add(label_3);

		solveSpeedText = new JTextField();
		solveSpeedText.setHorizontalAlignment(SwingConstants.CENTER);
		solveSpeedText.setText("0");
		solveSpeedText.setName("solveSpeed");
		solveSpeedText.setBounds(144, 120, 144, 20);
		frmArtificialIntelligenceProject.getContentPane().add(solveSpeedText);

		JLabel lblSolveAlgorithm = new JLabel();
		lblSolveAlgorithm.setText("Solve Algorithm");
		lblSolveAlgorithm.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSolveAlgorithm.setBounds(22, 153, 97, 17);
		frmArtificialIntelligenceProject.getContentPane().add(lblSolveAlgorithm);

		solveAlgorithm = new JComboBox<String>();
		solveAlgorithm.setModel(new DefaultComboBoxModel<String>(new String[] { "Depth First Search", "Breadth First Search", "Bidirectional Search" }));
		solveAlgorithm.setBounds(144, 150, 144, 20);
		frmArtificialIntelligenceProject.getContentPane().add(solveAlgorithm);
	}
}
