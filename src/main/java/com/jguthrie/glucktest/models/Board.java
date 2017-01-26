package com.jguthrie.glucktest.models;

import com.jguthrie.glucktest.models.Token.Color;

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
	
	public int[] playMove(int col, Token t) {
		if(col < 0 || col >= this.numCols || !cellIsEmpty(this.numRows-1, col)) {
			return null;
		}
		
		// Iterate down the column, checking if there is a token in the row below or not
		for(int row = this.numRows-1; row >= 0; row--) {
			if(row == 0) {
				setCell(row, col, t);
				return new int[]{row, col};
			} else if(!cellIsEmpty(row-1, col)) {
				setCell(row, col, t);
				return new int[]{row, col};
			}
		}
		
		// Should never reach this point
		return null;
	}
	
	public Color isWinner() {
		for(int row = 0; row < this.numRows; row++) {
			for(int col = 0; col < this.numCols; col++) {
				if(fourInARow(row, col)) {
					return getCell(row, col).getColor();
				}
			}
		}
		return null;
	}
	
	public Color isWinner(int row, int col) {
		checkRowCol(row, col);
		
		if(fourInARow(row, col)) {
			return getCell(row, col).getColor();
		} else {
			return null;
		}
	}
	
	private boolean fourInARow(int row, int col) {
		checkRowCol(row, col);

		// Check for chain of 4 tokens in each direction
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
	
	private boolean fourInARow(int row, int col, int[] dir) {
		checkRowCol(row, col);
		
		if(cellIsEmpty(row, col)) {
			return false;
		}
		
		Color c = getCell(row, col).getColor();
		int length = 1;
		int[] curCell = {row, col};
		int changeDir = 0;
		
		// Search for chain of tokens joining current token
		while(length < 4 && changeDir <= 1) {
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
			    changeDir++;
			}
		}
		
		return (length >= 4);
	}
	
	private void checkRowCol(int row, int col) {
		if(!isCell(row, col)) {
			throw new IndexOutOfBoundsException();
		}
	}
}
