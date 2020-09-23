package de.calltopower.simpletodo.impl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.calltopower.simpletodo.api.controller.STDController;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;

/**
 * Error controller
 */
@RestController
@RequestMapping(path = STDErrorController.PATH)
public class STDErrorController implements STDController, ErrorController {

    /**
     * The controller path
     */
    public static final String PATH = "";

    /**
     * The controller error path
     */
    public static final String PATH_ERROR = "/error";

    private static final Logger LOGGER = LoggerFactory.getLogger(STDErrorController.class);

    @SuppressWarnings("javadoc")
    @RequestMapping(value = PATH_ERROR)
    public void error() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested error");
        }

        throw new STDNotFoundException("Error");
    }

    @Override
    public String getErrorPath() {
        return PATH_ERROR;
    }

    @Override
    public String getPath() {
        return PATH;
    }

}
