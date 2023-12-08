package softuni.exam.drive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.RegisterBindingModel;
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
}
