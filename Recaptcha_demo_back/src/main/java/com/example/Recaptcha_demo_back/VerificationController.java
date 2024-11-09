package com.example.Recaptcha_demo_back;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class VerificationController {
    private static final String RECAPTCHA_SECRET_KEY = "6LeuxVYqAAAAAEyTNV-JBaJ0GtCFZtrwZtz5Y9wR";

    @CrossOrigin(origins = "http://127.0.0.1:5501")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyRecaptcha(@RequestBody Map<String, String> request) {
        String recaptchaToken = request.get("g-recaptcha-response");
        System.out.println("token:" + recaptchaToken);

        // Crear una instancia de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // URL con los parámetros en el formato correcto
        String url = "https://www.google.com/recaptcha/api/siteverify?secret="
                + RECAPTCHA_SECRET_KEY + "&response=" + recaptchaToken;

        System.out.println("hola"+recaptchaToken);

        ResponseEntity<RecaptchaResponse> response;
        try {
            response = restTemplate.postForEntity(url, null, RecaptchaResponse.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"error\": \"Error DE validación  reCAPTCHA.\"}");
        }

        // Comprobar el éxito de la respuesta de Google
        if (response.getBody() != null && response.getBody().isSuccess()) {
            return ResponseEntity.ok("{\"success\": true}");
        } else {
            return ResponseEntity.ok("{\"success\": false}");
        }
    }
}


