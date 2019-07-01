/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import com.rkbots.tools.app.uxmenu.slidinglayout.SLAnimator;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLConfig;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLKeyframe;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLPanel;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLSide;

import java.util.List;

import javax.swing.*;

/**
 * @author josh
 */
@Deprecated
public class OldMenuStyles extends MenuLayouts {

	public static final String BIGIMG = "BigLogoCard";
	public static final String MAIN = "DefaultCard";
	public static final String WIFI = "WifiCard";
	public static final String BUTTONS12 = "ButtonCard";
	public static final String BUTTONS20 = "DataEntryCard";
	public static final String TEXTS9 = "WallOfTextCard";
	public static final String PASSIVEICON = "PassiveIconCard";
	public static final String SOUND= "Sound";

	@Deprecated
	private OldMenuStyles(SLPanel panel, String type) {
		super(panel, type);
	}

	/**
	 * @deprecated
	 */

	@Deprecated
	public final static MenuLayouts createMainMenu(final SLPanel panel) {
		panel.setOpaque(false);
		final MenuLayouts ml = new MenuLayouts(panel);
		ml.type = MAIN;

		final UXButton title = UXButton.createTitle("");
		final UXButton subtitle = UXButton.createSubTitle("");
		final UXButton logo = UXButton.createImage();

		logo.setImagePath(UXMenu.LOGO_ON_WHITE_IMG);

		ml.allItems.add(title);
		ml.allItems.add(subtitle);
		ml.allItems.add(logo);
		final List<UXButton> buttons = ml.buildButtonItems(3);

//        title.label.setHorizontalAlignment(SwingConstants.LEFT);
//        subtitle.label.setHorizontalAlignment(SwingConstants.LEFT);
		ml.SLCfgs.add(new SLConfig(panel)
				.gap(2, 2)
				.row(20).row(20).row(1f).col(1f)
				.place(0, 0, title)//top text
				.place(1, 0, subtitle)//bottom text
				.beginGrid(2, 0)
				.row(1f).row(2f).row(1f).col(1f)
//				.beginGrid(0, 0)
//				.row(1f).col(1f).col(1f).col(1f)
//				.place(0, 0, buttons.get(0))
//				.place(0, 1, buttons.get(1))
//				.place(0, 2, buttons.get(2))
//				.endGrid()
				.place(1, 0, logo)
				.beginGrid(2, 0)
				.row(1f).col(1f).col(1f).col(1f)
				.place(0, 0, buttons.get(0))
				.place(0, 1, buttons.get(1))
				.place(0, 2, buttons.get(2))
				.endGrid()
				.endGrid());
		ml.SLCfgs.add(new SLConfig(panel)
				.gap(0, 0)
				.row(1f).col(1f)
				.place(0, 0, logo));

		ml.exitAnimation = new Runnable() {
			@Override
			public void run() {
				ml.running = true;
				SLAnimator.start();
				ml.disableActions();
				panel.createTransition() //move to logo only
						.push(new SLKeyframe(ml.getPrimarySLCfg(), 0.5f)
								.setEndSide(SLSide.LEFT, logo)
								.setEndSide(SLSide.TOP, title)
								.setEndSide(SLSide.TOP, subtitle)
//								.setEndSide(SLSide.LEFT, buttons.get(0))
//								.setEndSide(SLSide.TOP, buttons.get(1))
//								.setEndSide(SLSide.RIGHT, buttons.get(2))
								.setEndSide(SLSide.LEFT, buttons.get(0))
								.setEndSide(SLSide.BOTTOM, buttons.get(1))
								.setEndSide(SLSide.RIGHT, buttons.get(2))
								.setCallback(new SLKeyframe.Callback() {
									@Override
									public void done() {
										ml.running = false;
										SLAnimator.stop();
									}
								}))
						.play();
			}
		};
		ml.entranceAnimation = new Runnable() {
			@Override
			public void run() {
				ml.running = true;
				SLAnimator.start();
				ml.disableActions();
				panel.createTransition() //bring frames back
						.push(new SLKeyframe(ml.getPrimarySLCfg(), 0.5f)
								.setStartSide(SLSide.TOP, title)
								.setStartSide(SLSide.TOP, subtitle)
//								.setStartSide(SLSide.LEFT, buttons.get(0))
//								.setStartSide(SLSide.TOP, buttons.get(1))
								.setStartSide(SLSide.BOTTOM, logo)
//								.setStartSide(SLSide.RIGHT, buttons.get(2))
								.setStartSide(SLSide.LEFT, buttons.get(0))
								.setStartSide(SLSide.BOTTOM, buttons.get(1))
								.setStartSide(SLSide.RIGHT, buttons.get(2))
								.setDelay(0.5f, buttons.get(0), buttons.get(1),
										buttons.get(2))
								.setCallback(new SLKeyframe.Callback() {
									@Override
									public void done() {
										//logo.setAction(ml.menuAnimations.get(0));
										ml.enableActions();
										SLAnimator.stop();
										ml.running = false;
									}
								}))
						.play();
			}
		};

		ml.menuAnimations.add(new Runnable() {
			@Override
			public void run() {
				SLAnimator.start();
				ml.disableActions();
				panel.createTransition() //move to logo only
						.push(new SLKeyframe(ml.SLCfgs.get(1), 0.5f)
								.setDelay(0.5f, logo)
								.setEndSide(SLSide.TOP, title)
								.setEndSide(SLSide.TOP, subtitle)
//								.setEndSide(SLSide.LEFT, buttons.get(0))
//								.setEndSide(SLSide.TOP, buttons.get(1))
//								.setEndSide(SLSide.RIGHT, buttons.get(2))
								.setEndSide(SLSide.LEFT, buttons.get(0))
								.setEndSide(SLSide.BOTTOM, buttons.get(1))
								.setEndSide(SLSide.RIGHT, buttons.get(2))
								.setCallback(new SLKeyframe.Callback() {
									@Override
									public void done() {
										logo.setAction(ml.menuAnimations.get(1));
										ml.enableActions();
										SLAnimator.stop();
									}
								}))
						.play();
			}
		});
		ml.menuAnimations.add(new Runnable() {
			@Override
			public void run() {
				SLAnimator.start();
				ml.disableActions();
				panel.createTransition() //bring frames back
						.push(new SLKeyframe(ml.getPrimarySLCfg(), 0.5f)
								.setStartSide(SLSide.TOP, title)
								.setStartSide(SLSide.TOP, subtitle)
//								.setStartSide(SLSide.LEFT, buttons.get(0))
//								.setStartSide(SLSide.TOP, buttons.get(1))
//								//
//								.setStartSide(SLSide.RIGHT, buttons.get(2))
								.setStartSide(SLSide.LEFT, buttons.get(0))
								.setStartSide(SLSide.BOTTOM, buttons.get(1))
								.setStartSide(SLSide.RIGHT, buttons.get(2))
								.setDelay(0.5f, buttons.get(0), buttons.get(1),
										buttons.get(2))
								.setCallback(new SLKeyframe.Callback() {
									@Override
									public void done() {
										logo.setAction(ml.menuAnimations.get(0));
										ml.enableActions();
										SLAnimator.stop();
									}
								}))
						.play();
			}
		});

		logo.setAction(ml.menuAnimations.get(0));
		ml.enableActions();

		return ml;
	}

