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
import softuni.exam.drive.model.dto.ModelBindingModel;
import softuni.exam.drive.service.ModelService;

import java.text.MessageFormat;

/**
 * Controller responsible for handling model interactions
 * @author Vasil Mirchev
 */
@Controller
@RequestMapping("/v1/api/models")
@RequiredArgsConstructor
public class ModelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngineController.class);
    private final ModelService modelService;

    @PostMapping()
    public String createModel(
            @Valid @ModelAttribute("engineBindingModel") final ModelBindingModel modelBindingModel,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes)
    {

        final String redirect = "redirect:/add-model";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.modelBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("modelBindingModel", modelBindingModel);
            return redirect;
        }

        try {
            modelService.createModel(modelBindingModel);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("Model creation operation failed. {0}", ex.getMessage()));
            redirectAttributes.addFlashAttribute("modelBindingModel", modelBindingModel);
            redirectAttributes.addFlashAttribute("addSuccess", false);
            return redirect;
        }

        redirectAttributes.addFlashAttribute("addSuccess", true);
        return redirect;
    }
}
