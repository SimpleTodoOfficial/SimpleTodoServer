package de.calltopower.simpletodo.impl.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDUserModel;

/**
 * User model repository
 */
@Repository
public interface STDUserRepository extends JpaRepository<STDUserModel, UUID> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Optional<STDUserModel> findByUsername(String username);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Boolean existsByUsername(String username);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Boolean existsByEmail(String email);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    List<STDUserModel> findAll();

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    <S extends STDUserModel> S saveAndFlush(S entity);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Override
    Optional<STDUserModel> findById(UUID id);

}
