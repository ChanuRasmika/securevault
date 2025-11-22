package com.example.securevault.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.asgardeo")
public class AsgardeoProperties {

    private String userinfoUri;
    private String baseUrl;
    private String orgPath;
    private String redirectUri;
    private List<String> usernameClaims;

    public String getUserinfoUri() {
        return userinfoUri;
    }

    public void setUserinfoUri(String userinfoUri) {
        this.userinfoUri = userinfoUri;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public List<String> getUsernameClaims() {
        return usernameClaims;
    }

    public void setUsernameClaims(List<String> usernameClaims) {
        this.usernameClaims = usernameClaims;
    }
}
