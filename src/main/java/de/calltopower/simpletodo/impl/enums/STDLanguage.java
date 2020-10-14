package de.calltopower.simpletodo.impl.enums;

import java.util.Arrays;
import java.util.Optional;

import de.calltopower.simpletodo.api.enums.STDEnum;
import lombok.Getter;

/**
 * Enum for the language
 */
public enum STDLanguage implements STDEnum {
    // @formatter:off
    GERMAN("de-DE", "Deutsch"),
    ENGLISH("en-US", "English");
    // @formatter:on

    @Getter
    private String id;

    @Getter
    private String name;

    private STDLanguage(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the enum language
     * 
     * @param id The id as string
     * @return the language
     */
    public static Optional<STDLanguage> get(String id) {
        return Arrays.stream(STDLanguage.values()).filter(r -> r.getId().equalsIgnoreCase(id)).findFirst();
    }

}
