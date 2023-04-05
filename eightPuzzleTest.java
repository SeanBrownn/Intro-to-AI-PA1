package pa1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class eightPuzzleTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getState() {
        eightPuzzle test = new eightPuzzle();
        int[] expected = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, test.getState());
    }

    @Test
    void setState() {
        int[] expected = new int[]{0, 1, 2, 6, 8, 7, 3, 5, 4};
        eightPuzzle test = new eightPuzzle();
        test.setState("b12 687 354");
        assertArrayEquals(expected, test.getState());
    }

    @Test
    void printHelper() {
        eightPuzzle test = new eightPuzzle();
        test.setState("34b 578 162");
        String expected = "34b 578 162";
        assertEquals(expected, test.printHelper());
    }

    @Test
    void swap() {
        eightPuzzle test = new eightPuzzle();
        test.setState("b65 183 724");
        test.swap(2, 5); // swaps 5 and 3 in the puzzle above
        int[] expected = new int[]{0, 6, 3, 1, 8, 5, 7, 2, 4};
        assertArrayEquals(expected, test.getState());
    }

    @Test
    void move() {
        eightPuzzle test = new eightPuzzle();
        test.setState("421 5b6 873");
        boolean b = test.validMove("up"); // valid move up
        test.move("up");
        int[] expected = new int[]{4, 0, 1, 5, 2, 6, 8, 7, 3};
        assertArrayEquals(expected, test.getState());
        assertTrue(b);

        b = test.validMove("down"); // valid move down
        expected = new int[]{4, 2, 1, 5, 0, 6, 8, 7, 3};
        test.move("down");
        assertArrayEquals(expected, test.getState());
        assertTrue(b);

        b = test.validMove("left"); // valid move left
        expected = new int[]{4, 2, 1, 0, 5, 6, 8, 7, 3};
        test.move("left");
        assertArrayEquals(expected, test.getState());
        assertTrue(b);

        b = test.validMove("right"); // valid move right
        expected = new int[]{4, 2, 1, 5, 0, 6, 8, 7, 3};
        test.move("right");
        assertArrayEquals(expected, test.getState());
        assertTrue(b);

        test.setState("b84 371 265");
        b = test.validMove("up"); // invalid move up
        expected = new int[]{0, 8, 4, 3, 7, 1, 2, 6, 5};
        assertArrayEquals(expected, test.getState()); //check that no move was made
        assertFalse(b);

        test.setState("284 371 b65");
        b = test.validMove("down"); // invalid move down
        expected = new int[]{2, 8, 4, 3, 7, 1, 0, 6, 5};
        assertArrayEquals(expected, test.getState()); //check that no move was made
        assertFalse(b);

        test.setState("384 b71 265");
        b = test.validMove("left"); // invalid move left
        expected = new int[]{3, 8, 4, 0, 7, 1, 2, 6, 5};
        assertArrayEquals(expected, test.getState()); //check that no move was made
        assertFalse(b);

        test.setState("184 37b 265");
        b = test.validMove("right"); // invalid move right
        expected = new int[]{1, 8, 4, 3, 7, 0, 2, 6, 5};
        assertArrayEquals(expected, test.getState()); //check that no move was made
        assertFalse(b);
    }

    @Test
    void randomizeState() {
        eightPuzzle test = new eightPuzzle();
        double[] d = test.randomizeState(80);
        eightPuzzle expected = new eightPuzzle();
        expected.setState("b12 345 678");
        for (int i = 0; i < d.length; i++) {
            if (0 <= d[i] && d[i] < .25) {
                expected.move("up");
            } else if (.25 <= d[i] && d[i] < .5) {
                expected.move("down");
            } else if (.5 <= d[i] && d[i] < .75) {
                expected.move("left");
            } else {
                expected.move("right");
            }
        }
        assertArrayEquals(expected.getState(), test.getState());
    }

    @Test
    void h1() {
        eightPuzzle test = new eightPuzzle();
        test.setState("724 6b5 138"); // has correct and incorrect tiles
        assertEquals(6, test.h1());
        test.setState("b87 654 321");
        assertEquals(8, test.h1());
        test.setState("b12 345 678");
        assertEquals(0, test.h1());
    }

    @Test
    void h2() {
        eightPuzzle test = new eightPuzzle();
        test.setState("724 5b6 831");
        assertEquals(18,test.h2());
        test.setState("846 2b3 751");
        assertEquals(20, test.h2());
        test.setState("576 1b2 843");
        assertEquals(18, test.h2());
    }

    @Test
    void copyTest() // tests that copies are created correctly, and that modifying the state of
    // one eightPuzzle doesn't change the state of the other
    {
        eightPuzzle test = new eightPuzzle();
        test.setState("675 432 81b");
        eightPuzzle copy = new eightPuzzle();
        copy.copyState(test);
        test.setState("432 675 b18");
        assertNotEquals(test.printHelper(), copy.printHelper());

        test = new eightPuzzle();
        test.setState("b17 623 854");
        copy = new eightPuzzle();
        copy.copyState(test);
        copy.setState("432 675 b18");
        assertNotEquals(test.printHelper(), copy.printHelper());
    }

    @Test
    void expand() {
        eightPuzzle initial = new eightPuzzle();
        initial.setState("412 3b5 678"); // all moves are possible
        eightPuzzle[] children = initial.expand();
        assertEquals(4, children.length);
        int[] expectedUp = new int[]{4, 0, 2, 3, 1, 5, 6, 7, 8};
        assertArrayEquals(expectedUp, children[0].getState());
        assertEquals(initial, children[0].getParent());
        assertEquals("up", children[0].getMove());
        assertEquals(1, children[0].getPathCost());
        int[] expectedDown = new int[]{4, 1, 2, 3, 7, 5, 6, 0, 8};
        assertArrayEquals(expectedDown, children[1].getState());
        assertEquals(initial, children[1].getParent());
        assertEquals("down", children[1].getMove());
        assertEquals(1, children[1].getPathCost());
        int[] expectedLeft = new int[]{4, 1, 2, 0, 3, 5, 6, 7, 8};
        assertArrayEquals(expectedLeft, children[2].getState());
        assertEquals(initial, children[2].getParent());
        assertEquals("left", children[2].getMove());
        assertEquals(1, children[2].getPathCost());
        int[] expectedRight = new int[]{4, 1, 2, 3, 5, 0, 6, 7, 8};
        assertArrayEquals(expectedRight, children[3].getState());
        assertEquals(initial, children[3].getParent());
        assertEquals("right", children[3].getMove());
        assertEquals(1, children[3].getPathCost());

        initial.setState("b42 315 678"); // up and left are not valid moves
        children = initial.expand();
        assertNull(children[0]);
        assertNull(children[2]);

        initial.setState("842 315 67b"); // down and right are not valid moves
        children = initial.expand();
        assertNull(children[1]);
        assertNull(children[3]);
    }

    @Test
    void solveAStar() {
        eightPuzzle test = new eightPuzzle();
        test.randomizeState(20);
        assertEquals(3, test.solveAStar("h1", 3));
        System.out.println(test.solveAStar("h2", Integer.MAX_VALUE));
    }

    @Test
    void solveBeam() {
        eightPuzzle test = new eightPuzzle();
        assertEquals(3, test.solveBeam(1, 3));
        test.randomizeState(13);
    }
}
