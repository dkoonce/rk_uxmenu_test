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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.*;

/**
 * @author josh
 */
public class UXFrame extends javax.swing.JFrame {

	public static final String MENU_LEFT = "left";
	public static final String MENU_RIGHT = "right";
	public static final String MENU_UP = "up";
	public static final String MENU_DOWN = "down";

	private final SLPanel panel = new SLPanel();
	//private final List<MenuLayouts> myMenus = new ArrayList();
	private MenuLayouts currentMenu;
	private MenuLayouts previousMenu;
	private boolean propMonRunning = false;
	private static final BufferedImage theCursorImage
			= new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	private static final Cursor theBlankCursor
			= Toolkit.getDefaultToolkit().createCustomCursor(
			theCursorImage, new Point(0, 0), "blank cursor");

	final BlockingQueue<MenuLayouts> menuQueue = new ArrayBlockingQueue<>(512, true);
	MenuQueueWorker menuWorker = new MenuQueueWorker();

	public MenuLayouts getPreviousMenu() {
		return previousMenu;
	}

	//private final SLConfig mainCfg;

	/**
	 * Creates new form UXFrame
	 */
	public UXFrame() {
		//initComponents();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(240, 320);
		//setExtendedState(MAXIMIZED_BOTH);
		setLocation(0, 0);
		setUndecorated(true);
		setTitle("UX Menu");
		hideCursor(true);

		//getContentPane().setBackgrolund(Color.WHITE);
		setLayout(new BorderLayout());
		setContentPane(new JLabel(new ImageIcon(Resources.loadResource(UXMenu.BACKGROUND_IMG))));
		setLayout(new BorderLayout());

		getContentPane().add(panel, BorderLayout.CENTER);

		panel.setTweenManager(SLAnimator.createTweenManager());

		currentMenu = MenuLayouts.emptyMenu(panel);

		panel.initialize(currentMenu.getPrimarySLCfg());

		startPropteryMonitor();
	}


	public final void hideCursor(boolean hide) {
		if (hide) {
			getContentPane().setCursor(theBlankCursor);
		} else {
			getContentPane().setCursor(Cursor.getDefaultCursor());
		}
	}

	private class MenuQueueWorker extends Thread {

		//        public menuQueueWorker(Queue queue) {
//            this.queue = queue;
//            setName("MyWorker:" + (instance++));
//        }
		@Override
		public void run() {
			MenuLayouts nextMenu;
			while (true) {
				try {
					while (menuQueue.isEmpty()) {
//                        menuQueue.wait();
						Thread.sleep(20);
					}

					// Get the next work item off of the queue
					nextMenu = menuQueue.remove();

					// Process the work item
					while (SLAnimator.isRunning()) {
						SLAnimator.waitForAnim();
						Thread.sleep(20);
					}
					while (menuQueue.size() > 0) {
						nextMenu = menuQueue.remove();
					}
					setNewMenu(nextMenu);
				} catch (InterruptedException ie) {
					break;  // Terminate
				}
			}
		}
	}

	public void setNewMenuInQueue(MenuLayouts newMenu) {
//        synchronized (menuQueue) {
		menuQueue.add(newMenu);
//          synchronized(this){
//            menuQueue.notify();
//          }
//        }
		if (!menuWorker.isAlive()) {
			menuWorker.start();
		}

	}

