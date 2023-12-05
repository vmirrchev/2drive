package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.ModelBindingModel;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.service.ModelService;

import java.text.MessageFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@ExtendWith(OutputCaptureExtension.class)
class ModelControllerTest {

    private final ModelService modelService = mock(ModelService.class);
    private final ModelController modelController = new ModelController(modelService);
    private final ModelBindingModel modelBindingModel = mock(ModelBindingModel.class);
    private final BindingResult bindingResult = mock(BindingResult.class);
    private final RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
    private final Model model = mock(Model.class);
    private final Long modelId = 1L;
    private final Long brandId = 1L;
    private final String exceptionMessage = "message";
    private final String redirectUrl = "redirect:/add-model";

    @Test
    void createModelShouldAddNewModel(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);

        final String result = modelController.createModel(modelBindingModel, bindingResult, redirectAttributes);

        verify(modelService, times(1)).createModel(modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectUrl, result);
    }

    @Test
    void createModelShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String result = modelController.createModel(modelBindingModel, bindingResult, redirectAttributes);

        verify(modelService, times(0)).createModel(modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.modelBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute("modelBindingModel", modelBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectUrl, result);
    }

    @Test
    void createModelShouldHandleExceptions(CapturedOutput capturedOutput) {
        final String exceptionMessage = "message";
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(modelService).createModel(modelBindingModel);

        final String result = modelController.createModel(modelBindingModel, bindingResult, redirectAttributes);

        verify(modelService, times(1)).createModel(modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("modelBindingModel", modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Model creation operation failed. {0}", exceptionMessage));
        assertEquals(redirectUrl, result);
    }

    @Test
    void getModelShouldReturnModel() {
        when(modelService.getModelById(modelId)).thenReturn(model);

        final Model result = modelController.getModel(modelId);

        assertEquals(model, result);
        verify(modelService, times(1)).getModelById(modelId);
    }

    @Test
    void getModelShouldReturnModel(CapturedOutput capturedOutput) {
        doThrow(new RuntimeException(exceptionMessage)).when(modelService).getModelById(modelId);

        final Model result = modelController.getModel(modelId);

        assertNull(result);
        verify(modelService, times(1)).getModelById(modelId);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Model retrieval operation failed. {0}", exceptionMessage));
    }

    @Test
    void getModelsByBrandShouldReturnModels() {
        final List<Model> models = List.of(model);
        when(modelService.getAllModelsByBrandId(brandId)).thenReturn(models);

        final List<Model> result = modelController.getModelsByBrand(brandId);

        assertEquals(models, result);
        verify(modelService, times(1)).getAllModelsByBrandId(brandId);
    }

    @Test
    void getModelsByBrandShouldReturnEmptyListWhenBrandIdInvalid(CapturedOutput capturedOutput) {
        doThrow(new RuntimeException(exceptionMessage)).when(modelService).getAllModelsByBrandId(brandId);

        final List<Model> result = modelController.getModelsByBrand(brandId);

        assertThat(result).isEmpty();
        verify(modelService, times(1)).getAllModelsByBrandId(brandId);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Models retrieval operation failed. {0}", exceptionMessage));
    }
}