package organization.ui;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import organization.entity.Client;

import java.util.List;

public class ClientRow {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty displayName = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();
    private final List<String> searchableFields;

    private ClientRow(long id, String displayName, String phone, List<String> searchableFields) {
        this.id.set(id);
        this.displayName.set(displayName);
        this.phone.set(phone);
        this.searchableFields = searchableFields;
    }

    public static ClientRow fromEntity(Client c) {

        String displayName =
                notBlank(c.getCompanyName())
                        ? c.getCompanyName()
                        : (nullToEmpty(c.getFirstName()) + " " + nullToEmpty(c.getLastName())).trim();

        List<String> searchable = List.of(
                nullToEmpty(c.getFirstName()),
                nullToEmpty(c.getLastName()),
                nullToEmpty(c.getCompanyName()),
                nullToEmpty(c.getPhone()),
                nullToEmpty(c.getAddress()),
                nullToEmpty(c.getNip()),
                nullToEmpty(c.getIdNumber())
        );

        return new ClientRow(
                c.getId(),
                displayName,
                c.getPhone(),
                searchable
        );
    }

    public long getId() { return id.get(); }

    public StringProperty nameProperty() { return displayName; }
    public StringProperty phoneProperty() { return phone; }

    public List<String> getSearchableFields() {
        return searchableFields;
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
