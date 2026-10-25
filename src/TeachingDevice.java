public abstract class TeachingDevice implements IIdentifiable {
    private final String id;
    private final String name;
    private final String storageLocation;
    protected DeviceStatus status = DeviceStatus.Available;

    protected TeachingDevice(String id, String name, String storageLocation) {
        this.id = requireText(id);
        this.name = requireText(name);
        this.storageLocation = requireText(storageLocation);
    }

    protected static String requireText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Thong tin khong duoc rong");
        }
        return value;
    }

    protected static int requirePercentage(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Gia tri phai tu 0 den 100");
        }
        return value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public abstract boolean isReadyForUse();

    public abstract double getDefaultPriorityScore();

    public void borrow() {
        if (!isReadyForUse()) {
            throw new DeviceNotReadyException(id, status);
        }
        status = DeviceStatus.Borrowed;
    }

    public void returnDevice() {
        if (status != DeviceStatus.Borrowed) {
            throw new IllegalStateException("Thiet bi chua duoc muon: " + id);
        }
        status = DeviceStatus.Available;
    }
}
