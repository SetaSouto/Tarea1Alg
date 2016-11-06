import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by souto on 05-11-2016.
 */
class LeafNodeTest {
    private LeafNode leaf;
    private int m, M;

    LeafNodeTest() {
        this.m = 1;
        this.M = 3;
        leaf = new LeafNode(this.m, this.M, new LinearSplit(this.m, this.M));
    }

    @Test
    void insert() throws GeneralException {
        // It does not have children:
        assertEquals(0, this.leaf.getChildrenSize());

        Data data = new Data(5, 5, 0, 0);
        this.leaf.insert(data);
        // Now has a child:
        assertEquals(1, this.leaf.getChildrenSize());
    }

    @Test
    void overflow() {
        try {
            this.leaf.insert(new Data(1, 1, 0, 0));
            this.leaf.insert(new Data(2, 2, 0, 0));
            this.leaf.insert(new Data(3, 3, 0, 0));
            this.leaf.insert(new Data(4, 4, 0, 0));
        } catch (GeneralException e) {
            // Raise the exception but keeps having 4 children this node:
            assertEquals(4, this.leaf.getChildrenSize());
        }
    }

    @Test
    void refreshMBR() throws GeneralException {
        // Insert new data (has no data yet)
        Data data1 = new Data(1, 1, 0, 0);
        this.leaf.insert(data1);
        // Now has like MBR the data1 recently added:
        assertTrue(data1.equals(this.leaf.getMBR()));

        // Insert new data
        Data data2 = new Data(2, 2, 0, 0);
        this.leaf.insert(data2);
        // Data2 covers data1, so the new MBR is data2:
        assertTrue(data2.equals(this.leaf.getMBR()));
    }

    /**
     * A leaf is only one node, must be one always.
     */
    @Test
    void nodeCountTest() {
        assertEquals(1, this.leaf.nodeCount());
    }

    /**
     * The height of a leaf must be always zero.
     */
    @Test
    void heightTest() {
        assertEquals(0, this.leaf.height());
    }

}