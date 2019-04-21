package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	 JTextArea solutionSudoku;
	 Sudoku sudoku;
	 JTextArea puzzleSudoku;
	 JCheckBox auto;
	 
	public SudokuFrame() {
		super("Sudoku Solver");
		JComponent content = (JComponent)getContentPane();
		content.setLayout(new BorderLayout(4, 4));
		
		JPanel game = new JPanel();
		game.setLayout(new BoxLayout(game, BoxLayout.X_AXIS));

		puzzleSudoku = new JTextArea(15, 20);
		solutionSudoku = new JTextArea(15, 20);
		
		

		puzzleSudoku.getDocument().addDocumentListener(new DocumentListener() {
			// In some cases Document throws an illegalStateException while using 
			// puzzleSudoku.setText to avoid this I need to create new thread
			private void avoidIllegalStateExeption() {
				Runnable forCheck = new Runnable() {
					@Override
					public void run() {
						check();
					}
				};
				SwingUtilities.invokeLater(forCheck);
			}
			
			
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	        	if(auto.isSelected()) {
	        		avoidIllegalStateExeption();
	        	}
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	if(auto.isSelected()) {
	        		avoidIllegalStateExeption();
	        	}
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {

	        }
	    });
		
		
		puzzleSudoku.setBorder(new TitledBorder("Puzzle"));
		solutionSudoku.setBorder(new TitledBorder("Solution"));
		
		game.add(puzzleSudoku);
		game.add(solutionSudoku);
		
		content.add(BorderLayout.CENTER, game);
		
		
		JPanel down = new JPanel();
		down.setLayout(new BoxLayout(down, BoxLayout.X_AXIS));
		
		JButton checkButton = new JButton("Check");
		checkButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				check();
			}
		});
		down.add(checkButton);
		
		auto = new JCheckBox("Auto Check");
		auto.setSelected(true);
		down.add(auto);
		
		content.add(BorderLayout.SOUTH, down);
		// Could do this:
		// setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void check() {
		// If user writes " " program uses hardGrid from Sudoku.java and solves it
		if(puzzleSudoku.getText().equals(" ")) {
			sudoku = new Sudoku(Sudoku.hardGrid);
			puzzleSudoku.setText(sudoku.toString());
		} else {
			try {
				sudoku = new Sudoku(puzzleSudoku.getText());
			} catch (RuntimeException e) {
				solutionSudoku.setText("Parsing problem");
				return;
			}
		}
		int count = sudoku.solve();
		if(count > 0) {
			String solution = sudoku.getSolutionText();
			solution += "solutions: " + count;
			solution += "\nelapsed: " + sudoku.getElapsed();
			solutionSudoku.setText(solution);
		} else {
			solutionSudoku.setText("No solutions found");
		}
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
		frame.setSize(600, 400);
	}

}
