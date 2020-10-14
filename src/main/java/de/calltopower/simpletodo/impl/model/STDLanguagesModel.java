package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;
import java.util.Map;

import de.calltopower.simpletodo.api.model.STDModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model implementation for languages
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class STDLanguagesModel implements Serializable, STDModel {

    private static final long serialVersionUID = 939526081797608869L;

    public Map<String, String> languages;

    @Override
    public String toString() {
        // @formatter:off
        return String.format(
                "STDLanguagesModel["
                    + "size='%d'"
                + "]",
                languages.size()
               );
        // @formatter:on
    }

}
