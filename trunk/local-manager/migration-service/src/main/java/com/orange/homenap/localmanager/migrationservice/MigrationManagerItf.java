/*--------------------------------------------------------
* Module Name : migration-service
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
* File Name   : MigrationHandlerManagerItf.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.localmanager.migrationservice;

import java.util.Map;

public interface MigrationManagerItf
{
    public Map<String, Object> registerComponent(String instanceName);

    public void unRegisterComponent(Map<String, Object> propertiesMap, String instanceName);
}
