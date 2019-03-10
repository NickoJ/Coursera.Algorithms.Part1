import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class PointSET {

    private final TreeSet<Point2D> bst;

    public PointSET() {
        bst = new TreeSet<>();
    }

    public boolean isEmpty() {
        return bst.isEmpty();
    }

    public int size() {
        return bst.size();
    }

    public void insert(Point2D p) {
        assertNull(p);
        bst.add(p);
    }

    public boolean contains(Point2D p) {
        assertNull(p);
        return bst.contains(p);
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);

        for (Point2D p : bst) p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        assertNull(rect);
        Point2D minP = new Point2D(rect.xmin(), rect.ymin());
        Point2D maxP = new Point2D(rect.xmax(), rect.ymax());

        Set<Point2D> subset = bst.subSet(minP, true, maxP, true);
        List<Point2D> points = new ArrayList<>();
        for (Point2D p : subset) if (rect.contains(p)) points.add(p);
        return points;
    }

    public Point2D nearest(Point2D p) {
        assertNull(p);

        Point2D nearest = null;
        double minD = 0.0;
        for (Point2D rp : bst) {
            if (nearest == null || minD > rp.distanceSquaredTo(p)) {
                nearest = rp;
                minD = rp.distanceSquaredTo(p);
            }
        }
        return nearest;
    }

    private static void assertNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

}