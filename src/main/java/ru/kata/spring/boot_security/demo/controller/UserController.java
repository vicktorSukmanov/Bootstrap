package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;


import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/user")
    public String getUser(Principal principal, Model model) {
        try {
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("this_user", user);
            model.addAttribute("roles", roles);
            return "user";
        } catch (UsernameNotFoundException e) {
            return "userNotFound";
        }

    }
}