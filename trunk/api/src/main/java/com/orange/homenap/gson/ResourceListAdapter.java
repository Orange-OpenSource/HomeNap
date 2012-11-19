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
import java.util.ArrayList;
import java.util.List;

public class ResourceListAdapter implements JsonSerializer<List<Resource>>, JsonDeserializer<List<Resource>>
{
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    @Override
    public List<Resource> deserialize (JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        JsonArray array = jsonElement.getAsJsonArray();
        List<Resource> resourceList = new ArrayList<Resource>();

        for(int i = 0; i < array.size(); i++)
        {
            JsonElement element = array.get(i);
            JsonObject jsonObject = element.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
            String className = prim.getAsString();

            Class<?> klass = null;

            try {
                klass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new JsonParseException(e.getMessage());
            }

            Resource resource = context.deserialize(jsonObject.get(INSTANCE), klass);

            resourceList.add(resource);
        }

        return resourceList;
    }

    @Override
    public JsonElement serialize(List<Resource> resourceList, Type type, JsonSerializationContext context)
    {
        JsonArray array = new JsonArray();

        for(Resource resource : resourceList)
        {
            JsonObject jsonObject = new JsonObject();
            String className = resource.getClass().getCanonicalName();
            jsonObject.addProperty(CLASSNAME, className);
            JsonElement element = context.serialize(resource);
            jsonObject.add(INSTANCE, element);
            array.add(jsonObject);
        }

        return array;
    }
}
