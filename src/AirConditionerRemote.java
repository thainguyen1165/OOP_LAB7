import java.util.Set;
import java.util.Objects;

public class AirConditionerRemote extends TeachingDevice {
    private final Set<String> compatibleRooms;

    public AirConditionerRemote(String id, String name, String location, Set<String> rooms) {
        super(id, name, location);
        Objects.requireNonNull(rooms, "Danh sach phong khong duoc null");
        for (String room : rooms) {
            requireText(room);
        }
        compatibleRooms = Set.copyOf(rooms);
    }

    public Set<String> getCompatibleRooms() { return compatibleRooms; }
    public boolean isCompatibleWith(String roomId) {
        return roomId != null && compatibleRooms.contains(roomId);
    }

    public boolean isReadyForUse() { return status == DeviceStatus.Available; }

    public double getDefaultPriorityScore() { return compatibleRooms.isEmpty() ? 0 : 100; }
}
