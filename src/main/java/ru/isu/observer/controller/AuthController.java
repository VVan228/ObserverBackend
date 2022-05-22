package ru.isu.observer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.isu.observer.model.user.AuthRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    @ResponseBody
    @RequestMapping(
            value="/login",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest){
        return null;
    }


    @ResponseBody
    @RequestMapping(
            value="/logout",
            method = RequestMethod.POST
    )
    public void logout(HttpServletRequest request, HttpServletResponse response){
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
    }

}
