package com.virtuallyapp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.ArrayList

@QuarkusTest
class MyKotlinEntityResourceTest {

    private val BASE_PATH = "/my-kotlin-entities"
    private val createdEntityIds = ArrayList<Long>()

    @BeforeEach
    fun setup() {
        // Clean up any existing entities to ensure tests start with a clean state
        cleanupEntities()
    }

    @AfterEach
    fun cleanup() {
        // Clean up entities created during the test
        cleanupEntities()
    }

    private fun cleanupEntities() {
        // Get all entities
        val response = given()
            .`when`().get(BASE_PATH)
            .then()
            .extract().response()

        // If the response is successful, delete all entities
        if (response.statusCode == 200) {
            val ids = response.jsonPath().getList("id", Long::class.java)
            ids?.forEach { id ->
                given()
                    .`when`().delete("$BASE_PATH/$id")
                    .then()
                    .statusCode(204)
            }
        }

        // Clear the list of created entity IDs
        createdEntityIds.clear()
    }

    private fun createTestEntity(field: String = "test value"): Long {
        val response = given()
            .contentType(ContentType.JSON)
            .body("{\"field\": \"$field\"}")
            .`when`().post(BASE_PATH)
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("field", `is`(field))
            .extract().response()

        val id = response.jsonPath().getLong("id")
        createdEntityIds.add(id)
        println("[DEBUG_LOG] Created entity with ID: $id")
        return id
    }

    @Test
    fun testCreateEntity() {
        // Create a new entity
        createTestEntity("test value")
    }

    @Test
    fun testGetAllEntities() {
        // Create a test entity first
        createTestEntity("test value for getAll")

        // Now test getting all entities
        given()
            .`when`().get(BASE_PATH)
            .then()
            .statusCode(200)
            .body("size()", `is`(1))
            .body("[0].field", `is`("test value for getAll"))
    }

    @Test
    fun testGetEntityById() {
        // Create an entity to get its ID
        val id = createTestEntity("entity to get")

        // Then get it by ID
        given()
            .`when`().get("$BASE_PATH/$id")
            .then()
            .statusCode(200)
            .body("id", `is`(id.toInt()))
            .body("field", `is`("entity to get"))
    }

    @Test
    fun testUpdateEntity() {
        // Create an entity to update
        val id = createTestEntity("entity to update")

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
    fun testPartialUpdateEntity() {
        // Create an entity to update
        val id = createTestEntity("entity to patch")

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
    fun testDeleteEntity() {
        // Create an entity to delete
        val id = createTestEntity("entity to delete")

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
    fun testGetNonExistentEntity() {
        // Use a very large ID that is unlikely to exist
        given()
            .`when`().get("$BASE_PATH/999999")
            .then()
            .statusCode(404)
    }
}
