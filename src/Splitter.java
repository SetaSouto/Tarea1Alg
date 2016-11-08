import java.util.List;

/**
 * Interface for node splitters.
 * They must implement one of the described split heuristics.
 */
interface Splitter {
    /**
     * Splits the childrenPaths of a node into two different nodes.
     *
     * @param children the childrenPaths of the node to be split.
     * @return an array containing the two resulting nodes.
     */
    Node[] split(List<String> children, Node n1, Node n2) throws GeneralException;
}
