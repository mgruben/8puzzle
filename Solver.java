
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
    private final MinPQ<SearchNode> pqTwin;
    private SearchNode currentTwin;
    private boolean solvable = false;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        
        // Declare variables for given board
        current = new SearchNode(initial, null, 0);
        pq = new MinPQ<>();
        pq.insert(current);
        
        // Declare variables for given board's twin
        currentTwin = new SearchNode(initial.twin(), null, 0);
        pqTwin = new MinPQ<>();
        pqTwin.insert(currentTwin);
        
        // Look for a solution in either the given board or its twin
        while (!current.board.isGoal() && !currentTwin.board.isGoal()) {
            // Given board
            pq.delMin();
            for (Board neigh: current.board.neighbors()) {
                if (neigh != current.board) {
                    pq.insert(new SearchNode(neigh, current, current.numMoves + 1));
                }
            }
            
            // Its twin
            pqTwin.delMin();
            for (Board neigh: currentTwin.board.neighbors()) {
                if (neigh != currentTwin.board) {
                    pqTwin.insert(new SearchNode(
                            neigh, currentTwin, currentTwin.numMoves + 1));
                }
            }
            
            // Update the given board's pq and its twin's pq
            current = pq.min();
            currentTwin = pqTwin.min();
        }
        /**
         * Once we're out of the loop (that is, either the board or its twin was
         * solved), we need only check whether the given board was the one that
         * led to the solution.
         * If so, the given board is solvable.
         * If not, the given board is not solvable.
         */
        if (current.board.isGoal()) solvable = true;
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
        Stack s = new Stack();
        SearchNode currentTail = pq.min();
        while (currentTail != null) {
            s.push(currentTail.board);
            currentTail = currentTail.previous;
        }
        return s;
    }
    
    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int numMoves;
        SearchNode previous;
        
        public SearchNode(Board current, SearchNode previous, int numMoves) {
            this.board = current;
            this.previous = previous;
            this.numMoves = numMoves;
        }
        
        @Override
        public int compareTo(SearchNode that) {
            return Integer.valueOf(board.manhattan() + numMoves)
                    .compareTo(that.board.manhattan() + that.numMoves);
        }
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        In in = new In("tests-8puzzle/puzzle3x3-17.txt");
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
