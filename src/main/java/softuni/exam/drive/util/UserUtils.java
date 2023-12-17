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
     * Validates if a user is able to delete an offer
     * @param principal user performing the deletion operation
     * @param offerCreator user who added the offer
     * @throws RuntimeException if user is not admin and is not the offer creator
     */
    public static void verifyUserCanDeleteOffer(final User principal, User offerCreator) {
        if (!principal.getRole().equals(Role.ROLE_ADMIN) && !principal.equals(offerCreator)) {
            throw new RuntimeException(MessageFormat.format("Offer created by user ({0}) cannot be deleted by user ({1}).", offerCreator.getUsername(), principal.getUsername()));
        }
    }

    /**
     * Validates if a user is able to update user's data
     * @param principal user performing the update operation
     * @param user user which data will be updated
     * @throws RuntimeException if principal is different from the user
     */
    public static void verifyUserCanEditProfile(final User principal, final User user) {
        if (!principal.equals(user)) {
            throw new RuntimeException(MessageFormat.format("Profile of user ({0}) cannot be updated by user ({1}).", user.getUsername(), principal.getUsername()));
        }
    }
}
