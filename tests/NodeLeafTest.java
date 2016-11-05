import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by souto on 05-11-2016.
 */
class NodeLeafTest {
    private NodeLeaf leaf;
    private int m, M;

    NodeLeafTest() {
        this.m = 1;
        this.M = 3;
        leaf = new NodeLeaf(this.m, this.M, new LinearSplit(this.m, this.M));
    }

    @Test
    void insert() throws GeneralException {
        assertEquals(0, this.leaf.getChildrenSize());
        Data data = new Data(5, 5, 0, 0);
        this.leaf.insert(data);
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
        Data data1 = new Data(1, 1, 0, 0);
        System.out.println(this.leaf.getMBR().getTop());
        assertTrue(data1.equals(this.leaf.getMBR()));
        Data data2 = new Data(2, 2, 0, 0);
        this.leaf.insert(data2);
        assertTrue(data2.equals(this.leaf.getMBR()));
    }

}