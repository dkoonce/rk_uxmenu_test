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

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingConstants;

//import sun.awt.AWTAutoShutdown;
/**
 *
 * @author josh
 */
public class MenuLayouts {
    //Active Menu Layouts  (activeCard)
//    public static final int MAIN = 0;
//    public static final int BUTTONS12 = 1;

    public static final String TABBED4 = "Tabbed4";
    public static final String LOGO4BUTTON = "Logo4Button";
    public static final String LOGO3BUTTON = "Logo3Button";
    public static final String LOGO2BUTTON = "Logo2Button";
    public static final String PREVIOUSMENU = "LastMenu";
    public static final String KEYBOARD = "KeyBoard";

    public static final String ACTIVE_STYLE = "activeCard";
    public static final String SOUNDCARD = "SoundCard";
    public static final String WIFI = "Wifi";


    public static String keyBoardInput = "";

    String type = OldMenuStyles.MAIN;

    final List<UXPanelItem> allItems = new ArrayList<>();
    final List<SLConfig> SLCfgs = new ArrayList<>();
    final List<Runnable> menuAnimations = new ArrayList<>();
    Runnable exitAnimation = null;
    Runnable entranceAnimation = null;
    public boolean running = false;
    public boolean forceNoAnimation = false;

    public Runnable getExitAnimation() {
        return exitAnimation;
    }

    public Runnable getEntranceAnimation() {
        return entranceAnimation;
    }


    public SLConfig getPrimarySLCfg() {
        return SLCfgs.get(0);
    }

    public SLConfig getSLCfg(int i) {
        return SLCfgs.get(i);
    }

    //    public final static MenuLayouts create12Button(SLPanel panel) {
//        return new MenuLayouts(panel, BUTTONS12);
//    }
    public MenuLayouts(SLPanel panel) {
        this(panel, OldMenuStyles.MAIN);
    }

    public MenuLayouts(SLPanel panel, String type) {
        type = OldMenuStyles.MAIN;
    }

    public String getType() {
        return type;
    }

    public List<UXPanelItem> getAllItems() {
        return allItems;
    }

    List<UXButton> buildButtonItems(int buttonCount) {
        List<UXButton> buttons = new ArrayList<>();
        for (int i = 0; i < buttonCount; i++) {
            UXButton button = UXButton.createButton(i);
            buttons.add(button);
            allItems.add(button);
        }
        return buttons;
    }
    List<UXButton>  buildButtonItemsGapped(int buttonCount,int gap) {
        List<UXButton> buttons = new ArrayList<>();
        for (int i = 0; i < buttonCount; i++) {
            UXButton button = UXButton.createButton(i);
            button.setGap(gap);
            button.setBorderRoundness(Integer.toString(gap));
            buttons.add(button);
            allItems.add(button);
        }
        return buttons;
    }
    private List<UXButton> buildImageItems(int imageCount) {
        List<UXButton> images = new ArrayList<>();
        for (int i = 0; i < imageCount; i++) {
            UXButton image = UXButton.createImage(i);
            images.add(image);
            allItems.add(image);
        }
        return images;
    }

    List<UXButton> buildTextItems(int textCount) {
        List<UXButton> texts = new ArrayList<>();
        for (int i = 0; i < textCount; i++) {
            UXButton text = UXButton.createText(i);
            text.label.setHorizontalAlignment(SwingConstants.LEFT);
            texts.add(text);
            allItems.add(text);
        }
        return texts;
    }

//    public final static MenuLayouts createKeyBoard(final SLPanel panel){
//        final MenuLayouts ml = new MenuLayouts(panel);
//        ml.type = KEYBOARD;
//        keyBoardInput = "";
//        final UXButton prompt = UXButton.createTitle("Prompt");
//        final UXButton input = UXButton.createSubTitle("Input");
//        final List<UXButton> buttons = ml.buildKeyBoardButtons();
//
//
//    }

