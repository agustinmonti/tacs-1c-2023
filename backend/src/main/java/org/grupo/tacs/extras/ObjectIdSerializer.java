package org.grupo.tacs.extras;
import org.bson.types.ObjectId;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ObjectIdSerializer implements JsonSerializer<ObjectId> {

    @Override
    public JsonElement serialize(ObjectId objectId, Type type, JsonSerializationContext context) {
        return context.serialize(objectId.toHexString());
    }
}
