package de.calltopower.simpletodo.impl.db.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDUserVerificationTokenModel;

/**
 * User activation token model repository
 */
@Repository
public interface STDUserVerificationTokensRepository extends JpaRepository<STDUserVerificationTokenModel, UUID> {

    Set<STDUserVerificationTokenModel> findAllByUserId(UUID id);

    @Override
    Optional<STDUserVerificationTokenModel> findById(UUID id);

}
