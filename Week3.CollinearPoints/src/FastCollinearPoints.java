import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final List<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) if (point == null) throw new IllegalArgumentException();
        Point[] checkPoints = points.clone();

        List<Point> ps = new ArrayList<>();
        List<Point> qs = new ArrayList<>();

        int n = points.length;
        for (int i = 0; i < n; ++i) {
            Point p = points[i];
            Arrays.sort(checkPoints, p.slopeOrder());

            double slope = Double.NEGATIVE_INFINITY;
            int counter = 0;
            Point min = p;
            Point max = p;
            boolean findRepeat = false;
            for (Point q : checkPoints) {
                double newSlope = p.slopeTo(q);
                if (newSlope == Double.NEGATIVE_INFINITY) {
                    if (findRepeat) throw new IllegalArgumentException();
                    findRepeat = true;
                    continue;
                }

                if (counter > 0 && !approx(slope, newSlope)) {
                    tryToAdd(min, max, counter, ps, qs);
                    counter = 0;
                }

                slope = newSlope;

                if (counter == 0) {
                    counter = 1;
                    min = less(p, q) ? p : q;
                    max = min == p ? q : p;
                } else {
                    min = less(min, q) ? min : q;
                    max = less(q, max) ? max : q;
                }
                ++counter;
            }
            tryToAdd(min, max, counter, ps, qs);
        }

        for (int i = 0; i < ps.size(); ++i) segments.add(new LineSegment(ps.get(i), qs.get(i)));
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] array = new LineSegment[segments.size()];
        segments.toArray(array);
        return array;
    }

    private void tryToAdd(Point min, Point max, int counter, List<Point> ps, List<Point> qs) {
        if (counter < 4) return;
        if (min == null || max == null) return;

        for (int i = 0; i < ps.size(); ++i) {
            Point p = ps.get(i);
            Point q = qs.get(i);
            if (q == max && p == min) return;
        }

        ps.add(min);
        qs.add(max);
    }

    private boolean approx(double lhv, double rhv) {
        return lhv == rhv;
    }

    private boolean less(Point lhv, Point rhv) {
        return lhv.compareTo(rhv) < 0;
    }

}