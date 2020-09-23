package de.calltopower.simpletodo.api.dtoservice;

import java.util.Set;

import de.calltopower.simpletodo.api.dto.STDDto;
import de.calltopower.simpletodo.api.model.STDModel;

/**
 * A DTO service that converts models to DTOs
 *
 * @param <T> Class that extends a DTO of a model
 * @param <M> Class that extends a model
 */
public interface STDDtoService<T extends STDDto<M>, M extends STDModel> {

    /**
     * Converts a model into a DTO
     * 
     * @param model The model to convert
     * @return The converted DTO
     */
    T convert(M model);

    /**
     * Converts a model into a DTO - abridged
     * 
     * @param model The model to convert
     * @return The converted DTO - abridged
     */
    T convertAbridged(M model);

    /**
     * Converts a model set into a DTO set
     * 
     * @param model The model set to convert
     * @return The converted DTO set, empty set if model set is empty or null
     */
    Set<T> convert(Set<M> models);

    /**
     * Converts a model set into a DTO set - abridged
     * 
     * @param model The model set to convert
     * @return The converted DTO set, empty set if model set is empty or null -
     *         abridged
     */
    Set<T> convertAbridged(Set<M> models);

}
