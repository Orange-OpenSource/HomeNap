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
 * This class represents the aguments passed IN (for an action)
 * @author Marius Legros TANKEU DE KUIGWA
 *
 */

public class UPnPGenInArgument extends UPnPGenArgument{

	public UPnPGenInArgument(String name, String relatedStateVariable) {
		super(name, relatedStateVariable);
	}
	/*
	 *  (non-Javadoc)
	 * @see com.francetelecom.upnpgen.tests.UPnPGenArgument#getDirection()
	 */
	public boolean getDirection() {
		return UPnPGenArgument.IN;
	}
}

