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
import com.google.gson.reflect.TypeToken;
import com.orange.homenap.utils.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionListAdapter implements JsonDeserializer<List<Action>>
{
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    @Override
    public List<Action> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        JsonArray array = jsonElement.getAsJsonArray();
        List<Action> actionList = new ArrayList<Action>();

        for(int i = 0; i < array.size(); i++)
        {
            JsonElement element = array.get(i);
            JsonObject jsonObject = element.getAsJsonObject();

            Action action = context.deserialize(jsonObject, Action.class);
            action.setProperties(new HashMap<String, Object>());

            if(!jsonObject.get("properties").toString().equals("{}"))
            {
                //System.out.println(jsonObject.get("properties").toString());

                JsonObject properties = jsonObject.get("properties").getAsJsonObject();

                /*List<String> resources;
                JsonArray resourcesArray = properties.get("resources").getAsJsonArray();
                
                for(int j = 0; j < resourcesArray.size(); j++)
                    resources.add(context.deserialize())
                    resources.add(resourcesArray.get(j).));
                */
                List<String> resources = context.deserialize(properties.get("resources").getAsJsonArray(), (new TypeToken<List<String>>() {}).getType());

                action.getProperties().put("resources", resources);

                List<Component> components = context.deserialize(properties.get("components"), (new TypeToken<List<Component>>() {}).getType());

                action.getProperties().put("components", components);

                List<Architecture> architectures = context.deserialize(properties.get("architectures"), (new TypeToken<List<Architecture>>() {}).getType());

                action.getProperties().put("architectures", architectures);

                List<Device> devices = context.deserialize(properties.get("devices"), (new TypeToken<List<Device>>() {}).getType());

                action.getProperties().put("devices", devices);
            }

            actionList.add(action);
        }

        return actionList;
    }
}
