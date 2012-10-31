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
    public static enum BundleState {INSTALLED, STARTED, STOPPED, UNINSTALLED, UNAVAILABLE}
    public static enum ServiceDeployment {LOCALIZED, ALOCALIZED}
    public static enum ServiceMigrability {MIGRATABLE, STATIC}
    public static enum ServiceState {STATEFUL, STATELESS}
    public static enum Execution { MANDATORY, OPTIONAL }

    private Long id;
    private String bundleName;
    private String bundleUrl;
    private BundleState bundleState;
    private ServiceDeployment serviceDeployment;
    private ServiceMigrability serviceMigrability;
    private ServiceState serviceState;
    private Execution execution;
    private Map<String, StatefulComponent> components;
    private Map<String, Integer> resources = new HashMap<String, Integer>();

    public Service()
    {
        this.components = new HashMap<String, StatefulComponent>();
    }

    public Service(Long id, String bundleName, String bundleUrl, BundleState bundleState)
    {
        this.id = id;
        this.bundleName = bundleName;
        this.bundleUrl = bundleUrl;
        this.bundleState = bundleState;
        this.components = new HashMap<String, StatefulComponent>();
    }

    public void setId(Long id) { this.id = id; }

    public Long getId() { return this.id; }

    public void setBundleName(String bundleName) { this.bundleName = bundleName; }

    public String getBundleName() { return this.bundleName; }

    public String getName() { return this.bundleName; }

    public void setBundleUrl(String bundleUrl) { this.bundleUrl = bundleUrl; }

    public String getBundleUrl() { return this.bundleUrl; }

    public void setBundleState(BundleState bundleState) { this.bundleState = bundleState; }

    public BundleState getBundleState() { return this.bundleState; }

    public void setComponents(Map<String, StatefulComponent> components) { this.components = components; }

    public Map<String, StatefulComponent> getComponents() { return this.components; }

    public void setServiceDeployment(ServiceDeployment serviceDeployment) { this.serviceDeployment = serviceDeployment; }

    public ServiceDeployment getServiceDeployment() { return serviceDeployment; }

    public void setServiceMigrability(ServiceMigrability serviceMigrability) { this.serviceMigrability = serviceMigrability; }

    public ServiceMigrability getServiceMigrability() { return serviceMigrability; }

    public void setServiceState(ServiceState serviceState) { this.serviceState = serviceState; }

    public ServiceState getServiceState() { return serviceState; }

    public void setExecution(Execution execution) { this.execution = execution; }

    public Execution getExecution() { return execution; }

    public void setResources(Map<String, Integer> resources) { this.resources = resources; }

    public Map<String, Integer> getResources() { return resources; }
}
