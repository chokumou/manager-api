package xiaozhi.service;

import org.springframework.stereotype.Service;
import xiaozhi.dto.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeviceService {
    
    // 簡易インメモリストレージ（本来はDB）
    private final Map<String, DeviceInfo> devices = new ConcurrentHashMap<>();
    private final Map<String, String> deviceTokens = new ConcurrentHashMap<>();
    
    private static class DeviceInfo {
        String deviceId;
        String macAddress;
        String deviceType;
        String firmwareVersion;
        LocalDateTime lastHeartbeat;
        String status;
        
        DeviceInfo(String deviceId, String macAddress, String deviceType, String firmwareVersion) {
            this.deviceId = deviceId;
            this.macAddress = macAddress;
            this.deviceType = deviceType;
            this.firmwareVersion = firmwareVersion;
            this.lastHeartbeat = LocalDateTime.now();
            this.status = "online";
        }
    }
    
    public DeviceRegisterResponse registerDevice(DeviceRegisterRequest request) {
        // プロビジョニングキーの検証（簡易版）
        if (!"VALID_PROVISION_KEY".equals(request.getProvisionKey())) {
            throw new RuntimeException("Invalid provision key");
        }
        
        // 既存デバイスチェック
        String existingDeviceId = findDeviceByMac(request.getMacAddress());
        if (existingDeviceId != null) {
            // 既存デバイスの場合、トークンを再発行
            String newToken = generateToken();
            deviceTokens.put(existingDeviceId, newToken);
            return new DeviceRegisterResponse(existingDeviceId, newToken, "wss://nekota-server.com/ws");
        }
        
        // 新規デバイス登録
        String deviceId = "dev_" + UUID.randomUUID().toString().substring(0, 8);
        String accessToken = generateToken();
        
        DeviceInfo deviceInfo = new DeviceInfo(deviceId, request.getMacAddress(), 
                                             request.getDeviceType(), request.getFirmwareVersion());
        devices.put(deviceId, deviceInfo);
        deviceTokens.put(deviceId, accessToken);
        
        return new DeviceRegisterResponse(deviceId, accessToken, "wss://nekota-server.com/ws");
    }
    
    public void updateHeartbeat(String deviceId, String token) {
        // トークン検証
        if (!isValidToken(deviceId, token)) {
            throw new RuntimeException("Invalid token");
        }
        
        DeviceInfo device = devices.get(deviceId);
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        
        device.lastHeartbeat = LocalDateTime.now();
        device.status = "online";
    }
    
    public String getDeviceStatus(String deviceId) {
        DeviceInfo device = devices.get(deviceId);
        if (device == null) {
            return "not_found";
        }
        
        // 5分以上ハートビートがない場合はオフライン
        if (device.lastHeartbeat.isBefore(LocalDateTime.now().minusMinutes(5))) {
            device.status = "offline";
        }
        
        return device.status;
    }
    
    public String getCurrentFirmwareVersion(String deviceId) {
        DeviceInfo device = devices.get(deviceId);
        return device != null ? device.firmwareVersion : null;
    }
    
    private String findDeviceByMac(String macAddress) {
        return devices.entrySet().stream()
                .filter(entry -> entry.getValue().macAddress.equals(macAddress))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
    
    private String generateToken() {
        return "tok_" + UUID.randomUUID().toString().replace("-", "");
    }
    
    private boolean isValidToken(String deviceId, String token) {
        String expectedToken = deviceTokens.get(deviceId);
        return expectedToken != null && expectedToken.equals(token.replace("Bearer ", ""));
    }
}
