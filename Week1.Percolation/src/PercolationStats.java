import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONF_COEFF = 1.96;

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        double n2 = n * n;

        double[] tresholds = new double[trials];
        double sqrtT = Math.sqrt(trials);

        for (int i = 0; i < trials; ++i) {
            Percolation perc = new Percolation(n);
            boolean percolates = false;
            do {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (perc.isOpen(row, col)) continue;

                perc.open(row, col);
                percolates = perc.percolates();
            } while (!percolates);
            tresholds[i] = perc.numberOfOpenSites() / n2;
        }
        mean = StdStats.mean(tresholds);
        stddev = StdStats.stddev(tresholds);
        confidenceLo = mean - (CONF_COEFF * stddev) / sqrtT;
        confidenceHi = mean + (CONF_COEFF * stddev) / sqrtT;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

}