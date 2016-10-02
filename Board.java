
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

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
    private final int n;
    private final char[] blocks;
    private int loc;
    private int manhattanDistance;
    
    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j).
     * @param blocks 
     */
    public Board(int[][] blocks) {
        // Check for bad input
        if (blocks == null) throw new NullPointerException();
        
        // Initializations
        manhattanDistance = 0;
        n = blocks.length;
        this.blocks = new char[n * n]; // Use a 1-dimensional array
        
        for (int i = 0; i < n * n; i++) {
            this.blocks[i] = (char) blocks[i / n][i % n]; // Store entry
            
            // Store as tmp as we may need to mutate for Manhattan Distance
            int t = this.blocks[i]; 
            
            if (t == 0) {
                loc = i; // We found the zero block, store its location
                /**
                 * Mutate t for Manhattan Distance calculations.
                 * Specifically, the zero block in a solved board is on the last
                 * row and the last column, which is the n*n - 1 index,
                 * and thus we would expect to a value of n*n.
                 * 
                 * Changing this one tmp instance is computationally simpler
                 * than is using %(n*n) in both of the below calculations to
                 * compare the expected value with the actual value.
                 */
                t = n * n;
            }
            
            // Calculate vertical distance
            manhattanDistance += Math.abs(((t - 1) / n) - (i / n));
            
            // Calculate horizontal distance
            manhattanDistance += Math.abs(((t - 1) % n) - (i % n));
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
            if (blocks[i] != (i + 1) % (n * n)) ham++;
        }
        return ham;
    }
    
    // Sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDistance;
    } 
    
    // Is this board the goal board?
    public boolean isGoal() {
        /**
         * Note that the "goal board" has entries of (i + 1) % (n * n)
         * for each index i.
         */
        for (int i = 0; i < n * n; i++) {
            if (blocks[i] != (i + 1) % (n * n)) return false;
        }
        return true;
    }
    
    // A board that is obtained by exchanging any pair of blocks
    public Board twin() {
        char[] twin = iterateCopy(blocks);
        int i = 0;
        while (twin[i] == 0) i++;
        int j = i + 1;
        while (twin[j] == 0) j++;
        exch(twin, i, j);
        return new Board(toDoubleArray(twin, n));
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
        for (int i = 0; i < n * n; i++) {
            if (this.blocks[i] != x.blocks[i]) return false;
        }
        return true;
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
     * 
     * This method is for 1-dimensional arrays.
     */
    private void exch(char[] b, int i, int j) {
        char tmp = b[i];
        b[i] = b[j];
        b[j] = tmp;
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
     * 
     * This method is for 2-dimensional arrays.
     */
    private void exch(int[][] b, int i, int j) {
        int len = b.length;
        int tmp = b[i / len][i % len];
        b[i / len][i % len] = b[j / len][j % len];
        b[j / len][j % len] = tmp;
    }
    
    /**
     * Copies the entries from o to c, and returns c.
     * This method presumes that o is a square array
     * (e.g. width = depth).
     * @param o
     * @return 
     */
    private char[] iterateCopy(char[] o) {
        char[] c = new char[o.length];
        for (int i = 0; i < o.length; i++) {
            c[i] = o[i];
        }
        return c;
    }
    
    /**
     * Copies the entries from o to c, where o is a 1d array, and c is a 2d
     * array, and returns c.
     * This method presumes that o is a square array
     * (e.g. width = depth).
     * @param o
     * @param len
     * @return 
     */
    private int[][] toDoubleArray(char[] o, int len) {
        int[][] c = new int[len][len];
        for (int i = 0; i < o.length; i++) {
            c[i / n][i % n] = o[i];
        }
        return c;
    }
    
    // All neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> s = new Stack<>();
        
        // Calculate spatial variables for clarity
        int emptyCol = loc % n;
        int emptyRow = loc / n;

        // Construct new valid neighbors
        if (emptyRow - 1 >= 0) { // Empty space filled from above
            s.push(newNeighbor(loc - n));
        }
        if (emptyRow + 1 < n) { // Empty space filled from below
            s.push(newNeighbor(loc + n));
        }
        if (emptyCol - 1 >= 0) { // Empty space filled from the left
            s.push(newNeighbor(loc - 1));
        }
        if (emptyCol + 1 < n) { // Empty space filled from the right
            s.push(newNeighbor(loc + 1));
        }
        
        return s;
    }
    
    /**
     * Given the 1-dimensional index of the location from which to move
     * the next block, moves the block at that index into the empty space,
     * and returns a new Board representing that movement.
     * @param toBeEmpty
     * @return 
     */
    private Board newNeighbor(int toBeEmpty) {
        int[][] tmp = toDoubleArray(blocks, n);
        exch(tmp, loc, toBeEmpty);
        return new Board(tmp);
    }
    
    // string representation of this board (in the output format specified below)
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) blocks[i*n + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    // unit tests (not graded)
    public static void main(String[] args) {
        In in = new In("tests-8puzzle/puzzle3x3-unsolvable1.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial.toString());
        Board twin = initial.twin();
        System.out.println(twin.toString());
        System.out.println(initial.isGoal());
        System.out.println(twin.isGoal());
    }
}
