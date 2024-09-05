package test.es_crash.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import test.es_crash.dto.CreateRequest;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles({"test"})
@SpringBootTest
class StubServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StubServiceTest.class);
    @Autowired
    StubService stubService;

    static ElasticsearchContainer elastic = new ElasticsearchContainer(
            "elasticsearch:8.9.1"
    );

    @BeforeAll
    static void beforeAll() {
        elastic
                .withAccessToHost(true)
                .withEnv("xpack.security.enabled", "false")
                .withEnv("xpack.security.transport.ssl.enabled","false")
                .withEnv("xpack.security.http.ssl.enabled","false")
                .start();
    }

    @AfterAll
    static void afterAll() {
        elastic.stop();
    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.es.host", () -> elastic.getHttpHostAddress());
    }

    @Test
    void test() throws InterruptedException {
        var createBigDoc = new CreateRequest();
        createBigDoc.setNbItem(5);
        createBigDoc.setTexteLength(3_000_000);
        var bigDoc = stubService.post(createBigDoc);

        var createSmallDoc = new CreateRequest();
        createSmallDoc.setNbItem(5);
        createSmallDoc.setTexteLength(1);
        var smallDoc = stubService.post(createSmallDoc);


        ExecutorService executorService = Executors.newCachedThreadPool();
        var tasks = new ArrayList<Callable<Object>>();
        // These request may crash
        for(int i = 0; i < 25; i++) {
            tasks.add((Callable) () -> stubService.get(bigDoc.getId()));
        }
        executorService.invokeAll(tasks);

        // This request must work
        LOGGER.info("will call the small stub");
        assertDoesNotThrow(() -> stubService.get(smallDoc.getId()));
    }
}