package xiaozhi.modules.device.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import xiaozhi.common.utils.Result;

/**
 * DB接続なしの簡易OTAコントローラー
 */
@Tag(name = "簡易OTA管理")
@RestController
@RequestMapping("/otaMag")
public class SimpleOTAController {

    @GetMapping("/getDownloadUrl")
    @Operation(summary = "OTAダウンロードURL取得")
    public Result<String> getDownloadUrl(@RequestParam(required = false) String version) {
        // 固定のテストファームウェアURL
        String downloadUrl = "https://example.com/firmware/latest.bin";
        
        return new Result<String>().ok(downloadUrl);
    }

    @GetMapping("/health")
    @Operation(summary = "ヘルスチェック")
    public Result<String> health() {
        return new Result<String>().ok("manager-api is running");
    }
}
