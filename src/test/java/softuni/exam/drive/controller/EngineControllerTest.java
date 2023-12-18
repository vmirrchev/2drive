package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import softuni.exam.drive.BaseTest;
import softuni.exam.drive.model.dto.EngineDTO;
import softuni.exam.drive.model.entity.Engine;

import java.text.MessageFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@ExtendWith(OutputCaptureExtension.class)
class EngineControllerTest extends BaseTest {

    private final EngineController engineController = new EngineController(engineService, modelMapper);

    @Test
    void createEngineShouldAddNewEngine(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);

        final String result = engineController.createEngine(engineBindingModel, bindingResult, redirectAttributes);

        verify(engineService, times(1)).createEngine(engineBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectAddEngineUrl, result);
    }

    @Test
    void createEngineShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String result = engineController.createEngine(engineBindingModel, bindingResult, redirectAttributes);

        verify(engineService, times(0)).createEngine(engineBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.engineBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute(engineBindingModelAttribute, engineBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectAddEngineUrl, result);
    }

    @Test
    void createEngineShouldHandleExceptions(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(engineService).createEngine(engineBindingModel);

        final String result = engineController.createEngine(engineBindingModel, bindingResult, redirectAttributes);

        verify(engineService, times(1)).createEngine(engineBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute(engineBindingModelAttribute, engineBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Engine creation operation failed. {0}", exceptionMessage));
        assertEquals(redirectAddEngineUrl, result);
    }

    @Test
    void getEnginesByBrandShouldReturnEngines() {
        final List<Engine> engines = List.of(mock(Engine.class), mock(Engine.class));
        when(engineService.getAllEnginesByBrandId(brandId)).thenReturn(engines);

        final List<EngineDTO> result = engineController.getEnginesByBrand(brandId);

        assertEquals(result.size(), engines.size());
        verify(engineService, times(1)).getAllEnginesByBrandId(brandId);
    }

    @Test
    void getEnginesByBrandShouldHandleExceptions(CapturedOutput capturedOutput) {
        doThrow(new RuntimeException(exceptionMessage)).when(engineService).getAllEnginesByBrandId(brandId);

        final List<EngineDTO> result = engineController.getEnginesByBrand(brandId);

        assertThat(result).isEmpty();
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Engine retrieval operation failed. {0}", exceptionMessage));
        verify(engineService, times(1)).getAllEnginesByBrandId(brandId);
    }
}