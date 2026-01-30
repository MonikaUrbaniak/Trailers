package organization.ui.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;
import organization.entity.Client;
import organization.ui.ClientRow;
import organization.ui.search.TrigramUtil;
import java.util.List;
import java.util.function.Consumer;

@Component
public class ClientTableController {

    private final ObservableList<ClientRow> baseRows = FXCollections.observableArrayList();
    private final FilteredList<ClientRow> filteredRows = new FilteredList<>(baseRows);

    private TableView<ClientRow> table;

    public void bind(TableView<ClientRow> table) {
        this.table = table;
        table.setItems(filteredRows);
    }

    public void setClients(List<ClientRow> rows) {
        baseRows.setAll(rows);
    }

    public void filter(String query) {
        String q = query == null ? "" : query.trim();

        filteredRows.setPredicate(row -> {
            if (q.isEmpty()) return true;
            return row.getSearchableFields().stream()
                    .anyMatch(f -> TrigramUtil.matches(q, f));
        });
    }

    public void selectById(Long clientId) {
        if (clientId == null || table == null) return;

        baseRows.stream()
                .filter(r -> r.getId() == clientId)
                .findFirst()
                .ifPresent(row -> {
                    table.getSelectionModel().select(row);
                    table.scrollTo(row);
                });
    }

    public void onSelection(Consumer<ClientRow> consumer) {
        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, o, row) -> {
                    if (row != null) consumer.accept(row);
                });
    }

    public void refresh(List<Client> clients, Client selected) {
        setClients(clients.stream().map(ClientRow::fromEntity).toList());
        if (selected != null) {
            selectById(selected.getId());
        }
    }
}
