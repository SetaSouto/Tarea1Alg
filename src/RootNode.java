import java.util.List;

/**
 * RootNode is the root of an R-tree. When it has an overflow creates two nodes that contains all of
 * his old childrenPaths, and its new childrenPaths are this two new nodes.
 *
 * Created by souto on 06-11-2016.
 */
public class RootNode extends Node {
    /**
     * Constructor. Calls Node's constructor only.
     */
    public RootNode(int m, int M, Splitter splitter) {
        super(m, M, splitter);
    }

    @Override
    void addChildren(List<Rectangle> newNodes) {
        this.childrenPaths.addAll(newNodes);
        if (this.childrenPaths.size() > this.M) {
            // Difference with node: here we don't throw an exception, we reset our childrenPaths
            this.childrenPaths = this.split();
        }
    }


}
