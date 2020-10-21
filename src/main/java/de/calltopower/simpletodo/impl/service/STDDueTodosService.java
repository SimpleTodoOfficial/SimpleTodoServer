package de.calltopower.simpletodo.impl.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.model.STDListModel;
import de.calltopower.simpletodo.impl.model.STDTodoModel;
import de.calltopower.simpletodo.impl.model.STDUserModel;

/**
 * Service for due todos
 */
@Service
public class STDDueTodosService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDDueTodosService.class);

    private Set<STDTodoModel> dueTodos = new HashSet<>();

    /**
     * Initializes the service
     */
    @Autowired
    public STDDueTodosService() {
        // Nothing to see here...
    }

    public void set(Set<STDTodoModel> dueTodos) {
        this.dueTodos = dueTodos;
    }

    public Set<STDTodoModel> getFor(STDUserModel user) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Getting due todos for user %s", user.getId().toString()));
        }

        // @formatter:off
        Set<STDListModel> lists = user.getWorkspaces().stream()
                                                        .map(w -> w.getLists())
                                                        .flatMap(Collection::stream)
                                                        .collect(Collectors.toSet());
        // @formatter:on
        // @formatter:off
        return dueTodos.stream()
                        .filter(t -> lists.contains(t.getList()))
                        .collect(Collectors.toSet());
        // @formatter:on
    }

}
