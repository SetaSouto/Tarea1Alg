import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * An R-tree is a tree that handles rectangles, it is like a B-tree. It can insert and search with
 * rectangles. For more information go to: https://en.wikipedia.org/wiki/R-tree
 *
 * Created by souto on 06-11-2016.
 */
public class RTree {
    private int m, M;
    private Splitter splitter;
    private Node root;
    private static int elementCounter;

    /**
     * Constructor of the R-Tree.
     *
     * @param m        minimum size of a Node.
     * @param M        maximum size of a Node.
     * @param splitter contains split's heuristic to control overflow.
     */
    public RTree(int m, int M, Splitter splitter) {
        this.m = m;
        this.M = M;
        this.splitter = splitter;
        // It starts with a lonely leaf:
        this.root = new LeafNode(this.m, this.M, this.splitter, this.getNewPath());
    }

    /**
     * Gives a path that no other element in the Tree has.
     *
     * @return a new path.
     */
    public static String getNewPath() {
        return Integer.toString(++elementCounter) + ".ser";
    }

    /**
     * Insert a new rectangle (Data) in the R-Tree.
     *
     * @param C new Data to be inserted.
     * @return true if C was inserted correctly.
     * @throws GeneralException in case of an uncontrolled overflow.
     */
    public boolean insert(Data C) throws GeneralException {
        try {
            return this.root.insert(C);
        } catch (GeneralException e) {
            // In case of an exception doing insert() change root (LeafNode) to RootNode.
            Rectangle[] newNodes = this.root.split();
            this.root = new RootNode(this.m, this.M, this.splitter);
            this.root.addChild(newNodes[0]);
            this.root.addChild(newNodes[1]);
            // Now RootNode carries about the exceptions.
            return true;
        }
    }

    /**
     * Returns all rectangles that intersect with C.
     *
     * @param C rectangle to be used in the search.
     * @return a list with all the data (rectangles) that intersect with C.
     */
    public List<Data> search(Data C) {
        return this.root.search(C);
    }

    /**
     * Counts the number of nodes visited while performing a Search for the given data.
     *
     * @param C rectangle to be used in the search.
     * @return the number of nodes visited.
     */
    public int accessCountSearch(Data C) {
        return this.root.accessCountSearch(C);
    }

    /**
     * Determines the total number of Rectangle elements in the tree.
     *
     * @return the number of Rectangle elements.
     */
    public int rectangleCount() {
        return this.root.rectangleCount();
    }

    /**
     * Determines the total number of Node elements in the tree.
     *
     * @return the number if Node elements.
     */
    public int nodeCount() {
        return this.root.nodeCount();
    }

    /**
     * Determines the total of data rectangles are in the tree.
     *
     * @return the number of data rectangles in the tree.
     */
    public int dataCount() {
        return this.root.dataCount();
    }

    /**
     * Determines the height of the tree.
     *
     * @return the height of the tree.
     * @throws GeneralException if a node's childrenPaths have different heights.
     */
    public int height() {
        return this.root.height();
    }

    /**
     * Determines the percentage of the total possible element references that are actually used.
     *
     * @return the used reference percentage.
     */
    public double usagePercentage() {
        return 1.0 * (this.rectangleCount() - 1) / (this.M * this.nodeCount());  // -1 in order to ignore de root
    }

    /**
     * Serializes an object.
     *
     * @param c    object to serialize
     * @param path path to the serialized object file.
     */
    public static void save(Rectangle c, String path) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(c);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Deserializes and returns an object.
     *
     * @param path path to the serialized object file.
     * @return the corresponding object.
     */
    public static Object getObj(String path) {
        Object e = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return e;
    }
}
