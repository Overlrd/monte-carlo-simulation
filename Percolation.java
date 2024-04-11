import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] grid;
    private final WeightedQuickUnionUF unionFind;
    private final int virtualTopSite;
    private final int virtualBottomSite;
    private final int size;
    private int numOpenedSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        numOpenedSites = 0;
        grid = new boolean[n][n];

        // initialize the grid
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }

        int unionFindListLen = (n * n) + 2;
        unionFind = new WeightedQuickUnionUF(unionFindListLen);
        virtualTopSite = unionFindListLen - 2;
        virtualBottomSite = unionFindListLen - 1;

    }

    // open site(row, col)
    public void open(int row, int col) {
        validateBounds(row, col);
        row--;
        col--;
        if (grid[row][col]) return;

        grid[row][col] = true;
        numOpenedSites++;

        int site = xyTo1DPosition(row, col);
        if (row == 0) {
            unionFind.union(virtualTopSite, site);
        }
        if (row == this.size - 1) {
            unionFind.union(virtualBottomSite, site);
        }

        // check for opened neighbors to union with
        // top
        if (row - 1 >= 0 && grid[row - 1][col]) {
            unionFind.union(site, xyTo1DPosition(row - 1, col));
        }

        // left
        if (col - 1 >= 0 && grid[row][col - 1]) {
            unionFind.union(site, xyTo1DPosition(row, col - 1));
        }

        // bottom
        if (row + 1 < size && grid[row + 1][col]) {
            unionFind.union(site, xyTo1DPosition(row + 1, col));
        }

        // right
        if (col + 1 < size && grid[row][col + 1]) {
            unionFind.union(site, xyTo1DPosition(row, col + 1));
        }
    }

    // is the site(row, col) open ?
    public boolean isOpen(int row, int col) {
        validateBounds(row, col);
        return grid[row - 1][col - 1];
    }


    // is site connected to the virtualTopSite ?
    public boolean isFull(int row, int col) {
        validateBounds(row, col);
        row--;
        col--;
        if (!grid[row][col]) return false;
        int site = xyTo1DPosition(row, col);
        return unionFind.find(site) == unionFind.find(virtualTopSite);
    }


    public int numberOfOpenSites() {
        return numOpenedSites;
    }

    private void validateBounds(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException("Coordinates (" + row + ", " + col + ") is out of bounds for size: " + size);
        }
    }

    private int xyTo1DPosition(int row, int col) {
        return (row * size) + col;
    }

    // does the system percolate ?
    public boolean percolates() {
        return unionFind.find(virtualBottomSite) == unionFind.find(virtualTopSite);
    }
}