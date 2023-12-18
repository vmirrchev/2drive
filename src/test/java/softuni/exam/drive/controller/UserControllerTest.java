package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import softuni.exam.drive.BaseTest;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@ExtendWith(OutputCaptureExtension.class)
class UserControllerTest extends BaseTest {

    private final UserController userController = new UserController(userService);

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
        verify(redirectAttributes, times(1)).addFlashAttribute(registerBindingModelAttribute, registerBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectRegisterUrl, result);
    }

    @Test
    void createUserShouldHandleExceptions(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(userService).createUser(registerBindingModel);

        final String result = userController.createUser(registerBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).createUser(registerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute(registerBindingModelAttribute, registerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("addSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("User creation operation failed. {0}", exceptionMessage));
        assertEquals(redirectRegisterUrl, result);
    }

    @Test
    void updateUserShouldUpdateUserData(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(userId)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(user);

        final String result = userController.updateUser(userId, authentication, userBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).updateUser(user, userBindingModel);
        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(user, userBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("editSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectProfileUrl, result);
    }

    @Test
    void updateUserShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String result = userController.updateUser(userId, authentication, userBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(0)).updateUser(user, userBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.userBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute(userBindingModelAttribute, userBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectEditProfileUrl, result);
    }

    @Test
    void updateUserShouldHandleExceptions(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(userId)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(user);
        doThrow(new RuntimeException(exceptionMessage)).when(userService).updateUser(user, userBindingModel);

        final String result = userController.updateUser(userId, authentication, userBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).updateUser(user, userBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute(userBindingModelAttribute, userBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("editSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("User update operation failed. {0}", exceptionMessage));
        assertEquals(redirectProfileUrl, result);
    }

    @Test
    void updateUserRoleShouldUpdateRole(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(userId)).thenReturn(user);

        final String result = userController.updateUserRole(userId, roleBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(user, roleBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("editSuccess", true);
        assertEquals("", capturedOutput.getOut());
        assertEquals(redirectRolesUrl, result);
    }

    @Test
    void updateUserRoleShouldHandleErrors(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String result = userController.updateUserRole(userId, roleBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(0)).getUserById(userId);
        verify(userService, times(0)).updateUser(user, roleBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("org.springframework.validation.BindingResult.roleBindingModel", bindingResult);
        verify(redirectAttributes, times(1)).addFlashAttribute(roleBindingModelAttribute, roleBindingModel);
        assertEquals("", capturedOutput.getOut());
        assertEquals("redirect:/edit-role/" + userId, result);
    }

    @Test
    void updateUserRoleShouldHandleExceptions(CapturedOutput capturedOutput) {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(userId)).thenReturn(user);
        doThrow(new RuntimeException(exceptionMessage)).when(userService).updateUser(user, roleBindingModel);

        final String result = userController.updateUserRole(userId, roleBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(user, roleBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("editSuccess", false);
        assertThat(capturedOutput.getOut()).contains(MessageFormat.format("User role update operation failed. {0}", exceptionMessage));
        assertEquals(redirectRolesUrl, result);
    }
}