package de.calltopower.simpletodo.impl.model;

import java.io.Serializable;

import de.calltopower.simpletodo.api.model.STDModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model implementation for translations
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class STDTranslationsModel implements Serializable, STDModel {

    private static final long serialVersionUID = -7439491350403861301L;

    public String translations;

    @Override
    public String toString() {
        return "STDTranslationsModel";
    }

}
