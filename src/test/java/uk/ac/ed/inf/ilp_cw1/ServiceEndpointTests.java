package uk.ac.ed.inf.ilp_cw1;

import java.util.List;
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
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void uuidShouldReturnStudentId() throws Exception {
    String response = this.restTemplate.getForObject("http://localhost:" + port + "/uuid", String.class);
    assertThat(response).isEqualTo("s2281183");
  }

  @Test
  void distanceToShouldReturnCorrectDistance() throws Exception {

    LngLat point1 = new LngLat();
    point1.setLng(-3.186874);
    point1.setLat(55.944494);

    LngLat point2 = new LngLat();
    point2.setLng(-3.187874);
    point2.setLat(55.944594);

    LngLatPairRequest request = new LngLatPairRequest();
    request.setLngLat1(point1);
    request.setLngLat2(point2);

    ResponseEntity<Double> response = this.restTemplate.postForEntity("http://localhost:" + port + "/distanceTo",request, Double.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isCloseTo(0.00001, within(0.00001));
  }

  @Test
  void distanceToShouldHandleInvalidInput() throws Exception {
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/distanceTo", null, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("Invalid JSON structure");
  }

  @Test
  void isCloseToShouldReturnTrueForClosePoints() throws Exception {

    LngLat point1 = new LngLat();
    point1.setLng(-3.186874);
    point1.setLat(55.944494);

    LngLat point2 = new LngLat();
    point1.setLng(-3.187874);
    point1.setLat(55.944594);


    LngLatPairRequest request = new LngLatPairRequest();
    request.setLngLat1(point1);
    request.setLngLat2(point2);

    ResponseEntity<Boolean> response = this.restTemplate.postForEntity("http://localhost:" + port + "/isCloseTo", request, Boolean.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertEquals(Boolean.TRUE, response.getBody());
  }

  @Test
  void isCloseToShouldReturnFalseForFarPoints() throws Exception {
    LngLat point1 = new LngLat();
    point1.setLng(-3.186874);
    point1.setLat(55.944494);

    LngLat point2 = new LngLat();
    point2.setLng(-3.156874);
    point2.setLat(55.344894);

    LngLatPairRequest request = new LngLatPairRequest();

    request.setLngLat1(point1);
    request.setLngLat2(point2);

    ResponseEntity<Boolean> response = this.restTemplate.postForEntity("http://localhost:" + port + "/isCloseTo", request, Boolean.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.equals(response.getBody(), false));
  }

  @Test
  void nextPositionShouldReturnCorrectPoint() throws Exception {
    LngLat point1 = new LngLat();
    point1.setLng(-3.192473);
    point1.setLat(55.946233);

    LngLat resultantPoint = new LngLat();
    resultantPoint.setLng(-3.19233890050046);
    resultantPoint.setLat(55.94616578895758);


    NextPositionRequest request = new NextPositionRequest();
    request.setAngle(90.0);
    request.setLngLat(point1);

    ResponseEntity<LngLat> response = this.restTemplate.postForEntity("http://localhost:" + port + "/nextPosition", request, LngLat.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(resultantPoint);
  }

  @Test
  void isInRegionShouldReturnTrueForPointInRegion() throws Exception {

    LngLat position = new LngLat();
    position.setLng(-3.186000);
    position.setLat(55.944000);

    LngLat vertex1 = new LngLat();
    vertex1.setLng(-3.192473);
    vertex1.setLat(55.946233);

    LngLat vertex2 = new LngLat();
    vertex2.setLng(-3.192473);
    vertex2.setLat(55.942617);

    LngLat vertex3 = new LngLat();
    vertex3.setLng(-3.184319);
    vertex3.setLat(55.942617);

    LngLat vertex4 = new LngLat();
    vertex4.setLng(-3.184319);
    vertex4.setLat(55.946233);

    LngLat vertex5 = new LngLat();
    vertex5.setLng(-3.192473);
    vertex5.setLat(55.946233);

    Region region = new Region();
    region.setVertices(new LngLat[]{vertex1, vertex2, vertex3, vertex4, vertex5});


    isInRegionRequest request = new isInRegionRequest();
    request.setRegion(region);
    request.setLngLat(position);

    ResponseEntity<Boolean> response = this.restTemplate.postForEntity("http://localhost:" + port + "/isInRegion", request, Boolean.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isTrue();
  }
/*
  @Test
  void validateOrderShouldReturnValidResult() throws Exception {
    Order order = new Order(...); // Fill with valid data
    ResponseEntity<OrderValidationResult> response = this.restTemplate.postForEntity("http://localhost:" + port + "/validateOrder", order, OrderValidationResult.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getValidationCode()).isEqualTo(OrderValidationCode.NO_ERROR);
  }

  @Test
  void calcDeliveryPathShouldReturnValidPath() throws Exception {
    Order order = new Order(...); // Fill with valid data
    ResponseEntity<List> response = this.restTemplate.postForEntity("http://localhost:" + port + "/calcDeliveryPath", order, List.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  void calcDeliveryPathAsGeoJsonShouldReturnValidGeoJson() throws Exception {
    Order order = new Order(...); // Fill with valid data
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/calcDeliveryPathAsGeoJson", order, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).startsWith("{\"type\":\"FeatureCollection\"");
  }


 */
}
