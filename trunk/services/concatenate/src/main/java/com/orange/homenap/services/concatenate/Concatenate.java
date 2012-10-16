/*
 * --------------------------------------------------------
 *  Module Name : concatenate
 *  Version : 0.1-SNAPSHOT
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
 *  File Name   : Concatenate.java
 *
 *  Created     : 15/10/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.services.concatenate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

public class Concatenate implements ConcatenateItf
{
    public void concatenate(List<String> filesList) throws IOException
    {
        ListOfFiles listOfFiles = new ListOfFiles((filesList));

        SequenceInputStream sis = new SequenceInputStream(listOfFiles);
        FileOutputStream fos = new FileOutputStream("/tmp/"
                + filesList.get(0).split(".")[filesList.get(0).split(".").length - 1]
                + ".temp");

        int c;

        while ((c = sis.read()) != -1)
            fos.write(c);

        fos.close();
        sis.close();
    }

    public class ListOfFiles implements Enumeration
    {
        private List<String> filesList;
        private int current = 0;

        public ListOfFiles(List<String> list)
        {
            this.filesList = list;
        }

        public boolean hasMoreElements()
        {
            if(filesList.isEmpty())
                return false;
            else
                return true;
        }

        public Object nextElement()
        {
            InputStream in = null;

            while(hasMoreElements())
            {
                URL nextElement = null;

                try {
                    nextElement = new URL(filesList.get(current));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if(nextElement != null)
                {
                    current++;

                    try {
                        in = nextElement.openStream();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return in;
        }
    }
}
