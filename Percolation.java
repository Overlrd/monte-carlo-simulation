import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] grid;
    private final WeightedQuickUnionUF unionFind;
    private final int virtualTopSite;
    private final int virtualBottomSite;
    private final int size;
    private int numOpenedSites;

    public Percolation(int n) {
        grid = new boolean[n][n];
        unionFind = new WeightedQuickUnionUF((n * n) + 2);
        virtualTopSite = 0;
        virtualBottomSite = n * n + 1;
        size = n;
        numOpenedSites = 0;

        // initialize the grid
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
                if (i == 0) {
                    unionFind.union(virtualTopSite, j + 1);
                }
                if (i == n - 1) {
                    unionFind.union(virtualBottomSite, i * n + j);
                }
            }
        }
    }

    public static void main(String[] args) {
    }

    public int numberOfOpenSites() {
        return numOpenedSites;
    }

    private boolean validIndices(int row, int col) {
        return !(row < 1 || row > size || col < 1 || col > size);
    }

    private int shifted1dIdx(int row, int col) {
        return ((row * size) + col) + 1;
    }

    // open site(row, col)
    public void open(int row, int col) {
        if (!validIndices(row, col)) throw new IllegalArgumentException();
        if (!isOpen(row, col)) {
            int shiftedRow = row - 1;
            int shiftedCol = col - 1;

            grid[shiftedRow][shiftedCol] = true;
            numOpenedSites++;


            int siteIdx = shifted1dIdx(shiftedRow, shiftedCol);   // the idx is shifted by 1
            // remember 0 is idx of the virtualTopSite

            // check for opened neighbors to union with
            if (validIndices(row - 1, col) && isOpen(row - 1, col)) {
                int neighborIdx = shifted1dIdx(shiftedRow - 1, shiftedCol);
                unionFind.union(siteIdx, neighborIdx);
            }

            if (validIndices(row, col - 1) && isOpen(row, col - 1)) {
                int neighborIdx = shifted1dIdx(shiftedRow, shiftedCol - 1);
                unionFind.union(siteIdx, neighborIdx);
            }

            if (validIndices(row + 1, col) && isOpen(row + 1, col)) {
                int neighborIdx = shifted1dIdx(shiftedRow + 1, shiftedCol);
                unionFind.union(siteIdx, neighborIdx);
            }

            if (validIndices(row, col + 1) && isOpen(row, col + 1)) {
                int neighborIdx = shifted1dIdx(shiftedRow, shiftedCol + 1);
                unionFind.union(siteIdx, neighborIdx);
            }
        }
    }

    // is the site(row, col) open ?
    public boolean isOpen(int row, int col) {
        if (!validIndices(row, col)) throw new IllegalArgumentException();
        return grid[row - 1][col - 1];
    }

    // is site connected to the virtualTopSite ?
    public boolean isFull(int row, int col) {
        if (!validIndices(row, col)) throw new IllegalArgumentException();
        row -= 1;
        col -= 1;
        int siteIdx = (row * size + col) + 1;
        return unionFind.find(siteIdx) == virtualTopSite;
    }

    // is the system percolating ?
    public boolean percolates() {
        return unionFind.find(virtualBottomSite) == virtualTopSite;
    }
}