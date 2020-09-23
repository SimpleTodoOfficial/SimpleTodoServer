package de.calltopower.simpletodo.impl.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDUserModel;
import de.calltopower.simpletodo.impl.model.STDWorkspaceModel;

/**
 * Workspace model repository
 */
@Repository
public interface STDWorkspaceRepository extends JpaRepository<STDWorkspaceModel, UUID> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Set<STDWorkspaceModel> findAllByUsers(STDUserModel user);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    List<STDWorkspaceModel> findAll();

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    <S extends STDWorkspaceModel> S saveAndFlush(S entity);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    Optional<STDWorkspaceModel> findById(UUID id);

}
