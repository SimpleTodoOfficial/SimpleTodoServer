package de.calltopower.simpletodo.impl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.calltopower.simpletodo.api.dto.STDDto;
import de.calltopower.simpletodo.impl.model.STDTranslationsModel;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for translations
 */
@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class STDTranslationsDto implements STDDto<STDTranslationsModel> {

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String translations;

}
