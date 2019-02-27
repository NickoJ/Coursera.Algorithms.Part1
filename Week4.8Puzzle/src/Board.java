import edu.princeton.cs.algs4.Stack;

public class Board {

    private static final int[] INDICES_X = new int[] { -1, 1, 0, 0 };
    private static final int[] INDICES_Y = new int[] { 0, 0, -1, 1 };

    private final short[][] blocks;
    private int eX;
    private int eY;

    private int hammingValue = -1;
    private int manhattanValue = -1;

    public Board(int[][] blocks) {
        this.blocks = cloneBlocks(blocks);
    }

    private Board(short[][] blocks) {
        this.blocks = cloneBlocks(blocks);
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        calculate();
        return hammingValue;
    }

    public int manhattan() {
        calculate();
        return manhattanValue;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int dim = dimension();
        int x1 = dim - 1;
        int y1 = dim - 1;
        if (blocks[x1][y1] == 0) {
            --x1;
            --y1;
        }

        int x2 = 0;
        int y2 = 0;
        for (int i = 0; i < INDICES_X.length; ++i) {
            x2 = x1 + INDICES_X[i];
            y2 = y1 + INDICES_Y[i];
            if (x2 < 0 || x2 >= dim) continue;
            if (y2 < 0 || y2 >= dim) continue;
            if (blocks[x2][y2] != 0) break;
        }

        Board twin = new Board(blocks);
        twin.swap(x1, y1, x2, y2);
        return twin;
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (this == other) return true;

        if (other.getClass() == this.getClass()) {
            Board ob = (Board) other;

            int dim = dimension();
            if (dim != ob.dimension()) return false;

            for (int x = 0; x < dim; x++) {
                for (int y = 0; y < dim; y++) {
                    if (blocks[x][y] != ob.blocks[x][y]) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public Iterable<Board> neighbors() {
        calculate();

        Stack<Board> stack = new Stack<>();
        int dim = dimension();
        for (int i = 0; i < INDICES_X.length; ++i) {
            int x = eX + INDICES_X[i];
            int y = eY + INDICES_Y[i];

            if (x < 0 || y < 0 || x >= dim || y >= dim) continue;

            Board b = new Board(blocks);
            b.swap(x, y, eX, eY);
            stack.push(b);
        }
        return stack;
    }

    public String toString() {
        int dim = dimension();
        StringBuilder sb = new StringBuilder();
        sb.append(dim);
        sb.append('\n');
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                sb.append(' ');
                sb.append(blocks[x][y]);
                sb.append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    private void calculate() {
        if (hammingValue >= 0) return;

        hammingValue = 0;
        manhattanValue = 0;
        int dim = dimension();

        int searchValue = 0;
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                ++searchValue;
                if (blocks[x][y] == 0) {
                    eX = x;
                    eY = y;
                    continue;
                }
                if (blocks[x][y] == searchValue) continue;

                ++hammingValue;
                manhattanValue += calculateManhattanDistance(blocks[x][y], x, y, dim);
            }
        }
    }

    private static int calculateManhattanDistance(int value, int x, int y, int dim) {
        int index = value - 1;
        int rX = index / dim;
        int rY = index - rX * dim;
        return Math.abs(rX - x) + Math.abs(rY - y);
    }

    private void swap(int lhx, int lhy, int rhx, int rhy)
    {
        short tmp = blocks[lhx][lhy];
        blocks[lhx][lhy] = blocks[rhx][rhy];
        blocks[rhx][rhy] = tmp;
    }

    private static short[][] cloneBlocks(int[][] blocks) {
        int dim = blocks.length;
        short[][] newBlocks = new short[dim][dim];

        for (int x = 0; x < dim; ++x) {
            for (int y = 0; y < dim; ++y) {
                newBlocks[x][y] = (short) blocks[x][y];
            }
        }

        return newBlocks;
    }

    private static short[][] cloneBlocks(short[][] blocks) {
        int dim = blocks.length;
        short[][] newBlocks = new short[dim][dim];

        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                newBlocks[x][y] = blocks[x][y];
            }
        }

        return newBlocks;
    }

}