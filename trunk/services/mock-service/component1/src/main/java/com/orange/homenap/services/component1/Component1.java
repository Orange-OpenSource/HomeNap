/*
 * --------------------------------------------------------
 * Module Name : component1
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : HomeNap
 * Version : 0.1-SNAPSHOT
 *
 * Copyright © 28/06/2012 – 31/12/2013 France Télécom
 * This software is distributed under the Apache 2.0 license,
 * the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 * or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 * File Name   : Component1.java
 *
 * Created     : 20/11/2012
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.services.component1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path(value="/component1/{name}")
public class Component1 implements Component1Itf
{
    @GET
    @Produces("application/json")
    public String alert(@PathParam("name") String name)
    {
        return "Alert " + name;
    }
}
