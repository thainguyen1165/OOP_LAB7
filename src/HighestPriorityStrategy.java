import java.util.List;
import java.util.Objects;
import java.util.function.ToDoubleFunction;

public class HighestPriorityStrategy<T> implements ISelectionStrategy<T> {
    private final ToDoubleFunction<T> scoreSelector;

    public HighestPriorityStrategy(ToDoubleFunction<T> scoreSelector) {
        this.scoreSelector = Objects.requireNonNull(scoreSelector);
    }

    @Override
    public T select(List<T> candidates) {
        if (candidates.isEmpty()) {
            throw new NoSuitableItemException();
        }
        T best = candidates.get(0);
        double bestScore = scoreSelector.applyAsDouble(best);
        for (int i = 1; i < candidates.size(); i++) {
            T current = candidates.get(i);
            double score = scoreSelector.applyAsDouble(current);
            if (score > bestScore) {
                best = current;
                bestScore = score;
            }
        }
        return best;
    }
}
