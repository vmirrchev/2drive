package softuni.exam.drive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.model.dto.RoleBindingModel;
import softuni.exam.drive.model.dto.UserBindingModel;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.service.UserService;

import java.text.MessageFormat;

/**
 * Controller responsible for handling user interactions
 * @author Vasil Mirchev
 */
@Controller
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping()
    public String createUser(
            @Valid @ModelAttribute("registerBindingModel") final RegisterBindingModel registerBindingModel,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes)
    {
        final String redirect = "redirect:/register";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("registerBindingModel", registerBindingModel);
            return redirect;
        }

        try {
            userService.createUser(registerBindingModel);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("User creation operation failed. {0}", ex.getMessage()));
            redirectAttributes.addFlashAttribute("registerBindingModel", registerBindingModel);
            redirectAttributes.addFlashAttribute("addSuccess", false);
            return redirect;
        }

        redirectAttributes.addFlashAttribute("addSuccess", true);
        return "redirect:/login";
    }

    @PatchMapping("/{id}")
    public String updateUser(
            @PathVariable("id") final Long userId,
            @Valid @ModelAttribute("userBindingModel") final UserBindingModel userBindingModel,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes)
    {
        final String redirect = "redirect:/profile";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("userBindingModel", userBindingModel);
            return "redirect:/edit-profile";
        }

        try {
            final User user = userService.getUserById(userId);
            userService.updateUser(user, userBindingModel);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("User update operation failed. {0}", ex.getMessage()));
            redirectAttributes.addFlashAttribute("userBindingModel", userBindingModel);
            redirectAttributes.addFlashAttribute("editSuccess", false);
            return redirect;
        }

        redirectAttributes.addFlashAttribute("editSuccess", true);
        return redirect;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public String updateUserRole(
            @PathVariable("id") final Long userId,
            @Valid @ModelAttribute("roleBindingModel") final RoleBindingModel roleBindingModel,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes)
    {
        final String redirect = "redirect:/roles";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roleBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("roleBindingModel", roleBindingModel);
            return "redirect:/edit-role/" + userId;
        }

        try {
            final User user = userService.getUserById(userId);
            userService.updateUser(user, roleBindingModel);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("User role update operation failed. {0}", ex.getMessage()));
            redirectAttributes.addFlashAttribute("editSuccess", false);
            return redirect;
        }

        redirectAttributes.addFlashAttribute("editSuccess", true);
        return redirect;
    }

}
