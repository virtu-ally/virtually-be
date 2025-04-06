package com.virtuallyapp

import io.smallrye.mutiny.Uni
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Service for managing MyKotlinEntity instances.
 * Provides methods for CRUD operations using the repository.
 */
@ApplicationScoped
class MyKotlinEntityService {

    @Inject
    lateinit var repository: MyKotlinEntityRepository

    /**
     * Find all entities.
     * @return a Uni emitting a list of all entities
     */
    @WithTransaction
    fun findAll(): Uni<List<MyKotlinEntity>> {
        return repository.findAll().list()
    }

    /**
     * Find an entity by ID.
     * @param id the ID of the entity to find
     * @return a Uni emitting the found entity, or null if not found
     */
    @WithTransaction
    fun findById(id: Long): Uni<MyKotlinEntity?> {
        return repository.findById(id)
    }

    /**
     * Create a new entity.
     * @param entity the entity to create
     * @return a Uni emitting the created entity
     */
    @WithTransaction
    fun create(entity: MyKotlinEntity): Uni<MyKotlinEntity> {
        return repository.persist(entity)
    }

    /**
     * Update an existing entity.
     * @param id the ID of the entity to update
     * @param entity the updated entity data
     * @return a Uni emitting the updated entity, or null if not found
     */
    @WithTransaction
    fun update(id: Long, entity: MyKotlinEntity): Uni<MyKotlinEntity?> {
        return repository.findById(id)
            .onItem().ifNotNull().transformToUni { existingEntity ->
                existingEntity.field = entity.field
                repository.persist(existingEntity)
            }
    }

    /**
     * Partially update an existing entity.
     * @param id the ID of the entity to update
     * @param entity the entity with fields to update (null fields are ignored)
     * @return a Uni emitting the updated entity, or null if not found
     */
    @WithTransaction
    fun partialUpdate(id: Long, entity: MyKotlinEntity): Uni<MyKotlinEntity?> {
        return repository.findById(id)
            .onItem().ifNotNull().transformToUni { existingEntity ->
                entity.field?.let { existingEntity.field = it }
                repository.persist(existingEntity)
            }
    }

    /**
     * Delete an entity by ID.
     * @param id the ID of the entity to delete
     * @return a Uni emitting true if the entity was deleted, false otherwise
     */
    @WithTransaction
    fun deleteById(id: Long): Uni<Boolean> {
        return repository.deleteById(id)
    }
}
