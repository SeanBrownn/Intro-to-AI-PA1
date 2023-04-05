package pa1;

public class searchResult {
    private eightPuzzle solution;
    private int nodesGenerated;

    public searchResult(eightPuzzle e, int n)
    {
        solution=e;
        nodesGenerated=n;
    }
    public int getNodesGenerated() {
        return nodesGenerated;
    }
    public eightPuzzle getSolution() {
        return solution;
    }
}
