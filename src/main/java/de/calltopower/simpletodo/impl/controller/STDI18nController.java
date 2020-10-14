package de.calltopower.simpletodo.impl.controller;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.calltopower.simpletodo.api.controller.STDController;
import de.calltopower.simpletodo.impl.service.STDI18nService;

/**
 * I18n controller
 */
@RestController
@RequestMapping(path = STDI18nController.PATH)
public class STDI18nController implements STDController {

    /**
     * The controller path
     */
    public static final String PATH = APIPATH + "/i18n";

    private static final Logger LOGGER = LoggerFactory.getLogger(STDI18nController.class);

    private STDI18nService i18nService;

    /**
     * Initializes the controller
     * 
     * @param userDtoService Injected DTO service
     */
    @Autowired
    public STDI18nController(STDI18nService i18nService) {
        this.i18nService = i18nService;
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "/languages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getLanguages() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested languages");
        }

        return i18nService.getLanguages();
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "/languages/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getLanguageFile(@NotNull @PathVariable(name = "id") String id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested language");
        }

        return i18nService.getLanguageFile(id);
    }

    @Override
    public String getPath() {
        return PATH;
    }

}
