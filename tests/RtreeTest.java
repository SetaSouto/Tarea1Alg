import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by souto on 06-11-2016.
 */
class RtreeTest {
    RTree tree;

    @BeforeEach
    void setUp() {
        int m = 3, M = 6;
        this.tree = new RTree(m, M, new LinearSplit(m, M));
    }

    /**
     * Add only one Data, there are two rectangles in the tree, the MBR of the root and the data.
     */
    @Test
    void insertTest1() throws GeneralException {
        this.tree.insert(sampleData());
        assertEquals(2, this.tree.rectangleCount());
    }

    /**
     * Insert 5 data in the tree, how M=6 there are 6 rectangles in the tree (5 data and the MBR of
     * the root).
     */
    @Test
    void insertTest5() throws GeneralException {
        insertBash(5);
        assertEquals(6, this.tree.rectangleCount());
    }

    /**
     * Insert 10 data in the tree.
     */
    @Test
    void insert10() throws GeneralException {
        insertBash(10);
        assertEquals(10, this.tree.dataCount());
    }

    /**
     * Insert 1M data rectangles. There must be 1M data in the tree.
     */
    @Test
    void insert1000000() throws GeneralException {
        insertBash(1000000);
        assertEquals(1000000, this.tree.dataCount());
    }

    /**
     * Insert n randomnly data rectangles in the tree.
     *
     * @param n number of rectangles to be inserted in the tree.
     */
    private void insertBash(int n) throws GeneralException {
        for (int i = 0; i < n; i++) {
            this.tree.insert(sampleData());
        }
    }

    /**
     * Create a random data with corners between 0.0 and 1.0.
     *
     * @return a Data object with random values.
     */
    private Data sampleData() {
        try {
            return new Data(Math.random(), Math.random(), Math.random(), Math.random());
        } catch (GeneralException e) {
            e.printStackTrace();
            // Try again, the exceptions occurs because two random() get the same value.
            return sampleData();
        }
    }

}