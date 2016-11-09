import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Experiment framework class.
 *
 * System properties 1: RAM:    16 GB
 * HD:     SSD, NTFS, 4096 allocation unit size
 * OS:     Windows 10, 64 bit
 * System properties 2: RAM:    8 GB
 * HD:     HDD, NTFS, 4096 allocation unit size
 * OS:     Windows 10, 64 bit
 */
public class Experiment {
    private static int M = 4096;            // set to allocation unit size
        private static int m = 4 * M / 10;
    private static int maxCoord = 500001;   // 500 000 + 1 to compensate non-inclusion of the bound parameter in nextInt
    private static int maxDim = 100;
    private static PrintWriter writerCSV;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        for (int i = 9; i <= 14; i++) {
            writerCSV = new PrintWriter("Csv//GlobalStats_" + i + "_" + (System.currentTimeMillis() / 10000) + ".csv", "UTF-8");
            StringBuilder header = new StringBuilder("Heuristic").append(",").append("N").append(",");
            header.append("CreationTime").append(",").append("UsagePercentage").append(",").append("QueriesTime").append(",").append("DiscAccess");
            writerCSV.println(header.toString());
            try {
                experiment((int) Math.pow(2, i));
            } catch (GeneralException e) {
                e.printStackTrace();
            }
            writerCSV.close();
        }
    }

    /**
     * Prints several R-Tree metrics to the standard output for different split heuristics and data
     * sets.
     *
     * @param n the number of elements to be contained in each tree.
     */
    private static void experiment(int n) throws FileNotFoundException, UnsupportedEncodingException, GeneralException {
        Data[] randomDataset = generateData(n);
        Data[] queries = generateData(n / 10);

        experimentTree(n, new LinearSplit(m, M), "Linear Split", randomDataset, queries);
        experimentTree(n, new GreeneSplit(m, M), "Greene Split", randomDataset, queries);
    }

    /**
     * Creates metrics for a given tree type.
     *
     * @param n             the number of elements to be inserted.
     * @param splitter      the split heuristics the tree will use.
     * @param name          the name of the tree.
     * @param randomDataset the data to be inserted into the tree.
     * @param queries       the data to be searched in the tree.
     * @throws GeneralException             raised when a Data with no dimensions is inserted.
     * @throws FileNotFoundException        thrown by PrintWriter.
     * @throws UnsupportedEncodingException thrown by PrintWriter.
     */
    private static void experimentTree(int n, Splitter splitter, String name, Data[] randomDataset, Data[] queries) throws
            GeneralException, FileNotFoundException, UnsupportedEncodingException {

        String nameFile = "Stats//Stats_n_" + n + (System.currentTimeMillis() / 1000) + ".txt";
        PrintWriter writer = new PrintWriter(nameFile, "UTF-8");

        System.out.println("------------------");
        System.out.println("Test with n=" + n);
        System.out.println(" ");
        writer.println("------------------");
        writer.println("Test with n=" + n);
        writer.println(" ");

        // Measure tree creation times
        long startTime = System.currentTimeMillis();
        RTree tree = generateTree(randomDataset, splitter);
        long stopTime = System.currentTimeMillis();
        long creationTime = (stopTime - startTime);
        System.out.println(name + " creation time: " + format(creationTime));
        writer.println(name + " creation time: " + format(creationTime));

        // Measure usage percentage
        double usagePercentage = tree.usagePercentage();
        System.out.println(name + " usage percentage: " + usagePercentage);
        writer.println(name + " usage percentage: " + usagePercentage);

        // Measure search performance
        int discAccesses = 0;
        startTime = System.currentTimeMillis();
        for (Data data : queries) {
            tree.search(data);
            discAccesses += tree.getAccessCount();
        }
        stopTime = System.currentTimeMillis();
        long queriesTime = (stopTime - startTime);
        System.out.println(name + " queries time: " + format(queriesTime));
        writer.println(name + " queries time: " + format(queriesTime));
        System.out.println(name + " number of accesses: " + discAccesses);
        writer.println(name + " number of accesses: " + discAccesses);

        writer.close();

        toCSV(name, n, creationTime, usagePercentage, queriesTime, discAccesses);
    }

    /**
     * Creates a new R-Tree for the specified data and split heuristic.
     *
     * @param data     the data to be contained in the tree.
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
     *
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
            length = length < 1 ? 1 : length;
            double width = r.nextDouble() * maxDim;
            width = width < 1 ? 1 : width;
            try {
                data[i] = new Data(x, y, x + width, y + length, RTree.getNewPath());
            } catch (GeneralException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * Format time in ms to HH:MM:SS:MS
     *
     * @param time to farmat.
     * @return time in HH:MM:SS:MS
     */
    private static String format(long time) {
        long ms = time % 1000;
        long seg = (time / 1000) % 60;
        long min = (time / (60 * 1000)) % 60;
        long hour = (time / (60 * 60 * 1000)) % 60;
        return (new StringBuilder().append(hour).append(":").append(min).append(":").append(seg).append(":").append(ms)).toString();
    }

    /**
     * Writes stats in a CSV file.
     *
     * @param heuristic       name of the split heuristic.
     * @param n               number of elements of the experiment.
     * @param creationTime    time for creation.
     * @param usagePercentage usage percentage in of the leafs.
     * @param queriesTime     time for doing all the queries.
     * @param discAccess      number of discAccess.
     */
    private static void toCSV(String heuristic, int n, long creationTime, double usagePercentage, long queriesTime, int discAccess) throws FileNotFoundException, UnsupportedEncodingException {
        StringBuilder line = new StringBuilder(heuristic).append(",").append(n).append(",").append(creationTime).append(",");
        line.append(usagePercentage).append(",").append(queriesTime).append(",").append(discAccess);
        writerCSV.println(line.toString());
    }
}
