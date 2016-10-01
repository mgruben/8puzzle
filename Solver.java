
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
    MinPQ<SearchNode> pq;
    SearchNode current;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        current = new SearchNode(initial, null, 0);
        System.out.println(current.board.toString());
        pq = new MinPQ<>();
        pq.insert(current);
        while (!current.board.isGoal()) {
            pq.delMin();
            for (Board neigh: current.board.neighbors()) {
                if (neigh != current.board) {
                    pq.insert(new SearchNode(neigh, current, current.numMoves + 1));
                }
            }
            current = pq.min();
            System.out.println(current.board.toString());
        }
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        // TODO
        return true;
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
        SearchNode current = pq.min();
        while (current != null) {
            s.push(current.board);
            current = current.previous;
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
            return Integer.valueOf(board.hamming() + numMoves)
                    .compareTo(that.board.hamming() + that.numMoves);
        }
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        In in = new In("tests-8puzzle/puzzle04.txt");
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
