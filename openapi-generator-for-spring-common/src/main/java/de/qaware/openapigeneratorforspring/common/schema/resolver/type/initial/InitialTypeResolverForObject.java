package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameFactory;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class InitialTypeResolverForObject implements InitialTypeResolver {

    public static final Supplier<Schema> OBJECT_SCHEMA_SUPPLIER = () -> new Schema().type("object");

    // this resolver does not have any condition, so run this always later then the other resolvers
    public static final int ORDER = DEFAULT_ORDER + 100;

    private final SchemaNameFactory schemaNameFactory;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType) {
        Schema schema = OBJECT_SCHEMA_SUPPLIER.get();
        schema.setName(schemaNameFactory.createFromType(javaType));
        return InitialSchema.builder()
                .schema(schema)
                .hasNestedProperties(true)
                .build();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
