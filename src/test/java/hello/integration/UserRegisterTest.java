package hello.integration;

import hello.Application;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
public class UserRegisterTest {
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
    public void canRegisterNewUser() throws IOException {
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:" + port + "/auth/register");
        post.setEntity(new StringEntity("{\"username\":\"aaa\",\"password\":\"bbb\"}", ContentType.APPLICATION_JSON));
        httpClient.execute(post, (ResponseHandler<String>) httpResponse -> {
            Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
            String response = EntityUtils.toString(httpResponse.getEntity());

            Assertions.assertTrue(response.contains("注册成功"));
            return null;
        });

        post = new HttpPost("http://localhost:" + port + "/auth/login");
        post.setEntity(new StringEntity("{\"username\":\"aaa\",\"password\":\"bbb\"}", ContentType.APPLICATION_JSON));
        httpClient.execute(post, (ResponseHandler<String>) httpResponse -> {
            Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
            String response = EntityUtils.toString(httpResponse.getEntity());

            Assertions.assertTrue(response.contains("登陆成功"));
            return null;
        });

    }
}