	private void setNewMenu(MenuLayouts newMenu) {

				System.out.println("Displaying new menu : " + newMenu.getType());

				//If the LastMenu command was issued then we need to reactivate the mouse listeners
				if (newMenu.equals(previousMenu)) {
					for (UXPanelItem item : newMenu.getAllItems()) {
						item.reinstateItem();
					}
		}
		previousMenu = currentMenu;

		//disable mouse listeners for the current menu
		if (currentMenu != null) {
			for (UXPanelItem item : currentMenu.getAllItems()) {
				item.disposeItem();
			}
		}
		if (newMenu.forceNoAnimation) {
			exitOldMenuQuickly();

			currentMenu = newMenu;

			newMenuNoAnimation.run();

		} else {

			if (currentMenu.getExitAnimation() != null && newMenu.getEntranceAnimation() != null) {
				exitOldMenuAction();
				currentMenu = newMenu;
				currentMenu.getEntranceAnimation().run();

			} else if (currentMenu.getExitAnimation() != null) {
				exitOldMenuAction();
				currentMenu = newMenu;
				newMenuEnterOnlyAction.run();

			} else if (newMenu.getEntranceAnimation() != null) {

				//            SLAnimator.start();
//            currentMenu.disableActions();
//            panel.createTransition()
//                    .push(new SLKeyframe(new SLConfig(panel)
//                .gap(0, 0)
//                .row(1f).col(1f)
//                .place(0, 0, new UXPanelItem()), 0.5f)
//                            .setEndSideForOldCmps(SLSide.LEFT)
//                            //.setStartSideForNewCmps(SLSide.RIGHT)
//                            .setCallback(new SLKeyframe.Callback() {
//                                @Override
//                                public void done() {
//                                    //currentMenu.enableActions();
//                                    SLAnimator.stop();
//                                }
//                            }))
//                    .play();
				currentMenu.running = true;
				currentMenuExitOnlyAction.run();
				SLAnimator.waitForAnim();
//                while (currentMenu.running == true) {
//                    try {
//                        Thread.sleep(20);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(UXFrame.class).error(ex.getMessage(), ex);
//                    }
//                }
				currentMenu = newMenu;
				currentMenu.getEntranceAnimation().run();
				SLAnimator.waitForAnim();

			} else {

				currentMenu = newMenu;
				switch (UXMenu.tempDefaultAction) {
					case MENU_UP:
						newMenuDefaultUpAction.run();
						break;
					case MENU_DOWN:
						newMenuDefaultDownAction.run();
						break;
					case MENU_RIGHT:
						newMenuDefaultRightAction.run();
						break;
					case MENU_LEFT:
					default:
						newMenuDefaultLeftAction.run();
						break;
				}
				SLAnimator.waitForAnim();
			}
		}
		UXMenu.tempDefaultAction = "";
	}

	private void exitMenuAction() {

	}

	public MenuLayouts getCurrentMenu() {
		return currentMenu;
	}

