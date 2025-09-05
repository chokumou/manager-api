-- Supabase用テーブル作成SQL
-- Manager API用のテーブル定義

-- デバイス管理テーブル
CREATE TABLE IF NOT EXISTS devices (
    device_id VARCHAR(255) PRIMARY KEY,
    mac_address VARCHAR(17) UNIQUE NOT NULL,
    device_type VARCHAR(50),
    firmware_version VARCHAR(20),
    access_token VARCHAR(255),
    last_heartbeat TIMESTAMP,
    status VARCHAR(20) DEFAULT 'offline',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ファームウェア管理テーブル
CREATE TABLE IF NOT EXISTS firmware (
    id SERIAL PRIMARY KEY,
    version VARCHAR(20) NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    download_url TEXT NOT NULL,
    file_size BIGINT,
    checksum VARCHAR(64),
    is_latest BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(device_type, version)
);

-- インデックス作成
CREATE INDEX IF NOT EXISTS idx_devices_mac_address ON devices(mac_address);
CREATE INDEX IF NOT EXISTS idx_devices_access_token ON devices(access_token);
CREATE INDEX IF NOT EXISTS idx_devices_last_heartbeat ON devices(last_heartbeat);
CREATE INDEX IF NOT EXISTS idx_firmware_device_type_latest ON firmware(device_type, is_latest);

-- 初期ファームウェアデータ
INSERT INTO firmware (version, device_type, download_url, file_size, checksum, is_latest) 
VALUES 
    ('1.0.0', 'ESP32', 'https://example.com/firmware/esp32_v1.0.0.bin', 1024000, 'abc123', false),
    ('1.1.0', 'ESP32', 'https://example.com/firmware/esp32_v1.1.0.bin', 1048576, 'def456', true),
    ('1.0.0', 'ESP32-S3', 'https://example.com/firmware/esp32s3_v1.0.0.bin', 2097152, 'ghi789', true)
ON CONFLICT (device_type, version) DO NOTHING;
