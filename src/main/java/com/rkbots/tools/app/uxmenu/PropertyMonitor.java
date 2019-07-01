/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import com.rkbots.tools.app.uxmenu.slidinglayout.SLPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author josh
 */
public class PropertyMonitor implements Runnable {

	private static final Logger theLogger = LoggerFactory.getLogger(PropertyMonitor.class);

	private final UXFrame frame;
	private final SLPanel panel;
	private MenuLayouts newMenu;

	private static final Map<String, List<PropHandler>> myHandlerMap = new HashMap<>();

	public static Map<String, List<PropHandler>> getMyHandlerMap() {
		return myHandlerMap;
	}

	public PropertyMonitor(UXFrame frame, SLPanel panel) {
		this.panel = panel;
		this.frame = frame;
	}

	public static abstract class PropHandler {

		public String myKey;

		public PropHandler(String key) {
			myKey = key;
		}

		public String getPropKey() {
			return myKey;
		}

		public abstract void updateProperty(String value);

		@Override
		public String toString() {
			return "PropHandler{" + "myKey=" + myKey + '}';
		}


	}

	private MenuLayouts getMenu() {
		return newMenu;
	}

	@Override
	public void run() {
		//boolean newActiveCard, allNewButtons;
		while (true) {
//            newActiveCard = allNewButtons = false;
			Properties props = new Properties();

			try (InputStream pipe = new FileInputStream("/tmp/props_pipe")) {
				props.load(pipe);
				pipe.close();

				boolean hasActiveStyle = false;

				// Break up class properties and other properties, process class properties first
				Properties classProps = new Properties();
				for (Object prop : props.keySet()) {
					String key = prop.toString();
					Object val = props.getProperty(key);
					if (key.endsWith(UXButton.POST_CLASS)) {
						classProps.put(prop, val);
					} else if (key.equals(MenuLayouts.ACTIVE_STYLE)) {
						classProps.put(prop, val);
						hasActiveStyle = true;
					}
				}

				for (Object prop : classProps.keySet()) {
					props.remove(prop);
				}


				updateProperties(classProps, false);
				updateProperties(props, hasActiveStyle);
			} catch (IOException ex) {
				theLogger.warn("Cannot read properties.");
			}
		}
	}

	private void updateProperties(Properties props, boolean displayNewMenu) {
		BAD_MENU:
		{
			if (props.containsKey(MenuLayouts.ACTIVE_STYLE)) {
				myHandlerMap.clear();
				//System.out.println("Building New Menu: " + props.getProperty(MenuLayouts.ACTIVE_STYLE));
				switch (props.getProperty(MenuLayouts.ACTIVE_STYLE)) {
					case MenuLayouts.PREVIOUSMENU:
						newMenu = frame.getPreviousMenu();
						break;
					case MenuLayouts.TABBED4:
						buildNewMenu(MenuLayouts.create4TabbedMenu(panel), props);
						break;
					case MenuLayouts.LOGO3BUTTON:
						buildNewMenu(MenuLayouts.createLogo3ButtonMenu(panel), props);
						break;
//					case MenuLayouts.LOGO4BUTTON:
//						buildNewMenu(MenuLayouts.createLogo4ButtonMenu(panel), props);
//						break;
					case MenuLayouts.LOGO2BUTTON:
						buildNewMenu(MenuLayouts.createLogo2ButtonMenu(panel), props);
						break;
					case SysInfo.SYSINFO:
						buildNewMenu(SysInfo.createSysInfoMenu(panel), props);
						break;

					case "ButtonTonCard":   //Someone made a typo a long time ago this maintains compatibility
					case OldMenuStyles.BUTTONS12:
						buildNewMenu(OldMenuStyles.create12ButtonMenu(panel), props);
						break;
					case OldMenuStyles.WIFI:
						buildNewMenu(OldMenuStyles.createWifiMenu(panel),props);
						break;
					case OldMenuStyles.SOUND:
						buildNewMenu(OldMenuStyles.createSoundMenu(panel),props);
						break;
					case OldMenuStyles.BUTTONS20:
						buildNewMenu(OldMenuStyles.create20ButtonMenu(panel), props);
						break;
					case OldMenuStyles.TEXTS9:
						buildNewMenu(OldMenuStyles.createInfoMenu(panel), props);
						break;
					case OldMenuStyles.BIGIMG:
						buildNewMenu(OldMenuStyles.createFullScreenLogoMenu(panel), props);
						break;
					case OldMenuStyles.PASSIVEICON:
						buildNewMenu(OldMenuStyles.createPassiveIconMenu(panel), props);
						break;
					case OldMenuStyles.MAIN:
						buildNewMenu(OldMenuStyles.createMainMenu(panel), props);
						break;
					default:
						updateProps(props);
						break BAD_MENU;
				}

			} else {
				updateProps(props);
			}

			//New Menu is ready to be displayed
			if (displayNewMenu) {
				frame.setNewMenuInQueue(newMenu);
			}
		}
	}

	private void buildNewMenu(MenuLayouts menu, Properties props) {
		newMenu = menu;
//        setupHandlers();
		updateProps(props);
	}

	private void updateProps(Properties props) {
		for (Map.Entry entry : props.entrySet()) {
			String key = entry.getKey().toString();
			String newValue = entry.getValue().toString();
			//System.out.println("Got Key: "+key+" = "+newValue);
			updateProp(key, newValue);
		}
	}

	private void updateProp(String key, String value) {
		List<PropHandler> handlers = myHandlerMap.get(key);
		if (handlers == null || handlers.isEmpty()) {
			System.err.println("Could not find handler for " + key + " " + value);
			return;
		}
		for (PropHandler handler : handlers) {
			handler.updateProperty(value);
		}
	}
}
