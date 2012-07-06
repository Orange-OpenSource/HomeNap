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

package com.olnc.made.homenap.utils;

import java.util.HashMap;
import java.util.Map;

public class Service
{
    public static enum ServiceState {INSTALLED, STARTED, STOPPED, UNINSTALLED, UNAVAILABLE}

    private Long id;
    private String bundleName;
    private String bundleUrl;
    private ServiceState serviceState;
    private Map<String, StatefulComponent> components;

    public Service()
    {
        this.components = new HashMap<String, StatefulComponent>();
    }

    public Service(Long id, String bundleName, String bundleUrl, ServiceState serviceState)
    {
        this.id = id;
        this.bundleName = bundleName;
        this.bundleUrl = bundleUrl;
        this.serviceState = serviceState;
        this.components = new HashMap<String, StatefulComponent>();
    }

    public void setId(Long id) { this.id = id; }

    public Long getId() { return this.id; }

    public void setName(String bundleName) { this.bundleName = bundleName; }

    public String getName() { return this.bundleName; }

    public void setBundleUrl(String bundleUrl) { this.bundleUrl = bundleUrl; }

    public String getBundleUrl() { return this.bundleUrl; }

    public void setServiceState(ServiceState serviceState) { this.serviceState = serviceState; }

    public ServiceState getServiceState() { return this.serviceState; }

    public void setComponents(Map<String, StatefulComponent> components) { this.components = components; }
    
    public Map<String, StatefulComponent> getComponents() { return this.components; }
}
