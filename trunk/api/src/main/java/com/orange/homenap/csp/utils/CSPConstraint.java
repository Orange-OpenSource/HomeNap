/*--------------------------------------------------------
* Module Name : constraint-manager
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
* File Name   : CSPConstraint.java
*
* Created     : 30/12/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.csp.utils;

import choco.kernel.model.Model;
import com.orange.homenap.globalcoordinator.constraintmanager.ConstraintManagerItf;

public abstract class CSPConstraint
{
    private ConstraintManagerItf constraintManagerItf;

    public void start()
    {
        constraintManagerItf.registerConstraint(this);
    }

    public void stop()
    {
        constraintManagerItf.unRegisterConstraint(this);
    }
    
    public Model addConstraint(Model model)
    {
        return model;
    }
}
