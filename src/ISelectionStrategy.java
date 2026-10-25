import java.util.List;

public interface ISelectionStrategy<T> {
    T select(List<T> candidates);
}
