package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.schema.NestedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.SchemaAnnotationMapper;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultContentAnnotationMapper implements ContentAnnotationMapper {

    private final EncodingAnnotationMapper encodingAnnotationMapper;
    private final SchemaAnnotationMapper schemaAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final ExampleObjectAnnotationMapper exampleObjectAnnotationMapper;

    public Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations, NestedSchemaConsumer nestedSchemaConsumer) {
        return buildMapFromArray(
                contentAnnotations,
                io.swagger.v3.oas.annotations.media.Content::mediaType,
                annotation -> map(annotation, nestedSchemaConsumer),
                Content::new
        );
    }

    @Override
    public MediaType map(io.swagger.v3.oas.annotations.media.Content contentAnnotation, NestedSchemaConsumer nestedSchemaConsumer) {
        MediaType mediaType = new MediaType();
        setExampleOrExamples(mediaType, contentAnnotation.examples());
        setMapIfNotEmpty(encodingAnnotationMapper.mapArray(contentAnnotation.encoding(), nestedSchemaConsumer), mediaType::setEncoding);
        mediaType.setSchema(schemaAnnotationMapper.mapFromAnnotation(contentAnnotation.schema(), nestedSchemaConsumer));
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(contentAnnotation.extensions()), mediaType::setExtensions);
        return mediaType;
    }

    private void setExampleOrExamples(MediaType mediaType, ExampleObject[] exampleObjectAnnotations) {
        if (exampleObjectAnnotations.length == 1 && StringUtils.isBlank(exampleObjectAnnotations[0].name())) {
            mediaType.setExample(exampleObjectAnnotationMapper.map(exampleObjectAnnotations[0]));
        } else {
            setMapIfNotEmpty(exampleObjectAnnotationMapper.mapArray(exampleObjectAnnotations), mediaType::setExamples);
        }
    }
}
