/**
##############################################################################
# Copyright (C) 2004-2007 France Telecom R&D
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
##############################################################################
 * 
 */
package com.orange.homenap.localmanager.upnp.holders;

/**
 * This class represent an argument  for a service action. This clas is extended by @see com.orange.upnpgen.tests.UPnPGenInArgument and @see com.orange.upnpgen.tests.UPnPGenOutArgument
 * @author  Marius Legros TANKEU DE KUIGWA
 */
public abstract class UPnPGenArgument {

	public static final boolean IN = false;
	public static final boolean OUT = true;
	
	/**
	 * the argument name
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * the state variable whose this argument is related
	 * @uml.property  name="relatedStateVariable"
	 */
	private String relatedStateVariable;

	UPnPGenArgument(String name, String relatedStateVariable) {
		this.name = name;
		this.relatedStateVariable = relatedStateVariable;
	}

	/**
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}
	/**
	 * get the stateVariable related to that argument
	 * @return
	 * @uml.property  name="relatedStateVariable"
	 */
	public String getRelatedStateVariable() {
		return relatedStateVariable;
	}
	/**
	 * return the direction (IN or OUT)
	 * @return the boolean representing the direction false=in and true=out
	 */
	public abstract boolean getDirection();
}

