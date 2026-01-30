package organization.service;

public enum ClientType {

    PRIVATE("Osoba prywatna"),
    COMPANY("Firma");

    private final String displayName;

    ClientType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
