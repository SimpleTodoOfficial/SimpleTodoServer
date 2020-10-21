package de.calltopower.simpletodo.impl.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import de.calltopower.simpletodo.api.task.STDSchedulingTask;

/**
 * A monitoring background task
 */
@Configuration
public class STDTodoDueDateTask implements STDSchedulingTask {

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(STDTodoDueDateTask.class);

    // @formatter:off
    /*
    private final long INITIAL_DELAY_UPDATE_MILLIS = 10000;
    private final long FIXED_RATE_UPDATE_MILLIS = 10 * 1000; // TODO: 60 * 5 * 1000;
    private STDTodoRepository todoRepository;
    private STDDueTodosService dueTodosService;
    */
    // @formatter:on

    /**
     * Initializes the configuration. Loads the environments and caches the names.
     * 
     * @param todoRepository  Todo repository
     * @param dueTodosService Due Todos service
     */
    @Autowired
    public STDTodoDueDateTask(/* STDTodoRepository todoRepository, STDDueTodosService dueTodosService */) {
        // @formatter:off
        /*
        this.todoRepository = todoRepository;
        this.dueTodosService = dueTodosService;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Starting Todo due date task");
        }
        */
        // @formatter:on
    }

    /**
     * Updates the current todos periodically.
     */
    // @formatter:off
    /*
    @Scheduled(initialDelay = INITIAL_DELAY_UPDATE_MILLIS, fixedRate = FIXED_RATE_UPDATE_MILLIS)
    public void updateCurrentTodos() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Updating current todos");
        }

        Date dateFrom = new Date(System.currentTimeMillis() - 5 * 3600 * 1000); // - 5 * 60min
        Date dateTo = new Date(System.currentTimeMillis() + 5 * 3600 * 1000); // + 5 * 60min
        dueTodosService.set(todoRepository.findAllWithDueDateBetween(dateFrom, dateTo));
    }
    */
    // @formatter:on

}
