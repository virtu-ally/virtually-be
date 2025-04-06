package com.virtuallyapp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Order

@QuarkusTest
class MyKotlinEntityResourceTest {

    private val BASE_PATH = "/my-kotlin-entities"

    @Test
    @Order(1)
    fun testCreateEntity() {
        // Create a new entity
        val response = given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"test value\"}")
            .`when`().post(BASE_PATH)
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("field", `is`("test value"))
            .extract().response()

        // Extract the ID for use in other tests
        val id = response.jsonPath().getLong("id")
        println("[DEBUG_LOG] Created entity with ID: $id")
    }

    @Test
    @Order(2)
    fun testGetAllEntities() {
        given()
            .`when`().get(BASE_PATH)
            .then()
            .statusCode(200)
            .body("size()", `is`(1))
            .body("[0].field", `is`("test value"))
    }

    @Test
    @Order(3)
    fun testGetEntityById() {
        // First create an entity to get its ID
        val createResponse = given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"entity to get\"}")
            .`when`().post(BASE_PATH)
            .then()
            .statusCode(201)
            .extract().response()

        val id = createResponse.jsonPath().getLong("id")

        // Then get it by ID
        given()
            .`when`().get("$BASE_PATH/$id")
            .then()
            .statusCode(200)
            .body("id", `is`(id.toInt()))
            .body("field", `is`("entity to get"))
    }

    @Test
    @Order(4)
    fun testUpdateEntity() {
        // First create an entity to update
        val createResponse = given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"entity to update\"}")
            .`when`().post(BASE_PATH)
            .then()
            .statusCode(201)
            .extract().response()

        val id = createResponse.jsonPath().getLong("id")

        // Then update it
        given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"updated value\"}")
            .`when`().put("$BASE_PATH/$id")
            .then()
            .statusCode(200)
            .body("id", `is`(id.toInt()))
            .body("field", `is`("updated value"))

        // Verify the update
        given()
            .`when`().get("$BASE_PATH/$id")
            .then()
            .statusCode(200)
            .body("field", `is`("updated value"))
    }

    @Test
    @Order(5)
    fun testPartialUpdateEntity() {
        // First create an entity to update
        val createResponse = given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"entity to patch\"}")
            .`when`().post(BASE_PATH)
            .then()
            .statusCode(201)
            .extract().response()

        val id = createResponse.jsonPath().getLong("id")

        // Then patch it
        given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"patched value\"}")
            .`when`().patch("$BASE_PATH/$id")
            .then()
            .statusCode(200)
            .body("id", `is`(id.toInt()))
            .body("field", `is`("patched value"))

        // Verify the patch
        given()
            .`when`().get("$BASE_PATH/$id")
            .then()
            .statusCode(200)
            .body("field", `is`("patched value"))
    }

    @Test
    @Order(6)
    fun testDeleteEntity() {
        // First create an entity to delete
        val createResponse = given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"entity to delete\"}")
            .`when`().post(BASE_PATH)
            .then()
            .statusCode(201)
            .extract().response()

        val id = createResponse.jsonPath().getLong("id")

        // Then delete it
        given()
            .`when`().delete("$BASE_PATH/$id")
            .then()
            .statusCode(204)

        // Verify it's gone
        given()
            .`when`().get("$BASE_PATH/$id")
            .then()
            .statusCode(404)
    }

    @Test
    @Order(7)
    fun testGetNonExistentEntity() {
        given()
            .`when`().get("$BASE_PATH/999999")
            .then()
            .statusCode(404)
    }
}