	@Deprecated
	public final static MenuLayouts createInfoMenu(final SLPanel panel) {
		final MenuLayouts ml = new MenuLayouts(panel);
		ml.type = MAIN;
		final List<UXButton> buttons = ml.buildButtonItems(6);
		final List<UXButton> texts = ml.buildTextItems(9);
		final SLConfig slConfig = new SLConfig(panel);
		final SLConfig slConfig1 = new SLConfig(panel);
		// <editor-fold defaultstate="collapsed" desc="Old Style text handlers">
		//These handlers are only to maintain backward compatibility with old .properties files
		//New handler key values should be used (format is "text1" "text2" ....)
		//  text can now act as a button and run corresponding scripts "text1Script" ...)
		texts.get(0).addHandler(new PropertyMonitor.PropHandler("topText") {
			@Override
			public void updateProperty(String value) {
				texts.get(0).setText(value);
			}
		});
		texts.get(1).addHandler(new PropertyMonitor.PropHandler("secondText") {
			@Override
			public void updateProperty(String value) {
				texts.get(1).setText(value);
			}
		});
		texts.get(2).addHandler(new PropertyMonitor.PropHandler("thirdText") {
			@Override
			public void updateProperty(String value) {
				texts.get(2).setText(value);
			}
		});
		texts.get(3).addHandler(new PropertyMonitor.PropHandler("fourthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(3).setText(value);
			}
		});
		texts.get(4).addHandler(new PropertyMonitor.PropHandler("fifthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(4).setText(value);
			}
		});
		texts.get(5).addHandler(new PropertyMonitor.PropHandler("sixthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(5).setText(value);
			}
		});
		texts.get(6).addHandler(new PropertyMonitor.PropHandler("seventhText") {
			@Override
			public void updateProperty(String value) {
				texts.get(6).setText(value);
			}
		});
		texts.get(7).addHandler(new PropertyMonitor.PropHandler("eighthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(7).setText(value);
			}
		});
		texts.get(8).addHandler(new PropertyMonitor.PropHandler("bottomText") {
			@Override
			public void updateProperty(String value) {
				texts.get(8).setText(value);
			}
		});
		// </editor-fold>

		int i, r, c;
		ml.SLCfgs.add(slConfig
				.gap(2,2)
				.row(20).row(20).row(20).row(20).row(20).row(20).row(20).row(20).row(20).row(1f).col(1f)
				.place(r = 0, c = 0, texts.get(i = 0))//top text
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))//bottom text
				.beginGrid(9, 0)
				.row(1f).row(1f).col(1f).col(1f).col(1f)
				.place(r = 0, c = 0, buttons.get(i = 0))
				.place(r, ++c, buttons.get(++i))
				.place(r, ++c, buttons.get(++i))
				.place(++r, c = 0, buttons.get(++i))
				.place(r, ++c, buttons.get(++i))
				.place(r, ++c, buttons.get(++i))
				.endGrid());
		return ml;
	}

	@Deprecated
	public final static MenuLayouts createWifiMenu(final SLPanel panel) {
		final MenuLayouts ml = new MenuLayouts(panel);
		ml.type = WIFI;
		final List<UXButton> buttons = ml.buildButtonItemsGapped(6,2);
		final List<UXButton> texts = ml.buildTextItems(9);
		final SLConfig slConfig = new SLConfig(panel);
		final SLConfig slConfig1 = new SLConfig(panel);
		// <editor-fold defaultstate="collapsed" desc="Old Style text handlers">
		//These handlers are only to maintain backward compatibility with old .properties files
		//New handler key values should be used (format is "text1" "text2" ....)
		//  text can now act as a button and run corresponding scripts "text1Script" ...)
		texts.get(0).addHandler(new PropertyMonitor.PropHandler("topText") {
			@Override
			public void updateProperty(String value) {
				texts.get(0).setText(value);
			}
		});
		texts.get(1).addHandler(new PropertyMonitor.PropHandler("secondText") {
			@Override
			public void updateProperty(String value) {
				texts.get(1).setText(value);
			}
		});
		texts.get(2).addHandler(new PropertyMonitor.PropHandler("thirdText") {
			@Override
			public void updateProperty(String value) {
				texts.get(2).setText(value);
			}
		});
		texts.get(3).addHandler(new PropertyMonitor.PropHandler("fourthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(3).setText(value);
			}
		});
		texts.get(4).addHandler(new PropertyMonitor.PropHandler("fifthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(4).setText(value);
			}
		});
		texts.get(5).addHandler(new PropertyMonitor.PropHandler("sixthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(5).setText(value);
			}
		});
		texts.get(6).addHandler(new PropertyMonitor.PropHandler("seventhText") {
			@Override
			public void updateProperty(String value) {
				texts.get(6).setText(value);
			}
		});
		texts.get(7).addHandler(new PropertyMonitor.PropHandler("eighthText") {
			@Override
			public void updateProperty(String value) {
				texts.get(7).setText(value);
			}
		});
		texts.get(8).addHandler(new PropertyMonitor.PropHandler("bottomText") {
			@Override
			public void updateProperty(String value) {
				texts.get(8).setText(value);
			}
		});
		// </editor-fold>

		int i, r, c;
		ml.SLCfgs.add(slConfig
				.gap(0,0)
				.row(20).row(20).row(20).row(20).row(20).row(20).row(20).row(20).row(20).row(1f).col(1f)
				.place(r = 0, c = 0, texts.get(i = 0))//top text
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))
				.place(++r, c, texts.get(++i))//bottom text
				.beginGrid(9, 0)
				.row(1f).row(1f).col(1f).col(1f).col(1f)
				.place(r = 0, c = 0, buttons.get(i = 0))
				.place(r, ++c, buttons.get(++i))
				.place(r, ++c, buttons.get(++i))
				.place(++r, c = 0, buttons.get(++i))
				.place(r, ++c, buttons.get(++i))
				.place(r, ++c, buttons.get(++i))
				.endGrid());
		return ml;
	}
	@Deprecated
	public final static MenuLayouts create12ButtonMenu(SLPanel panel) {
		MenuLayouts ml = new MenuLayouts(panel);
		ml.type = BUTTONS12;
		List<UXButton> buttons = ml.buildButtonItems(12);

		UXButton title = UXButton.createTitle("Title");
		UXButton subtitle = UXButton.createSubTitle("Sub-Title");
		title.label.setHorizontalAlignment(SwingConstants.LEFT);
		subtitle.label.setHorizontalAlignment(SwingConstants.LEFT);

		ml.allItems.add(title);
		ml.allItems.add(subtitle);

		ml.SLCfgs.add(new SLConfig(panel)
				.gap(2, 2)
				.row(20).row(20).row(1f).col(1f)
				.place(0, 0, title)//top text
				.place(1, 0, subtitle)//bottom text
				.beginGrid(2, 0)
				.row(1f).row(1f).row(1f).row(1f).col(1f).col(1f).col(1f)
				.place(0, 0, buttons.get(0))
				.place(0, 1, buttons.get(1))
				.place(0, 2, buttons.get(2))
				.place(1, 0, buttons.get(3))
				.place(1, 1, buttons.get(4))
				.place(1, 2, buttons.get(5))
				.place(2, 0, buttons.get(6))
				.place(2, 1, buttons.get(7))
				.place(2, 2, buttons.get(8))
				.place(3, 0, buttons.get(9))
				.place(3, 1, buttons.get(10))
				.place(3, 2, buttons.get(11))
				.endGrid());
		return ml;
	}

	@Deprecated
	public final static MenuLayouts createSoundMenu(SLPanel panel) {
		MenuLayouts ml = new MenuLayouts(panel);
		ml.type = SOUNDCARD;
		List<UXButton> buttons = ml.buildButtonItems(12);

		UXButton title = UXButton.createTitle("Title");
		UXButton subtitle = UXButton.createSubTitle("Sub-Title");
		title.label.setHorizontalAlignment(SwingConstants.LEFT);
		subtitle.label.setHorizontalAlignment(SwingConstants.LEFT);

		ml.allItems.add(title);
		ml.allItems.add(subtitle);

		ml.SLCfgs.add(new SLConfig(panel)
				.gap(2, 2)
				.row(20).row(20).row(1f).col(1f)
				.place(0, 0, title)//top text
				.place(1, 0, subtitle)//bottom text
				.beginGrid(2,0)
				.row(1f).row(.2f).row(.2f).row(.2f).row(.2f).row(.2f).row(1f).col(1f).col(1f).col(1f)
				.place(0,1,buttons.get(3))
				.place(0,2,buttons.get(0))
				.place(1,1,buttons.get(6))
				.place(2,1,buttons.get(7))
				.place(3,1,buttons.get(8))
				.place(4,1,buttons.get(9))
				.place(5,1,buttons.get(10))
				.place(6,1,buttons.get(1))
				.place(6,0,buttons.get(4))
				.place(6,2,buttons.get(2))
				.endGrid());
		return ml;
	}

	@Deprecated
	public final static MenuLayouts create20ButtonMenu(SLPanel panel) {
		MenuLayouts ml = new MenuLayouts(panel);
		ml.type = BUTTONS12;
		List<UXButton> buttons = ml.buildButtonItems(20);

		UXButton title = UXButton.createTitle("Title");
		UXButton subtitle = UXButton.createSubTitle("Sub-Title");
		title.label.setHorizontalAlignment(SwingConstants.LEFT);
		subtitle.label.setHorizontalAlignment(SwingConstants.LEFT);

		ml.allItems.add(title);
		ml.allItems.add(subtitle);

		ml.SLCfgs.add(new SLConfig(panel)
				.gap(2, 2)
				.row(20).row(20).row(1f).col(1f)
				.place(0, 0, title)//top text
				.place(1, 0, subtitle)//bottom text
				.beginGrid(2, 0)
				.row(1f).row(1f).row(1f).row(1f).row(1f).col(1f).col(1f).col(1f).col(1f)
				.place(0, 0, buttons.get(0))
				.place(0, 1, buttons.get(1))
				.place(0, 2, buttons.get(2))
				.place(0, 3, buttons.get(3))
				.place(1, 0, buttons.get(4))
				.place(1, 1, buttons.get(5))
				.place(1, 2, buttons.get(6))
				.place(1, 3, buttons.get(7))
				.place(2, 0, buttons.get(8))
				.place(2, 1, buttons.get(9))
				.place(2, 2, buttons.get(10))
				.place(2, 3, buttons.get(11))
				.place(3, 0, buttons.get(12))
				.place(3, 1, buttons.get(13))
				.place(3, 2, buttons.get(14))
				.place(3, 3, buttons.get(15))
				.place(4, 0, buttons.get(16))
				.place(4, 1, buttons.get(17))
				.place(4, 2, buttons.get(18))
				.place(4, 3, buttons.get(19))
				.endGrid());
		return ml;
	}

	@Deprecated
	public final static MenuLayouts createFullScreenLogoMenu(SLPanel panel) {
		MenuLayouts ml = new MenuLayouts(panel);
		ml.type = BIGIMG;

		final UXButton logo = UXButton.createButton(0);

		logo.addHandler(new PropertyMonitor.PropHandler(UXButton.BIG_IMG_PATH) {
			@Override
			public void updateProperty(String value) {
				logo.setStdImageIconPath(value);
			}
		});
		logo.addHandler(new PropertyMonitor.PropHandler(UXButton.BIG_IMG_SCRIPT) {
			@Override
			public void updateProperty(String value) {
				logo.setScript(value);
			}
		});

		ml.allItems.add(logo);

		ml.SLCfgs.add(new SLConfig(panel)
				.gap(0, 0)
				.row(1f).col(1f)
				.place(0, 0, logo));

		return ml;
	}

	@Deprecated
	public final static MenuLayouts createPassiveIconMenu(SLPanel panel) {
		MenuLayouts ml = new MenuLayouts(panel);
		ml.type = PASSIVEICON;
		ml.forceNoAnimation = true;
		final UXButton logo = UXButton.createButton(0);

		logo.addHandler(new PropertyMonitor.PropHandler(UXButton.BIG_IMG_PATH) {
			@Override
			public void updateProperty(String value) {
				logo.setStdImageIconPath(value);
			}
		});
		logo.addHandler(new PropertyMonitor.PropHandler(UXButton.BIG_IMG_SCRIPT) {
			@Override
			public void updateProperty(String value) {
				logo.setScript(value);
			}
		});

		ml.allItems.add(logo);

		ml.SLCfgs.add(new SLConfig(panel)
				.gap(0, 0)
				.row(1f).col(1f)
				.place(0, 0, logo));

		return ml;
	}
}
