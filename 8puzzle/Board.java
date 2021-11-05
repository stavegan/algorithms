import java.lang.Math;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int n;
    private int[][] board;
    private int zero;

    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }

        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];

                if (board[i][j] == 0) {
                    zero = i * n + j;
                }
            }
        }
    }

    public String toString() {
        String str = Integer.toString(n) + "\n";

        String max = Integer.toString(n * n - 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String cur = Integer.toString(board[i][j]);

                for (int k = 0; k < max.length() - cur.length() + 1; k++) {
                    str += " ";
                }

                str += cur;
            }
            str += "\n";
        }

        return str;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int dist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i != n - 1 || j != n - 1) &&
                    board[i][j] != i * n + j + 1) {
                    dist++;
                }
            }
        }

        return dist;
    }

    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) {
                    dist += Math.abs(i - (board[i][j] - 1) / n);
                    dist += Math.abs(j - (board[i][j] - 1) % n);
                }     
            }
        }

        return dist;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    private boolean equals(Board y) {
        return toString().equals(y.toString());
    }

    public boolean equals(Object y) {
        if (y == null ||
            y.getClass() != Board.class) {
            throw new IllegalArgumentException();
        }

        return equals(y);
    }

    private void exch(int i, int j) {
        int tmp = board[i / n][i % n];
        board[i / n][i % n] = board[j / n][j % n];
        board[j / n][j % n] = tmp;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();

        if (zero / n > 0) {
            exch(zero, zero - n);
            neighbors.add(new Board(board));
            exch(zero, zero - n);
        }

        if (zero % n < n - 1) {
            exch(zero, zero + 1);
            neighbors.add(new Board(board));
            exch(zero, zero + 1);
        }

        if (zero / n < n - 1) {
            exch(zero, zero + n);
            neighbors.add(new Board(board));
            exch(zero, zero + n);
        }

        if (zero % n > 0) {
            exch(zero, zero - 1);
            neighbors.add(new Board(board));
            exch(zero, zero - 1);
        }

        return neighbors;
    }

    public Board twin() {
        int i = StdRandom.uniform(n * n);
        int j = StdRandom.uniform(n * n);

        if (i == j ||
            board[i / n][i % n] == 0 ||
            board[j / n][j % n] == 0) {
            return twin();
        }

        exch(i, j);
        Board twin = new Board(board);
        exch(i, j);

        return twin;
    }

    public static void main(String[] args) {
        int sz = 3;
        int[][] brd = new int[sz][sz];

        brd[0][0] = 8;
        brd[0][1] = 1;
        brd[0][2] = 3;

        brd[1][0] = 4;
        brd[1][1] = 0;
        brd[1][2] = 2;

        brd[2][0] = 7;
        brd[2][1] = 6;
        brd[2][2] = 5;

        /*for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                brd[i][j] = i * sz + j;
            }
        }*/

        Board cur = new Board(brd);
        Board another = new Board(brd);

        StdOut.println(cur);
        StdOut.println(cur.dimension());
        StdOut.println(cur.hamming());
        StdOut.println(cur.manhattan());
        StdOut.println(cur.isGoal());
        StdOut.println(cur.equals(another));

        Iterable<Board> neighbors = cur.neighbors();
        for (Board neighbor : neighbors) {
            StdOut.println(neighbor);
        }

        StdOut.println(cur.twin());
        StdOut.println(cur);
    }
}