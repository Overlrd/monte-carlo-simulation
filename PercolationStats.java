import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CFD_CRITICAL_VAL = 1.96;
    private final double[] results;
    private final double mean;
    private final double stddev;
    private final double cfdLow;
    private final double cfdHigh;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation sys = new Percolation(n);
            while (!sys.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                sys.open(row, col);
            }
            double result = (double) sys.numberOfOpenSites() / (n * n);
            this.results[i] = result;
        }

        this.mean = StdStats.mean(results);
        this.stddev = StdStats.stddev(results);
        this.cfdLow = this.mean - CFD_CRITICAL_VAL * (this.stddev / Math.sqrt(trials));
        this.cfdHigh = this.mean + CFD_CRITICAL_VAL * (this.stddev / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats statsOne = new PercolationStats(n, trials);
        StdOut.printf("%-20s= %f%n", "mean", statsOne.mean());
        StdOut.printf("%-20s= %f%n", "stddev", statsOne.stddev());
        StdOut.printf("%-20s= %f, %f%n", "95% confidence interval", statsOne.confidenceLo(),
                statsOne.confidenceHi());
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.cfdLow;
    }

    public double confidenceHi() {
        return this.cfdHigh;
    }
}