package test.es_crash.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "stub")
public class Stub {
    @Id
    private String id;
    private String title;
    private List<StubItem> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<StubItem> getItems() {
        return items;
    }

    public void setItems(List<StubItem> items) {
        this.items = items;
    }
}
