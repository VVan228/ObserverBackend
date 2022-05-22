package ru.isu.observer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.isu.observer.model.user.AuthRequest;
import ru.isu.observer.model.user.User;
import ru.isu.observer.security.JwTokenProvider;
import ru.isu.observer.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @ResponseBody
    @RequestMapping(
            value="/auth/login",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody AuthRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            User user = userService.findUserByEmail(authRequest.getEmail());
            String token = jwtTokenProvider.createToken(authRequest.getEmail(), user.getRole().name(), user.getOrganisationId());
            Map<String, String> response = new HashMap<>();
            response.put("email", authRequest.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }


    @ResponseBody
    @RequestMapping(
            value="/auth/logout",
            method = RequestMethod.POST
    )
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.badRequest().body(null);
    }

}
