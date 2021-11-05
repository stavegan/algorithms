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

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int compareTo(Point that) {
        if (this.x == that.x &&
            this.y == that.y) return 0;

        return this.y < that.y || this.y == that.y && this.x < that.x ? -1 : 1;
    }

    public double slopeTo(Point that) {
        if (this.x == that.x &&
            this.y == that.y) return Double.NEGATIVE_INFINITY;
        
        if (this.x == that.x) return Double.POSITIVE_INFINITY;

        if (this.y == that.y) return +0.0;

        return (double)(that.y - this.y) / (double)(that.x - this.x);
    }

    public Comparator<Point> slopeOrder() {
        return (Point p, Point q) -> {
            double slopeP = this.slopeTo(p);
            double slopeQ = this.slopeTo(q);

            if (slopeP == slopeQ) return 0;

            return slopeP < slopeQ ? -1 : 1;
        };
    }
}