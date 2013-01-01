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
* File Name   : ConstraintManagerItf.java
*
* Created     : 30/12/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.globalcoordinator.csp;

import com.orange.homenap.csp.utils.CSPConstraint;
import com.orange.homenap.csp.utils.CSPVariable;

import java.util.List;

public interface CSPPluginManagerItf
{
    public void registerVariable(CSPVariable cspVariable);

    public void unRegisterVariable(CSPVariable cspVariable);

    public void registerConstraint(CSPConstraint cspConstraint);

    public void unRegisterConstraint(CSPConstraint cspConstraint);

    public List<CSPVariable> getVariables();

    public List<CSPConstraint> getConstraints();
}
