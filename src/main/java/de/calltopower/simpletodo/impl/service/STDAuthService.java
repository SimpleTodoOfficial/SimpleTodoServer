package de.calltopower.simpletodo.impl.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.db.repository.STDUserRepository;
import de.calltopower.simpletodo.impl.db.repository.STDUserVerificationTokensRepository;
import de.calltopower.simpletodo.impl.enums.STDUserRole;
import de.calltopower.simpletodo.impl.exception.STDFunctionalException;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.exception.STDUserException;
import de.calltopower.simpletodo.impl.model.STDRoleModel;
import de.calltopower.simpletodo.impl.model.STDTokenModel;
import de.calltopower.simpletodo.impl.model.STDUserDetailsImpl;
import de.calltopower.simpletodo.impl.model.STDUserModel;
import de.calltopower.simpletodo.impl.model.STDUserVerificationTokenModel;
import de.calltopower.simpletodo.impl.properties.STDSettingsProperties;
import de.calltopower.simpletodo.impl.requestbody.STDSigninRequestBody;
import de.calltopower.simpletodo.impl.requestbody.STDSignupRequestBody;
import de.calltopower.simpletodo.impl.utils.STDJsonUtils;
import de.calltopower.simpletodo.impl.utils.STDTokenUtils;

/**
 * Service for auth results
 */
@Service
public class STDAuthService implements STDService {

	private static final Logger LOGGER = LoggerFactory.getLogger(STDAuthService.class);

	private AuthenticationManager authenticationManager;
	private PasswordEncoder encoder;
	private STDTokenUtils jwtUtils;
	private STDJsonUtils jsonUtils;
	private STDUserRepository userRepository;
	private STDRoleService roleService;
	private STDSettingsProperties functionalProperties;
	private STDUserVerificationTokensRepository userActivationTokensRepository;
	private STDUserActionService userActionService;

	/**
	 * Initializes the service
	 * 
	 * @param authenticationManager          The authentication manager
	 * @param encoder                        The encoder
	 * @param jwtUtils                       The JWT utilities
	 * @param jsonUtils                      The Json utilities
	 * @param userRepository                 The user repository
	 * @param roleService                    The role service
	 * @param functionalProperties           Functional properties
	 * @param userActivationTokensRepository user activation tokens repository
	 * @param userActionService              The user action service
	 */
	@Autowired
	public STDAuthService(AuthenticationManager authenticationManager, PasswordEncoder encoder, STDTokenUtils jwtUtils,
			STDJsonUtils jsonUtils, STDUserRepository userRepository, STDRoleService roleService,
			STDSettingsProperties functionalProperties,
			STDUserVerificationTokensRepository userActivationTokensRepository,
			STDUserActionService userActionService) {
		this.authenticationManager = authenticationManager;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
		this.jsonUtils = jsonUtils;
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.functionalProperties = functionalProperties;
		this.userActivationTokensRepository = userActivationTokensRepository;
		this.userActionService = userActionService;
	}

