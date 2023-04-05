package pa1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

public class eightPuzzle {
    private int[] puzzle;
    private eightPuzzle parent;
    private String move;
    private int pathCost; // # of moves needed to get from initial state to current state

    public eightPuzzle() // constructor for puzzle
    {
        puzzle = new int[9];
        parent = null;
        move = "";
        pathCost = 0;
    }

    public int[] getState() // get method, used for testing
    {
        return puzzle;
    }

    public void setState(String state) {
        int last = 0; // index of last char in state
        for (int i = 0; i < state.length(); i++) {
            if (i != 3 && i != 7) // excludes spaces
            {
                char c = state.charAt(i);
                if (c == 'b') // represents the blank tile as a 0
                {
                    puzzle[last] = 0;
                    last++;
                } else {
                    puzzle[last] = Character.getNumericValue(c);
                    last++;
                }
            }
        }
    }

    public void copyState(eightPuzzle p) // copies state of puzzle p into another puzzle
    {
        puzzle = p.getState().clone();
    }

    public eightPuzzle getParent() {
        return parent;
    }

    public String getMove() {
        return move;
    }

    public int getPathCost() {
        return pathCost;
    }

    public String printHelper() // used as helper method for printState and for testing
    {
        String str = "";
        for (int i = 0; i < puzzle.length; i++) {
            if (i == 3 || i == 6) // adds spaces to print in "b12 345 678" format
            {
                str += " ";
            }
            if (puzzle[i] == 0) {
                str += "b"; // for blank tile
            } else {
                str += puzzle[i];
            }
        }
        return str;
    }

    public void printState() {
        System.out.println(this.printHelper());
    }

    public void swap(int a, int b) // swaps tiles a and b in the puzzle
    // used as helper method for move method
    {
        int copy = puzzle[a];
        puzzle[a] = puzzle[b];
        puzzle[b] = copy;
    }

