import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int max = -1, m = -1;
        for (int i = 0, d = 0; i < nouns.length; i++, d = 0) {
            for (int j = 0; j < nouns.length; j++) {
                d += wordnet.distance(nouns[i], nouns[j]);
            }
            if (d > max) {
                max = d;
                m = i;
            }
        }

        return nouns[m];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}