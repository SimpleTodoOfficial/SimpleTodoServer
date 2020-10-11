package de.calltopower.simpletodo.impl.db.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDUserForgotPasswordTokenModel;

/**
 * User forgot password token model repository
 */
@Repository
public interface STDUserForgotPasswordTokensRepository extends JpaRepository<STDUserForgotPasswordTokenModel, UUID> {

    Set<STDUserForgotPasswordTokenModel> findAllByUserId(UUID id);

    @Override
    Optional<STDUserForgotPasswordTokenModel> findById(UUID id);

}
