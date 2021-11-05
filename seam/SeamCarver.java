import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    private void updateEnergy(int x, int y) {
        if (x < 0 || x > width() - 1 ||
            y < 0 || y > height() - 1) {
            throw new IllegalArgumentException("Coordinates are outside the picture bounds!");
        }

        if (x == 0 || x == width() - 1 ||
            y == 0 || y == height() - 1) {
            energy[x][y] = 1000.;
        } else {
            Color nextX = picture.get(x + 1, y);
            Color prevX = picture.get(x - 1, y);
            Color nextY = picture.get(x, y + 1);
            Color prevY = picture.get(x, y - 1);
            energy[x][y] = Math.sqrt(Math.pow((double)(nextX.getRed()   - prevX.getRed()),   2.) +
                                     Math.pow((double)(nextX.getGreen() - prevX.getGreen()), 2.) +
                                     Math.pow((double)(nextX.getBlue()  - prevX.getBlue()),  2.) +
                                     Math.pow((double)(nextY.getRed()   - prevY.getRed()),   2.) +
                                     Math.pow((double)(nextY.getGreen() - prevY.getGreen()), 2.) +
                                     Math.pow((double)(nextY.getBlue()  - prevY.getBlue()),  2.));
        }
    }

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Argument can't be null!");
        }

        this.picture = new Picture(picture);

        energy = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                updateEnergy(x, y);
            }
        }
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 ||
            y < 0 || y > height() - 1) {
            throw new IllegalArgumentException("Coordinates are outside the picture bounds!");
        }

        return energy[x][y];
    }

    private void findHorizontalSeam(int x, int y, boolean[][] used, double[][] distTo, int[][] edgeTo) {
        if (used[x][y]) return;
            used[x][y] = true;
            
        double f = Double.MAX_VALUE;
        double s = Double.MAX_VALUE;
        double t = Double.MAX_VALUE;

        if (x + 1 < width()) {
            if (y > 0) {
                findHorizontalSeam(x + 1, y - 1, used, distTo, edgeTo);
                f = distTo[x + 1][y - 1];
            }
            findHorizontalSeam(x + 1, y, used, distTo, edgeTo);
            s = distTo[x + 1][y];
            if (y < height() - 1) {
                findHorizontalSeam(x + 1, y + 1, used, distTo, edgeTo);
                t = distTo[x + 1][y + 1];
            }
        }

        if (f < s) {
            if (f < t) {
                if (f != Double.MAX_VALUE) {
                    distTo[x][y] = f + energy[x][y];
                    edgeTo[x][y] = y - 1;
                } else distTo[x][y] = energy[x][y];
            } else {
                if (t != Double.MAX_VALUE) {
                    distTo[x][y] = t + energy[x][y];
                    edgeTo[x][y] = y + 1;
                } else distTo[x][y] = energy[x][y];
            }
        } else {
            if (s < t) {
                if (s != Double.MAX_VALUE) {
                    distTo[x][y] = s + energy[x][y];
                    edgeTo[x][y] = y;
                } else distTo[x][y] = energy[x][y];
            } else {
                if (t != Double.MAX_VALUE) {
                    distTo[x][y] = t + energy[x][y];
                    edgeTo[x][y] = y + 1;
                } else distTo[x][y] = energy[x][y];
            }
        }
    }

    public int[] findHorizontalSeam() {
        boolean[][] used = new boolean[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        int[] seam = new int[width()];

        double min = Double.MAX_VALUE;
        for (int y = 0; y < height(); y++) {
            findHorizontalSeam(0, y, used, distTo, edgeTo);
            if (distTo[0][y] < min) {
                min = distTo[0][y];
                seam[0] = y;
            }
        }

        for (int x = 1; x < width(); x++) {  
            seam[x] = edgeTo[x - 1][seam[x - 1]];
        }

        return seam;
    }

    private void findVerticalSeam(int x, int y, boolean[][] used, double[][] distTo, int[][] edgeTo) {
        if (used[x][y]) return;
            used[x][y] = true;

        double f = Double.MAX_VALUE;
        double s = Double.MAX_VALUE;
        double t = Double.MAX_VALUE;
        
        if (y + 1 < height()) {
            if (x > 0) {
                findVerticalSeam(x - 1, y + 1, used, distTo, edgeTo);
                f = distTo[x - 1][y + 1];
            }
            findVerticalSeam(x, y + 1, used, distTo, edgeTo);
            s = distTo[x][y + 1];
            if (x < width() - 1) {
                findVerticalSeam(x + 1, y + 1, used, distTo, edgeTo);
                t = distTo[x + 1][y + 1];
            }
        }

        if (f < s) {
            if (f < t) {
                if (f != Double.MAX_VALUE) {
                    distTo[x][y] = f + energy[x][y];
                    edgeTo[x][y] = x - 1;
                } else distTo[x][y] = energy[x][y];
            } else {
                if (t != Double.MAX_VALUE) {
                    distTo[x][y] = t + energy[x][y];
                    edgeTo[x][y] = x + 1;
                } else distTo[x][y] = energy[x][y];
            }
        } else {
            if (s < t) {
                if (s != Double.MAX_VALUE) {
                    distTo[x][y] = s + energy[x][y];
                    edgeTo[x][y] = x;
                } else distTo[x][y] = energy[x][y];
            } else {
                if (t != Double.MAX_VALUE) {
                    distTo[x][y] = t + energy[x][y];
                    edgeTo[x][y] = x + 1;
                } else distTo[x][y] = energy[x][y];
            }
        }
    }

    public int[] findVerticalSeam() {
        boolean[][] used = new boolean[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        int[] seam = new int[height()];

        double min = Double.MAX_VALUE;
        for (int x = 0; x < width(); x++) {
            findVerticalSeam(x, 0, used, distTo, edgeTo);
            if (distTo[x][0] < min) {
                min = distTo[x][0];
                seam[0] = x;
            }
        }

        for (int y = 1; y < height(); y++) {
            seam[y] = edgeTo[seam[y - 1]][y - 1];
        }

        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Argument can't be null!");
        }

        if (height() <= 1) {
            throw new IllegalArgumentException("The picture height is too small!");
        }

        if (seam.length != width() ||
           (seam.length > 0 && (seam[0] < 0 || seam[0] > height() - 1))) {
            throw new IllegalArgumentException("Argument must be a valid seam!");
        }

        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1 ||
                Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Argument must be a valid seam!");
            }
        }

        Picture newPicture = new Picture(width(), height() - 1);

        for (int x = 0; x < seam.length; x++) {
            for (int y = 0; y < seam[x]; y++) {
                newPicture.set(x, y, picture.get(x, y));
            }
            for (int y = seam[x] + 1; y < height(); y++) {
                newPicture.set(x, y - 1, picture.get(x, y));
            }
        }

        picture = newPicture;

        for (int x = 0; x < seam.length; x++) {
            for (int y = Math.max(seam[x] - 1, 0); y < height(); y++) {
                updateEnergy(x, y);
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Argument can't be null!");
        }

        if (width() <= 1) {
            throw new IllegalArgumentException("The picture width is too small!");
        }

        if (seam.length != height() ||
           (seam.length > 0 && (seam[0] < 0 || seam[0] > width() - 1))) {
            throw new IllegalArgumentException("Argument must be a valid seam!");
        }

        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width() - 1 ||
                Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Argument must be a valid seam!");
            }
        }

        Picture newPicture = new Picture(width() - 1, height());

        for (int y = 0; y < seam.length; y++) {
            for (int x = 0; x < seam[y]; x++) {
                newPicture.set(x, y, picture.get(x, y));
            }
            for (int x = seam[y] + 1; x < width(); x++) {
                newPicture.set(x - 1, y, picture.get(x, y));
            }
        }

        picture = newPicture;

        for (int y = 0; y < height(); y++) {
            for (int x = Math.max(seam[y] - 1, 0); x < width(); x++) {
                updateEnergy(x, y);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: <filename> <carves>");
        }
        
        String filename = args[0];
        int carves = Integer.parseInt(args[1]);

        SeamCarver sc = new SeamCarver(new Picture(filename));

        for (int i = 0; i < carves; i++) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
        }

        sc.picture().save("COPY" + filename);
    }
}
