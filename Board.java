
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import java.util.Arrays;

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
    // Consider adding a private int to store the location of the empty space
    
    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j).
     * @param blocks 
     */
    public Board(int[][] blocks) {
        this.blocks = blocks;
        n = this.blocks.length;
        
        /**
         * Initialize and populate the goal board
         * Note that % (n * n) means that the last "block"
         * to be added will be 0.
         */
        goal = new int[n][n];
        for (int i = 0; i < n * n; i++) {
            goal[i / n][i % n] = (i + 1) % (n * n);
        }
    }
    
    // Board dimension n
    public int dimension() {
        return n;
    }
    
    /**
     * Number of blocks out of place
     * Note that % (n * n) means that the last "block"
     * to be added will be 0.
     */
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < n * n - 1; i++) {
            if (blocks[i / n][i % n] != (i + 1) % (n * n)) ham++;
        }
        return ham;
    }
    
    // Sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int man = 0;
        for (int i = 0; i < n * n - 1; i++) {
            int t = blocks[i / n][i % n]; // Store the entry at the index
            if (t == 0) t = n * n; // Handle the 0 block
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
        int[][] twin = blocks;
        int i = 0;
        while (twin[i / n][i % n] == 0) i++;
        int j = i + 1;
        while (twin[j / n][j % n] == 0) j++;
        exch(twin, i, j);
        return new Board(twin);
    }
    
    // Does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == this) return true; // Literally the same object
        if (y == null) return false; // Handle null comparison
        if (y.getClass() != this.getClass()) return false; // not even the same
                                                           // class
        
        // Cast and compare blocks
        Board x = (Board) y;
        return (this.blocks == x.blocks);
    }
    
    /**
     * Swaps blocks at i and j, where i and j represent straight-line indices.
     * 
     * Ex. the straight-line indices of a 3x3 grid are as follows:
     *   0 1 2
     *   3 4 5
     *   6 7 8
     * Or, stated differently:
     * [(0,0), (0,1), (0,2), (1,0), (1,1), (1,2), (2,0), (2,1), (2,2)]
     */
    private void exch(int[][] b, int i, int j) {
        int len = b.length;
        int tmp = b[i / len][i % len];
        b[i / len][i % len] = b[j / len][j % len];
        b[j / len][j % len]= tmp;
    }
    
    // All neighboring boards
    public Iterable<Board> neighbors() {
        Stack s = new Stack();
        
        // locate the straight-line index of the empty space
        int i = 0;
        while (blocks[i / n][i % n] != 0) i++;
        
        int emptyCol = i % n;
        int leftCol = emptyCol - 1;
        int rightCol = emptyCol + 1;
        int emptyRow = i / n;
        int aboveRow = emptyRow - 1;
        int belowRow = emptyRow + 1;
        
        /**
         * Given an empty space (x) within a grid, the following boolean array
         * denotes which neighbors are valid:
         *         0,0 
         *    1,0   x   1,1
         *         0,1 
         */
        
        boolean[][] validNeighbors = new boolean[2][2];
        for (boolean[] dimension: validNeighbors) Arrays.fill(dimension, true);
        
        if (leftCol < 0) {
            validNeighbors[1][0] = false;
        }
        else if (rightCol >= n) {
            validNeighbors[1][1] = false;
        }
        if (aboveRow < 0) {
            validNeighbors[0][0] = false;
        }
        else if (belowRow >= n) {
            validNeighbors[0][1] = false;
        }
        
        // Construct new valid neighbors
        int[][] tmp;
        if (validNeighbors[0][0]) { // Check above
            tmp = blocks;
            exch(tmp, i, i - n);
            s.push(new Board(tmp));
        }
        if (validNeighbors[0][1]) { // Check below
            tmp = blocks;
            exch(tmp, i, i + n);
            s.push(new Board(tmp));
        }
        if (validNeighbors[1][0]) { // Check left
            tmp = blocks;
            exch(tmp, i, i - 1);
            s.push(new Board(tmp));
        }
        if (validNeighbors[1][1]) { // Check right
            tmp = blocks;
            exch(tmp, i, i + 1);
            s.push(new Board(tmp));
        }
        
        return s;
    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        String ans = "";
        ans += n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans += String.format("%2d ", blocks[i][j]);
            }
            ans += "\n";
        }
        return ans;
    }
    // unit tests (not graded)
    public static void main(String[] args) {
        In in = new In("tests-8puzzle/puzzle04.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        System.out.print(initial.toString());
    }
}
