package test.es_crash.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import test.es_crash.dto.CreateRequest;
import test.es_crash.dto.CreateResponse;
import test.es_crash.dto.Stub;
import test.es_crash.dto.StubItem;

import java.util.ArrayList;
import java.util.List;

@Service
public class StubService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StubService.class);
    private final ElasticsearchOperations elasticsearchOperations;

    public StubService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Stub get(String id) {
        var stub = elasticsearchOperations.get(id, Stub.class);
        LOGGER.info("got {}", stub.getId());
        return stub;
    }

    public List<Stub> getAll() {
        var query = new NativeQueryBuilder().build();
        return elasticsearchOperations.search(query, Stub.class).get().map(SearchHit::getContent).toList();
    }

    public CreateResponse post(CreateRequest request) {
        var stub = new Stub();
        stub.setTitle("title - " + Math.random() * 100000);
        stub.setItems(new ArrayList<>());
        for(int i = 0; i < request.getNbItem(); i++) {
            var item = new StubItem();
            item.setText("a".repeat(request.getTexteLength()));
            stub.getItems().add(item);
        }
        var created = elasticsearchOperations.save(stub);
        var result = new CreateResponse();
        result.setId(created.getId());
        return result;
    }

    public void delete(String id) {
        elasticsearchOperations.delete(id, Stub.class);
    }
}
