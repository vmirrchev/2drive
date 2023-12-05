package softuni.exam.drive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.service.EngineService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for handling offer interactions
 * @author Vasil Mirchev
 */
@Controller
@RequestMapping("/api/v1/engines")
@RequiredArgsConstructor
public class EngineController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngineController.class);
    private final EngineService engineService;


    @GetMapping("/filter")
    @ResponseBody
    public List<Engine> getEnginesByBrand(@RequestParam final Long brandId) {
        try {
            return engineService.getAllEnginesByBrandId(brandId);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("Engine retrieval operation failed. {0}", ex.getMessage()));
            return new ArrayList<>();
        }
    }

    @PostMapping()
    public String createEngine(
                    @Valid @ModelAttribute("engineBindingModel") final EngineBindingModel engineBindingModel,
                    final BindingResult bindingResult,
                    final RedirectAttributes redirectAttributes)
    {

        final String redirect = "redirect:/add-engine";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.engineBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("engineBindingModel", engineBindingModel);
            return redirect;
        }

        try {
            engineService.createEngine(engineBindingModel);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("Engine creation operation failed. {0}", ex.getMessage()));
            redirectAttributes.addFlashAttribute("engineBindingModel", engineBindingModel);
            redirectAttributes.addFlashAttribute("addSuccess", false);
            return redirect;
        }

        redirectAttributes.addFlashAttribute("addSuccess", true);
        return redirect;
    }
}
