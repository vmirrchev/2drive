package softuni.exam.drive.model.enums;

/**
 * @author Vasil Mirchev
 */
public enum Role {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private final String roleText;

    Role(String roleText) {
        this.roleText = roleText;
    }

    public String text() {
        return roleText;
    }
}
