package softuni.exam.drive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.OfferBindingModel;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.service.OfferService;

import java.text.MessageFormat;

/**
 * Controller responsible for handling offer interactions
 * @author Vasil Mirchev
 */
@Controller
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
public class OfferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferController.class);
    private final OfferService offerService;

    @PostMapping()
    public String createOffer(
            final Authentication authentication,
            @Valid @ModelAttribute("offerBindingModel") final OfferBindingModel offerBindingModel,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes)
    {

        final String redirect = "redirect:/add-offer";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.offerBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("offerBindingModel", offerBindingModel);
            return redirect;
        }

        try {
            final User user = (User) authentication.getPrincipal();
            offerService.createOffer(offerBindingModel, user);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("Offer creation operation failed. {0}", ex.getMessage()));
            redirectAttributes.addFlashAttribute("offerBindingModel", offerBindingModel);
            redirectAttributes.addFlashAttribute("addSuccess", false);
            return redirect;
        }

        redirectAttributes.addFlashAttribute("addSuccess", true);
        return redirect;
    }
}
