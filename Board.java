/*
 * Copyright (C) 2016 Michael <GrubenM@GMail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Michael <GrubenM@GMail.com>
 */
public class Board {
    private int n;
    private int[][] blocks;
    private int[][] goal;
    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j).
     * @param blocks 
     */
    public Board(int[][] blocks) {
        this.blocks = blocks;
        n = this.blocks.length;
        
        // Initialize and populate the goal board
        goal = new int[n][n];
        for (int i = 0; i < n * n; i++) {
            goal[i / n][i % n] = i + 1;
        }
    }
    
    // Board dimension n
    public int dimension() {
        return n;
    }
    
    // Number of blocks out of place
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < n * n; i++) {
            if (blocks[i / n][i % n] != i + 1) ham++;
        }
        return ham;
    }
    
    // Sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int man = 0;
        for (int i = 0; i < n * n; i++) {
            int t = blocks[i / n][i % n]; // Store the entry at the index
            man += Math.abs(((t - 1) / n) - (i / n)); // V-distance
            man += Math.abs(((t - 1) % n) - (i % n)); // H-distance
        }
        return man;
    } 
    
    // Is this board the goal board?
    public boolean isGoal() {
        return blocks == goal;
    }
    // A board that is obtained by exchanging any pair of blocks
    public Board twin() {
        
    }
    // Does this board equal y?
    public boolean equals(Object y) {
        
    }
    
    // All neighbording boards
    public Iterable<Board> neighbors() {
        
    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        
    }
    // unit tests (not graded)
    public static void main(String[] args) {
        
    }
}
