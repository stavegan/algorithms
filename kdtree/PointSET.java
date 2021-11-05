import java.lang.Math;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> range = new ArrayList<Point2D>();

        for (Point2D p : set) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax() &&
                p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
                range.add(p);
            }
        }

        return range;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            return null;
        }

        Point2D nearest = new Point2D(Double.MAX_VALUE, Double.MAX_VALUE);
        double distance = Math.sqrt(Math.pow(p.x() - nearest.x(), 2.) +
                                    Math.pow(p.y() - nearest.y(), 2.));

        for (Point2D n : set) {
            double d = Math.sqrt(Math.pow(p.x() - n.x(), 2.) +
                                 Math.pow(p.y() - n.y(), 2.));
            if (d < distance) {
                nearest  = n;
                distance = d;
            }
        }

        return nearest;
    }
}