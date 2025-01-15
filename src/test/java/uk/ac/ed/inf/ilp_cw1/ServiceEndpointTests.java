package uk.ac.ed.inf.ilp_cw1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import uk.ac.ed.inf.ilp_cw1.controllers.BasicController;
import uk.ac.ed.inf.ilp_cw1.Data.*;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(
    webEnvironment = WebEnvironment.DEFINED_PORT,
    properties = {"server.port=8080"
    })

public class ServiceEndpointTests {
  @Autowired
  private BasicController basicController;
  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Test
  void contextLoads() throws Exception {
    assertThat(basicController).isNotNull();
  }

  @Test
  void isAliveShouldReturnTrue() throws Exception {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/isAlive",
        String.class)).contains("true");
  }

  @Test
  void restaurantRequest() throws Exception {
    ResponseEntity<Restaurant[]> result = this.restTemplate.getForEntity("http://localhost:" + port + "/restaurants",
        Restaurant[].class);
    assertThat(result.getStatusCode() == HttpStatus.OK).isTrue();
    assertThat(result.getBody().length > 0).isTrue();
  }
}
