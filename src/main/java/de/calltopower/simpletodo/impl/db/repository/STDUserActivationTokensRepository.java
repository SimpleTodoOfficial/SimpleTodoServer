package de.calltopower.simpletodo.impl.db.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDUserActivationTokenModel;

/**
 * User activation token model repository
 */
@Repository
public interface STDUserActivationTokensRepository extends JpaRepository<STDUserActivationTokenModel, UUID> {

    Set<STDUserActivationTokenModel> findAllByUserId(UUID id);

    @Override
    Optional<STDUserActivationTokenModel> findById(UUID id);

}
