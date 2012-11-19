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
import com.orange.homenap.utils.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ComponentListAdapter implements JsonSerializer<List<Component>>, JsonDeserializer<List<Component>>
{
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    @Override
    public List<Component> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        System.out.println("youhou je suis dedans");

        JsonArray array = jsonElement.getAsJsonArray();
        List<Component> componentList = new ArrayList<Component>();

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

            Component component = context.deserialize(jsonObject.get(INSTANCE), klass);

            System.out.println(component.getName());

            componentList.add(component);
        }

        return componentList;
    }

    @Override
    public JsonElement serialize(List<Component> componentList, Type type, JsonSerializationContext context)
    {
        JsonArray array = new JsonArray();

        for(Component component : componentList)
        {
            JsonObject jsonObject = new JsonObject();
            String className = component.getClass().getCanonicalName();
            jsonObject.addProperty(CLASSNAME, className);
            JsonElement element = context.serialize(component);
            jsonObject.add(INSTANCE, element);
            array.add(jsonObject);
        }

        return array;
    }
}
