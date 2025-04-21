package ru.kata.spring.boot_security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/admin-panel";
    }


    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/admin-panel";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("selectedRoles") List<String> selectedRoles) {
        Set<Role> roles = selectedRoles.stream()
                .map(roleService::findByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam int age,
                             @RequestParam String email,
                             @RequestParam(required = false) String password,
                             @RequestParam(value = "selectedRoles", required = false) List<String> roleNames,
                             RedirectAttributes redirectAttributes) {

        User user = userService.findById(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        Set<Role> roles = (roleNames != null) ?
                roleNames.stream()
                        .map(roleService::findByName)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        user.setRoles(roles);
        userService.update(user);

        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        return "redirect:/admin";
    }

}