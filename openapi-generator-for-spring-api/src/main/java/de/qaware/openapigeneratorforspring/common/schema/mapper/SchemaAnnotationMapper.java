package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.function.Consumer;

/**
 * Mapper for Schema annotation. Created by
 * {@link SchemaAnnotationMapperFactory}.
 */
public interface SchemaAnnotationMapper {
    void buildFromAnnotation(
            io.swagger.v3.oas.annotations.media.Schema schemaAnnotation,
            ReferencedSchemaConsumer referencedSchemaConsumer,
            Consumer<Schema> schemaSetter
    );

    void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}
