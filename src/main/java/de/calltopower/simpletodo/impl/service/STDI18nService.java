package de.calltopower.simpletodo.impl.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.enums.STDLanguage;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.model.STDLanguagesModel;
import de.calltopower.simpletodo.impl.model.STDTranslationsModel;
import de.calltopower.simpletodo.impl.utils.STDFileUtils;

/**
 * Service for i18n results
 */
@Service
public class STDI18nService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDI18nService.class);

    private STDFileUtils fileUtils;

    private static final String I18N_FOLDER_NAME = "i18n";

    /**
     * Initializes the service
     * 
     * @param fileUtils The file utilities
     */
    @Autowired
    public STDI18nService(STDFileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    /**
     * Returns all languages
     * 
     * @return all languages
     */
    public STDLanguagesModel getLanguages() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Returning all languages");
        }

        return STDLanguagesModel.builder()
                .languages(
                        Arrays.stream(STDLanguage.values()).collect(Collectors.toMap(l -> l.getId(), l -> l.getName())))
                .build();
    }

    /**
     * Returns a specific language file
     * 
     * @param id Language ID
     * @return a specific language file
     */
    public STDTranslationsModel getLanguageFile(String id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Returning all languages");
        }

        String langFileName = String.format("%s/%s.json", I18N_FOLDER_NAME, id);
        try {
            return STDTranslationsModel.builder().translations(fileUtils.getResourceFileAsString(langFileName)).build();
        } catch (IOException e) {
            throw new STDNotFoundException(String.format("Language file for language with id \"%s\" not found", id));
        }
    }

}
