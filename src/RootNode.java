import java.util.ArrayList;

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
    public RootNode(int m, int M, Splitter splitter, String path) {
        super(m, M, splitter, path);
    }

    @Override
    protected void addChild(Rectangle newNode) {
        if (!this.childrenPaths.contains(newNode.getPath())) {
            this.childrenPaths.add(newNode.getPath());
        }
        this.updateMBR(newNode);
        if (this.childrenPaths.size() > this.M) {
            // Difference with node: here we don't throw an exception, we reset our childrenPaths
            Rectangle[] newNodes = this.split();
            this.childrenPaths = new ArrayList<>();
            this.childrenPaths.add(newNodes[0].getPath());
            this.childrenPaths.add(newNodes[1].getPath());
            this.refreshPath();
        }
    }

    private void refreshPath() {
        this.path = RTree.getNewPath();
        RTree.save(this);
    }


}
