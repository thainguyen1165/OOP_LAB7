public class ProjectorRemote extends TeachingDevice {
    private final int batteryLevel;

    public ProjectorRemote(String id, String name, String location, int batteryLevel) {
        super(id, name, location);
        this.batteryLevel = requirePercentage(batteryLevel);
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public boolean isReadyForUse() {
        return status == DeviceStatus.Available && batteryLevel >= 20;
    }

    @Override
    public double getDefaultPriorityScore() {
        return batteryLevel;
    }
}
