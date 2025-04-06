package com.virtuallyapp

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder

/**
 * REST resource for managing MyKotlinEntity instances.
 * Provides endpoints for CRUD operations.
 */
@Path("/my-kotlin-entities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class MyKotlinEntityResource {

    @Inject
    lateinit var service: MyKotlinEntityService

    private val logger = Logger.getLogger(MyKotlinEntityResource::class.java)

    /**
     * GET all entities.
     * @return a list of all entities
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Uni<RestResponse<List<MyKotlinEntity>>> {
        logger.debug("Getting all entities")
        return service.findAll()
            .onItem().transform { entities ->
                logger.debug("Found ${entities.size} entities")
                // Convert to array which should be easier to serialize
                ResponseBuilder.ok(entities).build()
            }
            .onFailure().recoverWithItem { e ->
                logger.error("Error getting all entities", e)
                ResponseBuilder.serverError<List<MyKotlinEntity>>().build()
            }
    }

    /**
     * GET an entity by ID.
     * @param id the ID of the entity to retrieve
     * @return the entity if found, or 404 Not Found
     */
    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id: Long): Uni<Response> {
        logger.debug("Getting entity with ID: $id")
        return service.findById(id)
            .onItem().transform { entity ->
                if (entity != null) {
                    logger.debug("Found entity with ID: $id")
                    Response.ok(entity).build()
                } else {
                    logger.debug("Entity with ID: $id not found")
                    Response.status(Status.NOT_FOUND).build()
                }
            }
            .onFailure().recoverWithItem { e ->
                logger.error("Error getting entity with ID: $id", e)
                Response.serverError().entity("Error getting entity with ID $id: ${e.message}").build()
            }
    }

    /**
     * POST a new entity.
     * @param entity the entity to create
     * @return the created entity with 201 Created status
     */
    @POST
    fun create(entity: MyKotlinEntity): Uni<Response> {
        logger.debug("Creating new entity: $entity")
        return service.create(entity)
            .onItem().transform { createdEntity ->
                logger.debug("Created entity with ID: ${createdEntity.id}")
                Response.status(Status.CREATED)
                    .entity(createdEntity)
                    .build()
            }
            .onFailure().recoverWithItem { e ->
                logger.error("Error creating entity", e)
                Response.serverError().entity("Error creating entity: ${e.message}").build()
            }
    }

    /**
     * PUT to update an entity.
     * @param id the ID of the entity to update
     * @param entity the updated entity data
     * @return the updated entity, or 404 Not Found
     */
    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, entity: MyKotlinEntity): Uni<Response> {
        logger.debug("Updating entity with ID: $id")
        // Ensure the ID in the path matches the entity ID
        entity.id = id
        return service.update(id, entity)
            .onItem().transform { updatedEntity ->
                if (updatedEntity != null) {
                    logger.debug("Updated entity with ID: $id")
                    Response.ok(updatedEntity).build()
                } else {
                    logger.debug("Entity with ID: $id not found for update")
                    Response.status(Status.NOT_FOUND).build()
                }
            }
            .onFailure().recoverWithItem { e ->
                logger.error("Error updating entity with ID: $id", e)
                Response.serverError().entity("Error updating entity with ID $id: ${e.message}").build()
            }
    }

    /**
     * PATCH to partially update an entity.
     * @param id the ID of the entity to update
     * @param entity the entity with fields to update (null fields are ignored)
     * @return the updated entity, or 404 Not Found
     */
    @PATCH
    @Path("/{id}")
    fun partialUpdate(@PathParam("id") id: Long, entity: MyKotlinEntity): Uni<Response> {
        logger.debug("Partially updating entity with ID: $id")
        return service.partialUpdate(id, entity)
            .onItem().transform { updatedEntity ->
                if (updatedEntity != null) {
                    logger.debug("Partially updated entity with ID: $id")
                    Response.ok(updatedEntity).build()
                } else {
                    logger.debug("Entity with ID: $id not found for partial update")
                    Response.status(Status.NOT_FOUND).build()
                }
            }
            .onFailure().recoverWithItem { e ->
                logger.error("Error partially updating entity with ID: $id", e)
                Response.serverError().entity("Error partially updating entity with ID $id: ${e.message}").build()
            }
    }

    /**
     * DELETE an entity by ID.
     * @param id the ID of the entity to delete
     * @return 204 No Content if deleted, or 404 Not Found
     */
    @DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: Long): Uni<Response> {
        logger.debug("Deleting entity with ID: $id")
        return service.deleteById(id)
            .onItem().transform { deleted ->
                if (deleted) {
                    logger.debug("Deleted entity with ID: $id")
                    Response.noContent().build()
                } else {
                    logger.debug("Entity with ID: $id not found for deletion")
                    Response.status(Status.NOT_FOUND).build()
                }
            }
            .onFailure().recoverWithItem { e ->
                logger.error("Error deleting entity with ID: $id", e)
                Response.serverError().entity("Error deleting entity with ID $id: ${e.message}").build()
            }
    }
}
