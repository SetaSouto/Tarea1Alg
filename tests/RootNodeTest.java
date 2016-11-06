import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by souto on 06-11-2016.
 */
class RootNodeTest {
    int m, M;
    RootNode root;
    Splitter splitter;

    RootNodeTest() throws GeneralException {
        this.m = 1;
        this.M = 3;
        this.splitter = new LinearSplit(this.m, this.M);
        this.root = new RootNode(this.m, this.M, this.splitter);
    }

    /**
     * Add 6 children (leafs) to the root. It must have 2 children, that have all the leafs.
     */
    @Test
    void addChildrenTest() throws GeneralException {
        // Add one leaf:
        root.addChild(sampleLeaf());
        // It must have 1 child
        assertEquals(1, root.getChildrenSize());

        List<Rectangle> newChildren = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newChildren.add(sampleLeaf());
        }
        // Add 5 leafs
        root.addChildren(newChildren);

        // Overflow, it splits and now it must have two nodes
        assertEquals(2, root.getChildrenSize());
    }

    /**
     * Returns a leaf with only one data (keeps m invariant cause m = 1)
     *
     * @return a leaf
     */
    LeafNode sampleLeaf() throws GeneralException {
        LeafNode leaf = new LeafNode(this.m, this.M, this.splitter);
        leaf.insert(sampleData());
        return leaf;
    }

    /**
     * Returns a random data with corners between 0.0 and 1.0.
     */
    Data sampleData() throws GeneralException {
        return new Data(Math.random(), Math.random(), Math.random(), Math.random());
    }


}