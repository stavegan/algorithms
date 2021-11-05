import java.util.Comparator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean isSolvable;
    private Node solution;

    private class Node {
        public Board board;
        public int   moves;
        public Node  prev;
        public int   manhattan;

        public Node(Node another) {
            this.board     = another.board;
            this.moves     = another.moves;
            this.prev      = another.prev;
            this.manhattan = another.manhattan;
        }

        public Node(Board board, int moves, Node prev) {
            if (board == null) {
                throw new IllegalArgumentException();
            }

            this.board = board;
            this.moves = moves;
            this.prev  = prev;
            manhattan  = board.manhattan();
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Comparator<Node> comp = (Node first, Node second) -> {
            int p = first.manhattan + first.moves;
            int q = second.manhattan + second.moves;

            if (p == q) {
                return 0;
            }

            return p < q ? -1 : 1;
        };

        MinPQ<Node> orig = new MinPQ<Node>(comp);
        orig.insert(new Node(initial, 0, null));

        MinPQ<Node> twin = new MinPQ<Node>(comp);
        twin.insert(new Node(initial.twin(), 0, null));

        Node origMin = orig.delMin();
        Node twinMin = twin.delMin();
        while (!origMin.board.isGoal() &&
               !twinMin.board.isGoal()) {
            for (Board neighbor : origMin.board.neighbors()) {
                if (origMin.prev != null &&
                    neighbor.equals(origMin.prev.board)) {
                    continue;
                }
                orig.insert(new Node(neighbor, origMin.moves + 1, origMin));
            }
            origMin = orig.delMin();

            for (Board neighbor : twinMin.board.neighbors()) {
                if (twinMin.prev != null &&
                    neighbor.equals(twinMin.prev.board)) {
                    continue;
                }
                twin.insert(new Node(neighbor, twinMin.moves + 1, twinMin));
            }
            twinMin = twin.delMin();
        }

        if (origMin.board.isGoal()) {
            isSolvable = true;
            solution = origMin;
        } else {
            isSolvable = false;
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        if (!isSolvable) {
            return -1;
        }

        return solution.moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        }

        Stack<Board> seq = new Stack<Board>();

        Node cur = new Node(solution);
        while (cur != null) {
            seq.push(cur.board);
            cur = cur.prev;
        }

        return seq;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = StdIn.readInt();
            }
        }
            
        Board initial = new Board(tiles);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}