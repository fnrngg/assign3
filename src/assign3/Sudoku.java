package assign3;

import java.util.*;

import javax.swing.Box.Filler;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(easyGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	
	private int[][] givenInts;
	private HashSet<spot> spots;
	
	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		givenInts = new int[ints.length][ints[0].length];
		for (int i = 0; i < ints.length; i++) {
			System.arraycopy(ints[i], 0, givenInts[i], 0, ints[0].length);
		}
		spots = new HashSet<spot>();
		for(int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if(givenInts[i][j] == 0) {
					spot newSpot = new spot(i, j);
					newSpot.set(0);
					newSpot.countAssignableNumbers();
					spots.add(newSpot);
				}
			}
		}
	}
	
	private int[][] solvedGrid;
	private long startTime;
	private long endTime;
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		startTime = System.currentTimeMillis();
		if(spots.size() == 0) {
			if(solvedGrid == null) {
				solvedGrid = new int[9][9];
				for (int i = 0; i < 9; i++) {
					System.arraycopy(givenInts[i], 0, solvedGrid[i], 0, 9);
				}
			}
			endTime = System.currentTimeMillis();
			return 1;
		}
		int sols = 0;
		Iterator<spot> first = spots.iterator();
		spot curr = first.next();
		spots.remove(curr);
		for (int i = 1; i < 10; i++) {
			if(curr.fullCheck(i)) {
				givenInts[curr.getRow()][curr.getCol()] = i;
				sols += solve();
				givenInts[curr.getRow()][curr.getCol()] = 0;
			}
		}
		spots.add(curr);
		return sols;
	}
	
	public String getSolutionText() {
		String res = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				res += solvedGrid[i][j] + " ";
			}
			res += "\n";
		}
		return res;
	}
	
	public long getElapsed() {
		return endTime - startTime;
	}
	
	
	class spot {
		private int row, col, value, assignNumbers;
		
		public spot(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		public void set(int value) {
			this.value = value;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getCol() {
			return col;
		}
		
		public int getValue() {
			return value;
		}
		
		private boolean checkRow(int value) {
			for(int i = 0; i < 9; i++) {
				if(value == givenInts[i][col]) {
					return false;
				}
			}
			return true;
		}
		
		private boolean checkCol(int value) {
			for(int i = 0; i < 9; i++) {
				if(value == givenInts[row][i]) {
					return false;
				}
			}
			return true;
		}
		
		private boolean checkSquare(int value) {
			int remain = row % 3;
			int remainRow = row - remain;
			remain = col % 3;
			int remainCol = col - remain;
			for (int i = remainRow; i < remainRow + 3; i++) {
				for (int j = remainCol; j < remainCol + 3; j++) {
					if(givenInts[i][j] == value) {
						return false;
					}
				}
			}
			
			return true;
		}
		
		public boolean fullCheck(int value) {
			return checkCol(value) && checkRow(value) && checkSquare(value);
		}
		
		public void countAssignableNumbers() {
			int res = 0;
			for(int i = 1; i < 10; i++) {
				if(fullCheck(i)) {
					res++;
				}
			}
			assignNumbers = res;
		}
		
		@Override
		public int hashCode() {
			return assignNumbers;
		}
		
	}

}
