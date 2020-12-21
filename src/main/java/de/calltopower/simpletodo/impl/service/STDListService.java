package de.calltopower.simpletodo.impl.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.db.repository.STDListRepository;
import de.calltopower.simpletodo.impl.db.repository.STDWorkspaceRepository;
import de.calltopower.simpletodo.impl.exception.STDNotAuthorizedException;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.model.STDListModel;
import de.calltopower.simpletodo.impl.model.STDUserModel;
import de.calltopower.simpletodo.impl.model.STDWorkspaceModel;
import de.calltopower.simpletodo.impl.requestbody.STDListMovementRequestBody;
import de.calltopower.simpletodo.impl.requestbody.STDListRequestBody;

/**
 * Service for list results
 */
@Service
public class STDListService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDListService.class);

    private STDListRepository listRepository;
    private STDWorkspaceRepository workspaceRepository;
    private STDWorkspaceService workspaceService;
    private STDAuthService authService;

    /**
     * Initializes the service
     * 
     * @param listRepository      The DB repository
     * @param workspaceRepository The workspace DB repository
     * @param workspaceService    The workspace service
     * @param authService         The auth service
     */
    @Autowired
    public STDListService(STDListRepository listRepository, STDWorkspaceRepository workspaceRepository,
            STDWorkspaceService workspaceService, STDAuthService authService) {
        this.listRepository = listRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceService = workspaceService;
        this.authService = authService;
    }

    /**
     * Retrieves all lists of the workspace from DB
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @return a list of lists (empty if none found)
     */
    @Transactional(readOnly = true)
    public Set<STDListModel> getAllListsForWorkspace(UserDetails userDetails, String wsId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Searching for all lists in workspace with ID \"%s\"", wsId));
        }

        STDWorkspaceModel workspace = workspaceService.getWorkspace(userDetails, wsId);

        return listRepository.findAllByWorkspace(workspace);
    }

    /**
     * Returns a list if the user is a member of the workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param strId       The list ID
     * @return a list
     */
    @Transactional(readOnly = true)
    public STDListModel getList(UserDetails userDetails, String wsId, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Getting list with ID \"%s\" in workspace with ID \"%s\"", strId, wsId));
        }

        STDWorkspaceModel workspace = workspaceService.getWorkspace(userDetails, wsId);
        STDListModel list = getList(strId);
        assertListInWorkspace(workspace, list);

        return list;
    }

    /**
     * Creates a list if the user is a member of the workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param requestBody The request body
     * @return a list
     */
    @Transactional(readOnly = false)
    public STDListModel createList(UserDetails userDetails, String wsId, STDListRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Creating list in workspace with ID \"%s\" from request body \"%s\"", wsId,
                    requestBody));
        }

        STDWorkspaceModel workspace = workspaceService.getWorkspace(userDetails, wsId);

        String jsonData = requestBody.getJsonData();
        if (StringUtils.isBlank(jsonData)) {
            jsonData = "{}";
        }
        // @formatter:off
        STDListModel model = STDListModel.builder()
                                            .name(requestBody.getName())
                                            .workspace(workspace)
                                            .jsonData(jsonData)
                                         .build();
        // @formatter:on

        model = listRepository.saveAndFlush(model);
        workspace.getLists().add(model);

        return model;
    }

    /**
     * Updates a list if the user is a member of the workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param strId       The list ID
     * @param requestBody The request body
     * @return a list
     */
    @Transactional(readOnly = false)
    public STDListModel updateList(UserDetails userDetails, String wsId, String strId, STDListRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    String.format("Updating list with ID \"%s\" in workspace with ID \"%s\" from request body \"%s\"",
                            strId, wsId, requestBody));
        }

        STDWorkspaceModel workspace = workspaceService.getWorkspace(userDetails, wsId);
        STDListModel list = getList(strId);
        assertListInWorkspace(workspace, list);

        if (StringUtils.isNotBlank(requestBody.getName())) {
            list.setName(requestBody.getName());
        }
        if (StringUtils.isNotBlank(requestBody.getJsonData())) {
            list.setJsonData(requestBody.getJsonData());
        }

        return listRepository.saveAndFlush(list);
    }

    /**
     * Moves a list to the given workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param strId       The list ID
     * @param requestBody The request body
     * @return a list
     */
    @Transactional(readOnly = false)
    public STDListModel moveList(UserDetails userDetails, String wsId, String strId,
            STDListMovementRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    String.format("Moving list with ID \"%s\" in workspace with ID \"%s\" from request body \"%s\"",
                            strId, wsId, requestBody));
        }

        STDWorkspaceModel currentWorkspace = workspaceService.getWorkspace(userDetails, wsId);
        STDListModel list = getList(strId);
        assertListInWorkspace(currentWorkspace, list);

        if (StringUtils.isBlank(requestBody.getWorkspaceId())) {
            throw new STDNotFoundException(
                    String.format("Workspace with ID \"%s\" not found", requestBody.getWorkspaceId()));
        }
        STDWorkspaceModel newWorkspace = workspaceService.getWorkspace(userDetails, requestBody.getWorkspaceId());
        STDUserModel user = authService.authenticate(userDetails);
        assertUserInWorkspace(newWorkspace, user);

        currentWorkspace.getLists().remove(list);
        newWorkspace.getLists().add(list);

        list.setWorkspace(newWorkspace);
        return listRepository.saveAndFlush(list);
    }

    /**
     * Deletes a list from DB
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param strId       the ID
     */
    @Transactional(readOnly = false)
    public void deleteList(UserDetails userDetails, String wsId, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Deleting list with ID \"%s\" in workspace with ID \"%s\"", strId, wsId));
        }

        STDWorkspaceModel workspace = workspaceService.getWorkspace(userDetails, wsId);
        STDListModel list = getList(strId);
        assertListInWorkspace(workspace, list);

        Optional<STDListModel> listInWorkspace = workspace.getLists().stream()
                .filter(l -> l.getId().equals(UUID.fromString(strId))).findFirst();
        if (!listInWorkspace.isPresent()) {
            String errMsg = String.format("Could not find list with ID \"%s\" in workspace with ID \"%s\"", strId,
                    wsId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }
        try {
            workspace.getLists().remove(listInWorkspace.get());
            workspaceRepository.saveAndFlush(workspace);
        } catch (Exception ex) {
            String errMsg = String.format("Could not delete list with ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }
    }

    private STDListModel getList(String strId) {
        Optional<STDListModel> listOptional = listRepository.findById(UUID.fromString(strId));
        if (!listOptional.isPresent()) {
            String errorMsg = String.format("List with ID \"%s\" not found", strId);
            LOGGER.error(errorMsg);
            throw new STDNotFoundException(errorMsg);
        }

        return listOptional.get();
    }

    private void assertListInWorkspace(STDWorkspaceModel workspace, STDListModel list) {
        if (!workspace.getLists().contains(list)) {
            String errorMsg = String.format("List with ID \"%s\" is not contained in workspace with ID \"%s\"",
                    list.getId(), workspace.getId());
            LOGGER.error(errorMsg);
            throw new STDNotAuthorizedException(errorMsg);
        }
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
