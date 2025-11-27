package com.vikku.authservice.common.model.mapper;

/**
 * Interface for mapping between source and target types
 * Mapper class named {@link BaseMapper}
 *
 * @param <D> the source type
 * @param <E> the target type
 */

public interface BaseMapper<D, E> {

    /**
     * Maps to entity from source dto
     */
    E toEntity(D dto);

    /**
     * Maps to dto from source entity
     */
    D toDto(E entity);


}
