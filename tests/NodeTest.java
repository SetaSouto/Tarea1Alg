import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by souto on 05-11-2016.
 */
class NodeTest {
    private Node node;
    private int m, M;
    private Data data1 = new Data(10, 10, 0, 0);
    private Data data2 = new Data(5, 5, 0, 0);
    private Data data3 = new Data(0, 0, -10, -10);
    private Data data4 = new Data(0, 0, -5, -5);
    // Only intersects data1 anda data2: (It's IN data1 and data2)
    private Data data5 = new Data(2, 2, 1, 1);
    // First data in the leaf:
    private Data dataLeaf;


    NodeTest() throws GeneralException {
        this.m = 1;
        this.M = 5;
        this.node = new Node(m, M, new LinearSplit(m, M));
        // Has only one child (leaf):
        LeafNode leaf = new LeafNode(this.m, this.M, new LinearSplit(this.m, this.M));
        // Leaf has only one child
        this.dataLeaf = new Data(1, 1, 0, 0);
        leaf.insert(dataLeaf);
        this.node.addChild(leaf);
    }

    /**
     * Insert all the data, the new MBR must be (10,10,-10,-10) because data1 is (10,10,0,0) and
     * data3 is (0,0,-10,-10).
     */
    @Test
    void getMBR() throws GeneralException {
        this.node.insert(data1);
        this.node.insert(data2);
        this.node.insert(data3);
        this.node.insert(data4);
        assertTrue(new Data(10, 10, -10, -10).equals(node.getMBR()));
    }

    @Test
    void search() throws GeneralException {
        List<Data> list = new ArrayList<>();
        /* Nothing added yet, only the data in the child.
         * The data in the leaf intersects with data1, so if we search for data1 we must receive a
         * list with the data in the leaf.
         */
        list.add(dataLeaf);
        assertEquals(list, node.search(data1));

        // Add the data:
        this.node.insert(data1);
        this.node.insert(data2);
        this.node.insert(data3);
        this.node.insert(data4);
        // Now there are 5 Data in the leaf, we don't have overflow.

        // Rectangle that only intersects data1, data2 and the data in the leaf -> data5:
        // The original leaf's data is already in the list.
        list.add(data1);
        list.add(data2);
        assertEquals(list, node.search(data5));
    }

    /**
     * We'll check how much would increase the area if we add a new (11,11,0,0) rectangle. We had a
     * 20x20 MBR, with data6 we would have a 21x21 MBR. Area must increase 41 units. In the second
     * assert we check with a data that is IN the actual MBR, area would increase zero.
     */
    @Test
    void deltaAreaQuery() throws GeneralException {
        // Add data
        this.node.insert(data1);
        this.node.insert(data2);
        this.node.insert(data3);
        this.node.insert(data4);

        // Data that increments the MBR in 41 units: one more unit up, one mor unit right.
        Data data6 = new Data(11, 11, 0, 0);
        assertEquals(41, this.node.deltaAreaQuery(data6));

        // This is contained in the actual MBR, area increments in zero units
        assertEquals(0, this.node.deltaAreaQuery(data5));
    }

    @Test
    void insert() throws GeneralException {
        // Test insert and getChildrenSize():
        this.node.insert(data1);
        assertEquals(1, this.node.getChildrenSize());
        this.node.insert(data2);
        assertEquals(1, this.node.getChildrenSize());
        this.node.insert(data3);
        assertEquals(1, this.node.getChildrenSize());
        this.node.insert(data4);
        assertEquals(1, this.node.getChildrenSize());
    }

    /**
     * Node has only one child, a leaf. Now we added a new child, another leaf. It must have two
     * children now.
     */
    @Test
    void addChild() throws GeneralException {
        LeafNode newChild = new LeafNode(this.m, this.M, new LinearSplit(this.m, this.M));
        // We need to insert any data to have at least one rectangle
        newChild.insert(data5);
        // Add the new child
        this.node.addChild(newChild);
        // Now the node must have 2 children
        assertEquals(2, this.node.getChildrenSize());
    }

    /**
     * Insert 5 data, plus the leaf's data we have 6 data, so with the M=3 que must have 3 nodes.
     */
    @Test
    void nodeCountTest() throws GeneralException {
        // First is the node with one leaf:
        assertEquals(2, this.node.nodeCount());
        // Insert data:
        insertAllData();
        // Overflow. Now we have two leafs:
        assertEquals(3, this.node.nodeCount());
    }

    /**
     * With only one leaf it must have height 1. Wit all the data inserted must still be 1.
     */
    @Test
    void heightTest() throws GeneralException {
        assertEquals(1, this.node.height());
        insertAllData();
        assertEquals(1, this.node.height());
    }

    /**
     * First we only have one data in the leaf, there are 3 rectangles. When we
     * insert all the data (5 more data) we'll have 9 rectangles (1 node, 2 leaf and 6 data).
     */
    @Test
    void rectangleCountTest() throws GeneralException {
        assertEquals(3, this.node.rectangleCount());
        insertAllData();
        assertEquals(9, this.node.rectangleCount());
    }

    /**
     * With the only data in the leaf there is one data rectangle. When we insert all data (5 data
     * rectangles) there must be 6 data rectnagles.
     */
    @Test
    void dataCountTest() throws GeneralException {
        assertEquals(1, this.node.dataCount());
        insertAllData();
        assertEquals(6, this.node.dataCount());
    }

    /**
     * Inserts all the data available in this class.
     */
    private void insertAllData() throws GeneralException {
        this.node.insert(data1);
        this.node.insert(data2);
        this.node.insert(data3);
        this.node.insert(data4);
        this.node.insert(data5);
    }

}