/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author root
 */
public class Resources {
	public static URL loadResource(final String path) {
		return Resources.class.getClassLoader().getResource(path);
	}
	
	public static BufferedImage loadResourceAsBufferedImage(String path) throws IOException{
		InputStream resourceAsStream = loadResourceAsStream(path);
		return ImageIO.read(resourceAsStream);
	}

	public static InputStream loadResourceAsStream(String path) {
		if(!path.startsWith("/")){
			path = "/" + path;
		}
		final InputStream resourceAsStream = Resources.class.getResourceAsStream(path);
		return resourceAsStream;
	}
	
}