	/**
	 * Signs up a user
	 * 
	 * @param requestBody The signup request body
	 * @param userDetails User details. My be empty if registering
	 * @return A user model
	 */
	@Transactional(readOnly = false)
	public STDUserModel signup(STDSignupRequestBody requestBody, UserDetails userDetails) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Requesting signup new user: \"%s\"", requestBody));
		}

		if (userDetails == null && !functionalProperties.signupAllowed()) {
			LOGGER.error("New users are not allowed to register themselves");
			throw new STDFunctionalException("New users are not allowed to register themselves.");
		}

		Boolean existsByUsername = userRepository.existsByUsername(requestBody.getUsername());
		if (existsByUsername != null && existsByUsername.booleanValue()) {
			LOGGER.error("Username is already taken");
			throw new STDUserException("Username is already taken");
		}

		Boolean existsByEmail = userRepository.existsByEmail(requestBody.getEmail());
		if (existsByEmail != null && existsByEmail.booleanValue()) {
			LOGGER.error("Email is already in use");
			throw new STDUserException("Email is already in use");
		}

		String jsonData = jsonUtils.getNonEmptyJson(requestBody.getJsonData());
		// @formatter:off
        STDUserModel user = STDUserModel.builder()
                                            .username(requestBody.getUsername())
                                            .email(requestBody.getEmail())
                                            .password(encoder.encode(requestBody.getPassword()))
                                            .jsonData(jsonData)
                                        .build();
        // @formatter:on

		Set<STDRoleModel> roles = new HashSet<>();
		roles.add(roleService.getStandardUserRole());
		user.setRoles(roles);

		user = userRepository.saveAndFlush(user);

		userActionService.sendEmailAccountCreated(user);
		processUserActivation(user, user.getEmail());

		return user;
	}

	/**
	 * Signs up a user
	 * 
	 * @param requestBody The signin request body
	 * @return A token model
	 */
	@Transactional(readOnly = false)
	public STDTokenModel signin(STDSigninRequestBody requestBody) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Requesting signing in user: \"%s\"", requestBody));
		}

		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestBody.getUsername(), requestBody.getPassword()));
		} catch (Exception ex) {
			LOGGER.error("Could not authenticate");
			throw new STDUserException("Could not authenticate");
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		STDUserDetailsImpl userDetails = (STDUserDetailsImpl) authentication.getPrincipal();

		// @formatter:off
        return STDTokenModel.builder()
                .token(jwt)
                .user(authenticate((userDetails)))
               .build();
        // @formatter:off
    }

    /**
     * Authenticates a user
     * 
     * @param userDetails The user details
     * @return The authenticated user
     */
    @Transactional(readOnly = true)
    public STDUserModel authenticate(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new STDNotFoundException(
                String.format("User with username \"%s\" not found", userDetails.getUsername())));
    }

    /**
     * Returns whether the given user is an admin user
     * 
     * @param user The user to check
     * @return True if the given user is an admin user, false else
     */
    @Transactional(readOnly = true)
    public boolean isAdmin(STDUserModel user) {
        // @formatter:off
        return user.getRoles().stream()
                                .map(r -> r.getName())
                                .anyMatch(r -> r.equals(STDUserRole.ROLE_ADMIN));
        // @formatter:on
	}

	/**
	 * Checks whether the userDetails user is the requested authenticated user with
	 * ID strId
	 * 
	 * @param userDetails The user details
	 * @param strId       The requested ID
	 * @return true if the userDetails user is the requested authenticated user with
	 *         ID strId, false else
	 */
	@Transactional(readOnly = true)
	public boolean isAdminOrRequestedUser(STDUserModel user, String strId) {
		return user != null && (isAdmin(user) || user.getId().equals(UUID.fromString(strId)));
	}

	/**
	 * Checks whether the userDetails user is the requested authenticated user with
	 * ID strId
	 * 
	 * @param userDetails The user details
	 * @param strId       The requested ID
	 * @return true if the userDetails user is the requested authenticated user with
	 *         ID strId, false else
	 */
	@Transactional(readOnly = true)
	public boolean isAdminOrRequestedUser(UserDetails userDetails, String strId) {
		return isAdminOrRequestedUser(authenticate(userDetails), strId);
	}

	protected void processUserActivation(STDUserModel user, String newEmail) {
		LOGGER.debug(String.format("Deleting all old user activation tokens for user with ID \"%s\"", user.getId()));
		userActionService.deleteAllUserActivationTokensForUserId(user.getId());

		STDUserVerificationTokenModel model = STDUserVerificationTokenModel.builder().userId(user.getId()).build();
		model = userActivationTokensRepository.saveAndFlush(model);

		LOGGER.debug(String.format("Saved user activation token with id \"%s\"", model.getId()));

		userActionService.sendEmailVerifyEmailAddress(user, newEmail, model);
	}

}
