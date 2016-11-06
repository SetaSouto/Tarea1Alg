import java.util.List;

/**
 * RootNode is the root of an R-tree. When it has an overflow creates two nodes that contains all of
 * his old children, and its new children are this two new nodes.
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
        this.children.addAll(newNodes);
        if (this.children.size() > this.M) {
            // Difference with node: here we don't throw an exception, we reset our children
            this.children = this.split();
        }
    }


}
