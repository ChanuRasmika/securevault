package com.example.securevault.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Component
@Getter
@ConfigurationProperties(prefix = "app.asgardeo")
public class AsgardeoProperties {

    private String userinfoUri;
    private String baseUrl;
    private String orgPath;
    private String redirectUri;
    private List<String> usernameClaims = new ArrayList<>();

    public List<String> getUsernameClaims() {
        return usernameClaims != null ? Collections.unmodifiableList(usernameClaims) : Collections.emptyList();
    }

}
