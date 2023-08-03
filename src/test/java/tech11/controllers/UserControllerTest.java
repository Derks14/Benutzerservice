package tech11.controllers;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import tech11.dto.AddUserRequest;
import tech11.dto.UpdateUserRequest;
import tech11.models.User;
import tech11.repositories.UserRepository;

import java.time.LocalDate;
import java.time.Month;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserControllerTest {
    private static final String ENDPOINT = "/api/users";

    private User testUser;
    private static final String RANDOM_TEST_UUID = "3e9d8271-b457-4999-8ec6-acc3be930996";

    @Inject
    UserRepository repository;

    @BeforeEach
    @Transactional
    void setUp() {
        User user = User.builder()
                .firstname("Thomas").lastname("Müller")
                .phone("0248801964")
                .email("müller@munich.com").username("müller25")
                .birthday("22-04").dateOfBirth(LocalDate.of(1889, Month.SEPTEMBER, 13))
                .password("f0000b@R")
                .build();
        repository.persist(user);
        this.testUser = user;
    }

    @AfterEach
    @Transactional
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Add User Test")
    void test_addUser() throws Exception {
        AddUserRequest addUserRequest = new AddUserRequest(
                "Cristiano", "Ronaldo", "cr7","cristiano@utd.com",
                "f0000b@R", "05-02-1985", "0248801964"
        );

        given()
                .contentType(ContentType.JSON)
                .body(addUserRequest)
            .when()
                .post(ENDPOINT)
            .then()
                .statusCode(201)
                .body("data", notNullValue());

    }

    @Test
    @DisplayName("Add User with Existing Username or Email (Anomalous)")
    void test_anomalous_addExistingUser() {
        AddUserRequest addUserRequest = new AddUserRequest(
                "Cristiano", "Ronaldo", testUser.getUsername(), testUser.getEmail(),
                "f0000b@R", "05-02-1985", "0248801964"
        );
        given()
                .contentType(ContentType.JSON)
                .body(addUserRequest)
                .when()
                .post(ENDPOINT)
                .then()
                .statusCode(400)
                .body("message", is("User already exists"));

    }

    @Test
    @DisplayName("Fetch Users")
    void fetchUsers() throws Exception {
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
            .when()
                .get(ENDPOINT)
            .then()
                .statusCode(200)
                .body("data", hasSize(greaterThanOrEqualTo(1)))
                .body("pagination", notNullValue());
    }

    @Test
    @DisplayName("Get Single User")
    void getUser() throws Exception {
        given()
                .pathParam("id", testUser.getId())
            .when()
                .get(ENDPOINT + "/{id}")
            .then()
                .statusCode(200)
                .body("data", notNullValue());
    }

    @Test
    @DisplayName("Get Unavailable User (Anomalous)")
    void getUnavailableUser() throws Exception {

        given()
                .pathParam("id", RANDOM_TEST_UUID)
            .when()
                .get(ENDPOINT + "/{id}")
            .then()
                .statusCode(404)
                .body("message", is("We Cannot Find Requested User"));
    }

    @Test
    @DisplayName("Modify User")
    void modifyUser() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Gerd", "Muller", "0248801964");
        given()
                .pathParam("id", testUser.getId())
                .contentType(ContentType.JSON)
                .body(updateUserRequest)
            .when()
                .put(ENDPOINT + "/{id}")
            .then()
                .statusCode(200)
                .body("data", notNullValue());
    }

    @Test
    @DisplayName("Modify Unavailable User (Anomalous)")
    void modifyUnavailableUser() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Gerd", "Muller", "0248801964");
        given()
                .pathParam("id", "3e9d8271-b457-4999-8ec6-acc3be930996")
                .contentType(ContentType.JSON)
                .body(updateUserRequest)
            .when()
                .put(ENDPOINT + "/{id}")
            .then()
                .statusCode(404)
                .body("message", is("We Cannot Find Requested User"));
    }

    @Test
    @DisplayName("Delete User")
    void deleteUser() throws Exception {
        given()
                .pathParam("id", testUser.getId())
            .when()
                .delete(ENDPOINT + "/{id}")
            .then()
                .statusCode(200)
                .body("data", notNullValue());
    }
    @Test
    @DisplayName("Delete Unavailable User (Anomalous)")
    void deleteUnavailableUser() throws Exception {
        given()
                .pathParam("id", RANDOM_TEST_UUID)
            .when()
                .delete(ENDPOINT + "/{id}")
            .then()
                .statusCode(404)
                .body("message", is("We Cannot Find Requested User"));
    }
}