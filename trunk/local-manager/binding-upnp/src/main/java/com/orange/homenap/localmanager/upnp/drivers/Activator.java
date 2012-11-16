/*
 * --------------------------------------------------------
 * Module Name : binding-upnp
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
 * File Name   : ${NAME}
 *
 * Created     :
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.upnp.drivers;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

    public class Activator implements BundleActivator{
   static BundleContext bundleContext = null;

   public void start(BundleContext ctxt) throws Exception {
            bundleContext = ctxt;
   }

   public void stop(BundleContext arg0) throws Exception {

   }
}