    public boolean validMove(String direction) // returns if a move is valid but doesn't make the move
    {
        int blank = 0; // index of blank tile
        boolean valid = false;
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                blank = i;
            }
        }
        if (direction == "up" && blank > 2) {
            valid = true;
        } else if (direction == "down" && blank < 6) {
            valid = true;
        } else if (direction == "left" && (blank % 3 != 0)) {
            valid = true;
        } else if (direction == "right" && ((blank + 1) % 3 != 0)) {
            valid = true;
        }
        return valid;
    }

    public void move(String direction) {
        if (!validMove(direction)) {
            return;
        }
        int blank = 0; // index of blank tile
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                blank = i;
            }
        }
        if (direction == "up") {
            swap(blank, blank - 3);
        } else if (direction == "down") {
            swap(blank, blank + 3);
        } else if (direction == "left") {
            swap(blank, blank - 1);
        } else if (direction == "right") {
            swap(blank, blank + 1);
        }
    }

    public double[] randomizeState(int n) // returns random numbers generated so that we can test
    {
        this.setState("b12 345 678"); // sets puzzle to goal state
        Random random = new Random();
        random.setSeed(8); // seed is always the same to ensure that code generates same
        // random numbers across multiple runs
        double[] rands = new double[n]; // array of all random numbers generated
        for (int i = 0; i < n; i++) {
            double d = random.nextDouble();
            if (0 <= d && d < .25) {
                if (!validMove("up")) { // decrement i if the move is invalid, so that n legal moves are made
                    i--;
                } else {
                    move("up");
                    rands[i] = d;
                }
            } else if (.25 <= d && d < .5) {
                if (!validMove("down")) {
                    i--;
                } else {
                    move("down");
                    rands[i] = d;
                }
            } else if (.5 <= d && d < .75) {
                if (!validMove("left")) {
                    i--;
                } else {
                    move("left");
                    rands[i] = d;
                }
            } else {
                if (!validMove("right")) {
                    i--;
                } else {
                    move("right");
                    rands[i] = d;
                }
            }
        }
        return rands;
    }

    public int h1() // calculates heuristic h1 (number of misplaced tiles)
    {
        int h1 = 0;
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] != 0 && puzzle[i] != i) // works b/c the blank tile is represented using 0
            {   //blank "tile" is not counted as a tile
                h1++;
            }
        }
        return h1;
    }

    public int h2() // calculates heuristic h2 (Manhattan distance)
    {
        int diff = 0;
        int h2 = 0;
        for (int i = 0; i < puzzle.length; i++) {
            diff = i - puzzle[i];
            int diffAbs = Math.abs(diff);
            if (puzzle[i] == 0) {
                //do nothing - blank tile doesn't count as a "tile"
            } else if (diffAbs == 8 || (diffAbs == 4 && (i == 6 || i == 2))) {
                h2 += 4;
            } else if (diffAbs == 7 || diffAbs == 5 || (diff == 1 && (i == 3 || i == 6)) || (diff == -1 && (i == 2 || i == 5))) {
                h2 += 3;
            } else if (diffAbs == 6 || diffAbs == 4 || diffAbs == 2) {
                h2 += 2;
            } else if (diffAbs == 3 || diffAbs == 1) {
                h2 += 1;
            }
        }
        return h2;
    }

    public eightPuzzle[] expand() // returns states that can be reached from an initial puzzle
    {
        eightPuzzle[] arr = new eightPuzzle[4];
        if (validMove("up")) {
            eightPuzzle up = new eightPuzzle();
            up.copyState(this);
            up.move("up");
            up.parent = this;
            up.move = "up";
            up.pathCost = up.parent.pathCost + 1;
            arr[0] = up;
        }
        if (validMove("down")) {
            eightPuzzle down = new eightPuzzle();
            down.copyState(this);
            down.move("down");
            down.parent = this;
            down.move = "down";
            down.pathCost = down.parent.pathCost + 1;
            arr[1] = down;
        }
        if (validMove("left")) {
            eightPuzzle left = new eightPuzzle();
            left.copyState(this);
            left.move("left");
            left.parent = this;
            left.move = "left";
            left.pathCost = left.parent.pathCost + 1;
            arr[2] = left;
        }
        if (validMove("right")) {
            eightPuzzle right = new eightPuzzle();
            right.copyState(this);
            right.move("right");
            right.parent = this;
            right.move = "right";
            right.pathCost = right.parent.pathCost + 1;
            arr[3] = right;
        }
        return arr;
    }

    public searchResult solveAStarHelper(String heuristic, int maxNodes) // helper method for solveAStar
    {
        eightPuzzle initial = new eightPuzzle();
        initial.copyState(this);
        PriorityQueue<eightPuzzle> frontier = new PriorityQueue<>(new h1Comp());
        if (heuristic == "h2") {
            frontier = new PriorityQueue<>(new h2Comp());
        }
        frontier.add(initial);
        Hashtable reached = new Hashtable<int[], eightPuzzle>(); // key=state, value=eightPuzzle object
        reached.put(initial.getState(), initial);
        while (!frontier.isEmpty() && reached.size()-1 <= maxNodes) {
            // do -1 for reached.size() because initial state doesn't count as a "generated" node
            eightPuzzle node= frontier.poll();
            if (node.printHelper().equals("b12 345 678")) // tests if solution state is goal state
            {
                return new searchResult(node, reached.size()-1);
            }
            eightPuzzle[] children = node.expand();
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    int[] childState = children[i].getState();
                    int childCost = children[i].getPathCost();
                    eightPuzzle tableLookup = (eightPuzzle) reached.get(childState);
                    if (reached.size() <= maxNodes &&
                            (!reached.containsKey(childState) || childCost < tableLookup.pathCost)) {
                        reached.put(childState, children[i]);
                        frontier.add(children[i]);
                    }
                }
            }
        }
        System.out.println("Reached maximum number of nodes but didn't find solution.");
        return null;
    }

    public int printInfo(String searchMethod, String heuristic, int k, int n)
    // returns number of nodes generated by a search
    {
        searchResult result = new searchResult(new eightPuzzle(), 0);
        if (searchMethod == "A star") {
            result = this.solveAStarHelper(heuristic, n);
        } else if (searchMethod == "beam") {
            result = this.solveBeamHelper(k, n);
        }
        if (result == null) {
            return n; // generated n nodes, limit was exceeded during search
        }
        eightPuzzle solution = result.getSolution();
        String[] moves = new String[solution.pathCost];
        int movesIndex = moves.length - 1;
        eightPuzzle trav = solution;
        while (trav.parent != null) { // adds the moves needed to reach the goal state into an array
            moves[movesIndex] = trav.move;
            trav = trav.parent;
            movesIndex--;
        }
        System.out.println("Moves needed to obtain solution: " + solution.pathCost);
        System.out.print("Solution as sequence of moves: ");
        for (int i = 0; i < moves.length; i++) {
            System.out.print(moves[i] + " ");
        }
        System.out.println();
        return result.getNodesGenerated();
    }

    public int solveAStar(String heuristic, int n) {
        return printInfo("A star", heuristic, 0, n);
    }

    public searchResult solveBeamHelper(int k, int maxNodes) {
        eightPuzzle initial = new eightPuzzle();
        initial.copyState(this);
        PriorityQueue<eightPuzzle> kBest = new PriorityQueue<>(new beamEvaluation());
        kBest.add(initial);
        int nodesReached = 0;
        if(initial.printHelper().equals("b12 345 678"))
        {
            return new searchResult(initial, nodesReached);
        }
        PriorityQueue<eightPuzzle> generated = new PriorityQueue<>(new beamEvaluation());
        while (nodesReached < maxNodes) {
            while (!kBest.isEmpty()) // generates next states for all k states
            {
                eightPuzzle bestState = kBest.poll();
                eightPuzzle[] children = bestState.expand();
                for (int i = 0; i < children.length; i++) {
                    if (children[i] != null) {
                        nodesReached++;
                        if (children[i].printHelper().equals("b12 345 678")) {
                            return new searchResult(children[i], nodesReached);
                        }
                        generated.add(children[i]);
                    }
                }
            }
            kBest.clear();
            int popInd = 0;
            while (popInd < k && popInd < generated.size())
                // adds best k out of the generated next states
            {
                kBest.add(generated.poll());
                popInd++;
            }
            generated.clear();
        }
        System.out.println("Reached maximum number of nodes but didn't find solution.");
        return null;
    }

    public int solveBeam(int k, int n) {
        return printInfo("beam", "", k, n);
    }

    public static int[][] experiment() // method for running set of experiments
    {
        int[][] experiment=new int[15][200];
        /* runs a* with h1, a* with h2, k-beam with 5, k-beam with 10, k-beam with 20, each with
            varying maxNodes limits (100, 1000, and 10000) */
        eightPuzzle test=new eightPuzzle();
        for(int i=0; i<100; i++)
        {
            test.randomizeState(i+1); // does i+1 so we generated 1-100 random moves
            System.out.println("random moves:" +(i+1));
            experiment[0][i]=test.solveAStar("h1",100);
            experiment[1][i]=test.solveAStar("h2",100);
            experiment[2][i]=test.solveBeam(5,100);
            experiment[3][i]=test.solveBeam(10,100);
            experiment[4][i]=test.solveBeam(20,100);
            experiment[5][i]=test.solveAStar("h1",1000);
            experiment[6][i]=test.solveAStar("h2",1000);
            experiment[7][i]=test.solveBeam(5,1000);
            experiment[8][i]=test.solveBeam(10,1000);
            experiment[9][i]=test.solveBeam(20,1000);
            experiment[10][i]=test.solveAStar("h1",10000);
            experiment[11][i]=test.solveAStar("h2",10000);
            experiment[12][i]=test.solveBeam(5,10000);
            experiment[13][i]=test.solveBeam(10,10000);
            experiment[14][i]=test.solveBeam(20,10000);
        }
        return experiment;
    }

    public static void printResults(int[][] array) /* prints double[][] from experiment() and
                                                the fraction solvable for each method */
    {
        int[] numSolvable=new int[15]; // returns # solvable for each search method
        for (int i = 0; i < 15; i++) {
            int count=0; // count solvable for a particular search method
            for (int j = 0; j < 100; j++) {
                System.out.print(array[i][j] + " ");
                if(i<5) // if maxNodes=100
                {
                    if(array[i][j]!=100)
                    {
                        count++;
                    }
                }
                else if(i>4 && i<10) // if maxNodes=1000
                {
                    if(array[i][j]!=1000)
                    {
                        count++;
                    }
                }
                else // if maxNodes=10000
                {
                    if(array[i][j]!=10000)
                    {
                        count++;
                    }
                }
            }
            System.out.println();
            numSolvable[i]=count;
        }
        for(int i=0; i<15; i++)
        {
            System.out.print(numSolvable[i]+" ");
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        File textFile=new File("PA1Demo.txt");
        Scanner s=new Scanner(textFile);
        String command="";
        eightPuzzle test=new eightPuzzle(); // eightPuzzle that we will perform commands on
        while(s.hasNext())
        {
            command=s.next();
            if(command.contains("set"))
            {
                test.setState(s.next()+" "+s.next()+" "+s.next());
                //spaces are needed because white space separates arguments, so we need to add them
                //manually
            }
            else if(command.contains("print"))
            {
                System.out.print("Current state: ");
                test.printState();
            }
            else if(command.equals("moveUp"))
            {
                test.move("up");
                System.out.print("move up: ");
                test.printState();
            }
            else if(command.equals("moveDown"))
            {
                test.move("down");
                System.out.print("move down: ");
                test.printState();
            }
            else if(command.equals("moveLeft"))
            {
                test.move("left");
                System.out.print("move left: ");
                test.printState();
            }
            else if(command.equals("moveRight"))
            {
                test.move("right");
                System.out.print("move right: ");
                test.printState();
            }
            else if(command.contains("randomize"))
            {
                test.randomizeState(Integer.parseInt(s.next()));
                System.out.print("Random state: ");
                test.printState();
            }
            else if(command.contains("AStar"))
            {
                System.out.println("A star:");
                System.out.println(test.solveAStar(s.next(),Integer.parseInt(s.next())));
            }
            else if(command.contains("Beam"))
            {
                System.out.println("Beam:");
                System.out.println(test.solveBeam(Integer.parseInt(s.next()),
                        Integer.parseInt(s.next())));
            }
        }
        //printResults(experiment());
    }
}
