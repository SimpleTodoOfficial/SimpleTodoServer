package de.calltopower.simpletodo.impl.controller;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.calltopower.simpletodo.api.controller.STDController;
import de.calltopower.simpletodo.impl.dto.STDListDto;
import de.calltopower.simpletodo.impl.dtoservice.STDListDtoService;
import de.calltopower.simpletodo.impl.requestbody.STDListRequestBody;
import de.calltopower.simpletodo.impl.service.STDListService;

/**
 * List controller
 * 
 * Attention: This controller is located under the STDWorkspaceController path!
 */
@RestController
@RequestMapping(path = STDListController.PATH)
public class STDListController implements STDController {

    /**
     * The controller path
     */
    public static final String PATH = STDWorkspaceController.PATH;

    /**
     * The controller path - list
     */
    public static final String PATH_LIST = "/lists";

    private static final Logger LOGGER = LoggerFactory.getLogger(STDListController.class);

    private STDListDtoService listDtoService;
    private STDListService listService;

    /**
     * Initializes the controller
     * 
     * @param listDtoService Injected DTO service
     * @param listService    Injected service
     */
    @Autowired
    public STDListController(STDListDtoService listDtoService, STDListService listService) {
        this.listDtoService = listDtoService;
        this.listService = listService;
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "/{wsId}/" + PATH_LIST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Set<STDListDto> getAllListsForWorkspace(@NotNull @PathVariable(name = "wsId") String wsId,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested lists for workspace");
        }

        return listDtoService.convert(listService.getAllListsForWorkspace(userDetails, wsId));
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "/{wsId}/" + PATH_LIST + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public STDListDto getList(@NotNull @PathVariable(name = "wsId") String wsId,
            @NotNull @PathVariable(name = "id") String id, @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested list");
        }

        return listDtoService.convert(listService.getList(userDetails, wsId, id));
    }

    @SuppressWarnings("javadoc")
    @PostMapping(path = "/{wsId}/" + PATH_LIST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public STDListDto createList(@NotNull @PathVariable(name = "wsId") String wsId,
            @NotNull @RequestBody STDListRequestBody requestBody, @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested creating list");
        }

        return listDtoService.convert(listService.createList(userDetails, wsId, requestBody));
    }

    @SuppressWarnings("javadoc")
    @PutMapping(path = "/{wsId}/" + PATH_LIST + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public STDListDto updateList(@NotNull @PathVariable(name = "wsId") String wsId,
            @NotNull @PathVariable(name = "id") String id, @NotNull @RequestBody STDListRequestBody requestBody,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested updating list");
        }

        return listDtoService.convert(listService.updateList(userDetails, wsId, id, requestBody));
    }

    @SuppressWarnings("javadoc")
    @DeleteMapping(path = "/{wsId}/" + PATH_LIST + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteList(@NotNull @PathVariable(name = "wsId") String wsId,
            @NotNull @PathVariable(name = "id") String id, @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested deleting list");
        }

        listService.deleteList(userDetails, wsId, id);
    }

    @SuppressWarnings("javadoc")
    @DeleteMapping(path = "/{wsId}/" + PATH_LIST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteListsInWorkspace(@NotNull @PathVariable(name = "wsId") String wsId,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested deleting all list in workspace");
        }

        listService.deleteAllListInWorkspace(userDetails, wsId);
    }

    @Override
    public String getPath() {
        return PATH;
    }

}
