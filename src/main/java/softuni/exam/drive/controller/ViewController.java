package softuni.exam.drive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.model.enums.FuelType;
import softuni.exam.drive.service.BrandService;

/**
 * Controller responsible for rendering the views
 * @author Vasil Mirchev
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final BrandService brandService;

    @GetMapping("/")
    public String getIndex(final Model model) {
        return "index";
    }

    @GetMapping("/register")
    public String getRegister(final Model model) {return "register";}

    @GetMapping("/login")
    public String getLogin(final Model model) {
        return "login";
    }

    @GetMapping("/profile")
    public String getProfile(final Model model) {
        return "profile";
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
        return "offers";
    }

    @GetMapping("/offer")
    public String getOffer(final Model model) {
        return "offer";
    }

    @GetMapping("/add-offer")
    public String getAddOffer(final Model model) {
        return "add-offer";
    }

    @GetMapping("/add-engine")
    public String getAddEngine(final Model model) {
        if (!model.containsAttribute("engineBindingModel")) {
            model.addAttribute("engineBindingModel", new EngineBindingModel());
        }
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("fuelTypes", FuelType.values());

        return "add-engine";
    }

    @GetMapping("/my-offers")
    public String getMyOffers(final Model model) {
        return "offers";
    }

    @GetMapping("/add-model")
    public String getAddModel(final Model model) {
        return "add-model";
    }
}
