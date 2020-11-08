package de.qaware.openapigeneratorforspring.model.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ServerVariable
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#serverVariableObject"
 */
@Data
public class ServerVariable implements HasExtensions {
    @JsonProperty("enum")
    private List<String> enumValues;
    @JsonProperty("default")
    private String defaultValue;
    private String description;
    private Map<String, Object> extensions;
}

