package xiaozhi.controller;

import org.springframework.web.bind.annotation.*;
import xiaozhi.dto.ApiResponse;

@RestController
@RequestMapping("/otaMag")
public class OTAController {

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.ok("manager-api is running");
    }

    @GetMapping("/getDownloadUrl")
    public ApiResponse<String> getDownloadUrl(
            @RequestParam(required = false) String version,
            @RequestParam(required = false) String deviceType) {
        
        // 固定のファームウェアダウンロードURL
        String downloadUrl = "https://example.com/firmware/latest.bin";
        
        return ApiResponse.ok(downloadUrl);
    }

    @PostMapping("/uploadFirmware")
    public ApiResponse<String> uploadFirmware() {
        return ApiResponse.ok("Firmware upload not implemented in simple mode");
    }

    @GetMapping("/version")
    public ApiResponse<String> getVersion() {
        return ApiResponse.ok("1.0.0");
    }
}
