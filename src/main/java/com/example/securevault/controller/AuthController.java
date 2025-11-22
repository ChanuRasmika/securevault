package com.example.securevault.controller;

import com.example.securevault.config.AsgardeoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class AuthController {

    private final AsgardeoProperties asgardeoProperties;

    @Autowired
    public AuthController(AsgardeoProperties asgardeoProperties) {
        this.asgardeoProperties = asgardeoProperties;
    }

    private String getAttribute(Map<String, Object> attributes, String... keys) {
        if (attributes == null) return null;
        for (String key : keys) {
            if (key == null) continue;
            Object value = attributes.get(key);
            if (value != null) return value.toString();
        }
        return null;
    }


    @GetMapping("/")
    public String showLandingPage() {
        return "landing";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User != null ? oauth2User.getAttributes() : null;

        // Retrieve username claims from the configuration
        String username = null;
        if (asgardeoProperties.getUsernameClaims() != null) {
            username = getAttribute(attributes,
                    asgardeoProperties.getUsernameClaims().toArray(new String[0]));
        }

        // Fallback to OAuth2User name if no claims matched
        if (username == null && oauth2User != null) {
            username = oauth2User.getName();
        }

        model.addAttribute("username", username != null ? username : "Unknown User");
        model.addAttribute("appTitle", "SecureVault");

        return "dashboard";
    }

}
