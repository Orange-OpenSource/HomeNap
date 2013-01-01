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
* File Name   : ConstraintManager.java
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

public class CSPPluginManager implements CSPPluginManagerItf
{
    private List<CSPVariable> variables;
    private List<CSPConstraint> constraints;

    public void registerVariable(CSPVariable cspVariable)
    {
        variables.add(cspVariable);
    }

    public void unRegisterVariable(CSPVariable cspVariable)
    {
        variables.remove(cspVariable);
    }
    
    public void registerConstraint(CSPConstraint cspConstraint)
    {
        constraints.add(cspConstraint);
    }

    public void unRegisterConstraint(CSPConstraint cspConstraint)
    {
        constraints.remove(cspConstraint);
    }

    public List<CSPVariable> getVariables() { return variables; }

    public List<CSPConstraint> getConstraints() { return constraints; }
}
