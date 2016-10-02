
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
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
public class Solver {
    private final MinPQ<SearchNode> pq;
    private SearchNode current;
    private boolean solvable = false;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        
        // Declare variables for given board
        current = new SearchNode(initial, null, 0);
        pq = new MinPQ<>();
        pq.insert(current);
        
        /**
         * Also insert the given board's twin, because we know that exactly
         * one of them will be solvable.
         */
        pq.insert(new SearchNode(initial.twin(), null, 0));
        
        // Look for a solution in either the given board or its twin
        while (!current.board.isGoal()) {
            // Given board
            pq.delMin();
            for (Board neigh: current.board.neighbors()) {
                if (current.previous == null || !neigh.equals(current.previous.board)) {
                    pq.insert(new SearchNode(neigh, current, current.numMoves + 1));
                }
            }
            
            // Update the given board's pq
            current = pq.min();
        }
        /**
         * Once we're out of the loop (that is, either the board or its twin was
         * solved), we need to check whether the given board was the one that
         * led to the solution.
         * 
         * To do this, we follow the chain from the current node (the solution)
         * back to its ancestor.
         * If and only if that ancestor is the initial board,
         * then the initial board is solvable.
         */
        SearchNode tmp = current;
        while (tmp.previous != null) tmp = tmp.previous;
        if (tmp.board.equals(initial)) solvable = true;
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        else return current.numMoves;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        
        // Store items in a stack, as per FAQ suggestion
        Stack<Board> s = new Stack<>();
        SearchNode currentTail = pq.min();
        while (currentTail != null) {
            s.push(currentTail.board);
            currentTail = currentTail.previous;
        }
        return s;
    }
    
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int numMoves;
        private SearchNode previous;
        
        public SearchNode(Board current, SearchNode previous, int numMoves) {
            this.board = current;
            this.previous = previous;
            this.numMoves = numMoves;
        }
        
        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(board.manhattan() + numMoves,
                    that.board.manhattan() + that.numMoves);
        }
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        In in = new In("tests-8puzzle/puzzle28.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
