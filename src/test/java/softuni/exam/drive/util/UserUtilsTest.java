package softuni.exam.drive.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vasil Mirchev
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserUtilsTest {

    private User user = mock(User.class);
    private User principal = mock(User.class);
    private final String principalUsername = "user";
    private final String userUsername = "other user";

    @BeforeAll
    public void setUp() {
        when(user.getUsername()).thenReturn(userUsername);
        when(principal.getUsername()).thenReturn(principalUsername);
    }

    @Test
    void verifyUserCanDeleteOfferWhenUserAdmin() {
        when(principal.getRole()).thenReturn(Role.ROLE_ADMIN);
        assertDoesNotThrow(() -> UserUtils.verifyUserCanDeleteOffer(principal, user));
    }

    @Test
    void verifyUserCanDeleteOfferWhenUserIsOfferCreator() {
        when(principal.getRole()).thenReturn(Role.ROLE_USER);
        assertDoesNotThrow(() -> UserUtils.verifyUserCanDeleteOffer(principal, principal));
    }

    @Test
    void verifyUserCanDeleteOfferThrows() {
        final String message = MessageFormat.format("Offer created by user ({0}) cannot be deleted by user ({1}).", userUsername, principalUsername);
        when(principal.getRole()).thenReturn(Role.ROLE_USER);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> UserUtils.verifyUserCanDeleteOffer(principal, user));
        assertEquals(message, thrown.getMessage());
    }

    @Test
    void verifyUserCanEditProfile() {
        assertDoesNotThrow(() -> UserUtils.verifyUserCanEditProfile(principal, principal));
    }

    @Test
    void verifyUserCanEditProfileThrows() {
        final String message = MessageFormat.format("Profile of user ({0}) cannot be updated by user ({1}).", userUsername, principalUsername);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> UserUtils.verifyUserCanEditProfile(principal, user));
        assertEquals(message, thrown.getMessage());
    }
}