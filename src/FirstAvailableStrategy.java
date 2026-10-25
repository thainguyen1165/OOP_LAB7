import java.util.List;

public class FirstAvailableStrategy<T> implements ISelectionStrategy<T> {
    @Override
    public T select(List<T> candidates) {
        if (candidates.isEmpty()) {
            throw new NoSuitableItemException();
        }
        return candidates.get(0);
    }
}
