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
 *  File Name   : Component.java
 *
 *  Created     : 05/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.utils;

import org.osgi.framework.BundleEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Component
{
    public static enum Deployment { LOCALIZED, ALOCALIZED }
    public static enum Migrability { MIGRATABLE, STATIC }
    public static enum State { STATEFUL, STATELESS }

    private Long id;
    private String name;
    private String url;
    private Deployment deployment;
    private Migrability migrability;
    private State state;
    private int bundleEvent;
    private Map<String, Integer> resources = new HashMap<String, Integer>();
    private List<String> require = new ArrayList<String>();
    private Map<String, Object> properties = new HashMap<String, Object>();;

    public Component()
    {
        bundleEvent = BundleEvent.UNINSTALLED;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public List<String> getRequire() { return require; }

    public void setRequire(List<String> require) { this.require = require; }

    public Deployment getDeployment() { return deployment; }

    public void setDeployment(Deployment deployment) { this.deployment = deployment; }

    public Migrability getMigrability() { return migrability; }

    public void setMigrability(Migrability migrability) { this.migrability = migrability; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

    public int getBundleEvent() { return bundleEvent; }

    public void setBundleEvent(int bundleEvent) { this.bundleEvent = bundleEvent; }

    public void setResources(Map<String, Integer> resources) { this.resources = resources; }

    public Map<String, Integer> getResources() { return resources; }

    public void setProperties(Map<String, Object> properties) { this.properties = properties; }

    public Map<String, Object> getProperties() { return this.properties; }
}