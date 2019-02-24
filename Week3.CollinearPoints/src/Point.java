import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        if (this.x == that.x) return Double.POSITIVE_INFINITY;
        if (this.y == that.y) return 0.0;

        return (double) (that.y - this.y) / (double) (that.x - this.x);
    }

    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return 1;
        return this.x - that.x;
    }

    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    private class SlopeComparator implements Comparator<Point> {

        @Override
        public int compare(Point lhv, Point rhv) {
            double lhvSlope = Point.this.slopeTo(lhv);
            double rhvSlope = Point.this.slopeTo(rhv);
            if (lhvSlope < rhvSlope) return -1;
            if (lhvSlope > rhvSlope) return 1;
            return 0;
        }

    }

}
