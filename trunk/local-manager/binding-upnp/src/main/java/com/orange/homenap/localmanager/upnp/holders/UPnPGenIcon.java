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
package com.orange.homenap.localmanager.upnp.holders;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.osgi.service.upnp.UPnPIcon;

public class UPnPGenIcon implements UPnPIcon{
	
	/**
	 * @uml.property  name="mimeType"
	 */
	private String mimeType;
	/**
	 * @uml.property  name="width"
	 */
	private int width;
	/**
	 * @uml.property  name="height"
	 */
	private int height;
	/**
	 * @uml.property  name="inputStream"
	 */
	private InputStream inputStream;
	/**
	 * @uml.property  name="depth"
	 */
	private int depth;
	/**
	 * @uml.property  name="url"
	 */
	private String url;

	public UPnPGenIcon(int depth, int height, int width, 
			String mimeType, String url) {
		this.depth = depth;
		this.height = height;
		this.width = width;
		this.mimeType = mimeType;
		this.url = url;		
       try { 
		    this.inputStream = new BufferedInputStream(new FileInputStream(new URL(url).getPath()));
       }
       catch(Exception e) {
           this.inputStream = null;
       }
	}

	/**
	 * @return  Returns the depth.
	 * @uml.property  name="depth"
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth  The depth to set.
	 * @uml.property  name="depth"
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * @return  Returns the height.
	 * @uml.property  name="height"
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height  The height to set.
	 * @uml.property  name="height"
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return  Returns the inputStream.
	 * @uml.property  name="inputStream"
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream  The inputStream to set.
	 * @uml.property  name="inputStream"
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return  Returns the mimeType.
	 * @uml.property  name="mimeType"
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType  The mimeType to set.
	 * @uml.property  name="mimeType"
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return  Returns the url.
	 * @uml.property  name="url"
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url  The url to set.
	 * @uml.property  name="url"
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return  Returns the width.
	 * @uml.property  name="width"
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width  The width to set.
	 * @uml.property  name="width"
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getSize() {
		try {
			return getInputStream().available();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
}

