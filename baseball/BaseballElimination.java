import java.util.TreeMap;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
    private FlowNetwork base;
    private Team teams[];
    private TreeMap<String, Integer> index;

    private class Team {
        String name;
        int w, l, r, g[];
        boolean c, isEliminated;
        LinkedList<String> certificateOfElimination;
    }

    private FlowNetwork copyFlowNetwork(FlowNetwork original) {
        FlowNetwork copy = new FlowNetwork(original.V());
        for (FlowEdge e : original.edges())
            copy.addEdge(new FlowEdge(e));
        return copy;
    }

    public BaseballElimination(String filename) {        
        In in = new In(filename);

        int n = in.readInt(), g = n * (n - 1) / 2;
        
        base = new FlowNetwork(2 + g + n);
        
        teams = new Team[n];
        index = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            teams[i] = new Team();
            teams[i].name = in.readString();
            teams[i].w = in.readInt();
            teams[i].l = in.readInt();
            teams[i].r = in.readInt();
            teams[i].g = new int[n];
            for (int j = 0; j < n; j++) {
                teams[i].g[j] = in.readInt();
                if (j > i) {
                    int ij = g - (n - i) * (n - i - 1) / 2 + (j - i - 1);
                    base.addEdge(new FlowEdge(0, 1 + ij, (double)teams[i].g[j]));
                    base.addEdge(new FlowEdge(1 + ij, 1 + g + i, Double.POSITIVE_INFINITY));
                    base.addEdge(new FlowEdge(1 + ij, 1 + g + j, Double.POSITIVE_INFINITY));
                }
            }
            teams[i].certificateOfElimination = new LinkedList<>();
            index.put(teams[i].name, i);
        }
    }

    public int numberOfTeams() {
        return teams.length;
    }

    public Iterable<String> teams() {
        return index.keySet();
    }

    public int wins(String team) {
        Integer i = index.get(team);

        if (i == null) {
            throw new IllegalArgumentException("Argument must be a valid team!");
        }

        return teams[i].w;
    }

    public int losses(String team) {
        Integer i = index.get(team);

        if (i == null) {
            throw new IllegalArgumentException("Argument must be a valid team!");
        }

        return teams[i].l;
    }

    public int remaining(String team) {
        Integer i = index.get(team);

        if (i == null) {
            throw new IllegalArgumentException("Argument must be a valid team!");
        }

        return teams[i].r;
    }

    public int against(String team1, String team2) {
        Integer i = index.get(team1), j = index.get(team2);

        if (i == null || j == null) {
            throw new IllegalArgumentException("Argument must be a valid team!");
        }

        return teams[i].g[j];
    }

    private void calculateElimination(int i) {
        teams[i].c = true;
        FlowNetwork nw = copyFlowNetwork(base);
        for (int j = 0, p = teams[i].w + teams[i].r; j < teams.length; j++) {
            if (p < teams[j].w) {
                teams[i].isEliminated = true;
                teams[i].certificateOfElimination.add(teams[j].name);
                continue;
            }
            nw.addEdge(new FlowEdge(nw.V() - 1 - teams.length + j, nw.V() - 1, p - (i == j ? 0 : teams[j].w)));
        }
        if (teams[i].isEliminated) return;
        FordFulkerson ff = new FordFulkerson(nw, 0, nw.V() - 1); 
        for (FlowEdge e : nw.adj(0)) {
            if (e.flow() != e.capacity()) {
                teams[i].isEliminated = true;
                for (int j = nw.V() - teams.length - 1; j < nw.V() - 1; j++) {
                    if (ff.inCut(j)) {
                        teams[i].certificateOfElimination.add(teams[j - nw.V() + 1 + teams.length].name);
                    }
                }
                break;
            }
        }
    }

    public boolean isEliminated(String team) {
        Integer i = index.get(team);

        if (i == null) {
            throw new IllegalArgumentException("Argument must be a valid team!");
        }

        if (!teams[i].c) calculateElimination(i);

        return teams[i].isEliminated;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team)) {
            return null;
        }

        Integer i = index.get(team);

        if (i == null) {
            throw new IllegalArgumentException("Argument must be a valid team!");
        }

        return teams[i].certificateOfElimination;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
