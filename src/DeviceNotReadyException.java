public class DeviceNotReadyException extends RuntimeException {
    private final String deviceId;
    private final DeviceStatus currentStatus;

    public DeviceNotReadyException(String deviceId, DeviceStatus currentStatus) {
        super("Thiet bi khong san sang: " + deviceId + " (" + currentStatus + ")");
        this.deviceId = deviceId;
        this.currentStatus = currentStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceStatus getCurrentStatus() {
        return currentStatus;
    }
}
