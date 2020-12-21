package de.calltopower.simpletodo.impl.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import de.calltopower.simpletodo.api.utils.STDUtils;
import lombok.NoArgsConstructor;

/**
 * Json utilities
 */
@Component
@NoArgsConstructor
public class STDJsonUtils implements STDUtils {

    public static final String JSON_STR_DEFAULT_EMPTY = "{}";

    /**
     * Returns a valid empty Json string if given string is empty, returns the given
     * string else
     * 
     * @param str The string
     * @return valid empty Json string if given string is empty, the given string
     *         else
     */
    public String getNonEmptyJson(String str) {
        return StringUtils.isNotBlank(str) ? str : JSON_STR_DEFAULT_EMPTY;
    }

}
