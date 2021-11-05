import java.lang.Math;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;

    private class Node {
        public Point2D point;
        public Node left, right;
        public int size;

        public Node(Point2D point) {
            this.point = point;
            size = 1;
        }
    }

    private class LineSegment {
        public Point2D first;
        public Point2D second;

        public LineSegment(Point2D first, Point2D second) {
            this.first  = first;
            this.second = second;
        }

        public void draw(boolean even) {
            if (even) {
                StdDraw.setPenColor(StdDraw.RED);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
            }
            
            StdDraw.line(first.x(), first.y(), second.x(), second.y());
        }
    }

    public KdTree() {}

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        if (isEmpty()) {
            return 0;
        }

        return root.size;
    }

    private Node insert(Node node, Point2D point, boolean even) {
        if (node == null) {
            return new Node(point);
        }

        if ((even && point.x() < node.point.x()) ||
           (!even && point.y() < node.point.y())) {
            node.left  = insert(node.left,  point, !even);
        } else {
            node.right = insert(node.right, point, !even);
        }

        node.size = (node.left  == null ? 0 : node.left.size) +
                    (node.right == null ? 0 : node.right.size) + 1;
        
        return node;
    }

    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            root = new Node(point);
        } else if (!contains(point)) {
            root = insert(root, point, true);
        }
    }

    private boolean contains(Node node, Point2D point, boolean even) {
        if (node == null) {
            return false;
        }

        if (node.point.x() == point.x() &&
            node.point.y() == point.y()) {
            return true;
        }

        if ((even && point.x() < node.point.x()) ||
           (!even && point.y() < node.point.y())) {
            return contains(node.left,  point, !even);
        } else {
            return contains(node.right, point, !even);
        }
    }

    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }

        return contains(root, point, true);
    }

    private void draw(Node node, LineSegment red, LineSegment blue, boolean even) {
        if (node == null) {
            return;
        }

        LineSegment r = red;
        LineSegment b = blue;

        if (even) {
            r = new LineSegment(new Point2D(node.point.x(), .0), new Point2D(node.point.x(), 1.));

            if (blue != null) {
                if (node.point.y() < blue.first.y()) {
                    r.second = new Point2D(r.second.x(), blue.first.y());

                    if (red != null) {
                        r.first  = new Point2D(r.first.x(),  red.first.y());
                    }
                } else {
                    r.first  = new Point2D(r.first.x(), blue.first.y());

                    if (red != null) {
                        r.second = new Point2D(r.second.x(), red.second.y());
                    }
                }
            }

            r.draw(even);
        } else {
            b = new LineSegment(new Point2D(.0, node.point.y()), new Point2D(1., node.point.y()));

            if (red != null) {
                if (node.point.x() < red.first.x()) {
                    b.second = new Point2D(red.first.x(), b.second.y());

                    if (blue != null) {
                        b.first  = new Point2D(blue.first.x(), b.first.y());
                    }
                } else {
                    b.first  = new Point2D(red.first.x(), b.first.y());

                    if (blue != null) {
                        b.second = new Point2D(blue.second.x(), b.second.y());
                    }
                }
            }

            b.draw(even);
        }

        StdDraw.setPenColor();
        node.point.draw();

        draw(node.left,  r, b, !even);
        draw(node.right, r, b, !even);
    }

    public void draw() {
        draw(root, null, null, true);
    }

    private void range(Node node, RectHV rect, LineSegment red, LineSegment blue, boolean even, ArrayList<Point2D> range) {
        if (node == null) {
            return;
        }

        if (rect.contains(node.point)) {
            range.add(node.point);
        }

        LineSegment r = red;
        LineSegment b = blue;

        if (even) {
            r = new LineSegment(new Point2D(node.point.x(), .0), new Point2D(node.point.x(), 1.));

            if (blue != null) {
                if (node.point.y() < blue.first.y()) {
                    r.second = new Point2D(r.second.x(), blue.first.y());

                    if (red != null) {
                        r.first  = new Point2D(r.first.x(),  red.first.y());
                    }
                } else {
                    r.first  = new Point2D(r.first.x(), blue.first.y());

                    if (red != null) {
                        r.second = new Point2D(r.second.x(), red.second.y());
                    }
                }
            }

            LineSegment t = blue;
            
            if (t == null) {
                t = new LineSegment(new Point2D(.0, .0), new Point2D(1., .0));
            }

            if (node.point.y() < t.first.y()) {
                if (rect.intersects(new RectHV(t.first.x(),  r.first.y(),  r.first.x(),  t.first.y()))) {
                    range(node.left,  rect, r, b, !even, range);
                }

                if (rect.intersects(new RectHV(r.first.x(),  r.first.y(),  t.second.x(), t.second.y()))) {
                    range(node.right, rect, r, b, !even, range);
                }
            } else {
                if (rect.intersects(new RectHV(t.first.x(),  t.first.y(),  r.second.x(), r.second.y()))) {
                    range(node.left,  rect, r, b, !even, range);
                }

                if (rect.intersects(new RectHV(r.second.x(), t.second.y(), t.second.x(), r.second.y()))) {
                    range(node.right, rect, r, b, !even, range);
                }
            }     
        } else {
            b = new LineSegment(new Point2D(.0, node.point.y()), new Point2D(1., node.point.y()));

            if (red != null) {
                if (node.point.x() < red.first.x()) {
                    b.second = new Point2D(red.first.x(), b.second.y());

                    if (blue != null) {
                        b.first  = new Point2D(blue.first.x(), b.first.y());
                    }
                } else {
                    b.first  = new Point2D(red.first.x(), b.first.y());

                    if (blue != null) {
                        b.second = new Point2D(blue.second.x(), b.second.y());
                    }
                }
            }

            LineSegment t = red;

            if (t == null) {
                t = new LineSegment(new Point2D(.0, .0), new Point2D(.0, 1.));
            }

            if (node.point.x() < t.first.x()) {
                if (rect.intersects(new RectHV(b.first.x(),  t.first.y(),  t.first.x(),  b.first.y()))) {
                    range(node.left,  rect, r, b, !even, range);
                }

                if (rect.intersects(new RectHV(b.first.x(),  b.first.y(),  t.second.x(), t.second.y()))) {
                    range(node.right, rect, r, b, !even, range);
                }
            } else {
                if (rect.intersects(new RectHV(t.first.x(),  t.first.y(),  b.second.x(), b.second.y()))) {
                    range(node.left,  rect, r, b, !even, range);
                }

                if (rect.intersects(new RectHV(t.second.x(), b.second.y(), b.second.x(), t.second.y()))) {
                    range(node.right, rect, r, b, !even, range);
                }
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> range = new ArrayList<Point2D>();

        range(root, rect, null, null, true, range);

        return range;
    }

    private Point2D nearest(Node node, Point2D point, boolean even, Point2D nearest) {
        if (node == null) {
            return nearest;
        }

        if (point.distanceTo(node.point) <
            point.distanceTo(nearest)) {
            nearest = node.point;
        }

        if ((even && point.x() < node.point.x()) ||
           (!even && point.y() < node.point.y())) {
            nearest = nearest(node.left,  point, !even, nearest);

            if ((even && point.distanceTo(new Point2D(node.point.x(), point.y())) < point.distanceTo(nearest)) ||
               (!even && point.distanceTo(new Point2D(point.x(), node.point.y())) < point.distanceTo(nearest))) {
                Point2D n = nearest(node.right, point, !even, nearest);

                if (point.distanceTo(n) <
                    point.distanceTo(nearest)) {
                    nearest = n;
                }
            }
        } else {
            nearest = nearest(node.right, point, !even, nearest);

            if ((even && point.distanceTo(new Point2D(node.point.x(), point.y())) < point.distanceTo(nearest)) ||
               (!even && point.distanceTo(new Point2D(point.x(), node.point.y())) < point.distanceTo(nearest))) {
                Point2D n = nearest(node.left, point, !even, nearest);

                if (point.distanceTo(n) <
                    point.distanceTo(nearest)) {
                    nearest = n;
                }
            }
        }

        return nearest;
    }

    public Point2D nearest(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }

        return nearest(root, point, true, new Point2D(Double.MAX_VALUE, Double.MAX_VALUE));
    }
}