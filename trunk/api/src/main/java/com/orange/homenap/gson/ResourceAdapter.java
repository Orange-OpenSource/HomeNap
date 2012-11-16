/*
 * --------------------------------------------------------
 * Module Name : api
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : $projectName
 * Version : 0.1-SNAPSHOT
 *
 * Copyright © 28/06/2012 – 31/12/2013 France Télécom
 * This software is distributed under the Apache 2.0 license,
 * the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 * or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 * File Name   : ${NAME}
 *
 * Created     :
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.gson;

import com.google.gson.*;
import com.orange.homenap.utils.Resource;

import java.lang.reflect.Type;

public class ResourceAdapter implements JsonSerializer<Resource>, JsonDeserializer<Resource>
{
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    @Override
    public Resource deserialize (JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> klass = null;

        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //throw new JsonParseException(e.getMessage());
        }

        return context.deserialize(jsonObject.get(INSTANCE), klass);
    }

    @Override
    public JsonElement serialize(Resource resource, Type type, JsonSerializationContext context)
    {
        JsonObject jsonObject = new JsonObject();
        String className = resource.getClass().getCanonicalName();
        jsonObject.addProperty(CLASSNAME, className);
        JsonElement element = context.serialize(resource);
        jsonObject.add(INSTANCE, element);

        return jsonObject;
    }
}
