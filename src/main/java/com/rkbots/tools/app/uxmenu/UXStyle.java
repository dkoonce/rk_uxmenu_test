/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author josh
 */
public class UXStyle implements Cloneable {

	public static String DEFAULT_STYLE = "default";
	public static String DISABLED_STYLE = "disabled";
	public static Map<String, UXStyle> buttonStyleClasses = loadButtonClasses();

	public Color fontColor = new Color(0xFFFFFF); //Default Text Color
	public Color activeFontColor = new Color(0xFFFFFF); //Default Text Color
	public Color backgroundColor = new Color(0x0171BB);//new Color(0x6797D0);new Color(0x0C72BA);
	public Color activeBackgroundColor = new Color(0xF58649);
	public Color borderColor = new Color(0x58595B);
	public Color activeBorderColor = new Color(0x58595B);
	public int borderRoundness = 15;
	public int borderSize = 2;
	public int hoverBorderSize = 6;
	public int currentBorderThickness = borderSize;
	public int borderTimeOut = 2000;

	public UXStyle() {

	}

	private static Map<String, UXStyle> loadButtonClasses() {
		Map<String, UXStyle> buttonClassMap = new TreeMap<>();

		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("button-classes.json");
			JSONParser parser = new JSONParser();
			JSONArray bundledResourcesArray = (JSONArray) parser.parse(new InputStreamReader(is));

			loadButtonStyleClasses(bundledResourcesArray, buttonClassMap);
			final String path = "resources/button-classes.json";

			if (new File(path).exists()) {
				JSONArray fileSystemArray = (JSONArray) parser.parse(new FileReader(path));
				loadButtonStyleClasses(fileSystemArray, buttonClassMap);
			}
		} catch (IOException ex) {
			org.slf4j.LoggerFactory.getLogger(UXStyle.class).error(ex.getMessage(), ex);
		} catch (ParseException ex) {
			org.slf4j.LoggerFactory.getLogger(UXStyle.class).error(ex.getMessage(), ex);
		}

		return buttonClassMap;
	}

	private static void loadButtonStyleClasses(JSONArray a, Map<String, UXStyle> buttonClassMap) {
		boolean newDefaultStyle = false;
		for (Object o : a) {
			JSONObject buttonClassJSON = (JSONObject) o;
			final String nameKey = "Name";

			String name = (String) buttonClassJSON.get(nameKey);
			if (name.equals(UXStyle.DEFAULT_STYLE)) {
				newDefaultStyle = true;
			}

			UXStyle buttonClassStyle = buttonClassMap.get(name);
			if (buttonClassStyle == null || newDefaultStyle) {
				buttonClassStyle = UXStyleFactory.createUXStyle(buttonClassMap);
				buttonClassMap.put(name, buttonClassStyle);
			}

			buttonClassStyle.backgroundColor = Colors.getColorFromStr((String) buttonClassJSON.get(UXButton.POST_BACKGROUND_COLOR), buttonClassStyle.backgroundColor);
			buttonClassStyle.borderColor = Colors.getColorFromStr((String) buttonClassJSON.get(UXButton.POST_BORDER_COLOR), buttonClassStyle.borderColor);
			buttonClassStyle.fontColor = Colors.getColorFromStr((String) buttonClassJSON.get(UXButton.POST_FONT_COLOR), buttonClassStyle.fontColor);
			buttonClassStyle.activeBackgroundColor = Colors.getColorFromStr((String) buttonClassJSON.get(UXButton.POST_ACTIVE_BACKGROUND_COLOR), buttonClassStyle.activeBackgroundColor);
			buttonClassStyle.activeBorderColor = Colors.getColorFromStr((String) buttonClassJSON.get(UXButton.POST_ACTIVE_BORDER_COLOR), buttonClassStyle.activeBorderColor);
			buttonClassStyle.activeFontColor = Colors.getColorFromStr((String) buttonClassJSON.get(UXButton.POST_ACTIVE_FONT_COLOR), buttonClassStyle.activeFontColor);
			buttonClassStyle.borderRoundness = Integers.parseInt(buttonClassJSON.get(UXButton.POST_BORDER_ROUNDNESS), buttonClassStyle.borderRoundness);
			buttonClassStyle.borderSize = Integers.parseInt(buttonClassJSON.get(UXButton.POST_BORDER_SIZE), buttonClassStyle.borderSize);
			buttonClassStyle.hoverBorderSize = Integers.parseInt(buttonClassJSON.get(UXButton.POST_HOVER_BORDER_SIZE), buttonClassStyle.hoverBorderSize);
		}
	}

	@Override
	public UXStyle clone() {
		UXStyle style = new UXStyle();
		style.fontColor = fontColor; //Default Text Color
		style.activeFontColor = activeFontColor; //Default Text Color
		style.backgroundColor = backgroundColor;//new Color(0x6797D0);new Color(0x0C72BA);
		style.activeBackgroundColor = activeBackgroundColor;
		style.borderColor = borderColor;
		style.activeBorderColor = activeBorderColor;
		style.borderRoundness = borderRoundness;
		style.borderSize = borderSize;
		style.hoverBorderSize = hoverBorderSize;
		return style;
	}

	@Override
	public String toString() {
		return "UXStyle{" + "fontColor=" + fontColor + ", activeFontColor=" + activeFontColor + ", backgroundColor=" + backgroundColor + ", activeBackgroundColor=" + activeBackgroundColor + ", borderColor=" + borderColor + ", activeBorderColor=" + activeBorderColor + ", borderRoundness=" + borderRoundness + ", borderSize=" + borderSize + ", hoverBorderSize=" + hoverBorderSize + ", currentBorderThickness=" + currentBorderThickness + ", borderTimeOut=" + borderTimeOut + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + Objects.hashCode(this.fontColor);
		hash = 89 * hash + Objects.hashCode(this.activeFontColor);
		hash = 89 * hash + Objects.hashCode(this.backgroundColor);
		hash = 89 * hash + Objects.hashCode(this.activeBackgroundColor);
		hash = 89 * hash + Objects.hashCode(this.borderColor);
		hash = 89 * hash + Objects.hashCode(this.activeBorderColor);
		hash = 89 * hash + this.borderRoundness;
		hash = 89 * hash + this.borderSize;
		hash = 89 * hash + this.hoverBorderSize;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UXStyle other = (UXStyle) obj;
		if (!Objects.equals(this.fontColor, other.fontColor)) {
			return false;
		}
		if (!Objects.equals(this.activeFontColor, other.activeFontColor)) {
			return false;
		}
		if (!Objects.equals(this.backgroundColor, other.backgroundColor)) {
			return false;
		}
		if (!Objects.equals(this.activeBackgroundColor, other.activeBackgroundColor)) {
			return false;
		}
		if (!Objects.equals(this.borderColor, other.borderColor)) {
			return false;
		}
		if (!Objects.equals(this.activeBorderColor, other.activeBorderColor)) {
			return false;
		}
		if (this.borderRoundness != other.borderRoundness) {
			return false;
		}
		if (this.borderSize != other.borderSize) {
			return false;
		}
		if (this.hoverBorderSize != other.hoverBorderSize) {
			return false;
		}
		return true;
	}

}
