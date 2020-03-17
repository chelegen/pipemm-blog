package hello.integration;

import hello.Application;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class MyIntegrationTest {
    @Inject
    Environment environment;

    @BeforeAll
    public static void setUp() throws IOException {
        File projectDir = new File(System.getProperty("basedir", System.getProperty("user.dir")));
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(
                "jdbc:h2:mem:test",
                "test",
                "test"
        );
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void indexHtmlAccessible() throws IOException {
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:" + port + "/auth");
        CloseableHttpResponse httpResponse = httpClient.execute(get);
        Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        String response = EntityUtils.toString(httpResponse.getEntity());
        System.out.println(response);
        Assertions.assertTrue(response.contains("用户没有登录"));
    }

    @Test
    public void test() {
    }
}
