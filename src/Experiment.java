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

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        for (int i = 10; i <= 25; i += 1) {
            try {
                experiment((int) Math.pow(2, i));
            } catch (GeneralException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints several R-Tree metrics to the standard output for different split heuristics and data
     * sets.
     *
     * @param n the number of elements to be contained in each tree.
     */
    private static void experiment(int n) throws GeneralException, FileNotFoundException, UnsupportedEncodingException {
        String nameFile = "Stats_n_" + n + "_" + (System.currentTimeMillis() / 100000) + ".txt";
        PrintWriter writer = new PrintWriter(nameFile, "UTF-8");

        System.out.println("------------------");
        System.out.println("Test with n=" + n);
        System.out.println(" ");
        writer.println("------------------");
        writer.println("Test with n=" + n);
        writer.println(" ");

        Data[] randomDataset = generateData(n);
        Data[] queries = generateData(n / 10);

        // Measure tree creation times
        long startTime = System.currentTimeMillis();
        RTree linearTree = generateTree(randomDataset, new LinearSplit(m, M));
        long stopTime = System.currentTimeMillis();
        System.out.println("Linear split creation time: " + format((stopTime - startTime)) + " ms.");
        writer.println("Linear split creation time: " + format(stopTime - startTime) + " ms.");

        startTime = System.currentTimeMillis();
        RTree greeneTree = generateTree(randomDataset, new GreeneSplit(m, M));
        stopTime = System.currentTimeMillis();
        System.out.println("Greene split creation time: " + format(stopTime - startTime) + " ms.");
        writer.println("Greene split creation time: " + format(stopTime - startTime) + " ms.");

        // Measure usage percentage
        System.out.println("Linear split usage percentage: " + linearTree.usagePercentage());
        writer.println("Linear split usage percentage: " + linearTree.usagePercentage());

        System.out.println("Greene split usage percentage: " + greeneTree.usagePercentage());
        writer.println("Greene split usage percentage: " + greeneTree.usagePercentage());

        // Measure search performance in time units
        long batchSize = 50;
        System.out.println("QUERIES: batch size " + batchSize);
        startTime = System.currentTimeMillis();
        long lastTime = startTime;
        for (int i = 0; i < queries.length; i++) {
            if (i % batchSize == 0 && i!=0) {
                StringBuilder toShow = new StringBuilder(" - Queries until now:").append(i).append("\n");
                toShow.append(" - Time elapsed from beginning:").append(format(System.currentTimeMillis() - startTime)).append("\n");
                toShow.append(" - Time last batch:").append(format(System.currentTimeMillis() - lastTime)).append("\n").append("\n");
                lastTime = System.currentTimeMillis();
                System.out.println(toShow.toString());
                writer.println(toShow.toString());
            }
            linearTree.search(queries[i]);
        }
        stopTime = System.currentTimeMillis();
        System.out.println("Linear split queries time: " + format(stopTime - startTime) + " ms");
        writer.println("Linear split queries time: " + format(stopTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        lastTime = startTime;
        for (int i = 0; i < queries.length; i++) {
            if (i % batchSize == 0 && i!=0) {
                StringBuilder toShow = new StringBuilder(" - Queries until now:").append(i).append("\n");
                toShow.append(" - Time elapsed from beginning:").append(format(System.currentTimeMillis() - startTime)).append("\n");
                toShow.append(" - Time last batch:").append(format(System.currentTimeMillis() - lastTime)).append("\n");
                lastTime = System.currentTimeMillis();
                System.out.println(toShow.toString());
                writer.println(toShow.toString());
            }
            greeneTree.search(queries[i]);
        }
        stopTime = System.currentTimeMillis();
        System.out.println("Greene split queries time: " + format(stopTime - startTime) + " ms");
        writer.println("Greene split queries time: " + format(stopTime - startTime) + " ms");

        // Measure search performance in disc accesses
        long discAccesses = 0;
        System.out.println("DISC ACCESS: Batch size " + batchSize);
        lastTime = System.currentTimeMillis();
        startTime = lastTime;
        for (int i = 0; i < queries.length; i++) {
            if (i % batchSize == 0 && i!=0) {
                StringBuilder toShow = new StringBuilder(" - Queries until now:").append(i).append("\n");
                toShow.append(" - Disc access until now:").append(discAccesses).append("\n");
                toShow.append(" - Time from beginning:").append(format(System.currentTimeMillis() - startTime)).append("\n");
                toShow.append(" - Time last batch:").append(format(System.currentTimeMillis() - lastTime)).append("\n");
                lastTime = System.currentTimeMillis();
                System.out.println(toShow.toString());
                writer.println(toShow.toString());
            }
            discAccesses += linearTree.accessCountSearch(queries[i]);
        }
        System.out.println("Linear split number of accesses: " + discAccesses);
        writer.println("Linear split number of accesses: " + discAccesses);

        discAccesses = 0;
        lastTime = System.currentTimeMillis();
        startTime = lastTime;
        for (int i = 0; i < queries.length; i++) {
            if (i % batchSize == 0 && i!=0) {
                StringBuilder toShow = new StringBuilder(" - Queries until now:").append(i).append("\n");
                toShow.append(" - Disc access until now:").append(discAccesses).append("\n");
                toShow.append(" - Time from beginning:").append(format(System.currentTimeMillis() - startTime)).append("\n");
                toShow.append(" - Time last batch:").append(format(System.currentTimeMillis() - lastTime)).append("\n");
                lastTime = System.currentTimeMillis();
                System.out.println(toShow.toString());
                writer.println(toShow.toString());
            }
            discAccesses += greeneTree.accessCountSearch(queries[i]);
        }
        System.out.println("Greene split number of accesses: " + discAccesses);
        writer.println("Greene split number of accesses: " + discAccesses);

        writer.close();
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
     * Formats a given time in milliseconds to HH:MM:SS:MS.
     *
     * @param time in milliseconds.
     * @return time in format HH:MM:SS:MS.
     */
    private static String format(long time) {
        long ms = time % 1000;
        long seg = (time / 1000) % 60;
        long min = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 60;
        return hour + ":" + min + ":" + seg + ":" + ms;
    }
}
