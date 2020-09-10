package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.schema.Schema;
import org.apache.commons.lang3.StringUtils;

public class DefaultReferenceDecider implements ReferenceDecider {
    @Override
    public boolean turnIntoReference(Schema schema, int numberOfUsages) {
        if (onlyTypeIsSet(schema)) {
            return false;
        }
        if (StringUtils.isNotBlank(schema.getName())) {
            return true;
        }
        return numberOfUsages > 1;
    }

    private boolean onlyTypeIsSet(Schema schema) {
        String previousType = schema.getType();
        // check if the schema becomes "empty" once the type is also null
        // this is a little trick to avoid checking all other properties!
        boolean onlyTypeIsSet = schema.type(null).isEmpty();
        schema.setType(previousType);
        return onlyTypeIsSet;
    }
}
