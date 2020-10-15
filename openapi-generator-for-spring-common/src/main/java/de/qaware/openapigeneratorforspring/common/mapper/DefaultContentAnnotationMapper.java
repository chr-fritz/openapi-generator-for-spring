package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferencedExamplesConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultContentAnnotationMapper implements ContentAnnotationMapper {

    private final EncodingAnnotationMapper encodingAnnotationMapper;
    private final SchemaAnnotationMapper schemaAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final ExampleObjectAnnotationMapper exampleObjectAnnotationMapper;

    @Override
    public Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return buildStringMapFromStream(
                Arrays.stream(contentAnnotations),
                io.swagger.v3.oas.annotations.media.Content::mediaType,
                annotation -> map(annotation, referencedItemConsumerSupplier),
                Content::new
        );
    }

    @Override
    public MediaType map(io.swagger.v3.oas.annotations.media.Content contentAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        MediaType mediaType = new MediaType();
        setExampleOrExamples(mediaType, contentAnnotation.examples(), referencedItemConsumerSupplier);
        setMapIfNotEmpty(encodingAnnotationMapper.mapArray(contentAnnotation.encoding(), referencedItemConsumerSupplier), mediaType::setEncoding);
        ReferencedSchemaConsumer referencedSchemaConsumer = referencedItemConsumerSupplier.get(ReferencedSchemaConsumer.class);
        schemaAnnotationMapper.buildFromAnnotation(contentAnnotation.schema(), referencedSchemaConsumer, mediaType::setSchema);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(contentAnnotation.extensions()), mediaType::setExtensions);
        return mediaType;
    }

    private void setExampleOrExamples(MediaType mediaType, ExampleObject[] exampleObjectAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        if (exampleObjectAnnotations.length == 1 && StringUtils.isBlank(exampleObjectAnnotations[0].name())) {
            // one should not set the full example object here, just the value
            mediaType.setExample(exampleObjectAnnotationMapper.map(exampleObjectAnnotations[0]).getValue());
        } else {
            setCollectionIfNotEmpty(exampleObjectAnnotationMapper.mapArray(exampleObjectAnnotations),
                    examples -> referencedItemConsumerSupplier.get(ReferencedExamplesConsumer.class).maybeAsReference(examples, mediaType::setExamples)
            );
        }
    }
}
