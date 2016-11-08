import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for Data.
 * Created by souto on 05-11-2016.
 */
class DataTest {
    private Data data = new Data(10, 10, 0, 0, RTree.getNewPath());
    // Not intersected
    private Data data1 = new Data(20, 20, 11, 11, RTree.getNewPath());
    // Intersected
    private Data data2 = new Data(5, 5, 2, 2, RTree.getNewPath());

    DataTest() throws GeneralException {
    }

    @Test
    void getLeft() {
        assertEquals(0, data.getLeft());
    }

    @Test
    void getRight() {
        assertEquals(10, data.getRight());
    }

    @Test
    void getTop() {
        assertEquals(10, data.getTop());
    }

    @Test
    void getBottom() {
        assertEquals(0, data.getBottom());
    }

    @Test
    void getArea() {
        assertEquals(100, data.getArea());
    }

    @Test
    void intersect() throws GeneralException {
        assertFalse(data.intersect(data1));
        assertTrue(data.intersect(data2));
    }

    @Test
    void getMBR() {
        assertEquals(data, data.getMBR());
    }

    @Test
    void search() {
        List<Data> list = new ArrayList<>();
        // Not intersected, nothing in the list:
        assertEquals(list, data.search(data1));

        // Intersected, only data in the list.
        list.add(data);
        assertEquals(list, data.search(data2));
    }

    @Test
    void equals() throws GeneralException {
        assertTrue(this.data.equals(new Data(10, 10, 0, 0, RTree.getNewPath())));
    }

}