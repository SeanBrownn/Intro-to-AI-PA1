package pa1;

import java.util.Comparator;

public class beamEvaluation implements Comparator<eightPuzzle>
{   // evaluation function used for beam search
    public int compare(eightPuzzle a, eightPuzzle b)
    {
        Integer fa=a.h2();
        Integer fb=b.h2();
        return fa.compareTo(fb);
    }
}
