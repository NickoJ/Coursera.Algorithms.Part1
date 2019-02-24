import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> segments = new ArrayList<>();

    public BruteCollinearPoints(Point[] inPoints) {
        if (inPoints == null) throw new IllegalArgumentException();
        for (Point point : inPoints) if (point == null) throw new IllegalArgumentException();
        Point[] points = inPoints.clone();
        Arrays.sort(points);

        for (int pi = 0; pi < points.length; ++pi) {
            Point p = points[pi];
            for (int qi = pi + 1; qi < points.length; ++qi) {
                Point q = points[qi];
                if (p.slopeTo(q) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
                for (int ri = qi + 1; ri < points.length; ++ri) {
                    Point r = points[ri];
                    for (int si = ri + 1; si < points.length; ++si) {
                        Point s = points[si];
                        checkPoints(p, q, r, s, segments);
                    }
                }
            }
        }
    }

    private void checkPoints(Point p, Point q, Point r, Point s, List<LineSegment> list) {
        double pq = p.slopeTo(q);
        double pr = p.slopeTo(r);
        double ps = p.slopeTo(s);

        if (approx(pq, pr) && approx(pr, ps)) list.add(new LineSegment(p, s));
    }

    private boolean approx(double lhv, double rhv) {
        return lhv == rhv;
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] array = new LineSegment[segments.size()];
        segments.toArray(array);
        return array;
    }



}