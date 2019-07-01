package com.rkbots.tools.app.uxmenu.slidinglayout;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class SLAnimator {

	private static final List<TweenManager> tweenManagers = new ArrayList<>();
	private static boolean running = false;
	private static Thread SLThread;

	public static boolean isRunning() {
		return running;
	}

	static {
		Tween.registerAccessor(JComponent.class, new ComponentAccessor());
		Tween.setCombinedAttributesLimit(4);
	}

	public static TweenManager createTweenManager() {
		//System.out.println("Creating Tween Manager");
		TweenManager tm = new TweenManager();
		tweenManagers.add(tm);
		return tm;
	}

	public static void start() {
		running = true;
		//System.out.println("Running SLAnimator");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				long lastMillis = System.currentTimeMillis();
				long deltaLastMillis = System.currentTimeMillis();

				while (running) {
					long newMillis = System.currentTimeMillis();
					long sleep = 50 - (newMillis - lastMillis);
					lastMillis = newMillis;
					//System.out.println("SLAnimLoop with a sleep of: "+ sleep);
					if (sleep > 1) {
						try {
							Thread.sleep(sleep);
						} catch (InterruptedException ex) {
							System.out.println("Error Sleeping");
						}
					}

					long deltaNewMillis = System.currentTimeMillis();
					final float delta = (deltaNewMillis - deltaLastMillis) / 1000f;

					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								for (int i = 0, n = tweenManagers.size(); i < n; i++) {
									tweenManagers.get(i).update(delta);
									//if(i==0)System.out.println("Update Tween manager size: " + n);
								}
							}
						});
					} catch (InterruptedException ex) {
						System.out.println("Anim Error 1");
					} catch (InvocationTargetException ex) {
						System.out.println("Anim Error 2");
					}

					deltaLastMillis = newMillis;
				}
			}
		};
		SLThread = new Thread(runnable);
		SLThread.start();
	}

	public static void waitForAnim() {

		if (SLThread != null) {
			try {
				SLThread.join();
			} catch (InterruptedException ex) {
				org.slf4j.LoggerFactory.getLogger(SLAnimator.class).error(ex.getMessage(), ex);
			}
		}

	}

	public static void stop() {
		//System.out.println("Stopping SLAnimator");
		running = false;
	}

	// -------------------------------------------------------------------------
	// Accessors
	// -------------------------------------------------------------------------
	public static class ComponentAccessor implements TweenAccessor<Component> {

		public static final int X = 1;
		public static final int Y = 2;
		public static final int XY = 3;
		public static final int W = 4;
		public static final int H = 5;
		public static final int WH = 6;
		public static final int XYWH = 7;

		@Override
		public int getValues(Component target, int tweenType, float[] returnValues) {
			switch (tweenType) {
				case X:
					returnValues[0] = target.getX();
					return 1;
				case Y:
					returnValues[0] = target.getY();
					return 1;
				case XY:
					returnValues[0] = target.getX();
					returnValues[1] = target.getY();
					return 2;
				case W:
					returnValues[0] = target.getWidth();
					return 1;
				case H:
					returnValues[0] = target.getHeight();
					return 1;
				case WH:
					returnValues[0] = target.getWidth();
					returnValues[1] = target.getHeight();
					return 2;
				case XYWH:
					returnValues[0] = target.getX();
					returnValues[1] = target.getY();
					returnValues[2] = target.getWidth();
					returnValues[3] = target.getHeight();
					return 4;
				default:
					return -1;
			}
		}

		@Override
		public void setValues(Component target, int tweenType, float[] newValues) {
			switch (tweenType) {
				case X:
					target.setLocation(Math.round(newValues[0]), target.getY());
					break;
				case Y:
					target.setLocation(target.getX(), Math.round(newValues[0]));
					break;
				case XY:
					target.setLocation(Math.round(newValues[0]), Math.round(newValues[1]));
					break;
				case W:
					target.setSize(Math.round(newValues[0]), target.getHeight());
					target.validate();
					break;
				case H:
					target.setSize(target.getWidth(), Math.round(newValues[0]));
					target.validate();
					break;
				case WH:
					target.setSize(Math.round(newValues[0]), Math.round(newValues[1]));
					target.validate();
					break;
				case XYWH:
					target.setBounds(Math.round(newValues[0]), Math.round(newValues[1]), Math.round(
							newValues[2]), Math.round(newValues[3]));
					target.validate();
					break;
			}
		}
	}
}
