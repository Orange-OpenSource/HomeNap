/*
 * --------------------------------------------------------
 * Module Name : rose-importer
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
 * File Name   : RoSeImporter.java
 *
 * Created     : 03/12/2012
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.roseimporter;

import org.osgi.framework.BundleContext;
import org.osgi.service.remoteserviceadmin.EndpointDescription;

public class RoSeImporter
{
    private BundleContext bundleContext;

    public RoSeImporter(BundleContext bc)
    {
        bundleContext = bc;
    }

    public void addRemoteService(EndpointDescription endpointDescription)
    {

    }

    public void removeRemoteService()
    {

    }
}
