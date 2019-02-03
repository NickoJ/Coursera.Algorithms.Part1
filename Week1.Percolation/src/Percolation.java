import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int side;
    private final int n;
    private final int topRoot;
    private final int bottomRoot;
    private boolean[][] sites;

    private final WeightedQuickUnionUF qu;
    private final WeightedQuickUnionUF noRootQU;

    private int openCount;

    public Percolation(int side) {
        if (side <= 0) throw new IllegalArgumentException();

        this.side = side;
        this.n = side * side;
        this.topRoot = n;
        this.bottomRoot = n + 1;
        this.sites = new boolean[side][side];

        this.qu = new WeightedQuickUnionUF(n + 2);
        this.noRootQU = new WeightedQuickUnionUF(n + 2);
    }

    public void open(int row, int col) {
        --row;
        --col;
        validate(row, col);

        if (sites[row][col]) return;

        ++openCount;
        sites[row][col] = true;

        int index = toIndex(row, col);
        if (row == 0) {
            qu.union(index, topRoot);
            noRootQU.union(index, topRoot);
        } else if (sites[row - 1][col]) {
            int i = toIndex(row - 1, col);
            qu.union(index, i);
            noRootQU.union(index, i);
        }

        if (row == side - 1) {
            qu.union(index, bottomRoot);
        } else if (row < side - 1 && sites[row + 1][col]) {
            int i = toIndex(row + 1, col);
            qu.union(index, i);
            noRootQU.union(index, i);
        }

        if (col > 0 && sites[row][col - 1]) {
            int i = toIndex(row, col - 1);
            qu.union(index, i);
            noRootQU.union(index, i);
        }
        if (col < side - 1 && sites[row][col + 1]) {
            int i = toIndex(row, col + 1);
            qu.union(index, i);
            noRootQU.union(index, i);
        }
    }

    public boolean isOpen(int row, int col) {
        --row;
        --col;
        validate(row, col);

        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        --row;
        --col;
        validate(row, col);

        return sites[row][col] && noRootQU.connected(toIndex(row, col), n);
    }

    private void validate(int row, int col) {
        if (row < 0 || row >= side || col < 0 || col >= side) throw new IllegalArgumentException();
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        return qu.connected(topRoot, bottomRoot);
    }

    private int toIndex(int row, int col) {
        return side * row + col;
    }

}