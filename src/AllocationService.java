import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class AllocationService<T extends TeachingDevice> {
    private final IRepository<T> repository;
    private ISelectionStrategy<T> strategy;

    public AllocationService(IRepository<T> repository, ISelectionStrategy<T> strategy) {
        this.repository = Objects.requireNonNull(repository);
        this.strategy = Objects.requireNonNull(strategy);
    }

    public void changeStrategy(ISelectionStrategy<T> newStrategy) {
        strategy = Objects.requireNonNull(newStrategy);
    }

    public T allocate(Predicate<T> additionalCondition) {
        List<T> candidates = repository.findAll(device -> device.isReadyForUse()
                && (additionalCondition == null || additionalCondition.test(device)));
        T selected = strategy.select(candidates);
        selected.borrow();
        return selected;
    }
}
