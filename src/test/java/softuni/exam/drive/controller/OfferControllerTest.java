package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.OfferBindingModel;
import softuni.exam.drive.service.OfferService;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@ExtendWith(OutputCaptureExtension.class)
class OfferControllerTest {

    private final OfferService offerService = mock(OfferService.class);
    private final OfferController offerController = new OfferController(offerService);
    private final OfferBindingModel offerBindingModel = mock(OfferBindingModel.class);
    private final BindingResult bindingResult = mock(BindingResult.class);
    private final RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
    private final String redirectUrl = "redirect:/add-offer";

    @Test
    void createOfferShouldAddNewOffer(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);

        final String result = offerController.createOffer(offerBindingModel, bindingResult, redirectAttributes);

        verify(offerService, times(1)).createOffer(offerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectUrl, result);
    }

    @Test
    void createOfferShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String result = offerController.createOffer(offerBindingModel, bindingResult, redirectAttributes);

        verify(offerService, times(0)).createOffer(offerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.offerBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute("offerBindingModel", offerBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectUrl, result);
    }

    @Test
    void createOfferShouldHandleExceptions(CapturedOutput capturedOutput) {
        final String exceptionMessage = "message";
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(offerService).createOffer(offerBindingModel);

        final String result = offerController.createOffer(offerBindingModel, bindingResult, redirectAttributes);

        verify(offerService, times(1)).createOffer(offerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("offerBindingModel", offerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Offer creation operation failed. {0}", exceptionMessage));
        assertEquals(redirectUrl, result);
    }
}