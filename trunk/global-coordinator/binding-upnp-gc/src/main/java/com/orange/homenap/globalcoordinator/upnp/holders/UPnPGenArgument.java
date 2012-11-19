/*
 * --------------------------------------------------------
 * Module Name : binding-upnp-gc
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : $projectName
 * Version : 0.1-SNAPSHOT
 *
 * Copyright © 28/06/2012 – 31/12/2013 France Télécom
 * This software is distributed under the Apache 2.0 license,
 * the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 * or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 * File Name   : ${NAME}
 *
 * Created     :
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */
package com.orange.homenap.globalcoordinator.upnp.holders;

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

