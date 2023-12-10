package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.service.UserService;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@ExtendWith(OutputCaptureExtension.class)
class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);
    private final RegisterBindingModel registerBindingModel = mock(RegisterBindingModel.class);
    private final BindingResult bindingResult = mock(BindingResult.class);
    private final RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
    private final String redirectRegisterUrl = "redirect:/register";
    private final String redirectLoginUrl = "redirect:/login";

    @Test
    void createUserShouldAddNewUser(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);

        final String result = userController.createUser(registerBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).createUser(registerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectLoginUrl, result);
    }

    @Test
    void createUserShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String result = userController.createUser(registerBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(0)).createUser(registerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.registerBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute("registerBindingModel", registerBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectRegisterUrl, result);
    }

    @Test
    void createUserShouldHandleExceptions(CapturedOutput capturedOutput) {
        final String exceptionMessage = "message";
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(userService).createUser(registerBindingModel);

        final String result = userController.createUser(registerBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).createUser(registerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("registerBindingModel", registerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("User creation operation failed. {0}", exceptionMessage));
        assertEquals(redirectRegisterUrl, result);
    }
}