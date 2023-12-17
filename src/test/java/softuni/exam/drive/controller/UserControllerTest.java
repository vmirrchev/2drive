package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.model.dto.RoleBindingModel;
import softuni.exam.drive.model.dto.UserBindingModel;
import softuni.exam.drive.model.entity.User;
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
    private final User user = mock(User.class);
    private final Authentication authentication = mock(Authentication.class);
    private final UserBindingModel userBindingModel = mock(UserBindingModel.class);
    private final RoleBindingModel roleBindingModel = mock(RoleBindingModel.class);
    private final BindingResult bindingResult = mock(BindingResult.class);
    private final RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
    private final String redirectRegisterUrl = "redirect:/register";
    private final String redirectLoginUrl = "redirect:/login";
    private final String redirectProfileUrl = "redirect:/profile";
    private final String redirectEditProfileUrl = "redirect:/edit-profile";
    private final String redirectRolesUrl = "redirect:/roles";
    private final String exceptionMessage = "message";
    private final Long userId = 1L;

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
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(userService).createUser(registerBindingModel);

        final String result = userController.createUser(registerBindingModel, bindingResult, redirectAttributes);

        verify(userService, times(1)).createUser(registerBindingModel);
        verify(redirectAttributes, times(1)).addFlashAttribute("registerBindingModel", registerBindingModel);
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
        verify(redirectAttributes, times(1)).addFlashAttribute("userBindingModel", userBindingModel);
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
        verify(redirectAttributes, times(1)).addFlashAttribute("userBindingModel", userBindingModel);
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
        verify(redirectAttributes, times(1)).addFlashAttribute("roleBindingModel", roleBindingModel);
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