package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import com.emramirez.islandtrip.repository.CalendarDateRepository;
import com.emramirez.islandtrip.repository.ReservationRepository;
import com.emramirez.islandtrip.utils.IntegrationTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllerIT {

    public static final String CUSTOMER_NAME = "John Doe";
    public static final String CUSTOMER_EMAIL = "john@test.com";
    public static final String ERRORS = "errors";

    @LocalServerPort
    private int port;

    @Autowired
    private CalendarDateRepository calendarDateRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        reservationRepository.deleteAll();
        calendarDateRepository.deleteAll();
    }

    @Test
    public void createReservation_validReservationGiven_status201Expected() {
        // arrange
        Reservation reservation = IntegrationTestUtils.buildReservation(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), CUSTOMER_NAME, CUSTOMER_EMAIL);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);

        // act
        ResponseEntity<Reservation> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, Reservation.class);

        // assert
        Reservation result = response.getBody();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(result.getId(), notNullValue());
        assertThat(result.getStatus(), equalTo(ReservationStatus.ACTIVE));
        assertThat(result.getArrivalDate(), equalTo(reservation.getArrivalDate()));
        assertThat(result.getDepartureDate(), equalTo(reservation.getDepartureDate()));
        assertThat(result.getCustomerName(), equalTo(CUSTOMER_NAME));
        assertThat(result.getCustomerEmail(), equalTo(CUSTOMER_EMAIL));
    }

    @Test
    public void createReservation_reservationWithoutArrivalDateGiven_status400Expected() throws IOException {
        // arrange
        Reservation reservation = IntegrationTestUtils.buildReservation(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), CUSTOMER_NAME, CUSTOMER_EMAIL);
        reservation.setArrivalDate(null);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);

        // act
        ResponseEntity<String> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, String.class);

        // assert
        Map map = mapper.readValue(response.getBody(), Map.class);
        List<String> errors = (List<String>) map.get("errors");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(errors.get(0), equalTo("Please provide the arrival date"));
    }

    @Test
    public void createReservation_reservationWithoutDepartureDateGiven_status400Expected() throws IOException {
        // arrange
        Reservation reservation = IntegrationTestUtils.buildReservation(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), CUSTOMER_NAME, CUSTOMER_EMAIL);
        reservation.setDepartureDate(null);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);

        // act
        ResponseEntity<String> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, String.class);

        // assert
        Map map = mapper.readValue(response.getBody(), Map.class);
        List<String> errors = (List<String>) map.get("errors");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(errors.get(0), equalTo("Please provide the departure date"));
    }

    @Test
    public void createReservation_reservationWithoutCustomerNameGiven_status400Expected() throws IOException {
        // arrange
        Reservation reservation = IntegrationTestUtils.buildReservation(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), null, CUSTOMER_EMAIL);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);

        // act
        ResponseEntity<String> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, String.class);

        // assert
        Map map = mapper.readValue(response.getBody(), Map.class);
        List<String> errors = (List<String>) map.get(ERRORS);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(errors.get(0), equalTo("Please provide the customer name"));
    }

    @Test
    public void createReservation_reservationWithoutCustomerEmailGiven_status400Expected() throws IOException {
        // arrange
        Reservation reservation = IntegrationTestUtils.buildReservation(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), CUSTOMER_NAME, null);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);

        // act
        ResponseEntity<String> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, String.class);

        // assert
        Map map = mapper.readValue(response.getBody(), Map.class);
        List<String> errors = (List<String>) map.get("errors");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(errors.get(0), equalTo("Please provide the customer email"));
    }

    @Test
    public void createReservation_reservationMissingMultipleFieldsGiven_status400AndMultipleErrorsExpected() throws IOException {
        // arrange
        Reservation reservation = IntegrationTestUtils.buildReservation(null, null, null, null);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);

        // act
        ResponseEntity<String> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, String.class);

        // assert
        Map map = mapper.readValue(response.getBody(), Map.class);
        List<String> errors = (List<String>) map.get("errors");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(errors.size(), equalTo(4));
    }

    @Test
    public void createReservation_reservationWithAlreadyBookedDatesGiven_status409Expected() throws IOException {
        // arrange
        IntStream.range(0, 10)
                .mapToObj(value -> {
                    CalendarDate calendarDate = new CalendarDate();
                    calendarDate.setDate(LocalDate.now().plusDays(value));
                    calendarDate.setStatus(CalendarDateStatus.BOOKED);
                    return calendarDate;
                })
                .forEach(calendarDateRepository::save);

        Reservation reservation = IntegrationTestUtils.buildReservation(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), CUSTOMER_NAME, CUSTOMER_EMAIL);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);

        // act
        ResponseEntity<String> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, String.class);

        // assert
        Map map = mapper.readValue(response.getBody(), Map.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CONFLICT));
        assertThat(map.get("message"), equalTo("The provided date range is no longer available for booking"));
    }

    @Test
    public void updateReservation_concurrentUpdatesGiven_firstUpdateExecutesAndSecondUpdateFailsWith412Expected() throws IOException {
        // arrange
        Reservation reservation = IntegrationTestUtils.buildReservation(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), CUSTOMER_NAME, CUSTOMER_EMAIL);
        HttpEntity<Reservation> requestEntity = new HttpEntity<>(reservation, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(createURLWithPort("/reservations"), requestEntity, String.class);
        Map postResponse = mapper.readValue(response.getBody(), Map.class);
        reservation.setId(UUID.fromString(String.valueOf(postResponse.get("id"))));
        reservation.setVersion(Long.parseLong(String.valueOf(postResponse.get("version"))));
        reservation.setStatus(ReservationStatus.valueOf(String.valueOf(postResponse.get("status"))));

        // act
        // execute first update
        reservation.setCustomerName("Customer1");
        headers.add("If-Match", reservation.getVersion().toString());
        HttpEntity<Reservation> firstRequest = new HttpEntity<>(reservation, headers);
        ResponseEntity<String> firstInvocationResponse = restTemplate
                .exchange(createURLWithPort(String.format("/reservations/%s", reservation.getId())),
                        HttpMethod.PUT, firstRequest, String.class);

        // execute second update
        reservation.setCustomerName("Customer2");
        headers.add("If-Match", reservation.getVersion().toString());
        HttpEntity<Reservation> secondRequest = new HttpEntity<>(reservation, headers);
        ResponseEntity<String> secondInvocationResponse = restTemplate
                .exchange(createURLWithPort(String.format("/reservations/%s", reservation.getId())),
                        HttpMethod.PUT, secondRequest, String.class);

        // assert
        assertThat(firstInvocationResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(secondInvocationResponse.getStatusCode(), equalTo(HttpStatus.PRECONDITION_FAILED));
    }

    private String createURLWithPort(String uri) {
        return String.format("http://localhost:%d%s", port, uri);
    }
}
