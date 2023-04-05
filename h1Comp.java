package pa1;

import java.util.Comparator;

public class h1Comp implements Comparator<eightPuzzle>
{   // compares 2 solutions by their cost, measured by # of moves to get there + # of misplaced tiles
    public int compare(eightPuzzle a, eightPuzzle b)
    {
        Integer fa=a.getPathCost()+a.h1(); // f(a) = g(a)+h(a)
        Integer fb=b.getPathCost()+b.h1();
        return fa.compareTo(fb);
    }
}
