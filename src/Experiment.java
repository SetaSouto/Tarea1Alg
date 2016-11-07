import java.util.Random;

/**
 * Experiment framework class.
 *
 * System properties 1: RAM:    16 GB
 *                      HD:     SSD, NTFS, 4096 allocation unit size
 *                      OS:     Windows 10, 64 bit
 * System properties 2: RAM:    8 GB
 *                      HD:     HDD, NTFS, 4096 allocation unit size
 *                      OS:     Windows 10, 64 bit
 */
public class Experiment {
    private static int M = 4096;            // set to allocation unit size
    private static int m = 4 * M / 10;
    private static int maxCoord = 500001;   // 500 000 + 1 to compensate non-inclusion of the bound parameter in nextInt
    private static int maxDim = 100;

    public static void main(String[] args) {
        for (int i = 10; i <= 25; i+=5) {
            try {
                experiment(2^i);
            } catch (GeneralException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints several R-Tree metrics to the standard output for different split heuristics and data sets.
     * @param n the number of elements to be contained in each tree.
     * @throws GeneralException
     */
    private static void experiment(int n) throws GeneralException {
        Data[] randomDataset = generateData(n);
        Data[] queries = generateData(n/10);

        // Measure tree creation times
        long startTime = System.currentTimeMillis();
        RTree linearTree = generateTree(randomDataset, new LinearSplit(m, M));
        long stopTime = System.currentTimeMillis();
        System.out.println("Linear split creation time: " + (startTime - stopTime) + " ms.");

        startTime = System.currentTimeMillis();
        RTree greeneTree = generateTree(randomDataset, new GreeneSplit(m, M));
        stopTime = System.currentTimeMillis();
        System.out.println("Greene split creation time: " + (startTime - stopTime) + " ms.");

        // Measure usage percentage
        System.out.println("Linear split usage percentage: " + linearTree.usagePercentage());

        System.out.println("Greene split usage percentage: " + greeneTree.usagePercentage());

        // Measure search performance in time units
        startTime = System.currentTimeMillis();
        for (Data data : queries) {
            linearTree.search(data);
        }
        stopTime = System.currentTimeMillis();
        System.out.println("Linear split queries time: " + (startTime - stopTime) + " ms");

        startTime = System.currentTimeMillis();
        for (Data data : queries) {
            linearTree.search(data);
        }
        stopTime = System.currentTimeMillis();
        System.out.println("Greene split queries time: " + (startTime - stopTime) + " ms");

        // Measure search performance in disc accesses
        int discAccesses = 0;
        for (Data data : queries) {
            discAccesses += linearTree.accessCountSearch(data);
        }
        System.out.println("Linear split number of accesses: " + discAccesses);

        discAccesses = 0;
        for (Data data : queries) {
            discAccesses += linearTree.accessCountSearch(data);
        }
        System.out.println("Greene split number of accesses: " + discAccesses);
    }

    /**
     * Creates a new R-Tree for the specified data and split heuristic.
     * @param data the data to be contained in the tree.
     * @param splitter the split heuristic object.
     * @return the root of the resulting tree.
     * @throws GeneralException raised when a Data with no dimensions is inserted.
     */
    private static RTree generateTree(Data[] data, Splitter splitter) throws GeneralException {
        RTree tree = new RTree(m, M, splitter);
        for (Data d : data) {
            tree.insert(d);
        }
        return tree;
    }

    /**
     * Generates an array of Data with random location and dimensions.
     * @param n the amount of Data to be generated.
     * @return an array containing random Data objects.
     */
    private static Data[] generateData(int n) {
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
