package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import softuni.exam.drive.BaseTest;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@ExtendWith(OutputCaptureExtension.class)
class OfferControllerTest extends BaseTest {

    private final OfferController offerController = new OfferController(offerService);
    private final User principal = mock(User.class);

    @Test
    void createOfferShouldAddNewOffer(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authentication.getPrincipal()).thenReturn(user);

        final String result = offerController.createOffer(authentication, offerBindingModel, bindingResult, redirectAttributes);

        verify(offerService, times(1)).createOffer(offerBindingModel, user);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectAddOfferUrl, result);
    }

    @Test
    void createOfferShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        final String result = offerController.createOffer(authentication, offerBindingModel, bindingResult, redirectAttributes);

        verify(offerService, times(0)).createOffer(offerBindingModel, user);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.offerBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute(offerBindingModelAttribute, offerBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectAddOfferUrl, result);
    }

    @Test
    void createOfferShouldHandleExceptions(CapturedOutput capturedOutput) {
        final String exceptionMessage = "message";
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authentication.getPrincipal()).thenReturn(user);
        doThrow(new RuntimeException(exceptionMessage)).when(offerService).createOffer(offerBindingModel, user);

        final String result = offerController.createOffer(authentication, offerBindingModel, bindingResult, redirectAttributes);

        verify(offerService, times(1)).createOffer(offerBindingModel, user);
        verify(redirectAttributes, times(1)).addFlashAttribute(offerBindingModelAttribute, offerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Offer creation operation failed. {0}", exceptionMessage));
        assertEquals(redirectAddOfferUrl, result);
    }

    @Test
    void deleteOfferShouldDelete(CapturedOutput capturedOutput) {
        when(authentication.getPrincipal()).thenReturn(principal);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(offer.getAddedBy()).thenReturn(principal);
        when(principal.getRole()).thenReturn(Role.ROLE_USER);

        final String result = offerController.deleteOffer(offerId, authentication, redirectAttributes);

        verify(offerService, times(1)).getOfferById(offerId);
        verify(offerService, times(1)).deleteOffer(offer);
        verify(redirectAttributes, times(1)).addFlashAttribute("deleteSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectOffersUrl, result);
    }

    @Test
    void deleteOfferShouldDeleteHandleExceptions(CapturedOutput capturedOutput) {
        when(authentication.getPrincipal()).thenReturn(principal);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(offer.getAddedBy()).thenReturn(user);
        when(principal.getRole()).thenReturn(Role.ROLE_USER);


        final String result = offerController.deleteOffer(offerId, authentication, redirectAttributes);

        verify(offerService, times(1)).getOfferById(offerId);
        verify(offerService, times(0)).deleteOffer(offer);
        verify(redirectAttributes, times(1)).addFlashAttribute("deleteSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("Offer created by user ({0}) cannot be deleted by user ({1}).", user.getUsername(), principal.getUsername()));
        assertEquals(redirectOffersUrl, result);
    }
}