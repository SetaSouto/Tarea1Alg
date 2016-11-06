import java.util.Random;
import java.util.stream.IntStream;

/**
 * Experiment framework class.
 *
 * System properties 1: RAM:    16 GB
 *                      HD:     SSD, NTFS, 4096 allocation unit size
 *                      OS:     Windows 10, 64 bit
 * System properties 2: RAM:
 *                      HD:
 *                      OS:
 *
 * Created by fcocl_000 on 2016-11-06.
 */
public class Experiment {
    private static int M = 4096;            // set to allocation unit size
    private static int m = 4 * M / 10;
    private static int maxCoord = 500001;   // 500 000 + 1 to compensate non-inclusion of the bound parameter in nextInt
    private static int maxDim = 100;

    public static void main(String[] args) {

    }

    /**
     * Prints several R-Tree metrics to the standard output for different split heuristics and data sets.
     * @param n the number of elements to be contained in each tree.
     * @throws GeneralException
     */
    private void experiment(int n) throws GeneralException {
        Data[] randomDataset = generateData(n);
        Data[] queries = generateData(n/10);

        // Measure tree creation times
        long startTime = System.currentTimeMillis();
        Node linearTree = generateTree(randomDataset, new LinearSplit(m, M));
        long stopTime = System.currentTimeMillis();
        System.out.println("Linear split creation time: " + (startTime - stopTime) + " ms.");

        startTime = System.currentTimeMillis();
        Node greeneTree = generateTree(randomDataset, new GreeneSplit(m, M));
        stopTime = System.currentTimeMillis();
        System.out.println("Greene split creation time: " + (startTime - stopTime) + " ms.");

        // Measure search performance

    }

    /**
     * Creates a new R-Tree for the specified data and split heuristic.
     * @param data the data to be contained in the tree.
     * @param splitter the split heuristic object.
     * @return the root of the resulting tree.
     * @throws GeneralException raised when a Data with no dimensions is inserted.
     */
    private Node generateTree(Data[] data, Splitter splitter) throws GeneralException {
        Node root = new Node(m, M, splitter);
        for (Data d : data) {
            root.insert(d);
        }
        return root;
    }

    /**
     * Generates an array of Data with random location and dimensions.
     * @param n the amount of Data to be generated.
     * @return an array containing random Data objects.
     */
    private Data[] generateData(int n) {
        Random r = new Random();
        Data[] data = new Data[n];
        for (int i = 0; i < n; i++) {
            double x = r.nextDouble() * maxCoord;
            double y = r.nextDouble() * maxCoord;
            double length = r.nextDouble() * maxDim;
            double width = r.nextDouble() * maxDim;
            try {
                data[i] = new Data(x, y, x + width, y + length);
            } catch (GeneralException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
