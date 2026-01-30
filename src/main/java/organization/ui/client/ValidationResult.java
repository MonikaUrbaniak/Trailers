package organization.ui.client;

public class ValidationResult {

    private final boolean valid;
    private final String title;
    private final String message;

    private ValidationResult(boolean valid, String title, String message) {
        this.valid = valid;
        this.title = title;
        this.message = message;
    }

    public static ValidationResult ok() {
        return new ValidationResult(true, null, null);
    }

    public static ValidationResult error(String title, String message) {
        return new ValidationResult(false, title, message);
    }

    public boolean isValid() {
        return valid;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}