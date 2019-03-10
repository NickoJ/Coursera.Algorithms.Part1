import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Comparator;

public class KdTree {

    private Node root;
    private int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        assertNull(p);
        if (root == null) {
            root = new Node(p, Node.VERTICAL);
            ++size;
            return;
        }

        Node node = findNode(p, true);
        if (!node.p.equals(p)) {
            Node newNode = new Node(p, !node.divider);
            if (node.divider == Node.HORIZONTAL) {
                if (Point2D.Y_ORDER.compare(p, node.p) < 0) node.lb = newNode;
                else node.rt = newNode;
            } else {
                if (Point2D.X_ORDER.compare(p, node.p) < 0) node.lb = newNode;
                else node.rt = newNode;
            }
            ++size;
        }
    }

    public boolean contains(Point2D p) {
        assertNull(p);
        return findNode(p, false) != null;
    }

    public void draw() {
        if (root != null) draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV rect) {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(node.p.x(), node.p.y());

        StdDraw.setPenRadius();
        if (node.divider == Node.HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), node.p.y(), rect.xmax(), node.p.y());
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), rect.ymin(), node.p.x(), rect.ymax());
        }

        if (node.lb != null) {
            draw(node.lb, createLbRectForNode(node, rect));
        }
        if (node.rt != null) {
            draw(node.rt, createRtRectForNode(node, rect));
        }
    }

    private RectHV createLbRectForNode(Node node, RectHV oRect) {
        if (node.divider == Node.HORIZONTAL) {
            return new RectHV(oRect.xmin(), oRect.ymin(), oRect.xmax(), node.p.y());
        } else {
            return new RectHV(oRect.xmin(), oRect.ymin(), node.p.x(), oRect.ymax());
        }
    }

    private RectHV createRtRectForNode(Node node, RectHV oRect) {
        if (node.divider == Node.HORIZONTAL) {
            return new RectHV(oRect.xmin(), node.p.y(), oRect.xmax(), oRect.ymax());
        } else {
            return new RectHV(node.p.x(), oRect.ymin(), oRect.xmax(), oRect.ymax());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        assertNull(rect);
        if (root == null) return Collections.emptyList();

        List<Point2D> points = new ArrayList<>();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node node = queue.remove();
            if (rect.contains(node.p)) points.add(node.p);
            if (node.divider == Node.HORIZONTAL) {
                if (rect.ymin() <= node.p.y() && node.lb != null) queue.add(node.lb);
                if (rect.ymax() >= node.p.y() && node.rt != null) queue.add(node.rt);
            } else {
                if (rect.xmin() <= node.p.x() && node.lb != null) queue.add(node.lb);
                if (rect.xmax() >= node.p.x() && node.rt != null) queue.add(node.rt);
            }
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        assertNull(p);
        Node found = nearest(root, root, p);
        return found != null ? found.p : null;
    }

    private Node nearest(Node curNode, Node minNode, Point2D p) {
        if (curNode == null) return minNode;

        if (curNode.p.distanceSquaredTo(p) < minNode.p.distanceSquaredTo(p)) {
            minNode = curNode;
        }

        Node first;
        Node second;
        double minMaxDist;

        Comparator<Point2D> comparator = curNode.divider == Node.VERTICAL ? Point2D.X_ORDER : Point2D.Y_ORDER;
        if (comparator.compare(p, curNode.p) <= 0) {
            first = curNode.lb;
            second = curNode.rt;
        } else {
            first = curNode.rt;
            second = curNode.lb;
        }
        if (curNode.divider == Node.VERTICAL) {
            minMaxDist = p.distanceSquaredTo(new Point2D(curNode.p.x(), p.y()));
        } else {
            minMaxDist = p.distanceSquaredTo(new Point2D(p.x(), curNode.p.y()));
        }

        minNode = nearest(first, minNode, p);
        if (p.distanceSquaredTo(minNode.p) > minMaxDist) minNode = nearest(second, minNode, p);
        return minNode;
    }

    private Node findNode(Point2D p, boolean forInsert) {
        Node currentNode = root;
        while (currentNode != null) {
            if (p.equals(currentNode.p)) break;

            Node nextNode = null;
            if (currentNode.divider == Node.HORIZONTAL) {
                if (Point2D.Y_ORDER.compare(p, currentNode.p) < 0) nextNode = currentNode.lb;
                else nextNode = currentNode.rt;
            } else {
                if (Point2D.X_ORDER.compare(p, currentNode.p) < 0) nextNode = currentNode.lb;
                else nextNode = currentNode.rt;
            }

            if (nextNode == null && forInsert) break;
            currentNode = nextNode;
        }
        return currentNode;
    }

    private static void assertNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    private static class Node {
        private static final boolean HORIZONTAL = true;
        private static final boolean VERTICAL = false;

        private final Point2D p;
        private final boolean divider;
        private Node lb;
        private Node rt;

        private Node(Point2D p, boolean divider) {
            this.p = p;
            this.divider = divider;
        }

    }

}