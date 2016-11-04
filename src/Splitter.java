import java.util.List;

interface Splitter < E > {
  List<E> split(List<Rectangle> children);
}
