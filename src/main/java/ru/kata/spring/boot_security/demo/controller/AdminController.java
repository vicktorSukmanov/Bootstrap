package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/admin")
    public String getAllUser(Model model, Principal principal) {
        try {
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());
            List<User> users = userService.getListUser();
            List<Role> roles = roleService.findAll();
            model.addAttribute("users", users);
            model.addAttribute("this_user", user);
            model.addAttribute("roles", roles);
            return "admin";
        } catch (UsernameNotFoundException e) {
            return "userNotFound";
        }
    }


    @PostMapping("/admin")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            try {
                List<Role> roles = roleService.findAll();
                User this_user = (User) userDetailsService.loadUserByUsername(principal.getName());
                model.addAttribute("roles", roles);
                model.addAttribute("this_user", this_user);
                return "admin";
            } catch (UsernameNotFoundException e) {
                return "userNotFound";
            }
        }

        if (userService.createUser(user)) {
            return "redirect:/admin";
        } else {
            return "userexist";
        }
    }


    @PostMapping("admin/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            try {
                List<Role> roles = roleService.findAll();
                User this_user = (User) userDetailsService.loadUserByUsername(principal.getName());
                model.addAttribute("roles", roles);
                model.addAttribute("this_user", this_user);
                return "admin";
            } catch (UsernameNotFoundException e) {
                return "userNotFound";
            }
        }
        try {
            if (userService.updateUser(user)) {
                return "redirect:/admin";
            } else {
                return "userexist";
            }
        } catch (EntityNotFoundException e) {
            return "userNotFound";
        }
    }


    @PostMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
