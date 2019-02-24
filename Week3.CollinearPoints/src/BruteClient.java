import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteClient {

    private static final Point[] CUSTOM_POINTS;

    static {
        CUSTOM_POINTS = new Point[5];
        CUSTOM_POINTS[0] = new Point(13615, 24431);
        CUSTOM_POINTS[1] = new Point(12794,26529);
        CUSTOM_POINTS[2] = new Point(8875, 27639);
        CUSTOM_POINTS[3] = new Point(2172, 23156);
        CUSTOM_POINTS[4] = new Point(8875, 27639);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments

        points = setCustomPoints(points);

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private static Point[] setCustomPoints(Point[] points) {
        return CUSTOM_POINTS != null ? CUSTOM_POINTS : points;
    }

}