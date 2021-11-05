import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private ArrayList<Point> points;
    private ArrayList<Point> first;
    private ArrayList<Point> second;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        this.points = new ArrayList<Point>();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < points.length; j++) {
                if (i != j && points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
            this.points.add(points[i]);
        }

        first  = new ArrayList<Point>();
        second = new ArrayList<Point>();
        for (int i = 0; i < points.length; i++) {
            this.points.sort(points[i].slopeOrder());

            int j = 1;
            int p = 0;
            Point min = new Point(32768, 32768);
            Point max = new Point(-1, -1);
            while (j < this.points.size()) {
                if (points[i].slopeTo(this.points.get(j)) ==
                    points[i].slopeTo(this.points.get(j - 1))) {
                    if (min.compareTo(this.points.get(j)) > 0) min = this.points.get(j);
                    if (max.compareTo(this.points.get(j)) < 0) max = this.points.get(j);
                } else {
                    if (j - p > 2) {
                        boolean skip = false;
                        for (int k = 0; k < first.size(); k++) {
                            if ((min == first.get(k) ||
                                 min.slopeTo(first.get(k)) ==
                                 min.slopeTo(second.get(k))) &&
                                (max == second.get(k) ||
                                 max.slopeTo(first.get(k)) ==
                                 max.slopeTo(second.get(k)))) {
                                skip = true;
                            }
                        }
                        
                        if (!skip) {
                            first.add(min);
                            second.add(max);
                        }
                    }
                    
                    if (points[i].compareTo(this.points.get(j)) < 0) {
                        min = points[i];
                        max = this.points.get(j);
                    } else {
                        min = this.points.get(j);
                        max = points[i];
                    }

                    p = j;
                }
                j++;
            }
            if (j - p > 2) {
                boolean skip = false;
                for (int k = 0; k < first.size(); k++) {
                    if ((min == first.get(k) ||
                         min.slopeTo(first.get(k)) ==
                         min.slopeTo(second.get(k))) &&
                        (max == second.get(k) ||
                         max.slopeTo(first.get(k)) ==
                         max.slopeTo(second.get(k)))) {
                        skip = true;
                    }
                }
                
                if (!skip) {
                    first.add(min);
                    second.add(max);
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