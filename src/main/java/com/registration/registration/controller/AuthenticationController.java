package com.registration.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.registration.registration.model.User;
import com.registration.registration.service.AuthenticationResponse;
import com.registration.registration.service.AuthenticationService;
import com.registration.registration.service.ErrorResponse;


@RestController
public class AuthenticationController {

    /*
    * @RestController: Cette annotation indique que cette classe est un contrôleur
    *  où chaque méthode retourne une réponse JSON (ou similaire) directement dans
    * le corps de la réponse HTTP. C'est une combinaison de @Controller et @ResponseBody.
    *
    * ResponseEntity est utilisé pour contrôler le statut de la réponse HTTP ainsi
    * que le corps de la réponse.
    *
    * @RequestBody User userRegister : Le corps de la requête (qui contient probablement des données JSON)
    * est converti en un objet User par Spring.
     */

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok(authenticationService.register(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("duplicate"));
        }
        
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.login(user));
    }


    @GetMapping("/")
    public String test(@RequestBody User user) {
        return "cc le server fonctionne";
    }
}
