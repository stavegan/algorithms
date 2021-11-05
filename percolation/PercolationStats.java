import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96; 

    private final double trials;
    private final double[] stats;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }

        this.trials = trials;
        this.stats = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }

            stats[i] = (double) percolation.numberOfOpenSites() / ((double) n * (double) n);
        }
    }

    public double mean() {
        return StdStats.mean(this.stats);
    }

    public double stddev() {
        return StdStats.stddev(this.stats);
    }

    public double confidenceLo() {
        return StdStats.mean(this.stats) - CONFIDENCE_95 * StdStats.stddev(this.stats) / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        return StdStats.mean(this.stats) + CONFIDENCE_95 * StdStats.stddev(this.stats) / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}