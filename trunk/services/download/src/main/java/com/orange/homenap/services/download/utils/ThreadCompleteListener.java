/*
 * --------------------------------------------------------
 *  Module Name : download
 *  Version : 1.0-SNAPSHOT
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
 *  File Name   : ThreadCompleteListener.java
 *
 *  Created     : 15/10/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.services.download.utils;

public interface ThreadCompleteListener
{
    void notifyOfThreadComplete(final Thread thread);
}
