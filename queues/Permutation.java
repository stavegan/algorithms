import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> random = new RandomizedQueue<String>();
        
        if (k > 0) {
            while (!StdIn.isEmpty()) {
                if (random.size() == k) random.dequeue();
                random.enqueue(StdIn.readString());
            }
        }

        for (String s : random) {
            StdOut.println(s);
        }
    }
}