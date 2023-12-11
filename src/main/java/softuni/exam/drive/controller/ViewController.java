package softuni.exam.drive.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softuni.exam.drive.model.dto.*;
import softuni.exam.drive.model.entity.Offer;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.*;
import softuni.exam.drive.service.BrandService;
import softuni.exam.drive.service.OfferService;
import softuni.exam.drive.service.UserService;

import java.text.MessageFormat;

/**
 * Controller responsible for rendering the views
 * @author Vasil Mirchev
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);
    private final BrandService brandService;
    private final OfferService offerService;
    private final UserService userService;

    @GetMapping("/")
    public String getIndex(final Model model) {
        model.addAttribute("bodyTypes", BodyType.values());

        return "index";
    }

    @GetMapping("/register")
    public String getRegister(final Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }

        if (!model.containsAttribute("registerBindingModel")) {
            model.addAttribute("registerBindingModel", new RegisterBindingModel());
        }

        return "register";
    }

    @RequestMapping("/login")
    public String getLogin(final Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/login-error")
    public String getLoginError(final Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/profile")
    public String getProfile(final Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        final User user = (User) authentication.getPrincipal();
        final User dbUser = userService.getUserById(user.getId());
        model.addAttribute("username", dbUser.getUsername());
        model.addAttribute("firstName", dbUser.getFirstName());
        model.addAttribute("lastName", dbUser.getLastName());
        model.addAttribute("email", dbUser.getEmail());
        model.addAttribute("phoneNumber", dbUser.getPhoneNumber());

        return "profile";
    }

    @GetMapping("/edit-profile")
    public String getEditProfile(final Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        final User user = (User) authentication.getPrincipal();
        final User dbUser = userService.getUserById(user.getId());
        model.addAttribute("userId", user.getId());

        if (!model.containsAttribute("userBindingModel")) {
            final UserBindingModel userBindingModel = new UserBindingModel();
            userBindingModel.setUsername(dbUser.getUsername());
            userBindingModel.setFirstName(dbUser.getFirstName());
            userBindingModel.setLastName(dbUser.getLastName());
            userBindingModel.setEmail(dbUser.getEmail());
            userBindingModel.setPhoneNumber(dbUser.getPhoneNumber());
            model.addAttribute("userBindingModel", userBindingModel);
        }

        return "edit-profile";
    }

    @GetMapping("/about-us")
    public String getAboutUs(final Model model) {
        return "about-us";
    }

    @GetMapping("/privacy-policy")
    public String getPrivacyPolicy(final Model model) {
        return "privacy-policy";
    }

    @GetMapping("/terms-and-conditions")
    public String getTermsAndConditions(final Model model) {
        return "terms-and-conditions";
    }

    @GetMapping("/site-map")
    public String getSiteMap(final Model model) {
        return "site-map";
    }

    @GetMapping("/contact-us")
    public String getContactUs(final Model model) {
        return "contact-us";
    }

    @GetMapping("/offers")
    public String getOffers(final Model model) {
        model.addAttribute("offers", offerService.getAllOffers());

        return "offers";
    }

    @GetMapping("/offers/{id}")
    public String getOffer(@PathVariable("id") final Long offerId, final Model model) {
        Offer offer = null;

        try {
            offer = offerService.getOfferById(offerId);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("Offer retrieval operation failed. {0}", ex.getMessage()));
        }

        model.addAttribute("offer", offer);

        return "offer";
    }

    @GetMapping("/offers/filter")
    public String getOffers(@RequestParam() final BodyType bodyType, final Model model) {
        model.addAttribute("offers", offerService.getAllOffersByBodyType(bodyType));

        return "offers";
    }

    @GetMapping("/add-offer")
    public String getAddOffer(final Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        if (!model.containsAttribute("offerBindingModel")) {
            model.addAttribute("offerBindingModel", new OfferBindingModel());
        }
        model.addAttribute("brands", brandService.getAllBrands());

        return "add-offer";
    }

    @GetMapping("/add-engine")
    public String getAddEngine(final Model model, HttpServletRequest http) {
        if (!http.isUserInRole(Role.ROLE_ADMIN.name())) {
            return "redirect:/";
        }

        if (!model.containsAttribute("engineBindingModel")) {
            model.addAttribute("engineBindingModel", new EngineBindingModel());
        }
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("fuelTypes", FuelType.values());

        return "add-engine";
    }

    @GetMapping("/my-offers")
    public String getMyOffers(final Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        return "offers";
    }

    @GetMapping("/add-model")
    public String getAddModel(final Model model, HttpServletRequest http) {
        if (!http.isUserInRole(Role.ROLE_ADMIN.name())) {
            return "redirect:/";
        }

        if (!model.containsAttribute("modelBindingModel")) {
            model.addAttribute("modelBindingModel", new ModelBindingModel());
        }
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("bodyTypes", BodyType.values());
        model.addAttribute("driveTypes", DriveType.values());
        model.addAttribute("transmissionTypes", TransmissionType.values());

        return "add-model";
    }
}
