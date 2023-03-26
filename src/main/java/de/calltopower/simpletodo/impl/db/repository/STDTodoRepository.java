package de.calltopower.simpletodo.impl.db.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDListModel;
import de.calltopower.simpletodo.impl.model.STDTodoModel;
import jakarta.persistence.QueryHint;

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

    @Query("SELECT t FROM STDTodoModel t WHERE t.dueDate <= :dateTo")
    Set<STDTodoModel> findAllWithDueDateBefore(@Param("dateTo") Date dateTo);

}
