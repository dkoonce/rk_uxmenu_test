/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author root
 */
public class Colors {
	
	private static Color getColorFromStr(String colorProperty){
		if(colorProperty == null){
			return null;
		}
		
		String sanitizedColorProperty = colorProperty.trim().toLowerCase();
		Color color = null;
		
		// Try decoding if text color
		try {
			Field field = Color.class.getField(sanitizedColorProperty);
			color = (Color) field.get(null);
		} catch (Exception e) {
		}

		// Try setting if hex
		if (sanitizedColorProperty.startsWith("#")) {
			try {
				color = Color.decode(sanitizedColorProperty);
			} catch (Exception e) {
				System.err.println(colorProperty + " is in an invalid hex format. Cannot set color.");
			}
		}

		// Try setting if rgb
		if (sanitizedColorProperty.startsWith("rgb")) {
			Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
			Matcher m = c.matcher(sanitizedColorProperty);

			if (m.matches()) {
				try {
					color = new Color(Integer.valueOf(m.group(1)), // r
							Integer.valueOf(m.group(2)), // g
							Integer.valueOf(m.group(3))); // b 
				} catch (Exception e) {
					System.err.println(colorProperty + " is in an invalid rgb format. Cannot set color.");
				}
			} else {
				System.err.println(colorProperty + " is in an invalid rgb format. Cannot set color.");
			}

		}

		if (color == null) {
			System.err.println("Could not set color. Argument " + colorProperty + " is not in a recognizeable format.");
			return null;
		}
		return color;
	}
	
	public static Color getColorFromStr(String colorProperty, Color backupColor){
		final Color color = getColorFromStr(colorProperty);
		if(color != null){
			return color;
		}
		return backupColor;
	}
}
