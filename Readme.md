# Spring and Es Es crash
Demonstrate that when the Elasticsearch client has errors, it is not possible to make further request.

```bash
./gradlew test --tests test.es_crash.service.StubServiceTest.test
```