package de.calltopower.simpletodo.impl.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.db.repository.STDListRepository;
import de.calltopower.simpletodo.impl.db.repository.STDUserRepository;
import de.calltopower.simpletodo.impl.db.repository.STDWorkspaceRepository;
import de.calltopower.simpletodo.impl.exception.STDNotAuthorizedException;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.model.STDUserModel;
import de.calltopower.simpletodo.impl.model.STDWorkspaceModel;
import de.calltopower.simpletodo.impl.requestbody.STDWorkspaceRequestBody;

/**
 * Service for workspace results
 */
@Service
public class STDWorkspaceService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDWorkspaceService.class);

    private STDWorkspaceRepository workspaceRepository;
    private STDAuthService authService;
    private STDUserRepository userRepository;
    private STDListRepository listRepository;

    /**
     * Initializes the service
     * 
     * @param workspaceRepository The workspace DB repository
     * @param userService         The user service
     * @param userRepository      The user DB repository
     * @param listRepository      The list DB repository
     */
    @Autowired
    public STDWorkspaceService(STDWorkspaceRepository workspaceRepository, STDAuthService authService,
            STDUserRepository userRepository, STDListRepository listRepository) {
        this.workspaceRepository = workspaceRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.listRepository = listRepository;
    }

    /**
     * Retrieves all workspaces from DB
     * 
     * @param userDetails The user authentication
     * @return a list of workspaces (empty if none found)
     */
    @Transactional(readOnly = true)
    public Set<STDWorkspaceModel> getAllWorkspacesForUser(UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Searching for all workspaces of user \"%s\"", userDetails.getUsername()));
        }

        STDUserModel user = authService.authenticate(userDetails);

        return workspaceRepository.findAllByUsers(user);
    }

    /**
     * Returns a workspace if the user is a member of the workspace
     * 
     * @param userDetails The user authentication
     * @param strId       The workspace ID
     * @return a workspace
     */
    @Transactional(readOnly = true)
    public STDWorkspaceModel getWorkspace(UserDetails userDetails, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Getting workspace with ID \"%s\"", strId));
        }

        STDUserModel user = authService.authenticate(userDetails);
        STDWorkspaceModel workspace = getWorkspace(strId);
        assertUserInWorkspace(workspace, user);

        return workspace;
    }

    /**
     * Creates and persists a new workspace
     * 
     * @param userDetails The user authentication
     * @param requestBody The request body
     * @return The newly persisted todo
     */
    @Transactional(readOnly = false)
    public STDWorkspaceModel createWorkspace(UserDetails userDetails, STDWorkspaceRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Creating workspace from request body \"%s\"", requestBody));
        }

        STDUserModel user = authService.authenticate(userDetails);

        Set<String> userList = requestBody.getUsers();
        if (userList == null) {
            userList = new HashSet<>();
        }
        // @formatter:off
        Set<STDUserModel> users = userList.stream()
                                            .map(userId -> userRepository.findById(UUID.fromString(userId)).orElse(null))
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toSet());
        // @formatter:on

        users.add(user);

        // @formatter:off
        String jsonData = requestBody.getJsonData();
        if (StringUtils.isBlank(jsonData)) {
            jsonData = "{}";
        }
        STDWorkspaceModel model = STDWorkspaceModel.builder()
                                                    .name(requestBody.getName())
                                                    .users(users)
                                                    .jsonData(jsonData)
                                                   .build();
        // @formatter:on

        model = workspaceRepository.saveAndFlush(model);

        for (STDUserModel u : model.getUsers()) {
            u.getWorkspaces().add(model);
        }

        return model;
    }

    /**
     * Updates and persists a new workspace
     * 
     * @param userDetails The user authentication
     * @param strId       the ID
     * @param requestBody The request body
     * @return The newly persisted todo
     */
    @Transactional(readOnly = false)
    public STDWorkspaceModel updateWorkspace(UserDetails userDetails, String strId,
            STDWorkspaceRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    String.format("Updating workspace with ID \"%s\" from request body \"%s\"", strId, requestBody));
        }

        STDUserModel user = authService.authenticate(userDetails);

        STDWorkspaceModel workspace = getWorkspace(userDetails, strId);

        if (StringUtils.isNotBlank(requestBody.getName())) {
            workspace.setName(requestBody.getName());
        }
        if (StringUtils.isNotBlank(requestBody.getJsonData())) {
            workspace.setJsonData(requestBody.getJsonData());
        }
        Set<String> userList = requestBody.getUsers();
        if (userList != null) {
            // @formatter:off
            Set<STDUserModel> users = userList.stream()
                                                .map(userId -> userRepository.findById(UUID.fromString(userId)).orElse(null))
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toSet());
            // @formatter:on
            if (users.isEmpty()) {
                users.add(user);
            }

            workspace.setUsers(users);
        }

        return workspaceRepository.saveAndFlush(workspace);
    }

    /**
     * Deletes a workspace from DB
     * 
     * @param userDetails The user authentication
     * @param strId       the ID
     */
    @Transactional(readOnly = false)
    public void deleteWorkspace(UserDetails userDetails, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Deleting workspace with ID \"%s\"", strId));
        }

        @SuppressWarnings("unused")
        STDWorkspaceModel workspace = getWorkspace(userDetails, strId);
        if (!workspace.getLists().isEmpty()) {
            try {
                listRepository.deleteAll(workspace.getLists());
            } catch (Exception ex) {
                String errMsg = String.format("Could not delete all list in workspace with ID \"%s\"", strId);
                LOGGER.error(errMsg);
                throw new STDNotFoundException(errMsg);
            }
        }

        try {
            workspaceRepository.deleteById(UUID.fromString(strId));
        } catch (Exception ex) {
            String errMsg = String.format("Could not delete workspace with ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }
    }

    /**
     * Deletes all given workspaces
     * 
     * @param userDetails  The user details
     * @param workspaceIDs The workspace IDs for all the workspaces to be deleted
     */
    @Transactional(readOnly = false)
    public void deleteWorkspaces(UserDetails userDetails, Set<String> workspaceIDs) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Deleting workspaces with IDs \"%s\"", workspaceIDs));
        }

        workspaceIDs.forEach(wsId -> deleteWorkspace(userDetails, wsId));
    }

    private STDWorkspaceModel getWorkspace(String strId) {
        Optional<STDWorkspaceModel> workspaceOptional = workspaceRepository.findById(UUID.fromString(strId));
        if (!workspaceOptional.isPresent()) {
            String errorMsg = String.format("Workspace with ID \"%s\" not found", strId);
            LOGGER.error(errorMsg);
            throw new STDNotFoundException(errorMsg);
        }

        return workspaceOptional.get();
    }

    private void assertUserInWorkspace(STDWorkspaceModel workspace, STDUserModel user) {
        if (!workspace.getUsers().contains(user) && !authService.isAdmin(user)) {
            String errorMsg = String.format(
                    "User with username \"%s\" is not allowed to access workspace with ID \"%s\"", user.getUsername(),
                    workspace.getId());
            LOGGER.error(errorMsg);
            throw new STDNotAuthorizedException(errorMsg);
        }
    }

}
