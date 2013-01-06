/*
 * --------------------------------------------------------
 *  Module Name : api
 *  Version : 0.1-SNAPSHOT
 *
 *  Software Name : HomeNap
 *  Version : 0.1-SNAPSHOT
 *
 *  Copyright © 28/06/2012 – 28/06/2012 France Télécom
 *  This software is distributed under the Apache 2.0 license,
 *  the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 *  or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 *  File Name   : ConstraintAdapter.java
 *
 *  Created     : 06/01/2013
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.gson;

import com.google.gson.*;
import com.orange.homenap.utils.Constraint;

import java.lang.reflect.Type;
import java.util.Map;

public class ConstraintAdapter implements JsonDeserializer<Constraint>
{
    @Override
    public Constraint deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        Constraint constraint = new Constraint();

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            constraint.setName(entry.getKey());
            constraint.setValue(entry.getValue().toString());
        }

        return constraint;
    }
}
