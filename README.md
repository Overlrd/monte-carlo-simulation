# monte-carlo-simulation

Write a program to estimate the value of the _percolation threshold_ via Monte Carlo simulation.

**Percolation.** Given a composite systems comprised of randomly distributed insulating and metallic materials: what fraction of the materials need to be metallic so that the composite system is an electrical conductor? Given a porous landscape with water on the surface (or oil below), under what conditions will the water be able to drain through to the bottom (or the oil to gush through to the surface)? Scientists have defined an abstract process known as _percolation_ to model such situations.

**The model.** We model a percolation system using an _N_\-by-_N_ grid of _sites_. Each site is either _open_ or _blocked_. A _full_ site is an open site that can be connected to an open site in the top row via a chain of neighboring (left, right, up, down) open sites. We say the system _percolates_ if there is a full site in the bottom row. In other words, a system percolates if we fill all open sites connected to the top row and that process fills some open site on the bottom row. (For the insulating/metallic materials example, the open sites correspond to metallic materials, so that a system that percolates has a metallic path from top to bottom, with full sites conducting. For the porous substance example, the open sites correspond to empty space through which water might flow, so that a system that percolates lets water fill open sites, flowing from top to bottom.)

![Percolates](https://introcs.cs.princeton.edu/java/assignments/percolates.png)

**The problem.** In a famous scientific problem, researchers are interested in the following question: if sites are independently set to be open with probability _p_ (and therefore blocked with probability 1 − _p_), what is the probability that the system percolates? When _p_ equals 0, the system does not percolate; when _p_ equals 1, the system percolates. The plots below show the site vacancy probability _p_ versus the percolation probability for 20-by-20 random grid (left) and 100-by-100 random grid (right).

![Percolation threshold for 20-by-20 grid](https://introcs.cs.princeton.edu/java/assignments/percolation-threshold20.png)                ![Percolation threshold for 100-by-100 grid](https://introcs.cs.princeton.edu/java/assignments/percolation-threshold100.png)          

When _N_ is sufficiently large, there is a _threshold_ value _p_\* such that when _p_ < _p_\* a random _N_\-by-_N_ grid almost never percolates, and when _p_ > _p_\*, a random _N_\-by-_N_ grid almost always percolates. No mathematical solution for determining the percolation threshold _p_\* has yet been derived. Your task is to write a computer program to estimate _p_\*.

**Percolation data type.** To model a percolation system, create a data type Percolation with the following API:

> ```
> public class Percolation {
>    public Percolation(int N)             // create N-by-N grid, with all sites blocked
>    public void open(int i, int j)        // open site (row i, column j) if it is not open already
>    public boolean isOpen(int i, int j)   // is site (row i, column j) open?
>    public boolean isFull(int i, int j)   // is site (row i, column j) full?
>    public boolean percolates()           // does the system percolate?
> }
> ```

_Corner cases._  By convention, the row and column indices _i_ and _j_ are integers between 0 and _N_ − 1, where (0, 0) is the upper-left site: Throw a java.lang.IndexOutOfBoundsException if any argument to open(), isOpen(), or isFull() is outside its prescribed range. The constructor should throw a java.lang.IllegalArgumentException if _N_ ≤ 0.

_Performance requirements._  The constructor should take time proportional to _N_<sup>2</sup>; all methods should take constant time plus a constant number of calls to the union-find methods union(), find(), connected(), and count().

**Monte Carlo simulation.** To estimate the percolation threshold, consider the following computational experiment:

-   Initialize all sites to be blocked.
-   Repeat the following until the system percolates:
    -   Choose a site (row _i_, column _j_) uniformly at random among all blocked sites.
    -   Open the site (row _i_, column _j_).
-   The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.

For example, if sites are opened in a 20-by-20 grid according to the snapshots below, then our estimate of the percolation threshold is 204/400 = 0.51 because the system percolates when the 204th site is opened.

<table><tbody><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><img src="https://introcs.cs.princeton.edu/java/assignments/percolation-50.png" alt="Percolation 50 sites"><br><center><span size="-1"><em>50 open sites</em></span></center></td><td><img src="https://introcs.cs.princeton.edu/java/assignments/percolation-100.png" alt="Percolation 100 sites"><br><center><span size="-1"><em>100 open sites</em></span></center></td><td><img src="https://introcs.cs.princeton.edu/java/assignments/percolation-150.png" alt="Percolation 150 sites"><br><center><span size="-1"><em>150 open sites</em></span></center></td><td><img src="https://introcs.cs.princeton.edu/java/assignments/percolation-204.png" alt="Percolation 204 sites"><br><center><span size="-1"><em>204 open sites</em></span></center></td></tr></tbody></table>

By repeating this computation experiment _T_ times and averaging the results, we obtain a more accurate estimate of the percolation threshold. Let _x<sub>t</sub>_ be the fraction of open sites in computational experiment _t_. The sample mean μ provides an estimate of the percolation threshold; the sample standard deviation σ measures the sharpness of the threshold.

> ![Estimating the sample mean and variance](https://introcs.cs.princeton.edu/java/assignments/percolation-stats.png)

Assuming _T_ is sufficiently large (say, at least 30), the following provides a 95% confidence interval for the percolation threshold:

> ![95% confidence interval for percolation threshold](https://introcs.cs.princeton.edu/java/assignments/percolation-confidence.png)

To perform a series of computational experiments, create a data type PercolationStats with the following API.

> ```
> public class PercolationStats {
>    public PercolationStats(int N, int T) // perform T independent experiments on an N-by-N grid</span>
>    public double mean()                  // sample mean of percolation threshold</span>
>    public double stddev()                // sample standard deviation of percolation threshold</span>
>    public double confidenceLow()         // low  endpoint of 95% confidence interval</span>
>    public double confidenceHigh()        // high endpoint of 95% confidence interval</span>
> }
> ```

The constructor should throw a java.lang.IllegalArgumentException if either _N_ ≤ 0 or _T_ ≤ 0.

The constructor should take two arguments _N_ and _T_, and perform _T_ independent computational experiments (discussed above) on an _N_\-by-_N_ grid. Using this experimental data, it should calculate the mean, standard deviation, and the _95% confidence interval_ for the percolation threshold. Use _standard random_ from stdlib.jar to generate random numbers; use _standard statistics_ from stdlib.jar to compute the sample mean and standard deviation.


<small>This assignment was developed by Bob Sedgewick and Kevin Wayne.<br>Copyright © 2008.</small>
