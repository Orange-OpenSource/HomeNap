/*--------------------------------------------------------
* Module Name : API
* Version : 0.1-SNAPSHOT
*
* Software Name : HomeNap
* Version : 0.1-SNAPSHOT
*
* Copyright © 28/06/2012 – 28/06/2012 France Télécom
* This software is distributed under the Apache 2.0 license,
* the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
* or see the "LICENSE-2.0.txt" file for more details.
*
*--------------------------------------------------------
* File Name   : Service.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.utils;

import java.util.HashMap;
import java.util.Map;

public class Service
{
    public static enum Deployement {LOCALIZED, ALOCALIZED}
    public static enum Migrability {MIGRATABLE, STATIC}
    public static enum State {STATEFUL, STATELESS}
    public static enum Execution { MANDATORY, OPTIONAL }

    private Long id;
    private String name;
    private String url;
    private Deployement deployement;
    private Migrability migrability;
    private State state;
    private Execution execution;
    private Map<String, StatefulComponent> components;
    private Map<String, Integer> resources = new HashMap<String, Integer>();

    public Service()
    {
        this.components = new HashMap<String, StatefulComponent>();
    }

    public Service(Long id, String name, String bundleUrl)
    {
        this.id = id;
        this.name = name;
        this.url = bundleUrl;
        this.components = new HashMap<String, StatefulComponent>();
    }

    public void setId(Long id) { this.id = id; }

    public Long getId() { return this.id; }

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public void setUrl(String url) { this.url = url; }

    public String getUrl() { return this.url; }

    public void setComponents(Map<String, StatefulComponent> components) { this.components = components; }

    public Map<String, StatefulComponent> getComponents() { return this.components; }

    public State getState() { return state; }

    public void setResources(Map<String, Integer> resources) { this.resources = resources; }

    public Map<String, Integer> getResources() { return resources; }
}
