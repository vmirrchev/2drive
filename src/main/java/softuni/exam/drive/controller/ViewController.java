package softuni.exam.drive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller responsible for rendering the views
 * @author Vasil Mirchev
 */
@Controller
public class ViewController {

    @GetMapping("/")
    public ModelAndView getIndex() { return new ModelAndView("index"); }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return new ModelAndView("register");
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        return new ModelAndView("login");
    }

    @GetMapping("/profile")
    public ModelAndView getProfile() {
        return new ModelAndView("profile");
    }

    @GetMapping("/about-us")
    public ModelAndView getAboutUs() {
        return new ModelAndView("about-us");
    }

    @GetMapping("/privacy-policy")
    public ModelAndView getPrivacyPolicy() {
        return new ModelAndView("privacy-policy");
    }

    @GetMapping("/terms-and-conditions")
    public ModelAndView getTermsAndConditions() {
        return new ModelAndView("terms-and-conditions");
    }

    @GetMapping("/site-map")
    public ModelAndView getSiteMap() {
        return new ModelAndView("site-map");
    }

    @GetMapping("/contact-us")
    public ModelAndView getContactUs() {
        return new ModelAndView("contact-us");
    }

    @GetMapping("/offers")
    public ModelAndView getOffers() {return new ModelAndView("offers");}

    @GetMapping("/offer")
    public ModelAndView getOffer() {return new ModelAndView("offer");}

    @GetMapping("/add-offer")
    public ModelAndView getAddOffer() {
        return new ModelAndView("add-offer");
    }

    @GetMapping("/my-offers")
    public ModelAndView getMyOffers() {return new ModelAndView("offers");}

    @GetMapping("/add-engine")
    public ModelAndView getAddEngine() {return new ModelAndView("add-engine");}

    @GetMapping("/add-model")
    public ModelAndView getAddModel() {return new ModelAndView("add-model");}
}
