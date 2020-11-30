/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverSupport;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class InitialSchemaBuilderForCollectionLikeType implements InitialSchemaBuilder, TypeResolverSupport {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver) {
        return javaType.isCollectionLikeType() ? buildArrayInitialSchema() : null;
    }

    @Override
    public boolean supports(InitialSchema initialSchema) {
        return initialSchema.getSchema() instanceof ArraySchema;
    }

    public InitialSchema buildArrayInitialSchema() {
        return InitialSchema.builder().schema(new ArraySchema()).build();
    }

    @EqualsAndHashCode(callSuper = true)
    private static class ArraySchema extends Schema {
        public ArraySchema() {
            setType("array");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