	private final Runnable newMenuDefaultLeftAction = new Runnable() {

		@Override
		public void run() {
			SLAnimator.start();
			currentMenu.disableActions();
			panel.createTransition()
					.push(new SLKeyframe(currentMenu.getPrimarySLCfg(), 0.5f)
							.setEndSideForOldCmps(SLSide.LEFT)
							.setStartSideForNewCmps(SLSide.RIGHT)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
									currentMenu.enableActions();
									SLAnimator.stop();
								}
							}))
					.play();

		}
	};
	private final Runnable newMenuEnterOnlyAction = new Runnable() {

		@Override
		public void run() {
			SLAnimator.start();
			currentMenu.disableActions();
			panel.createTransition()
					.push(new SLKeyframe(currentMenu.getPrimarySLCfg(), 0.5f)
							//.setEndSideForOldCmps(SLSide.LEFT)
							.setStartSideForNewCmps(SLSide.RIGHT)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
									currentMenu.enableActions();
									SLAnimator.stop();
								}
							}))
					.play();

		}
	};

	private final Runnable currentMenuExitOnlyAction = new Runnable() {

		@Override
		public void run() {

			SLAnimator.start();
			currentMenu.disableActions();
			panel.createTransition()
					.push(new SLKeyframe(new SLConfig(panel)
							.gap(0, 0), 0.5f)
							.setEndSideForOldCmps(SLSide.LEFT)
							//.setStartSideForNewCmps(SLSide.RIGHT)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
									//currentMenu.enableActions();
									SLAnimator.stop();
									currentMenu.running = false;
								}
							}))
					.play();

		}
	};

	private void exitOldMenuAction() {
		currentMenu.running = true;
		currentMenu.getExitAnimation().run();
		SLAnimator.waitForAnim();
//        while (currentMenu.running == true) {
//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(UXFrame.class).error(ex.getMessage(), ex);
//            }
//        }
	}

	private void exitOldMenuQuickly() {
		currentMenu.running = true;
		newMenuNoAnimation.run();
		SLAnimator.waitForAnim();
//        while (currentMenu.running == true) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(UXFrame.class).error(ex.getMessage(), ex);
//            }
//        }
	}

	private final Runnable newMenuDefaultRightAction = new Runnable() {

		@Override
		public void run() {
			SLAnimator.start();
			currentMenu.disableActions();
			panel.createTransition()
					.push(new SLKeyframe(currentMenu.getPrimarySLCfg(), 0.5f)
							.setEndSideForOldCmps(SLSide.RIGHT)
							.setStartSideForNewCmps(SLSide.LEFT)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
									currentMenu.enableActions();
									SLAnimator.stop();
									currentMenu.running = false;
								}
							}))
					.play();

		}
	};
	private final Runnable newMenuNoAnimation = new Runnable() {

		@Override
		public void run() {
			SLAnimator.start();
			currentMenu.disableActions();
			panel.createTransition()
					.push(new SLKeyframe(currentMenu.getPrimarySLCfg(), 0.01f)
							.setEndSideForOldCmps(SLSide.TOP)
							.setStartSideForNewCmps(SLSide.BOTTOM)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
									currentMenu.enableActions();
									SLAnimator.stop();
									currentMenu.running = false;
								}
							}))
					.play();

		}
	};
	private final Runnable newMenuDefaultUpAction = new Runnable() {

		@Override
		public void run() {
			SLAnimator.start();
			currentMenu.disableActions();
			panel.createTransition()
					.push(new SLKeyframe(currentMenu.getPrimarySLCfg(), 0.5f)
							.setEndSideForOldCmps(SLSide.TOP)
							.setStartSideForNewCmps(SLSide.BOTTOM)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
									currentMenu.enableActions();
									SLAnimator.stop();
									currentMenu.running = false;
								}
							}))
					.play();

		}
	};
	private final Runnable newMenuDefaultDownAction = new Runnable() {

		@Override
		public void run() {
			SLAnimator.start();
			currentMenu.disableActions();
			panel.createTransition()
					.push(new SLKeyframe(currentMenu.getPrimarySLCfg(), 0.5f)
							.setEndSideForOldCmps(SLSide.BOTTOM)
							.setStartSideForNewCmps(SLSide.TOP)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
									currentMenu.enableActions();
									SLAnimator.stop();
									currentMenu.running = false;
								}
							}))
					.play();

		}
	};

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setMaximumSize(new java.awt.Dimension(240, 320));
        setMinimumSize(new java.awt.Dimension(240, 320));
        setPreferredSize(new java.awt.Dimension(240, 320));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            org.slf4j.LoggerFactory.getLogger(UXFrame.class).error(ex.getMessage(), ex);
//        } catch (InstantiationException ex) {
//            org.slf4j.LoggerFactory.getLogger(UXFrame.class).error(ex.getMessage(), ex);
//        } catch (IllegalAccessException ex) {
//            org.slf4j.LoggerFactory.getLogger(UXFrame.class).error(ex.getMessage(), ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            org.slf4j.LoggerFactory.getLogger(UXFrame.class).error(ex.getMessage(), ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
////        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                new UXFrame().setVisible(true);
////            }
////        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	private void startPropteryMonitor() {
		if (!propMonRunning) {
			Thread propThread;
			propThread = new Thread(new PropertyMonitor(this, panel));
			propThread.start();
			propMonRunning = true;
		}
	}

}
