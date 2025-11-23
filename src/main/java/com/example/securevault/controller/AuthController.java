package com.example.securevault.controller;

import com.example.securevault.config.AsgardeoProperties;
import com.example.securevault.service.UserService;
import com.example.securevault.service.DocumentService;
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
    private final UserService userService;
    private final DocumentService documentService;

    @Autowired
    public AuthController(AsgardeoProperties asgardeoProperties, UserService userService, DocumentService documentService) {
        this.asgardeoProperties = asgardeoProperties;
        this.userService = userService;
        this.documentService = documentService;
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
        userService.findOrCreateUser(oauth2User);
        Map<String, Object> attributes = oauth2User.getAttributes();

        String username = null;
        if (asgardeoProperties.getUsernameClaims() != null) {
            username = getAttribute(attributes,
                    asgardeoProperties.getUsernameClaims().toArray(new String[0]));
        }
        if (username == null) {
            username = oauth2User.getName();
        }

        model.addAttribute("username", username != null ? username : "Unknown User");
        model.addAttribute("appTitle", "SecureVault");

        String userId = oauth2User.getName();
        if (userId != null) {
            model.addAttribute("documents", documentService.listMyDocuments(userId));
        }

        return "dashboard";
    }

}
