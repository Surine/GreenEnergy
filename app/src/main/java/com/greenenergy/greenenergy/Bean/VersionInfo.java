package com.greenenergy.greenenergy.Bean;

/**
 * Created by surine on 2017/8/21.
 */

public class VersionInfo {
    private String version_code;
    private String version_log;
    private String version_url;

    public VersionInfo(String version_code, String version_log, String version_url) {
        this.version_code = version_code;
        this.version_log = version_log;
        this.version_url = version_url;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getVersion_log() {
        return version_log;
    }

    public void setVersion_log(String version_log) {
        this.version_log = version_log;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }
}
