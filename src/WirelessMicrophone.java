public class WirelessMicrophone extends TeachingDevice {
    private final int batteryLevel;
    private final int signalQuality;

    public WirelessMicrophone(String id, String name, String location, int batteryLevel, int signalQuality) {
        super(id, name, location);
        this.batteryLevel = requirePercentage(batteryLevel);
        this.signalQuality = requirePercentage(signalQuality);
    }

    public int getBatteryLevel() { return batteryLevel; }
    public int getSignalQuality() { return signalQuality; }

    @Override
    public boolean isReadyForUse() {
        return status == DeviceStatus.Available && batteryLevel >= 30 && signalQuality >= 40;
    }

    @Override
    public double getDefaultPriorityScore() { return (batteryLevel + signalQuality) / 2.0; }
}
