package de.calltopower.simpletodo.impl.service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import de.calltopower.simpletodo.impl.db.repository.STDTodoRepository;
import de.calltopower.simpletodo.impl.exception.STDNotAuthorizedException;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.model.STDListModel;
import de.calltopower.simpletodo.impl.model.STDTodoModel;
import de.calltopower.simpletodo.impl.requestbody.STDTodoMovementRequestBody;
import de.calltopower.simpletodo.impl.requestbody.STDTodoRequestBody;

/**
 * Service for todo results
 */
@Service
public class STDTodoService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDTodoService.class);

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; // "2020-09-02 06:16:50"
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    private STDTodoRepository todoRepository;
    private STDListService listService;
    private STDListRepository listRepository;

    /**
     * Initializes the service
     * 
     * @param todoRepository   The DB repository
     * @param listService      The list service
     * @param listRepository   The list db Repository
     * @param workspaceService The workspace service
     */
    @Autowired
    public STDTodoService(STDTodoRepository todoRepository, STDListService listService,
            STDListRepository listRepository) {
        this.todoRepository = todoRepository;
        this.listService = listService;
        this.listRepository = listRepository;
    }

    /**
     * Retrieves all todos of the list from DB
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param lId         The list ID
     * @return a list of todos (empty if none found)
     */
    @Transactional(readOnly = true)
    public Set<STDTodoModel> getAllTodos(UserDetails userDetails, String wsId, String lId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Searching for all todos in list with ID \"%s\" in workspace with ID \"%s\"",
                    lId, wsId));
        }

        STDListModel list = listService.getList(userDetails, wsId, lId);

        return todoRepository.findAllByList(list);
    }

    /**
     * Returns a todo if the user is a member of the workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param lId         The list ID
     * @param strId       The todo ID
     * @return a todo
     */
    @Transactional(readOnly = true)
    public STDTodoModel getTodo(UserDetails userDetails, String wsId, String lId, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Searching for todo with ID \"%s\"", strId));
        }

        STDListModel list = listService.getList(userDetails, wsId, lId);
        STDTodoModel todo = getTodo(strId);
        assertTodoInList(list, todo);

        return todo;
    }

    /**
     * Creates a todo if the user is a member of the workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param lId         The list ID
     * @param requestBody The request body
     * @return a list
     */
    @Transactional(readOnly = false)
    public STDTodoModel createTodo(UserDetails userDetails, String wsId, String lId, STDTodoRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(
                    "Creating todo in list with ID \"%s\" in workspace with ID \"%s\" from request body \"%s\"", lId,
                    wsId, requestBody));
        }

        STDListModel list = listService.getList(userDetails, wsId, lId);

        if (StringUtils.isNotBlank(requestBody.getDueDate())) {
            try {
                dateTimeFormatter.parse(requestBody.getDueDate());
            } catch (DateTimeParseException ex) {
                String errMsg = String.format("Could not parse date and time from string \"%s\", format is: \"%s\"",
                        requestBody.getDueDate(), DATETIME_FORMAT);
                LOGGER.error(errMsg);
                throw new STDNotFoundException(errMsg);
            }
        }
        String jsonData = requestBody.getJsonData();
        if (StringUtils.isBlank(jsonData)) {
            jsonData = "{}";
        }

        // @formatter:off
        STDTodoModel model = STDTodoModel.builder()
                                            .msg(requestBody.getMsg())
                                            .dateDue(requestBody.getDueDate())
                                            .statusDone(requestBody.isDone())
                                            .jsonData(jsonData)
                                            .list(list)
                                         .build();
        // @formatter:on

        model = todoRepository.saveAndFlush(model);
        list.getTodos().add(model);

        return model;
    }

    /**
     * Updates a todo if the user is a member of the workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param lId         The list ID
     * @param strId       The todo ID
     * @param requestBody The request body
     * @return a list
     */
    @Transactional(readOnly = false)
    public STDTodoModel updateTodo(UserDetails userDetails, String wsId, String lId, String strId,
            STDTodoRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(
                    "Updating todo with ID \"%s\" in list with ID \"%s\" in workspace with ID \"%s\" from request body \"%s\"",
                    strId, lId, wsId, requestBody));
        }

        if (StringUtils.isNotBlank(requestBody.getDueDate())) {
            try {
                dateTimeFormatter.parse(requestBody.getDueDate());
            } catch (DateTimeParseException ex) {
                String errMsg = String.format(
                        "Could not find parse date and time from string \"%s\", format is: \"%s\"",
                        requestBody.getDueDate(), DATETIME_FORMAT);
                LOGGER.error(errMsg);
                throw new STDNotFoundException(errMsg);
            }
        }

        STDListModel list = listService.getList(userDetails, wsId, lId);
        STDTodoModel todo = getTodo(strId);
        assertTodoInList(list, todo);

        if (StringUtils.isNotBlank(requestBody.getMsg())) {
            todo.setMsg(requestBody.getMsg());
        }
        if (StringUtils.isNotBlank(requestBody.getJsonData())) {
            todo.setJsonData(requestBody.getJsonData());
        }
        todo.setStatusDone(requestBody.isDone());
        todo.setDateDue(requestBody.getDueDate());

        return todoRepository.saveAndFlush(todo);
    }

    /**
     * Moves a list to the given workspace
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param lId         The list ID
     * @param strId       The list ID
     * @param requestBody The request body
     * @return a list
     */
    @Transactional(readOnly = false)
    public STDTodoModel moveTodo(UserDetails userDetails, String wsId, String lId, String strId,
            STDTodoMovementRequestBody requestBody) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(
                    "Moving todo with ID \"%s\" in list with ID \"%s\" in workspace with ID \"%s\" from request body \"%s\"",
                    strId, lId, wsId, requestBody));
        }

        STDListModel currentList = listService.getList(userDetails, wsId, lId);
        STDTodoModel todo = getTodo(strId);
        assertTodoInList(currentList, todo);

        if (StringUtils.isBlank(requestBody.getListId())) {
            throw new STDNotFoundException(String.format("List with ID \"%s\" not found", requestBody.getListId()));
        }
        STDListModel newList = listService.getList(userDetails, wsId, requestBody.getListId());

        currentList.getTodos().remove(todo);
        newList.getTodos().add(todo);

        // listRepository.saveAndFlush(currentList);
        // listRepository.saveAndFlush(newList);
        todo.setList(newList);
        return todoRepository.saveAndFlush(todo);
    }

    /**
     * Deletes a todo from DB
     * 
     * @param userDetails The user authentication
     * @param wsId        The workspace ID
     * @param lId         The list ID
     * @param strId       the ID
     */
    @Transactional(readOnly = false)
    public void deleteTodo(UserDetails userDetails, String wsId, String lId, String strId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    String.format("Deleting todo with ID \"%s\" in list with ID \"%s\" in workspace with ID \"%s\"",
                            strId, lId, wsId));
        }

        STDListModel list = listService.getList(userDetails, wsId, lId);
        STDTodoModel todo = getTodo(strId);
        assertTodoInList(list, todo);

        Optional<STDTodoModel> todoInWorkspace = list.getTodos().stream()
                .filter(t -> t.getId().equals(UUID.fromString(strId))).findFirst();
        if (!todoInWorkspace.isPresent()) {
            String errMsg = String.format(
                    "Could not find todo with ID \"%s\" in list with ID \"%s\" in workspace with ID \"%s\"", strId, lId,
                    wsId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }
        try {
            list.getTodos().remove(todoInWorkspace.get());
            listRepository.saveAndFlush(list);
        } catch (Exception ex) {
            String errMsg = String.format("Could not delete todo with ID \"%s\"", strId);
            LOGGER.error(errMsg);
            throw new STDNotFoundException(errMsg);
        }
    }

    private STDTodoModel getTodo(String strId) {
        Optional<STDTodoModel> todoOptional = todoRepository.findById(UUID.fromString(strId));
        if (!todoOptional.isPresent()) {
            String errorMsg = String.format("Todo with ID \"%s\" not found", strId);
            LOGGER.error(errorMsg);
            throw new STDNotFoundException(errorMsg);
        }

        return todoOptional.get();
    }

    private void assertTodoInList(STDListModel list, STDTodoModel todo) {
        if (!list.getTodos().contains(todo)) {
            String errorMsg = String.format("Todo with ID \"%s\" is not contained in list with ID \"%s\"", todo.getId(),
                    list.getId());
            LOGGER.error(errorMsg);
            throw new STDNotAuthorizedException(errorMsg);
        }
    }

}
