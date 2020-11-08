package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferencedExamplesConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultContentAnnotationMapper implements ContentAnnotationMapper {

    private final EncodingAnnotationMapper encodingAnnotationMapper;
    private final SchemaAnnotationMapper schemaAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final ExampleObjectAnnotationMapper exampleObjectAnnotationMapper;

    @Override
    public Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations, Class<? extends HasContent> owningType, MapperContext mapperContext) {
        return Arrays.stream(contentAnnotations)
                .flatMap(contentAnnotation -> {
                    MediaType mediaTypeValue = map(contentAnnotation, mapperContext);
                    if (StringUtils.isBlank(contentAnnotation.mediaType())) {
                        // if the mapperContext doesn't have any suggested media types,
                        // the mediaTypeValue is discarded!
                        return mapperContext.getSuggestedMediaTypes(owningType).stream()
                                .map(mediaType -> Pair.of(mediaType, mediaTypeValue));
                    }
                    return Stream.of(Pair.of(contentAnnotation.mediaType(), mediaTypeValue));
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> {
                    if (!Objects.equals(a, b)) {
                        throw new IllegalStateException("Conflicting media type found for " + a + " vs. " + b);
                    }
                    return a;
                }, Content::new));
    }

    @Override
    public MediaType map(io.swagger.v3.oas.annotations.media.Content contentAnnotation, MapperContext mapperContext) {
        MediaType mediaType = new MediaType();
        setExampleOrExamples(mediaType, contentAnnotation.examples(), mapperContext);
        setMapIfNotEmpty(encodingAnnotationMapper.mapArray(contentAnnotation.encoding(), mapperContext), mediaType::setEncoding);
        ReferencedSchemaConsumer referencedSchemaConsumer = mapperContext.getReferenceConsumer(ReferencedSchemaConsumer.class);
        setIfNotEmpty(schemaAnnotationMapper.buildFromAnnotation(contentAnnotation.schema(), referencedSchemaConsumer),
                schema -> referencedSchemaConsumer.maybeAsReference(schema, mediaType::setSchema)
        );
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(contentAnnotation.extensions()), mediaType::setExtensions);
        return mediaType;
    }

    private void setExampleOrExamples(MediaType mediaType, ExampleObject[] exampleObjectAnnotations, MapperContext mapperContext) {
        if (exampleObjectAnnotations.length == 1 && StringUtils.isBlank(exampleObjectAnnotations[0].name())) {
            setIfNotEmpty(exampleObjectAnnotationMapper.map(exampleObjectAnnotations[0]),
                    // one should not set the full example object here, just the value
                    // so no referencing is needed in this case
                    example -> mediaType.setExample(example.getValue())
            );
        } else {
            ReferencedExamplesConsumer referencedExamplesConsumer = mapperContext.getReferenceConsumer(ReferencedExamplesConsumer.class);
            setMapIfNotEmpty(exampleObjectAnnotationMapper.mapArray(exampleObjectAnnotations),
                    examples -> referencedExamplesConsumer.maybeAsReference(examples, mediaType::setExamples)
            );
        }
    }
}
