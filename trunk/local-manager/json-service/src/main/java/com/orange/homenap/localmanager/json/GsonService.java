/*
 * --------------------------------------------------------
 *  Module Name : json-service
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
 *  File Name   : GsonServiceItf.java
 *
 *  Created     : 16/10/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonService implements GsonServiceItf
{
    private Gson gson;

    public void start()
    {
        gson = new Gson();
    }
    
    public <T> T fromJson(String str, Class<T> c)
    {
        try {
            return gson.fromJson(str, c);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();

            System.out.println(str);
        }

        return null;
    }

    public String toJson(Object object)
    {
        return gson.toJson(object);
    }
}