    public final static MenuLayouts createLogo3ButtonMenu(final SLPanel panel) {
        final MenuLayouts ml = new MenuLayouts(panel);
        ml.type = LOGO3BUTTON;
        final List<UXButton> buttons = ml.buildButtonItems(3);
        final List<UXButton> images = ml.buildImageItems(1);

        buttons.get(0).setStdImageIconPath(UXMenu.UTIL_ICON);
        buttons.get(1).setStdImageIconPath(UXMenu.DEMO_ICON);
        buttons.get(2).setStdImageIconPath(UXMenu.POWER_ICON);
        buttons.get(0).setHoverImageIconPath(UXMenu.UTIL_ICON_HOVER);
        buttons.get(1).setHoverImageIconPath(UXMenu.DEMO_ICON_HOVER);
        buttons.get(2).setHoverImageIconPath(UXMenu.POWER_ICON_HOVER);

        buttons.get(2).setActiveImageIconPath(UXMenu.TEST_GIF);
        buttons.get(2).setScript("sleep 3");
        images.get(0).setImagePath(UXMenu.LOGO_ON_WHITE_IMG);

        ml.SLCfgs.add(new SLConfig(panel)
                .gap(8, 8)
                .row(1f).row(50).col(1f)
                .place(0, 0, images.get(0))
                .beginGrid(1, 0)
                .row(1f).col(1f).col(50).col(50).col(50).col(50).col(1f)
                .place(0, 1, buttons.get(0))
                .place(0, 2, buttons.get(1))
                .place(0, 3, buttons.get(2))
                .endGrid()
        );

        ml.SLCfgs.add(new SLConfig(panel)
                .gap(0, 0)
                .row(1f).col(1f)
                .place(0, 0, images.get(0))
        );
        ml.menuAnimations.add(new Runnable() {
            @Override
            public void run() {
                SLAnimator.start();
                ml.disableActions();
                panel.createTransition() //move to logo only
                        .push(new SLKeyframe(ml.getPrimarySLCfg(), 0.6f)
                                .setDelay(0.6f, buttons.get(0), buttons.get(1), buttons.get(2))
                                .setStartSide(SLSide.LEFT, buttons.get(0))
                                .setStartSide(SLSide.BOTTOM, buttons.get(1))
                                .setStartSide(SLSide.RIGHT, buttons.get(2))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        images.get(0).setAction(ml.menuAnimations.get(1));
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
                        .push(new SLKeyframe(ml.getSLCfg(1), 0.6f)
                                .setDelay(0.6f, images.get(0))
                                .setEndSide(SLSide.LEFT, buttons.get(0))
                                .setEndSide(SLSide.BOTTOM, buttons.get(1))
                                .setEndSide(SLSide.RIGHT, buttons.get(2))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        images.get(0).setAction(ml.menuAnimations.get(0));
                                        SLAnimator.stop();
                                        ml.enableActions();
                                    }
                                }))
                        .play();
            }
        });

        images.get(0).setAction(ml.menuAnimations.get(1));
        ml.enableActions();
        return ml;
    }

    public final static MenuLayouts createWifiCard(final SLPanel panel) {
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

        ml.menuAnimations.add(new Runnable() {
            @Override
            public void run() {
                SLAnimator.start();
                ml.disableActions();
                panel.createTransition() //move to logo only
                        .push(new SLKeyframe(ml.getPrimarySLCfg(), 0.6f)
//                                .setDelay(0.6f, buttons.get(0), buttons.get(1), buttons.get(2))
                                .setStartSide(SLSide.LEFT, texts.get(0))
                                .setStartSide(SLSide.LEFT, texts.get(1))
                                .setStartSide(SLSide.LEFT, texts.get(2))
                                .setStartSide(SLSide.LEFT, texts.get(3))
                                .setStartSide(SLSide.LEFT, texts.get(4))
                                .setStartSide(SLSide.LEFT, texts.get(5))
                                .setStartSide(SLSide.LEFT, texts.get(6))
                                .setStartSide(SLSide.LEFT, texts.get(7))
                                .setStartSide(SLSide.LEFT, texts.get(8))

                                .setStartSide(SLSide.LEFT, buttons.get(0))
                                .setStartSide(SLSide.BOTTOM, buttons.get(1))
                                .setStartSide(SLSide.RIGHT, buttons.get(2))
                                .setStartSide(SLSide.LEFT,buttons.get(3))
                                .setStartSide(SLSide.BOTTOM, buttons.get(4))
                                .setStartSide(SLSide.RIGHT, buttons.get(5))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
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
                        .push(new SLKeyframe(ml.getSLCfg(1), 0.6f)
                                .setEndSide(SLSide.LEFT, texts.get(0))
                                .setEndSide(SLSide.LEFT, texts.get(1))
                                .setEndSide(SLSide.LEFT, texts.get(2))
                                .setEndSide(SLSide.LEFT, texts.get(3))
                                .setEndSide(SLSide.LEFT, texts.get(4))
                                .setEndSide(SLSide.LEFT, texts.get(5))
                                .setEndSide(SLSide.LEFT, texts.get(6))
                                .setEndSide(SLSide.LEFT, texts.get(7))
                                .setEndSide(SLSide.LEFT, texts.get(8))

                                .setEndSide(SLSide.LEFT, buttons.get(0))
                                .setEndSide(SLSide.BOTTOM, buttons.get(1))
                                .setEndSide(SLSide.RIGHT, buttons.get(2))
                                .setEndSide(SLSide.LEFT,buttons.get(3))
                                .setEndSide(SLSide.BOTTOM, buttons.get(4))
                                .setEndSide(SLSide.RIGHT, buttons.get(5))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        SLAnimator.stop();
                                        ml.enableActions();
                                    }
                                }))
                        .play();
            }
        });


        return ml;
    }

    public final static MenuLayouts createSoundLayout(final SLPanel panel) {
        final MenuLayouts ml = new MenuLayouts(panel);
        ml.type = SOUNDCARD;
        final List<UXButton> buttons = ml.buildButtonItems(11);

        buttons.get(0).setStdImageIconPath(UXMenu.VOLUME_UP);
        buttons.get(7).setStdImageIconPath(UXMenu.VOLUME_DOWN);

        ml.SLCfgs.add(new SLConfig(panel)
                .gap(8, 8)
                .row(1f).row(50).col(1f)

                .beginGrid(7, 3)
                .row(2f).row(1f).row(1f).row(1f).row(1f).row(1f).row(2f).col(1f).col(1f).col(1f)
                .place(0,1,buttons.get(0))
                .place(1,1,buttons.get(1))
                .place(2,1,buttons.get(2))
                .place(3,1,buttons.get(3))
                .place(4,1,buttons.get(4))
                .place(5,1,buttons.get(5))
                .place(6,1,buttons.get(6))
                .place(6,0,buttons.get(7))
                .place(6,2,buttons.get(8))
                .endGrid()
        );
//        ml.SLCfgs.add(new SLConfig(panel)
//                .gap(0, 0)
//                .row(1f).col(1f)
//                .place(0, 0, images.get(0))
//        );
        ml.menuAnimations.add(new Runnable() {
            @Override
            public void run() {
                SLAnimator.start();
                ml.disableActions();
                panel.createTransition() //move to logo only
                        .push(new SLKeyframe(ml.getPrimarySLCfg(), 0.6f)
//                                .setDelay(0.6f, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3))
                                .setStartSide(SLSide.LEFT, buttons.get(0))
                                .setStartSide(SLSide.LEFT, buttons.get(1))
                                .setStartSide(SLSide.RIGHT, buttons.get(2))
                                .setStartSide(SLSide.RIGHT, buttons.get(3))
                                .setStartSide(SLSide.LEFT, buttons.get(4))
                                .setStartSide(SLSide.LEFT, buttons.get(5))
                                .setStartSide(SLSide.RIGHT, buttons.get(6))
                                .setStartSide(SLSide.RIGHT, buttons.get(7))
                                .setStartSide(SLSide.LEFT, buttons.get(8))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
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
                        .push(new SLKeyframe(ml.getSLCfg(1), 0.6f)
                                .setEndSide(SLSide.LEFT, buttons.get(0))
                                .setEndSide(SLSide.LEFT, buttons.get(1))
                                .setEndSide(SLSide.RIGHT, buttons.get(2))
                                .setEndSide(SLSide.RIGHT, buttons.get(3))
                                .setEndSide(SLSide.LEFT, buttons.get(4))
                                .setEndSide(SLSide.LEFT, buttons.get(5))
                                .setEndSide(SLSide.RIGHT, buttons.get(6))
                                .setEndSide(SLSide.RIGHT, buttons.get(7))
                                .setEndSide(SLSide.LEFT, buttons.get(8))
                                .setEndSide(SLSide.LEFT, buttons.get(8))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        SLAnimator.stop();
                                        ml.enableActions();
                                    }
                                }))
                        .play();
            }
        });

        ml.enableActions();
        return ml;
    }


    public final static MenuLayouts createLogo4ButtonMenu(final SLPanel panel) {
        final MenuLayouts ml = new MenuLayouts(panel);
        ml.type = LOGO4BUTTON;
        final List<UXButton> buttons = ml.buildButtonItems(4);
        final List<UXButton> images = ml.buildImageItems(1);

        buttons.get(0).setStdImageIconPath(UXMenu.DEMO_ICON);
        buttons.get(1).setStdImageIconPath(UXMenu.UTIL_ICON);
        buttons.get(2).setStdImageIconPath(UXMenu.POWER_ICON);
        buttons.get(3).setStdImageIconPath(UXMenu.VOLUME_ICON);
        buttons.get(0).setHoverImageIconPath(UXMenu.DEMO_ICON_HOVER);
        buttons.get(1).setHoverImageIconPath(UXMenu.UTIL_ICON_HOVER);
        buttons.get(2).setHoverImageIconPath(UXMenu.POWER_ICON_HOVER);
        buttons.get(3).setHoverImageIconPath(UXMenu.VOLUME_ICON_HOVER);

        buttons.get(3).setActiveImageIconPath(UXMenu.TEST_GIF);
        buttons.get(3).setScript("sleep 3");
        images.get(0).setImagePath(UXMenu.LOGO_ON_WHITE_IMG);

        ml.SLCfgs.add(new SLConfig(panel)
                .gap(8, 8)
                .row(1f).row(50).col(1f)
                .place(0, 0, images.get(0))
                .beginGrid(1, 0)
                .row(1f).col(1f).col(50).col(50).col(50).col(50).col(1f)
                .place(0, 1, buttons.get(0))
                .place(0, 2, buttons.get(1))
                .place(0, 3, buttons.get(2))
                .place(0, 4, buttons.get(3))
                .endGrid()
        );
        ml.SLCfgs.add(new SLConfig(panel)
                .gap(0, 0)
                .row(1f).col(1f)
                .place(0, 0, images.get(0))
        );
        ml.menuAnimations.add(new Runnable() {
            @Override
            public void run() {
                SLAnimator.start();
                ml.disableActions();
                panel.createTransition() //move to logo only
                        .push(new SLKeyframe(ml.getPrimarySLCfg(), 0.6f)
                                .setDelay(0.6f, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3))
                                .setStartSide(SLSide.LEFT, buttons.get(0))
                                .setStartSide(SLSide.LEFT, buttons.get(1))
                                .setStartSide(SLSide.RIGHT, buttons.get(2))
                                .setStartSide(SLSide.RIGHT, buttons.get(3))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        images.get(0).setAction(ml.menuAnimations.get(1));
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
                        .push(new SLKeyframe(ml.getSLCfg(1), 0.6f)
                                .setDelay(0.6f, images.get(0))
                                .setEndSide(SLSide.LEFT, buttons.get(0))
                                .setEndSide(SLSide.LEFT, buttons.get(1))
                                .setEndSide(SLSide.RIGHT, buttons.get(2))
                                .setEndSide(SLSide.RIGHT, buttons.get(3))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        images.get(0).setAction(ml.menuAnimations.get(0));
                                        SLAnimator.stop();
                                        ml.enableActions();
                                    }
                                }))
                        .play();
            }
        });

        images.get(0).setAction(ml.menuAnimations.get(1));
        ml.enableActions();
        return ml;
    }

    public final static MenuLayouts create4TabbedMenu(final SLPanel panel) {
        final MenuLayouts ml = new MenuLayouts(panel);
        ml.type = TABBED4;

        final List<UXButton> texts = ml.buildTextItems(4);

//        title.label.setHorizontalAlignment(SwingConstants.LEFT);
//        subtitle.label.setHorizontalAlignment(SwingConstants.LEFT);
        ml.SLCfgs.add(new SLConfig(panel)
                .gap(2, 2)
                .row(1f).row(30).row(30).row(30).col(1f)
                .place(0, 0, texts.get(0))//top text
                .place(1, 0, texts.get(1))//bottom text
                .place(2, 0, texts.get(2))
                .place(3, 0, texts.get(3)));
        ml.SLCfgs.add(new SLConfig(panel)
                .gap(2, 2)
                .row(30).row(1f).row(30).row(30).col(1f)
                .place(0, 0, texts.get(0))//top text
                .place(1, 0, texts.get(1))//bottom text
                .place(2, 0, texts.get(2))
                .place(3, 0, texts.get(3)));
        ml.SLCfgs.add(new SLConfig(panel)
                .gap(2, 2)
                .row(30).row(30).row(1f).row(30).col(1f)
                .place(0, 0, texts.get(0))//top text
                .place(1, 0, texts.get(1))//bottom text
                .place(2, 0, texts.get(2))
                .place(3, 0, texts.get(3)));
        ml.SLCfgs.add(new SLConfig(panel)
                .gap(2, 2)
                .row(30).row(30).row(30).row(1f).col(1f)
                .place(0, 0, texts.get(0))//top text
                .place(1, 0, texts.get(1))//bottom text
                .place(2, 0, texts.get(2))
                .place(3, 0, texts.get(3)));

        ml.menuAnimations.add(new Runnable() {
            @Override
            public void run() {
                SLAnimator.start();
                ml.disableActions();
                panel.createTransition() //move to logo only
                        .push(new SLKeyframe(ml.getPrimarySLCfg(), 0.5f)
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {

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
                        .push(new SLKeyframe(ml.getSLCfg(1), 0.5f)
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {

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
                        .push(new SLKeyframe(ml.getSLCfg(2), 0.5f)
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {

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
                        .push(new SLKeyframe(ml.getSLCfg(3), 0.5f)
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {

                                        ml.enableActions();
                                        SLAnimator.stop();
                                    }
                                }))
                        .play();
            }
        });
        texts.get(0).setAction(ml.menuAnimations.get(0));
        texts.get(1).setAction(ml.menuAnimations.get(1));
        texts.get(2).setAction(ml.menuAnimations.get(2));
        texts.get(3).setAction(ml.menuAnimations.get(3));
        ml.enableActions();

        return ml;
    }

    public final static MenuLayouts emptyMenu(final SLPanel panel) {
        final MenuLayouts ml = new MenuLayouts(panel);
        ml.SLCfgs.add(new SLConfig(panel)
                .gap(0, 0)
                .row(1f).col(1f)
        );
        return ml;
    }

    public final static MenuLayouts createLogo2ButtonMenu(final SLPanel panel) {
        final MenuLayouts ml = new MenuLayouts(panel);
        ml.type = LOGO2BUTTON;
        final List<UXButton> buttons = ml.buildButtonItems(2);
        final List<UXButton> images = ml.buildImageItems(1);

        buttons.get(0).setStdImageIconPath(UXMenu.DEMO_ICON);
        buttons.get(1).setStdImageIconPath(UXMenu.UTIL_ICON);
//        buttons.get(2).setStdImageIconPath(UXMenu.POWER_ICON);
//        buttons.get(3).setStdImageIconPath(UXMenu.VOLUME_ICON);
        buttons.get(0).setHoverImageIconPath(UXMenu.DEMO_ICON_HOVER);
        buttons.get(1).setHoverImageIconPath(UXMenu.UTIL_ICON_HOVER);
//        buttons.get(2).setHoverImageIconPath(UXMenu.POWER_ICON_HOVER);
//        buttons.get(3).setHoverImageIconPath(UXMenu.VOLUME_ICON_HOVER);

        buttons.get(1).setActiveImageIconPath(UXMenu.TEST_GIF);
        buttons.get(1).setScript("sleep 3");
        images.get(0).setImagePath(UXMenu.LOGO_ON_WHITE_IMG);

        ml.SLCfgs.add(new SLConfig(panel)
                .gap(8, 8)
                .row(1f).row(100).col(1f)
                .place(0, 0, images.get(0))
                .beginGrid(1, 0)
                .row(1f).col(1f).col(1f)
                .place(0, 0, buttons.get(0))
                .place(0, 1, buttons.get(1))
                .endGrid()
        );
        ml.SLCfgs.add(new SLConfig(panel)
                .gap(0, 0)
                .row(1f).col(1f)
                .place(0, 0, images.get(0))
        );
        ml.menuAnimations.add(new Runnable() {
            @Override
            public void run() {
                SLAnimator.start();
                ml.disableActions();
                panel.createTransition() //move to logo only
                        .push(new SLKeyframe(ml.getPrimarySLCfg(), 0.6f)
                                .setDelay(0.6f, buttons.get(0), buttons.get(1))
                                .setStartSide(SLSide.LEFT, buttons.get(0))
                                .setStartSide(SLSide.RIGHT, buttons.get(1))
                                //                                .setStartSide(SLSide.RIGHT, buttons.get(2))
                                //                                .setStartSide(SLSide.RIGHT, buttons.get(3))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        images.get(0).setAction(ml.menuAnimations.get(1));
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
                        .push(new SLKeyframe(ml.getSLCfg(1), 0.6f)
                                .setDelay(0.6f, images.get(0))
                                .setEndSide(SLSide.LEFT, buttons.get(0))
                                .setEndSide(SLSide.RIGHT, buttons.get(1))
                                //                                .setEndSide(SLSide.RIGHT, buttons.get(2))
                                //                                .setEndSide(SLSide.RIGHT, buttons.get(3))
                                .setCallback(new SLKeyframe.Callback() {
                                    @Override
                                    public void done() {
                                        images.get(0).setAction(ml.menuAnimations.get(0));
                                        SLAnimator.stop();
                                        ml.enableActions();
                                    }
                                }))
                        .play();
            }
        });

        images.get(0).setAction(ml.menuAnimations.get(1));
        ml.enableActions();
        return ml;
    }

    void disableActions() {
        for (UXPanelItem item : allItems) {
            item.disableAction();
        }
    }

    void enableActions() {
        for (UXPanelItem item : allItems) {
            item.enableAction();
        }
    }

}
