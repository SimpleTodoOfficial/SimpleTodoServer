package de.calltopower.simpletodo.impl.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.enums.STDUserRole;
import de.calltopower.simpletodo.impl.model.STDRoleModel;
import jakarta.persistence.QueryHint;

/**
 * Role model repository
 */
@Repository
public interface STDUserRoleRepository extends JpaRepository<STDRoleModel, UUID> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Optional<STDRoleModel> findByName(STDUserRole name);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    <S extends STDRoleModel> S saveAndFlush(S entity);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    List<STDRoleModel> findAll();

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    Optional<STDRoleModel> findById(UUID id);

}
