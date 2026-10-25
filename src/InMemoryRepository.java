import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class InMemoryRepository<T extends IIdentifiable> implements IRepository<T> {
    private final Map<String, T> items = new LinkedHashMap<>();

    @Override
    public void add(T item) {
        Objects.requireNonNull(item, "Phan tu khong duoc null");
        if (items.containsKey(item.getId())) {
            throw new DuplicateIdException(item.getId());
        }
        items.put(item.getId(), item);
    }

    @Override
    public boolean remove(String id) {
        return items.remove(id) != null;
    }

    @Override
    public T findById(String id) {
        return items.get(id);
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(items.values());
    }

    @Override
    public List<T> findAll(Predicate<T> condition) {
        Objects.requireNonNull(condition, "Dieu kien khong duoc null");
        return items.values().stream().filter(condition).toList();
    }
}
