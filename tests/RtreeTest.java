import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by souto on 06-11-2016.
 */
class RtreeTest {
    RTree treeLinear;
    RTree treeGreen;

    @BeforeEach
    void setUp() {
        int m = 3, M = 6;
        this.treeLinear = new RTree(m, M, new LinearSplit(m, M));
        //this.treeGreen = new RTree(m, M, new GreeneSplit(m, M));
    }

    /**
     * Add only one Data, there are two rectangles in the treeLinear, the MBR of the root and the
     * data.
     */
    @Test
    void insertLinearTest1() throws GeneralException {
        this.treeLinear.insert(sampleData());
        assertEquals(2, this.treeLinear.rectangleCount());
    }

    /**
     * Insert 5 data in the treeLinear, how M=6 there are 6 rectangles in the treeLinear (5 data and
     * the MBR of the root).
     */
    @Test
    void insertLinearTest5() throws GeneralException {
        insertBash(5, this.treeLinear);
        assertEquals(6, this.treeLinear.rectangleCount());
    }

    /**
     * Insert 10 data in the treeLinear.
     */
    @Test
    void insertLinearTest10() throws GeneralException {
        insertBash(10, this.treeLinear);
        assertEquals(10, this.treeLinear.dataCount());
    }

    @Test
    void height() throws GeneralException {
        // With no data, has no height.
        assertEquals(0, this.treeLinear.height());
        //assertEquals(0, this.treeGreen.height());

        // Insert 5 elements, has height zero, es only one leaf
        insertBash(5, this.treeLinear);
        //insertBash(5, this.treeGreen);
        assertEquals(0, this.treeLinear.height());
        //assertEquals(0, this.treeGreen.height());

        // Insert 2 more elements, force to do split (M=6) and now it must have height 1
        insertBash(2, this.treeLinear);
        //insertBash(2, this.treeGreen);
        //assertEquals(1, this.treeGreen.height());
        assertEquals(1, this.treeLinear.height());

        // We have 7 elements, we force the root to do split by inserting 30 more elements.
        // Total elements in the tree: 37
        insertBash(30, this.treeLinear);
        //insertBash(30, this.treeGreen);
        assertEquals(2, this.treeLinear.height());
        //assertEquals(2, this.treeGreen.height());

        // Now we force to have height 3 by adding 200 data. Total Elements in the tree: 237
        insertBash(200, this.treeLinear);
        //insertBash(200, this.treeGreen);
        assertEquals(3, this.treeLinear.height());
        //assertEquals(3, this.treeGreen.height());
    }

    /**
     * Insert n randomnly data rectangles in the treeLinear.
     *
     * @param n number of rectangles to be inserted in the treeLinear.
     */
    private void insertBash(int n, RTree tree) throws GeneralException {
        for (int i = 0; i < n; i++) {
            tree.insert(sampleData());
        }
    }

    /**
     * Create a random data with corners between 0.0 and 1.0.
     *
     * @return a Data object with random values.
     */
    private Data sampleData() {
        try {
            return new Data(Math.random(), Math.random(), Math.random(), Math.random(), RTree.getNewPath());
        } catch (GeneralException e) {
            e.printStackTrace();
            // Try again, the exceptions occurs because two random() get the same value.
            return sampleData();
        }
    }

}