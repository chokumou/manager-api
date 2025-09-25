package xiaozhi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceInfo {
    @JsonProperty("uuid")
    private String uuid;
    
    @JsonProperty("device_number")
    private String deviceNumber;

    public DeviceInfo() {}

    public DeviceInfo(String uuid, String deviceNumber) {
        this.uuid = uuid;
        this.deviceNumber = deviceNumber;
    }

    // Getters and setters
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getDeviceNumber() { return deviceNumber; }
    public void setDeviceNumber(String deviceNumber) { this.deviceNumber = deviceNumber; }
}
