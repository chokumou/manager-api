package xiaozhi.service;

import org.springframework.stereotype.Service;
import xiaozhi.dto.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OTAService {
    
    private final DeviceService deviceService;
    
    // 簡易ファームウェア情報ストレージ
    private final Map<String, FirmwareInfo> firmwareDatabase = new ConcurrentHashMap<>();
    
    private static class FirmwareInfo {
        String version;
        String deviceType;
        String downloadUrl;
        Long fileSize;
        String checksum;
        
        FirmwareInfo(String version, String deviceType, String downloadUrl, Long fileSize, String checksum) {
            this.version = version;
            this.deviceType = deviceType;
            this.downloadUrl = downloadUrl;
            this.fileSize = fileSize;
            this.checksum = checksum;
        }
    }
    
    public OTAService(DeviceService deviceService) {
        this.deviceService = deviceService;
        
        // 初期ファームウェア情報を設定
        initializeFirmwareDatabase();
    }
    
    public OTACheckResponse checkUpdate(OTACheckRequest request) {
        String deviceType = request.getDeviceType() != null ? request.getDeviceType() : "ESP32";
        String currentVersion = request.getCurrentVersion();
        
        // デバイスの現在のバージョンを取得
        if (currentVersion == null && request.getDeviceId() != null) {
            currentVersion = deviceService.getCurrentFirmwareVersion(request.getDeviceId());
        }
        
        FirmwareInfo latestFirmware = getLatestFirmware(deviceType);
        
        if (latestFirmware == null) {
            return new OTACheckResponse(false);
        }
        
        // バージョン比較（簡易版）
        if (currentVersion == null || !currentVersion.equals(latestFirmware.version)) {
            return new OTACheckResponse(true, latestFirmware.version, latestFirmware.downloadUrl, 
                                      latestFirmware.fileSize, latestFirmware.checksum);
        }
        
        return new OTACheckResponse(false);
    }
    
    public String getDownloadUrl(String version, String deviceType) {
        if (deviceType == null) {
            deviceType = "ESP32";
        }
        
        if (version == null) {
            // 最新バージョンのURLを返す
            FirmwareInfo latest = getLatestFirmware(deviceType);
            return latest != null ? latest.downloadUrl : "https://example.com/firmware/latest.bin";
        }
        
        // 指定バージョンのURLを返す
        String key = deviceType + "_" + version;
        FirmwareInfo firmware = firmwareDatabase.get(key);
        return firmware != null ? firmware.downloadUrl : "https://example.com/firmware/" + version + ".bin";
    }
    
    private FirmwareInfo getLatestFirmware(String deviceType) {
        // 最新ファームウェアを取得（簡易版 - 実際にはバージョンソートが必要）
        return firmwareDatabase.get(deviceType + "_latest");
    }
    
    private void initializeFirmwareDatabase() {
        // ESP32用の初期ファームウェア情報
        firmwareDatabase.put("ESP32_1.0.0", new FirmwareInfo("1.0.0", "ESP32", 
                "https://example.com/firmware/esp32_v1.0.0.bin", 1024000L, "abc123"));
        firmwareDatabase.put("ESP32_1.1.0", new FirmwareInfo("1.1.0", "ESP32", 
                "https://example.com/firmware/esp32_v1.1.0.bin", 1048576L, "def456"));
        firmwareDatabase.put("ESP32_latest", new FirmwareInfo("1.1.0", "ESP32", 
                "https://example.com/firmware/esp32_v1.1.0.bin", 1048576L, "def456"));
                
        // 他のデバイスタイプも追加可能
        firmwareDatabase.put("ESP32-S3_latest", new FirmwareInfo("1.0.0", "ESP32-S3", 
                "https://example.com/firmware/esp32s3_v1.0.0.bin", 2097152L, "ghi789"));
    }
}
