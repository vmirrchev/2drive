package softuni.exam.drive.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.ModelBindingModel;
import softuni.exam.drive.model.dto.ModelDTO;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.service.ModelService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for handling model interactions
 * @author Vasil Mirchev
 */
@Controller
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
public class ModelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngineController.class);
    private final ModelService modelService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    @ResponseBody
    public ModelDTO getModel(@PathVariable("id") final Long modelId) {
        try {
            final Model model = modelService.getModelById(modelId);
            return modelMapper.map(model, ModelDTO.class);
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("Model retrieval operation failed. {0}", ex.getMessage()));
            return null;
        }
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<ModelDTO> getModelsByBrand(@RequestParam final Long brandId) {
        try {
            final List<Model> models = modelService.getAllModelsByBrandId(brandId);
            return models.stream()
                    .map(model -> modelMapper.map(model, ModelDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error(MessageFormat.format("Models retrieval operation failed. {0}", ex.getMessage()));
            return new ArrayList<>();
        }
    }

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
