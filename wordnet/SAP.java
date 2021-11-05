import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

public class SAP {
    private Digraph G, R;

    private int root(int v, boolean[] marked) {
        marked[v] = true;
        for (int a : G.adj(v)) {
            if (!marked[a]) {
                return root(a, marked);
            }
        }  
        return v;
    }

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }

        this.G = G;
        R = G.reverse();
    }

    private int dfs1(int v, int[] distance, boolean[] marked) {
        if (marked[v]) {
            return distance[v];
        }
        
        marked[v] = true;
        for (int a : R.adj(v)) {
            int length = dfs1(a, distance, marked);
            if (length != -1) {
                if (distance[v] == -1 || length + 1 < distance[v]) {
                    distance[v] = length + 1;
                }
            }
        }

        return distance[v];
    }

    private int dfs2(int v, int[] distance, int[] ancestor, boolean[] marked) {
        if (marked[v]) {
            return distance[v];
        }
        
        marked[v] = true;
        for (int a : G.adj(v)) {
            int length = dfs2(a, distance, ancestor, marked);
            if (length != -1) {
                if (distance[v] == -1 || length + 1 < distance[v]) {
                    distance[v] = length + 1;
                    ancestor[v] = ancestor[a];
                }
            }
        }

        return distance[v];
    }

    public int length(int v, int w) {
        if (v < 0 || v >= G.V() ||
            w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("There is no such vertices in graph!");
        }

        LinkedList<Integer> ve = new LinkedList<>(); ve.add(v);
        LinkedList<Integer> we = new LinkedList<>(); we.add(w);

        return length(ve, we);
    }

    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() ||
            w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("There is no such vertices in graph!");
        }

        LinkedList<Integer> ve = new LinkedList<>(); ve.add(v);
        LinkedList<Integer> we = new LinkedList<>(); we.add(w);

        return ancestor(ve, we);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null ||
            w == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }

        for (Integer vertex : v) {
            if (vertex == null) {
                throw new IllegalArgumentException("Arguments can't be null!");
            } else {
                if (vertex < 0 || vertex >= G.V()) {
                    throw new IllegalArgumentException("There is no such vertices in graph!");
                }
            }
        }

        for (Integer vertex : w) {
            if (vertex == null) {
                throw new IllegalArgumentException("Arguments can't be null!");
            } else {
                if (vertex < 0 || vertex >= G.V()) {
                    throw new IllegalArgumentException("There is no such vertices in graph!");
                }
            }
        }

        int[] length = new int[G.V()];
        boolean[] marked1 = new boolean[G.V()];
        
        for (int i = 0; i < G.V(); i++) {
            length[i] = -1;
        }

        for (int a : v) {
            length[a] = 0;
        }

        for (int i = 0; i < G.V(); i++) {
            if (!marked1[i]) {
                dfs1(i, length, marked1);
            }
        }

        int[] ancestor = new int[G.V()];
        boolean[] marked2 = new boolean[G.V()];

        for (int i = 0; i < G.V(); i++) {
            if (length[i] == -1) {
                ancestor[i] = -1;
            } else {
                ancestor[i] = i;
            }
        }

        for (int a : w) {
            if (!marked2[a]) {
                dfs2(a, length, ancestor, marked2);
            }
        }

        int len = -1;
        for (int a : w) {
            if (length[a] < (len == -1 ? Integer.MAX_VALUE : len)) {
                len = length[a];
            }
        }

        return len;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null ||
            w == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }

        for (Integer vertex : v) {
            if (vertex == null) {
                throw new IllegalArgumentException("Arguments can't be null!");
            } else {
                if (vertex < 0 || vertex >= G.V()) {
                    throw new IllegalArgumentException("There is no such vertices in graph!");
                }
            }
        }

        for (Integer vertex : w) {
            if (vertex == null) {
                throw new IllegalArgumentException("Arguments can't be null!");
            } else {
                if (vertex < 0 || vertex >= G.V()) {
                    throw new IllegalArgumentException("There is no such vertices in graph!");
                }
            }
        }

        int[] length = new int[G.V()];
        boolean[] marked1 = new boolean[G.V()];
        
        for (int i = 0; i < G.V(); i++) {
            length[i] = -1;
        }

        for (int a : v) {
            length[a] = 0;
        }

        for (int i = 0; i < G.V(); i++) {
            if (!marked1[i]) {
                dfs1(i, length, marked1);
            }
        }

        int[] ancestor = new int[G.V()];
        boolean[] marked2 = new boolean[G.V()];

        for (int i = 0; i < G.V(); i++) {
            if (length[i] == -1) {
                ancestor[i] = -1;
            } else {
                ancestor[i] = i;
            }
        }

        for (int a : w) {
            if (!marked2[a]) {
                dfs2(a, length, ancestor, marked2);
            }
        }

        int len = -1;
        int anc = -1;
        for (int a : w) {
            if (length[a] < (len == -1 ? Integer.MAX_VALUE : len)) {
                len = length[a];
                anc = ancestor[a];
            }
        }

        return anc;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
