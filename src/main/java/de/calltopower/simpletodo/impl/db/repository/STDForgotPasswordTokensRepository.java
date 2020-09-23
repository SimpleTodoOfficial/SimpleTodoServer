package de.calltopower.simpletodo.impl.db.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.calltopower.simpletodo.impl.model.STDForgotPasswordTokenModel;

/**
 * Forgot password token model repository
 */
@Repository
public interface STDForgotPasswordTokensRepository extends JpaRepository<STDForgotPasswordTokenModel, UUID> {

    Set<STDForgotPasswordTokenModel> findAllByUserId(UUID id);

    @Override
    Optional<STDForgotPasswordTokenModel> findById(UUID id);

}
