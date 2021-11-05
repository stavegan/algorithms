import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private TreeMap<String, List<Integer>> nouns;
    private String[] synsets;
    private Digraph G;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null ||
            hypernyms == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }

        String[] synsetArray = (new In(synsets)).readAllLines();

        nouns = new TreeMap<String, List<Integer>>();
        this.synsets = new String[synsetArray.length];

        for (int i = 0; i < synsetArray.length; i++) {
            String[] synset = synsetArray[i].split(",");
            int id = Integer.parseInt(synset[0]); 
            
            this.synsets[id] = synset[1];

            for (String noun : synset[1].split(" ")) {
                if (!nouns.containsKey(noun)) {
                    nouns.put(noun, new LinkedList<Integer>());
                }
                List<Integer> value = nouns.get(noun);
                value.add(id);
            }
        }

        String[] hypernymArray = (new In(hypernyms)).readAllLines();

        G = new Digraph(synsetArray.length);

        for (int i = 0; i < hypernymArray.length; i++) {
            String[] hypernym = hypernymArray[i].split(",");
            int v = Integer.parseInt(hypernym[0]);

            for (int j = 1; j < hypernym.length; j++) {
                int w = Integer.parseInt(hypernym[j]);
                G.addEdge(v, w);
            }
        }

        if (!(new Topological(G)).hasOrder()) {
            throw new IllegalArgumentException("G doesn't correspond to a rooted DAG!");
        }

        int roots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) roots++;
        }

        if (roots != 1) {
            throw new IllegalArgumentException("G doesn't correspond to a rooted DAG!");
        }

        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }

        return nouns.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null ||
            nounB == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }

        if (!(isNoun(nounA) &&
              isNoun(nounB))) {
            throw new IllegalArgumentException("There is no such nouns in synsets!");
        }

        Iterable<Integer> v = nouns.get(nounA);
        Iterable<Integer> w = nouns.get(nounB);

        return sap.length(v, w);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null ||
            nounB == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }

        if (!(isNoun(nounA) &&
              isNoun(nounB))) {
            throw new IllegalArgumentException("There is no such nouns in synsets!");
        }

        Iterable<Integer> v = nouns.get(nounA);
        Iterable<Integer> w = nouns.get(nounB);

        int ancestor = sap.ancestor(v, w);

        if (ancestor == -1) {
            return "";
        }

        return synsets[ancestor];
    }

    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];

        WordNet wordnet = new WordNet(synsets, hypernyms);

        int count = 0;
        for (String noun : wordnet.nouns()) {
            count++;
        }

        System.out.println(count);
    }
}