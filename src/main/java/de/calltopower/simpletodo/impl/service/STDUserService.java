package de.calltopower.simpletodo.impl.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.db.repository.STDUserForgotPasswordTokensRepository;
import de.calltopower.simpletodo.impl.db.repository.STDUserRepository;
import de.calltopower.simpletodo.impl.db.repository.STDUserVerificationTokensRepository;
import de.calltopower.simpletodo.impl.exception.STDFunctionalException;
import de.calltopower.simpletodo.impl.exception.STDGeneralException;
import de.calltopower.simpletodo.impl.exception.STDNotAuthorizedException;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.exception.STDUserException;
import de.calltopower.simpletodo.impl.model.STDRoleModel;
import de.calltopower.simpletodo.impl.model.STDUserForgotPasswordTokenModel;
import de.calltopower.simpletodo.impl.model.STDUserModel;
import de.calltopower.simpletodo.impl.model.STDUserVerificationTokenModel;
import de.calltopower.simpletodo.impl.requestbody.STDForgotPasswordRequestBody;
import de.calltopower.simpletodo.impl.requestbody.STDUserRequestBody;

/**
 * Service for user results
 */
@Service
public class STDUserService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDUserService.class);

    private STDUserRepository userRepository;
    private STDUserForgotPasswordTokensRepository userForgotPasswordTokensRepository;
    private STDUserVerificationTokensRepository userActivationTokensRepository;
    private STDRoleService roleService;
    private STDAuthService authService;
    private STDWorkspaceService workspaceService;
    private STDEmailService emailService;
    private PasswordEncoder encoder;

    /**
     * Initializes the service
     * 
     * @param userRepository   The DB repository
     * @param roleService      The role service
     * @param authService      The authentication service
     * @param workspaceService The workspace service
     * @param emailService     The email service
     * @param encoder          The encoder
     */
    @Autowired
    public STDUserService(STDUserRepository userRepository,
            STDUserForgotPasswordTokensRepository userForgotPasswordTokensRepository,
            STDUserVerificationTokensRepository userActivationTokensRepository, STDRoleService roleService,
            STDAuthService authService, STDWorkspaceService workspaceService, STDEmailService emailService,
            PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userForgotPasswordTokensRepository = userForgotPasswordTokensRepository;
        this.userActivationTokensRepository = userActivationTokensRepository;
        this.roleService = roleService;
        this.authService = authService;
        this.workspaceService = workspaceService;
        this.emailService = emailService;
        this.encoder = encoder;
    }

    /**
     * Retrieves all users from DB
     * 
     * @param userDetails The user authentication
     * @return a list of users (empty if none found)
     */
    @Transactional(readOnly = true)
    public Set<STDUserModel> getAllUsers(UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Searching for all users"));
        }

        @SuppressWarnings("unused")
        STDUserModel user = authService.authenticate(userDetails);

        return userRepository.findAll().stream().collect(Collectors.toSet());
    }

    /**
     * Returns a user
     * 
     * @param userDetails The user authentication
     * @param strId       The user ID
     * @return a user
     */
    @Transactional(readOnly = true)
    public STDUserModel getUser(UserDetails userDetails, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Getting user with ID \"%s\"", strId));
        }

        @SuppressWarnings("unused")
        STDUserModel user = authService.authenticate(userDetails);

        return getUser(strId);
    }

    /**
     * Updates a user
     * 
     * @param userDetails The user authentication
     * @param strId       The user ID
     * @param requestBody The request Body
     * @return the updated user
     */
    @Transactional(readOnly = false)
    public STDUserModel updateUser(UserDetails userDetails, String strId, STDUserRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Updating user with ID \"%s\"", strId));
        }

        STDUserModel authenticatedUser = authService.authenticate(userDetails);

        STDUserModel user = getUser(strId);

        if (authService.isAdminOrRequestedUser(authenticatedUser, strId)) {
            if (StringUtils.isNotBlank(requestBody.getUsername())) {
                user.setUsername(requestBody.getUsername());
            }
            if (StringUtils.isNotBlank(requestBody.getEmail())) {
                if (!user.getEmail().equals(requestBody.getEmail())) {
                    user.setStatusVerified(false);
                    processUserActivation(user, requestBody.getEmail());
                }
                user.setEmail(requestBody.getEmail());
            }
            if (StringUtils.isNotBlank(requestBody.getPassword())) {
                user.setPassword(encoder.encode(requestBody.getPassword()));
            }
            if (StringUtils.isNotBlank(requestBody.getJsonData())) {
                user.setJsonData(requestBody.getJsonData());
            }
            if ((requestBody.getRoles() != null) && authService.isAdmin(authenticatedUser)) {
                Set<STDRoleModel> roles = roleService.convertRoles(requestBody.getRoles());
                STDRoleModel standardUserRole = roleService.getStandardUserRole();
                if (!roles.contains(standardUserRole)) {
                    roles.add(standardUserRole);
                }
                // Make sure one admin exists at any time
                if (!atLeastOneAdminExists(user)) {
                    roles.add(roleService.getAdminUserRole());
                }
                user.setRoles(roles);
            }
        }

        return userRepository.saveAndFlush(user);
    }

    /**
     * Deletes a user from DB
     * 
     * @param userDetails The user authentication
     * @param strId       the ID
     */
    @Transactional(readOnly = false)
    public void deleteUser(UserDetails userDetails, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Deleting user with ID \"%s\"", strId));
        }

        STDUserModel authenticatedUser = authService.authenticate(userDetails);

        if (authService.isAdminOrRequestedUser(authenticatedUser, strId)) {
            try {
                // Delete all workspaces where this user is the alone user
                STDUserModel user = getUser(strId);
                // Make sure one admin exists at any time
                if (!atLeastOneAdminExists(user)) {
                    throw new STDFunctionalException("There has to be at least one admin.");
                }
                // @formatter:off
                Set<String> workspaceIDs = user.getWorkspaces().stream()
                                    .filter(w -> w.getUsers().size() == 1)
                                    .map(w -> w.getId().toString())
                                    .collect(Collectors.toSet());
                // @formatter:on
                workspaceService.deleteWorkspaces(userDetails, workspaceIDs);
                userRepository.deleteById(UUID.fromString(strId));
                deleteAllUserForgotPasswordTokensForUserId(user.getId());
                deleteAllUserActivationTokensForUserId(user.getId());
            } catch (Exception ex) {
                String errMsg = String.format("Could not delete user with ID \"%s\"", strId);
                LOGGER.error(errMsg);
                throw new STDNotFoundException(errMsg);
            }
        } else {
            String errMsg = String.format("User with ID \"%s\" is not allowed to delete user with ID \"%s\"",
                    authenticatedUser.getId(), strId);
            LOGGER.error(errMsg);
            throw new STDNotAuthorizedException(errMsg);
        }
    }

    @Transactional(readOnly = false)
    public void forgotPassword(STDForgotPasswordRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Processing forgot password for \"%s\"", requestBody.toString()));
        }

        if (requestBody.getUsername() == null || requestBody.getEmail() == null) {
            String errMsg = String.format("Username and email must be provided");
            LOGGER.error(errMsg);
            throw new STDUserException(errMsg);
        }

        Optional<STDUserModel> userOptional = userRepository.findByUsername(requestBody.getUsername());
        if (!userOptional.isPresent()) {
            String errMsg = String.format("User with username \"%s\" not found", requestBody.getUsername());
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }

        STDUserModel foundUser = userOptional.get();
        if (!foundUser.getEmail().equalsIgnoreCase(requestBody.getEmail())) {
            String errMsg = String.format("User with username \"%s\" not associated with email \"%s\"",
                    requestBody.getUsername(), requestBody.getEmail());
            LOGGER.error(errMsg);
            throw new STDUserException(errMsg);
        }

        LOGGER.debug(
                String.format("Deleting all old forgot password tokens for user with ID \"%s\"", foundUser.getId()));
        deleteAllUserForgotPasswordTokensForUserId(foundUser.getId());

        STDUserForgotPasswordTokenModel model = STDUserForgotPasswordTokenModel.builder().userId(foundUser.getId())
                .build();
        model = userForgotPasswordTokensRepository.saveAndFlush(model);

        LOGGER.debug(String.format("Saved forgot password token with id \"%s\"", model.getId()));

        sendEmailForgotPassword(foundUser, model);
    }

    @Transactional(readOnly = false)
    public void resetPassword(String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Processing reset password for \"%s\"", strId));
        }

        STDUserForgotPasswordTokenModel token = getForgotPasswordToken(strId);

        STDUserModel user = getUser(token.getUserId());
        String newPassword = String.format("std_pw_%s", UUID.randomUUID().toString());
        user.setPassword(encoder.encode(newPassword));
        // JSON validation workaround
        if (StringUtils.isBlank(user.getJsonData())) {
            user.setJsonData("{}");
        }

        userRepository.saveAndFlush(user);

        LOGGER.debug(String.format("Deleting all old forgot password tokens for user with ID \"%s\"", user.getId()));
        deleteAllUserForgotPasswordTokensForUserId(user.getId());

        sendEmailNewPasswordGenerated(user, newPassword);
    }

    /**
     * Resends the activation
     * 
     * @param userDetails The user authentication
     */
    @Transactional(readOnly = false)
    public void resendVerification(UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Resending verification"));
        }

        @SuppressWarnings("unused")
        STDUserModel user = authService.authenticate(userDetails);

        processUserActivation(user, user.getEmail());
    }

    @Transactional(readOnly = false)
    public void verify(String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Processing verification for \"%s\"", strId));
        }

        STDUserVerificationTokenModel token = getUserActivationToken(strId);

        STDUserModel user = getUser(token.getUserId());
        user.setStatusVerified(true);
        // JSON validation workaround
        if (StringUtils.isBlank(user.getJsonData())) {
            user.setJsonData("{}");
        }

        userRepository.saveAndFlush(user);

        LOGGER.debug(String.format("Deleting all old user activation tokens for user with ID \"%s\"", user.getId()));
        deleteAllUserActivationTokensForUserId(user.getId());

        sendEmailEmailAddressVerified(user);
    }

    private STDUserModel getUser(String strId) {
        return getUser(UUID.fromString(strId));
    }

    private STDUserModel getUser(UUID strId) {
        Optional<STDUserModel> userOptional = userRepository.findById(strId);
        if (!userOptional.isPresent()) {
            String errMsg = String.format("Could not find user with ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }

        return userOptional.get();
    }

    private void sendEmailForgotPassword(STDUserModel user, STDUserForgotPasswordTokenModel model) {
        LOGGER.debug("Sending forgot password email");
        try {
            emailService.sendPasswordForgotEmail(user.getEmail(), model);
        } catch (Exception ex) {
            LOGGER.error("Something went wrong with the email service: ", ex);
        }
    }

    private void sendEmailNewPasswordGenerated(STDUserModel user, String newPassword) {
        LOGGER.debug("Sending new password generated email");
        try {
            emailService.sendNewPasswordGeneratedEmail(user.getEmail(), newPassword);
        } catch (Exception ex) {
            LOGGER.error("Something went wrong with the email service: ", ex);
        }
    }

    // TODO: Duplicate in STDAuthService::sendEmailVerifyEmailAddress
    private void sendEmailVerifyEmailAddress(STDUserModel user, String newEmail, STDUserVerificationTokenModel model) {
        LOGGER.debug("Sending verify email address email");
        try {
            emailService.sendVerifyEmailAddressEmail(newEmail, user, model);
        } catch (Exception ex) {
            LOGGER.error("Something went wrong with the email service: ", ex);
        }
    }

    private void sendEmailEmailAddressVerified(STDUserModel user) {
        LOGGER.debug("Sending email address verified email");
        try {
            emailService.sendEmailAddressverifiedEmail(user.getEmail());
        } catch (Exception ex) {
            LOGGER.error("Something went wrong with the email service: ", ex);
        }
    }

    private STDUserForgotPasswordTokenModel getForgotPasswordToken(String strId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(strId);
        } catch (Exception ex) {
            String errMsg = String.format("Could not process ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }

        Optional<STDUserForgotPasswordTokenModel> tokenOptional = userForgotPasswordTokensRepository.findById(uuid);
        if (!tokenOptional.isPresent()) {
            String errMsg = String.format("Could not find forgot password token with ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }

        return tokenOptional.get();
    }

    // TODO: Duplicate in STDAuthService::processUserActivation
    private void processUserActivation(STDUserModel user, String newEmail) {
        LOGGER.debug(String.format("Deleting all old user activation tokens for user with ID \"%s\"", user.getId()));
        deleteAllUserActivationTokensForUserId(user.getId());

        STDUserVerificationTokenModel model = STDUserVerificationTokenModel.builder().userId(user.getId()).build();
        model = userActivationTokensRepository.saveAndFlush(model);

        LOGGER.debug(String.format("Saved user activation token with id \"%s\"", model.getId()));

        sendEmailVerifyEmailAddress(user, newEmail, model);
    }

    private STDUserVerificationTokenModel getUserActivationToken(String strId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(strId);
        } catch (Exception ex) {
            String errMsg = String.format("Could not process ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }

        Optional<STDUserVerificationTokenModel> tokenOptional = userActivationTokensRepository.findById(uuid);
        if (!tokenOptional.isPresent()) {
            String errMsg = String.format("Could not find user activation token with ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }

        return tokenOptional.get();
    }

    private void deleteAllUserForgotPasswordTokensForUserId(UUID userId) {
        try {
            for (STDUserForgotPasswordTokenModel token : userForgotPasswordTokensRepository.findAllByUserId(userId)) {
                userForgotPasswordTokensRepository.deleteById(token.getId());
            }
        } catch (Exception ex) {
            String errMsg = String.format(
                    "Something went wrong deleting all forgot password tokens for user with username \"%s\"", userId);
            LOGGER.error(errMsg);
            throw new STDGeneralException(errMsg);
        }
    }

    // TODO: Duplicate in STDAuthService::deleteAllUserActivationTokensForUserId
    private void deleteAllUserActivationTokensForUserId(UUID userId) {
        try {
            for (STDUserVerificationTokenModel token : userActivationTokensRepository.findAllByUserId(userId)) {
                userActivationTokensRepository.deleteById(token.getId());
            }
        } catch (Exception ex) {
            String errMsg = String.format(
                    "Something went wrong deleting all user activation tokens for user with username \"%s\"", userId);
            LOGGER.error(errMsg);
            throw new STDGeneralException(errMsg);
        }
    }

    private boolean atLeastOneAdminExists(STDUserModel excludeUserInSearch) {
        // @formatter:off
        long nrOfAdmins = userRepository.findAll().stream()
                                                    .filter(u -> !u.getId().equals(excludeUserInSearch.getId()))
                                                    .filter(u -> u.getRoles().contains(roleService.getAdminUserRole()))
                                                    .count();
        // @formatter:on
        return nrOfAdmins > 0;
    }

}
