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
        this.tree = new RTree(3, 6, new LinearSplit(3, 6));
    }

    /**
     * Add only one Data, it must have size 1.
     */
    @Test
    void insertTest1() {

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