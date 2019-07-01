/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import aurelienribon.tweenengine.Tween;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author josh
 */
public class UXMenu {

	public static UXStyle style = UXStyleFactory.createUXStyle();
	private static final int MAJOR = 0;
	private static final int MINOR = 5;
	private static UXFrame frame;
	public static String tempDefaultAction = "";

	public static String BACKGROUND_IMG = "menu_background_black.png";
	public static String LOGO_ON_BLACK_IMG = "logo_on_black.png";
	public static String LOGO_ON_WHITE_IMG = "logo_on_orange.png";

	public static String DEMO_ICON = "icons/btn_demo.png";
	public static String UTIL_ICON = "icons/btn_util.png";
	public static String POWER_ICON = "icons/btn_power.png";
	public static String VOLUME_ICON = "icons/btn_volume.png";

	public static String VOLUME_UP = "icons/Up.png";
	public static String VOLUME_DOWN = "icons/Down.png";

	public static String DEMO_ICON_HOVER = "icons/btn_demo_clicked.png";
	public static String UTIL_ICON_HOVER = "icons/btn_util_clicked.png";
	public static String POWER_ICON_HOVER = "icons/btn_power_clicked.png";
	public static String VOLUME_ICON_HOVER = "icons/btn_volume_clicked.png";

	public static String TEST_GIF = "test_gif.gif";

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Starting UX Display Version " + MAJOR + "." + MINOR);

		Fonts.loadFonts();
		Tween.registerAccessor(UXPanelItem.class, new UXPanelItem.Accessor());

		frame = new UXFrame();

		if (args.length == 1) {
			try {
				System.out.println(args[0]);
				Runtime.getRuntime().exec(args[0]);
			} catch (IOException ex) {
				System.err.println("Cannot execute init menu:");
				ex.printStackTrace();
			}
		}

		frame.setVisible(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (screenSize.width < 350) {
			frame.hideCursor(true);
		} else {
			frame.hideCursor(false);
		}

		FileWatcher fileWatcher = new FileWatcher("audioMuteLineInVolumeTracker","/usr/robokind/etc/") {
			@Override
			protected void hasChanged() {
				String[] inputs = new String[3];
				inputs = this.getValue().split(" ");
//				System.err.println(inputs);
			}
		};
	}

	

}
