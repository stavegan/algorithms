import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private ArrayList<Point> first;
    private ArrayList<Point> second;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < points.length; j++) {
                if (i != j && points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Arrays.sort(points);

        first  = new ArrayList<Point>();
        second = new ArrayList<Point>();
        for (int i1 = 0; i1 < points.length; i1++) {
            for (int i2 = i1 + 1; i2 < points.length; i2++) {
                boolean skip = false;
                for (int j = 0; j < first.size(); j++) {
                    if ((points[i1] == first.get(j) ||
                         points[i1].slopeTo(first.get(j)) ==
                         points[i1].slopeTo(second.get(j))) &&
                        (points[i2] == second.get(j) ||
                         points[i2].slopeTo(first.get(j)) ==
                         points[i2].slopeTo(second.get(j)))) {
                        skip = true;
                    }
                }
                double slope = points[i1].slopeTo(points[i2]);
                for (int i3 = i2 + 1; i3 < points.length && !skip; i3++) {    
                    if (slope == points[i2].slopeTo(points[i3])) {
                        for (int i4 = points.length - 1; i4 > i3 && !skip; i4--) {
                            if (slope == points[i3].slopeTo(points[i4])) {
                                first.add(points[i1]);
                                second.add(points[i4]);
                                skip = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return first.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[first.size()];
        for (int i = 0; i < first.size(); i++) {
            segments[i] = new LineSegment(first.get(i), second.get(i));
        }

        return segments;
    }
}