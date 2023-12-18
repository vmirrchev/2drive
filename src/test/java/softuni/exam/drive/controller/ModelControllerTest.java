package softuni.exam.drive.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import softuni.exam.drive.BaseTest;
import softuni.exam.drive.model.dto.ModelDTO;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(OutputCaptureExtension.class)
class ModelControllerTest extends BaseTest {

    private final ModelController modelController = new ModelController(modelService, modelMapper);

    @BeforeAll
    public void setUp() {
        when(model.getId()).thenReturn(modelId);
        when(model.getName()).thenReturn(modelName);
        when(model.getStartYear()).thenReturn(startYear);
        when(model.getEndYear()).thenReturn(endYear);
        when(model.getBodyTypes()).thenReturn(List.of(BodyType.SEDAN, BodyType.COUPE, BodyType.WAGON, BodyType.CONVERTIBLE));
        when(model.getDriveTypes()).thenReturn(List.of(DriveType.RWD, DriveType.AWD));
        when(model.getTransmissionTypes()).thenReturn(List.of(TransmissionType.MANUAL, TransmissionType.AUTOMATIC));
        when(model.getEngines()).thenReturn(Set.of(mock(Engine.class)));
    }

    @Test
    void createModelShouldAddNewModel(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);

        final String result = modelController.createModel(modelBindingModel, bindingResult, redirectAttributes);

        verify(modelService, times(1)).createModel(modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectAddModelUrl, result);
    }

    @Test
    void createModelShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String result = modelController.createModel(modelBindingModel, bindingResult, redirectAttributes);

        verify(modelService, times(0)).createModel(modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.modelBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute(modelBindingModelAttribute, modelBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectAddModelUrl, result);
    }

    @Test
    void createModelShouldHandleExceptions(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(modelService).createModel(modelBindingModel);

        final String result = modelController.createModel(modelBindingModel, bindingResult, redirectAttributes);

        verify(modelService, times(1)).createModel(modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute(modelBindingModelAttribute, modelBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Model creation operation failed. {0}", exceptionMessage));
        assertEquals(redirectAddModelUrl, result);
    }

    @Test
    void getModelShouldReturnModel() {
        when(modelService.getModelById(modelId)).thenReturn(model);

        final ModelDTO result = modelController.getModel(modelId);

        assertEquals(model.getId(), result.getId());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getStartYear(), result.getStartYear());
        assertEquals(model.getEndYear(), result.getEndYear());
        assertEquals(model.getBodyTypes(), result.getBodyTypes());
        assertEquals(model.getDriveTypes(), result.getDriveTypes());
        assertEquals(model.getTransmissionTypes(), result.getTransmissionTypes());
        assertEquals(model.getEngines(), result.getEngines());
        verify(modelService, times(1)).getModelById(modelId);
    }

    @Test
    void getModelShouldReturnModel(CapturedOutput capturedOutput) {
        doThrow(new RuntimeException(exceptionMessage)).when(modelService).getModelById(modelId);

        final ModelDTO result = modelController.getModel(modelId);

        assertNull(result);
        verify(modelService, times(1)).getModelById(modelId);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Model retrieval operation failed. {0}", exceptionMessage));
    }

    @Test
    void getModelsByBrandShouldReturnModels() {
        final List<Model> models = List.of(model);
        when(modelService.getAllModelsByBrandId(brandId)).thenReturn(models);

        final List<ModelDTO> result = modelController.getModelsByBrand(brandId);

        assertEquals(models.size(), result.size());
        verify(modelService, times(1)).getAllModelsByBrandId(brandId);
    }

    @Test
    void getModelsByBrandShouldReturnEmptyListWhenBrandIdInvalid(CapturedOutput capturedOutput) {
        doThrow(new RuntimeException(exceptionMessage)).when(modelService).getAllModelsByBrandId(brandId);

        final List<ModelDTO> result = modelController.getModelsByBrand(brandId);

        assertThat(result).isEmpty();
        verify(modelService, times(1)).getAllModelsByBrandId(brandId);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Models retrieval operation failed. {0}", exceptionMessage));
    }
}