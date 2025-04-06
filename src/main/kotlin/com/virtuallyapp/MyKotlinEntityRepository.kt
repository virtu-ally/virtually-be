package com.virtuallyapp

import io.quarkus.hibernate.reactive.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * Repository for managing MyKotlinEntity instances.
 * Provides basic CRUD operations using Hibernate Reactive Panache.
 */
@ApplicationScoped
class MyKotlinEntityRepository : PanacheRepository<MyKotlinEntity>