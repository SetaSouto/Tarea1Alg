import java.util.Collections;
import java.util.List;

/**
 * A Greene split determines the two most distant rectangles as in a LinearSplit, sorts rectangles
 * along the axis in which the two are separated and splits original rectangles into the lower and
 * upper halves of the sorted rectangle array.
 *
 * Created by fcocl_000 on 2016-11-05.
 */
public class GreeneSplit extends LinearSplit {
    /**
     * Constructor.
     *
     * @param m indicates minimum size of a node.
     * @param M indicates the maximum size of a node.
     */
    public GreeneSplit(int m, int M) {
        super(m, M);
    }

    @Override
    public Node[] split(List<String> childrenPaths, Node n1, Node n2) throws GeneralException {
        List<Rectangle> children = this.getObjList(childrenPaths);
        if (splitAxisX(children)) {
            Collections.sort(children, (r1, r2) -> (int) (r1.getMBR().getLeft() - r2.getMBR().getLeft()));
        } else {
            Collections.sort(children, (r1, r2) -> (int) (r1.getMBR().getBottom() - r2.getMBR().getBottom()));
        }
        for (Rectangle child : children.subList(0, M / 2)) {
            n1.addChild(child);
        }
        for (Rectangle child : children.subList(M / 2, children.size())) {
            n2.addChild(child);
        }
        return new Node[]{n1, n2};
    }

    /**
     * Determines the axis (x or y) along which the starting rectangles are separated.
     *
     * @param children the rectangles being split.
     * @return true if the separation axis is the x axis.
     */
    private boolean splitAxisX(List<Rectangle> children) {
        Rectangle[] pairX = this.getPairX(children);
        Rectangle[] pairY = this.getPairY(children);
        // (max xL - min xR)/(range X):
        double distXNorm =
                (pairX[0].getMBR().getLeft() - pairX[1].getMBR().getRight()) / (this.getRangeX(children));
        // (max yB - min yT)/(range Y):
        double distYNorm =
                (pairY[0].getMBR().getBottom() - pairY[1].getMBR().getTop()) / (this.getRangeY(children));

        return distXNorm > distYNorm;
    }
}
