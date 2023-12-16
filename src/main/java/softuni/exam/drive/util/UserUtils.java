package softuni.exam.drive.util;

import lombok.experimental.UtilityClass;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;

import java.text.MessageFormat;

/**
 * Utility class used for different user related operations
 */
@UtilityClass
public final class UserUtils {

    /**
     * Validates if user is able to delete an offer
     * @param principal user performing the deletion operation
     * @param offerCreator user who added the offer
     * @throws RuntimeException if user is not admin and is not the offer creator
     */
    public static void verifyUserCanDeleteOffer(User principal, User offerCreator) {
        if (!principal.getRole().equals(Role.ROLE_ADMIN) && !principal.equals(offerCreator)) {
            throw new RuntimeException(MessageFormat.format("Offer created by user ({0}) cannot be deleted by user ({1}).", offerCreator.getUsername(), principal.getUsername()));
        }
    }
}
