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
 *  File Name   : Architecture.java
 *
 *  Created     : 05/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.utils;

import java.util.ArrayList;
import java.util.List;

public class Architecture
{
    public static enum Execution { MANDATORY, OPTIONAL }
    public static enum Status { STARTED, STOPPED }

    private String name;
    private Status status;
    private List<Component> component = new ArrayList<Component>();
    private Execution execution;

    public Architecture()
    {
        this.status = Status.STOPPED;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    public List<Component> getComponent() { return component; }

    public void setComponent(List<Component> component) {this.component = component; }

    public Execution getExecution() { return execution; }

    public void setExecution(Execution execution) { this.execution = execution; }
}
