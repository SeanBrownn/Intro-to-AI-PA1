package pa1;

import java.util.Comparator;

public class h2Comp implements Comparator<eightPuzzle>
{   /* compares 2 solutions by their cost, measured by # of moves to get there + (for each tile)
    # of minimum moves needed to take tile to its right place */
    public int compare(eightPuzzle a, eightPuzzle b)
    {
        Integer fa=a.getPathCost()+a.h2(); // f(a) = g(a)+h(a)
        Integer fb=b.getPathCost()+b.h2();
        return fa.compareTo(fb);
    }
}