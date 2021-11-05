import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int openedSites;
    private boolean percolates;
    private boolean[][] opened;

    private WeightedQuickUnionUF top;
    private WeightedQuickUnionUF bottom;

    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }

        this.size = n;
        this.openedSites = 0;
        this.percolates = false;
        this.opened = new boolean[n][n];

        top = new WeightedQuickUnionUF(n * n + 1);
        bottom = new WeightedQuickUnionUF(n * n + 1);

        for (int i = 0; i < n; i++) {
            top.union(n * n, i);
            bottom.union(n * n, i);
        }
    }

    public void open(int r, int c) {
        if (r < 1 || r-- > this.size ||
            c < 1 || c-- > this.size) {
            throw new IllegalArgumentException();
        }

        if (!this.opened[r][c]) {
            this.opened[r][c] = true;

            int i = r * this.size + c;
            int j = this.size * this.size - i - 1;

            if (r > 0 && this.opened[r - 1][c]) {
                this.top.union(i, i - this.size);
                this.bottom.union(j, j + this.size);
            }

            if (c < this.size - 1 && this.opened[r][c + 1]) {
                this.top.union(i, i + 1);
                this.bottom.union(j, j - 1);
            }

            if (r < this.size - 1 && this.opened[r + 1][c]) {
                this.top.union(i, i + this.size);
                this.bottom.union(j, j - this.size);
            }

            if (c > 0 && this.opened[r][c - 1]) {
                this.top.union(i, i - 1);
                this.bottom.union(j, j + 1);
            }

            if (this.top.find(i) == this.top.find(this.size * this.size) &&
                this.bottom.find(j) == this.bottom.find(this.size * this.size)) {
                this.percolates = true;
            }

            this.openedSites++;
        }
    }

    public boolean isOpen(int r, int c) {
        if (r < 1 || r-- > this.size ||
            c < 1 || c-- > this.size) {
            throw new IllegalArgumentException();
        }

        return this.opened[r][c];
    }

    public boolean isFull(int r, int c) {
        if (r < 1 || r-- > this.size ||
            c < 1 || c-- > this.size) {
            throw new IllegalArgumentException();
        }

        return this.opened[r][c] && this.top.find(this.size * this.size) == this.top.find(r * this.size + c);
    }

    public int numberOfOpenSites() {
        return this.openedSites;
    }

    public boolean percolates() {
        return this.percolates;
    }
}

