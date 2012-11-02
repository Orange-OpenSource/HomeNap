/*
 * --------------------------------------------------------
 *  Module Name : manifest-reader
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
 *  File Name   : WakeOnLanItf.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.manifestreader;

import com.orange.homenap.utils.Service;
import org.osgi.framework.Bundle;

import java.util.Map;

public interface ManifestReaderItf
{
    public Service.ServiceDeployment getServiceDeployment(Bundle bundle);

    public Service.ServiceMigrability getServiceMigrability(Bundle bundle);

    public Service.ServiceState getServiceState(Bundle bundle);

    public Service.Execution getExecution(Bundle bundle);

    public Map<String, String> getServiceResources(Bundle bundle);
}