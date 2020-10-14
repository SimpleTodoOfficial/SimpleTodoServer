package de.calltopower.simpletodo.impl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.calltopower.simpletodo.api.utils.STDUtils;
import de.calltopower.simpletodo.impl.service.STDI18nService;

/**
 * Token utilities
 */
@Component
public class STDFileUtils implements STDUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDFileUtils.class);

    /**
     * Constructor
     */
    @Autowired
    STDFileUtils() {
        // Nothing to see here...
    }

    /**
     * Returns a file from the resource folder
     * 
     * @param fileName The file name
     * @return The file content as string
     * @throws IOException If not found or another error occurred
     */
    public String getResourceFileAsString(String fileName) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Loading file from resources: \"%s\"", fileName));
        }

        InputStream is = getResourceFileAsInputStream(fileName);
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } else {
            throw new IOException("File not found");
        }
    }

    private InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = STDI18nService.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

}
