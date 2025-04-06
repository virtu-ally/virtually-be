package com.virtuallyapp

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status

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

    /**
     * GET all entities.
     * @return a list of all entities
     */
    @GET
    fun getAll(): Uni<List<MyKotlinEntity>> {
        return service.findAll()
    }

    /**
     * GET an entity by ID.
     * @param id the ID of the entity to retrieve
     * @return the entity if found, or 404 Not Found
     */
    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id: Long): Uni<Response> {
        return service.findById(id)
            .onItem().transform { entity ->
                if (entity != null) {
                    Response.ok(entity).build()
                } else {
                    Response.status(Status.NOT_FOUND).build()
                }
            }
    }

    /**
     * POST a new entity.
     * @param entity the entity to create
     * @return the created entity with 201 Created status
     */
    @POST
    fun create(entity: MyKotlinEntity): Uni<Response> {
        return service.create(entity)
            .onItem().transform { createdEntity ->
                Response.status(Status.CREATED)
                    .entity(createdEntity)
                    .build()
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
        // Ensure the ID in the path matches the entity ID
        entity.id = id
        return service.update(id, entity)
            .onItem().transform { updatedEntity ->
                if (updatedEntity != null) {
                    Response.ok(updatedEntity).build()
                } else {
                    Response.status(Status.NOT_FOUND).build()
                }
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
        return service.partialUpdate(id, entity)
            .onItem().transform { updatedEntity ->
                if (updatedEntity != null) {
                    Response.ok(updatedEntity).build()
                } else {
                    Response.status(Status.NOT_FOUND).build()
                }
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
        return service.deleteById(id)
            .onItem().transform { deleted ->
                if (deleted) {
                    Response.noContent().build()
                } else {
                    Response.status(Status.NOT_FOUND).build()
                }
            }
    }
}