import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {

    private int moves;
    private SolveNode solutionNode;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Board twin = initial.twin();

        MinPQ<SolveNode> pq = new MinPQ<>();
        pq.insert(new SolveNode(initial));
        pq.insert(new SolveNode(twin));

        boolean solutionFounded = false;
        do {
            SolveNode node = pq.delMin();
            if (node.board.isGoal()) {
                if (node.root == initial) {
                    moves = node.moves;
                    prepareSolution(node);
                } else {
                    moves = -1;
                }
                solutionFounded = true;
            }
            else {
                Board prevBoard = node.prev != null ? node.prev.board : null;
                for (Board neighbor : node.board.neighbors()) {
                    if (neighbor.equals(prevBoard)) continue;
                    pq.insert(new SolveNode(neighbor, node));
                }
            }

        } while (!solutionFounded);
    }

    public boolean isSolvable() {
        return solutionNode != null;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return isSolvable() ? () -> new SolveNodeIterator(solutionNode) : null;
    }

    private void prepareSolution(SolveNode node) {
        solutionNode = node;
        while (node.prev != null) {
            solutionNode = node.prev;
            solutionNode.next = node;
            node = solutionNode;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
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

    private static class SolveNode implements Comparable<SolveNode> {

        Board board;
        int moves;
        Board root;
        SolveNode prev;
        SolveNode next;

        SolveNode(Board board) {
            this(board, null);
        }

        SolveNode(Board board, SolveNode prev) {
            this.board = board;
            this.prev = prev;
            if (prev == null) {
                this.root = board;
                this.moves = 0;
            } else {
                this.root = prev.root;
                this.moves = prev.moves + 1;
            }
        }

        @Override
        public int compareTo(SolveNode other) {
            return (this.board.manhattan() + this.moves) - (other.board.manhattan() + other.moves);
        }

    }

    private static class SolveNodeIterator implements Iterator<Board> {

        private SolveNode node;

        private SolveNodeIterator(SolveNode node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Board next() {
            if (!hasNext()) throw new NoSuchElementException();

            Board board = node.board;
            node = node.next;

            return board;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}