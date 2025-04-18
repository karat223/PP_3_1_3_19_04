package ru.kata.spring.boot_security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.model.Role;
import ru.kata.spring.boot_security.model.User;
import ru.kata.spring.boot_security.service.RoleService;
import ru.kata.spring.boot_security.service.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPanel(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUser", userService.findByEmail(principal.getName()));
        return "admin/admin-panel";
    }




    @GetMapping("/new")
    public String showNewUserForm(Model model, Principal principal) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());

        // Добавляем currentUser в модель
        if (principal != null) {
            User currentUser = userService.findByEmail(principal.getName());
            model.addAttribute("currentUser", currentUser);
        }

        return "admin/new-user";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("selectedRoles") List<String> roleNames) {
        Set<Role> roles = roleNames.stream()
                .map(roleService::findByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit-user";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "selectedRoles", required = false) List<String> roleNames) {
        // Обработка ролей
        Set<Role> roles = (roleNames != null) ?
                roleNames.stream()
                        .map(roleService::findByName)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        user.setRoles(roles);
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String showDeleteForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/delete-user";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/userAdmin-page")
    public String userPageAdmin(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "admin/userAdmin-page";
    }

}