package de.calltopower.simpletodo.impl.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDListModel;
import de.calltopower.simpletodo.impl.model.STDTodoModel;

/**
 * Todo model repository
 */
@Repository
public interface STDTodoRepository extends JpaRepository<STDTodoModel, UUID> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Set<STDTodoModel> findAllByList(STDListModel list);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    List<STDTodoModel> findAll();

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    <S extends STDTodoModel> S saveAndFlush(S entity);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    Optional<STDTodoModel> findById(UUID id);

}
