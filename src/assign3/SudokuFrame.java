package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	public SudokuFrame() {
		super("Sudoku Solver");
		JComponent content = (JComponent)getContentPane();
		content.setLayout(new BorderLayout(4, 4));
		
		JPanel game = new JPanel();
		game.setLayout(new BoxLayout(game, BoxLayout.X_AXIS));
		
		JTextArea puzzleSudoku = new JTextArea(15, 20);
		JTextArea solutionSudoku = new JTextArea(15, 20);
		
		puzzleSudoku.setBorder(new TitledBorder("Puzzle"));
		solutionSudoku.setBorder(new TitledBorder("Solution"));
		
		game.add(puzzleSudoku);
		game.add(solutionSudoku);
		
		content.add(BorderLayout.CENTER, game);
		
		
		JPanel down = new JPanel();
		down.setLayout(new BoxLayout(down, BoxLayout.X_AXIS));
		
		JButton check = new JButton("Check");
		check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		down.add(check);
		
		JCheckBox auto = new JCheckBox("Auto Check");
		down.add(auto);
		
		content.add(BorderLayout.SOUTH, down);
		// Could do this:
		// setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
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
