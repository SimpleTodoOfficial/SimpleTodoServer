package de.calltopower.simpletodo.impl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.calltopower.simpletodo.api.controller.STDController;
import de.calltopower.simpletodo.impl.dto.STDLanguagesDto;
import de.calltopower.simpletodo.impl.dto.STDTranslationsDto;
import de.calltopower.simpletodo.impl.dtoservice.STDLanguagesDtoService;
import de.calltopower.simpletodo.impl.dtoservice.STDTranslationsDtoService;
import de.calltopower.simpletodo.impl.service.STDI18nService;
import jakarta.validation.constraints.NotNull;

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
    private STDLanguagesDtoService languagesDtoService;
    private STDTranslationsDtoService translationsDtoService;

    /**
     * Initializes the controller
     * 
     * @param userDtoService         Injected service
     * @param languagesDtoService    Injected languages DTO service
     * @param translationsDtoService Injected translations DTO service
     */
    @Autowired
    public STDI18nController(STDI18nService i18nService, STDLanguagesDtoService languagesDtoService,
            STDTranslationsDtoService translationsDtoService) {
        this.i18nService = i18nService;
        this.languagesDtoService = languagesDtoService;
        this.translationsDtoService = translationsDtoService;
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "/languages", produces = MediaType.APPLICATION_JSON_VALUE)
    public STDLanguagesDto getLanguages() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested languages");
        }

        return languagesDtoService.convert(i18nService.getLanguages());
    }

    @SuppressWarnings("javadoc")
    @GetMapping(path = "/languages/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public STDTranslationsDto getTranslations(@NotNull @PathVariable(name = "id") String id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested language");
        }

        return translationsDtoService.convert(i18nService.getLanguageFile(id));
    }

    @Override
    public String getPath() {
        return PATH;
    }

}
