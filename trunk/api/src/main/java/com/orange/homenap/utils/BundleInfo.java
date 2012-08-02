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
* File Name   : BundleInfo.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BundleInfo
{
    private String DIRECTORY = "/tmp/homenap";

    private Long bundleId;
    private File file;
    private Integer numberOfComponents;
    private Integer numberOfComponentsMax;
    private List<Properties> propertiesList;

    public BundleInfo(Long bundleId, String bundleName)
    {
        System.out.println("BundleName: " + bundleName);

        this.bundleId = bundleId;
        this.file = new File(DIRECTORY + "/" + bundleName + ".xml");
        this.propertiesList = new ArrayList<Properties>();
        this.numberOfComponents = 0;
        this.numberOfComponentsMax = 0;
    }

    public Long getBundleId()
    {
        return bundleId;
    }

    public String getFileName()
    {
        return file.getName();
    }

    public void addComponent()
    {
        numberOfComponents++;

        if(numberOfComponents > numberOfComponentsMax)
            numberOfComponentsMax = numberOfComponents;
    }

    public void removeComponent()
    {
        numberOfComponents--;
    }

    public Integer getNumberOfComponentsMax()
    {
        return numberOfComponentsMax;
    }

    public Integer getNumberOfComponents()
    {
        return numberOfComponents;
    }

    public List<Properties> getPropertiesList()
    {
        return propertiesList;
    }

    public void setPropertiesList(List<Properties> propertiesList)
    {
        this.propertiesList = propertiesList;
    }

    public boolean migrationInfoExist()
    {
        if(file.length() == 0)
            return false;
        else
            return true;
    }
}
