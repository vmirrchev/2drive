package softuni.exam.drive.util;

import org.junit.jupiter.api.Test;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vasil Mirchev
 */
class UserUtilsTest {

    private User offerCreator = mock(User.class);
    private User principal = mock(User.class);

    @Test
    void verifyUserCanDeleteOfferWhenUserAdmin() {
        when(principal.getRole()).thenReturn(Role.ROLE_ADMIN);
        assertDoesNotThrow(() -> UserUtils.verifyUserCanDeleteOffer(principal, offerCreator));
    }

    @Test
    void verifyUserCanDeleteOfferWhenUserIsOfferCreator() {
        when(principal.getRole()).thenReturn(Role.ROLE_USER);
        assertDoesNotThrow(() -> UserUtils.verifyUserCanDeleteOffer(principal, principal));
    }

    @Test
    void verifyUserCanDeleteOfferThrows() {
        final String principalUsername = "user";
        final String offerCreatorUsername = "other user";
        final String message = MessageFormat.format("Offer created by user ({0}) cannot be deleted by user ({1}).", offerCreatorUsername, principalUsername);
        when(principal.getRole()).thenReturn(Role.ROLE_USER);
        when(offerCreator.getUsername()).thenReturn(offerCreatorUsername);
        when(principal.getUsername()).thenReturn(principalUsername);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> UserUtils.verifyUserCanDeleteOffer(principal, offerCreator));
        assertEquals(message, thrown.getMessage());
    }
}