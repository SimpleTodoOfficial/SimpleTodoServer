package de.calltopower.simpletodo.impl.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.calltopower.simpletodo.api.dto.STDDto;
import de.calltopower.simpletodo.impl.model.STDLanguagesModel;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for languages
 */
@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class STDLanguagesDto implements STDDto<STDLanguagesModel> {

    @SuppressWarnings("javadoc")
    @JsonProperty
    public Map<String, String> languages;

}
