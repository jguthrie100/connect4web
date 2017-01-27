package com.jguthrie.connect4web.models;

import com.jguthrie.connect4web.models.Token.Color;

/**
 * Models the board and contains most of the logic for determining whether there is a winner
 * @author jguthrie100
 *
 */
public class Board {

	private int numRows, numCols;
	private Token board[][];
	
	public Board(int rows, int cols) {
		if(rows <= 0 || cols <= 0) {
			throw new IndexOutOfBoundsException("Number of cols/rows must be greater than 0");
		}
		
		this.numRows = rows;
		this.numCols = cols;
		
		this.board = new Token[rows][cols];
	}
	
	public int getNumRows() {
		return this.numRows;
	}
	
	public int getNumCols() {
		return this.numCols;
	}
	
	public Token[][] getBoard() {
		return this.board;
	}
	
	public Token getCell(int row, int col) {
		checkRowCol(row, col);
		
		return this.board[row][col];
	}
	
	public void setCell(int row, int col, Token t) {
		checkRowCol(row, col);
		
		this.board[row][col] = t;
	}
	
	/**
	 * Returns whether or not the given row, col combination is a cell
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isCell(int row, int col) {
		if(row >= 0 && row < this.numRows && col >= 0 && col < this.numCols) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean cellIsEmpty(int row, int col) {
		checkRowCol(row, col);
		
		return (this.board[row][col] == null);
	}
	
	/**
	 * Method that metaphorically drops the token into the board at a specific column
	 * @param col
	 * @param t
	 * @return Final 'resting' position of the token
	 */
	public int[] dropToken(int col, Token t) {
		if(col < 0 || col >= this.numCols || !cellIsEmpty(this.numRows-1, col)) {
			return null;
		}
		
		// Iterate down the column, checking if there is a token in the row below or not
		for(int row = this.numRows-1; row >= 0; row--) {
			if(!isCell(row-1, col) || !cellIsEmpty(row-1, col)) {
				setCell(row, col, t);
				return new int[]{row, col};
			}
		}
		
		// Should never reach this point
		return null;
	}
	
	/**
	 * Determine whether there is a chain of four same-coloured tokens crossing the
	 *  point at the specified row & col
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean fourInARow(int row, int col) {
		checkRowCol(row, col);

		// Check for chain of 4 tokens in each direction (up, down, diagonal)
		if(fourInARow(row, col, new int[]{1, 0})) {
			return true;
		}
		if(fourInARow(row, col, new int[]{1, 1})) {
			return true;
		}
		if(fourInARow(row, col, new int[]{0, 1})) {
			return true;
		}
		if(fourInARow(row, col, new int[]{-1, 1})) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determine whether there is a chain of four same-coloured tokens crossing the
	 *  point at the specified row & col, and in the given direction.
	 *  The direction integer works by basically saying "look in direction [0, 1]", which means
	 *  look in the direction moving 0 rows up and 1 cols across.. i.e. in East direction
	 *  [1, 1] means go 1 row up and 1 row across, and [-1, 0] means go 1 row down and 0 cols across.
	 * @param row
	 * @param col
	 * @param dir An array of two integers (1 or 0)
	 * @return
	 */
	public boolean fourInARow(int row, int col, int[] dir) {
		checkRowCol(row, col);
		
		if(cellIsEmpty(row, col)) {
			return false;
		}
		
		Color c = getCell(row, col).getColor();
		int length = 1;
		int[] curCell = {row, col};
		int reverseDir = 0;
		
		// Search for chain of tokens
		while(length < 4 && reverseDir <= 1) {
			curCell[0] += dir[0];
			curCell[1] += dir[1];
			
			if(isCell(curCell[0], curCell[1]) && !cellIsEmpty(curCell[0], curCell[1]) && getCell(curCell[0], curCell[1]).getColor() == c) {
				length += 1;
			} else {
				// reset curCell
				curCell[0] = row;
				curCell[1] = col;
				
				// reverse direction (and search back the other way from 'current token')
				dir[0] = (0 - dir[0]);
				dir[1] = (0 - dir[1]);
				
				// log change in direction
			    reverseDir++;
			}
		}
		
		return (length >= 4);
	}
	
	public void checkRowCol(int row, int col) {
		if(!isCell(row, col)) {
			throw new IndexOutOfBoundsException();
		}
	}
}
