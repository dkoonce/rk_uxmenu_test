/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//                ClassLoader.getSystemClassLoader().loadClass(type)
package com.rkbots.tools.app.uxmenu;

import com.rkbots.tools.app.uxmenu.slidinglayout.SLAnimator;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLConfig;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLKeyframe;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLPanel;
import java.util.List;

/**
 *
 * @author josh
 */
public class SysInfo extends MenuLayouts {

    public static final String SYSINFO = "SystemInfo";
    public static final String NETWORK = "Network";

    private SysInfo(SLPanel panel, String type) {
        super(panel, type);
    }

    public final static MenuLayouts createSysInfoMenu(final SLPanel panel) {
        final MenuLayouts ml = new MenuLayouts(panel);
        ml.type = SYSINFO;

        final List<UXButton> texts = ml.buildTextItems(4);

        texts.get(0).setText("Top Info Page");
        texts.get(3).setText("Bottom Info Page");
        NetworkInfo ni = new NetworkInfo();
        ml.allItems.add(ni);

//        title.label.setHorizontalAlignment(SwingConstants.LEFT);
//        subtitle.label.setHorizontalAlignment(SwingConstants.LEFT);
        // <editor-fold defaultstate="collapsed" desc="Animation Config">
        ml.SLCfgs.add(new SLConfig(panel)
            .gap(2, 2)
            .row(1f).row(30).row(30).row(30).col(1f)
            .place(0, 0, ni)//top text
            .place(1, 0, texts.get(1))//bottom text
            .place(2, 0, texts.get(2))
            .place(3, 0, texts.get(3)));
        ml.SLCfgs.add(new SLConfig(panel)
            .gap(2, 2)
            .row(30).row(1f).row(30).row(30).col(1f)
            .place(0, 0, ni)//top text
            .place(1, 0, texts.get(1))//bottom text
            .place(2, 0, texts.get(2))
            .place(3, 0, texts.get(3)));
        ml.SLCfgs.add(new SLConfig(panel)
            .gap(2, 2)
            .row(30).row(30).row(1f).row(30).col(1f)
            .place(0, 0, ni)//top text
            .place(1, 0, texts.get(1))//bottom text
            .place(2, 0, texts.get(2))
            .place(3, 0, texts.get(3)));
        ml.SLCfgs.add(new SLConfig(panel)
            .gap(2, 2)
            .row(30).row(30).row(30).row(1f).col(1f)
            .place(0, 0, ni)//top text
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
        ni.setAction(ml.menuAnimations.get(0));
        //texts.get(0).setAction(ml.menuAnimations.get(0));
        texts.get(1).setAction(ml.menuAnimations.get(1));
        texts.get(2).setAction(ml.menuAnimations.get(2));
        texts.get(3).setAction(ml.menuAnimations.get(3));
        // </editor-fold>
        ml.enableActions();

        return ml;
    }

//    public final static UXPanelItem NetworkPanel() {
//        final UXPanelItem pi = new UXPanelItem(NETWORK, "IP:");
//        JPanel dummy = new JPanel();
//        dummy.setSize(new Dimension(0, 0));
//        dummy.setBackground(new Color(0, 0, 0, 0));
//        pi.add(dummy, BorderLayout.LINE_START);
//
//        JPanel Title = new JPanel();
//        Title.setSize(new Dimension(0, 0));
//        Title.setBackground(new Color(0, 0, 0, 0));
//        Title.add(new JLabel("Network Info"));
//        pi.add(Title, BorderLayout.PAGE_START);
//
//        pi.label.setHorizontalAlignment(SwingConstants.LEFT);
////        pi.addHandler(new PropertyMonitor.PropHandler(TOP_TEXT) {
////            @Override
////            public void updateProperty(String value) {
////                pi.setText(value);
////            }
////        });
//
//        return pi;
//    }
}
