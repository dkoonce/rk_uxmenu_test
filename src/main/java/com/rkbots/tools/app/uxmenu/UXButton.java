/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import com.rkbots.tools.app.uxmenu.PropertyMonitor.PropHandler;
import com.rkbots.tools.app.uxmenu.slidinglayout.SLAnimator;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author josh
 */
public class UXButton extends UXPanelItem {

	public static final String TOP_TEXT = "topText";
	public static final String BOTTOM_TEXT = "bottomText";
	public static final String IMG_PATH = "gifPath";
	public static final String BIG_IMG_PATH = "bigGifPath";
	public static final String BIG_IMG_SCRIPT = "bigLogoScript";
	public static final String MENU_CHANGE_ACTION = "menuAction";
	public static final String BUTTON = "button";
	public static final String IMAGE = "image";
	public static final String TEXT = "text";
	public static final String POST_IMAGE = "Image";
	public static final String POST_TEXT = "Text";
	public static final String POST_SCRIPT = "Script";
	public static final String POST_BACKGROUND_COLOR = "BackgroundColor";
	public static final String POST_BORDER_COLOR = "BorderColor";
	public static final String POST_FONT_COLOR = "FontColor";
	public static final String POST_ACTIVE_BACKGROUND_COLOR = "ActiveBackgroundColor";
	public static final String POST_ACTIVE_BORDER_COLOR = "ActiveBorderColor";
	public static final String POST_ACTIVE_FONT_COLOR = "ActiveFontColor";
	public static final String POST_BORDER_ROUNDNESS = "BorderRoundness";
	public static final String POST_BORDER_SIZE = "BorderSize";
	public static final String POST_HOVER_BORDER_SIZE = "HoverBorderSize";
	public static final String POST_CLASS = "Class";

	//public Color INVIS_COLOR = new Color(0, 0, 0, 0);
	public JLabel label = new JLabel();
	private BufferedImage currentImage;

	private ImageIcon activeImageIcon, hoverImageIcon, stdImageIcon;

	private boolean myMouseFocus = false;
	private int execsRunning = 0;
	private final long eventMask = AWTEvent.MOUSE_EVENT_MASK;
	private final AWTEventListener myAWTEventListener;
	private final MouseMotionListener myMouseMotionListener;
	private final MouseListener myMouseListener;
	private long lastMouseEventTime = 0;
	private boolean styleSetByUser = false;
	private int gap = 0;

	private UXStyle buttonStyle;

	//String refName = "";
	//private String name = "";
	//private String refScript = "";
	//private String script = "";
	ActionListener borderTimedOut = new ActionListener() {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			buttonStyle.currentBorderThickness = buttonStyle.borderSize;
			if (borderTimer.isRunning()) {
				borderTimer.stop();
				repaint();
				return;
			}

		}
	};
	Timer borderTimer = new Timer(UXMenu.style.borderTimeOut, borderTimedOut);

	@Override
	public String getDefaultMenuAction() {
		return defaultMenuAction;
	}

	@Override
	public void setDefaultMenuAction(final String defaultMenuAction) {
		this.defaultMenuAction = defaultMenuAction;
	}

	public void setText(final String text) {
		label.setText(text);
	}

	public void setBackgroundColor(final String colorProperty) {
		buttonStyle.backgroundColor = Colors.getColorFromStr(colorProperty, buttonStyle.backgroundColor);
		repaint();
		styleSetByUser = true;
	}

	public void setBorderColor(final String colorProperty) {
		buttonStyle.borderColor = Colors.getColorFromStr(colorProperty, buttonStyle.borderColor);
		repaint();
		styleSetByUser = true;
	}

	public void setFontColor(final String colorProperty) {
		buttonStyle.fontColor = Colors.getColorFromStr(colorProperty, buttonStyle.fontColor);
		repaint();
		styleSetByUser = true;
	}

	public void setActiveBackgroundColor(final String colorProperty) {
		buttonStyle.activeBackgroundColor = Colors.getColorFromStr(colorProperty, buttonStyle.activeBackgroundColor);
		styleSetByUser = true;
	}

	public void setActiveBorderColor(final String colorProperty) {
		buttonStyle.activeBorderColor = Colors.getColorFromStr(colorProperty, buttonStyle.activeBorderColor);
		styleSetByUser = true;
	}

	public void setActiveFontColor(final String colorProperty) {
		buttonStyle.activeFontColor = Colors.getColorFromStr(colorProperty, buttonStyle.activeFontColor);
		styleSetByUser = true;
	}

	public void setBorderRoundness(final String borderRoundnessProperty) {
		try {
			final int borderRoundness = Integer.parseInt(borderRoundnessProperty);
			buttonStyle.borderRoundness = borderRoundness;
		} catch (final Exception e) {
			System.err.println("Could not set border roundness. " + borderRoundnessProperty + " should be an integer.");
			e.printStackTrace();
		}
		styleSetByUser = true;
	}
	public int getGap()
	{
		return gap;
	}

	public void setGap(int gap){
		this.gap=gap;
	}
	public void setBorderSize(final String aBorderSizeProperty) {
		String borderSizeProperty = aBorderSizeProperty.trim().toLowerCase();
		if (borderSizeProperty.endsWith("px")) {
			borderSizeProperty = borderSizeProperty.replace("px", "");
		}

		try {
			final int borderSize = Integer.parseInt(borderSizeProperty);
			buttonStyle.borderSize = borderSize;
		} catch (final Exception e) {
			System.err.println("Could not set border size. " + aBorderSizeProperty + " should be an integer.");
		}
		styleSetByUser = true;
	}

	public void setHoverBorderSize(final String aBorderSizeProperty) {
		String borderSizeProperty = aBorderSizeProperty.trim().toLowerCase();
		if (borderSizeProperty.endsWith("px")) {
			borderSizeProperty = borderSizeProperty.replace("px", "");
		}

		try {
			final int borderSize = Integer.parseInt(borderSizeProperty);
			buttonStyle.hoverBorderSize = borderSize;
		} catch (final Exception e) {
			System.err.println("Could not set active border roundness. " + aBorderSizeProperty + " should be an integer.");
		}
		styleSetByUser = true;
	}

	public void setStyleClass(final String aStyleClass) {
		final String styleClass = aStyleClass.trim().toLowerCase();
		if (UXStyle.buttonStyleClasses.containsKey(styleClass)) {
			final UXStyle style = UXStyle.buttonStyleClasses.get(styleClass);
			buttonStyle = (UXStyle) style.clone();
			repaint();

		} else {
			System.err.println("Class not found " + aStyleClass);
		}
		styleSetByUser = true;
	}

	/**
	 * Creates new form UXButtonPanel
	 * <p>
	 */
	public UXButton() {
		this("", "");
	}

	public UXButton(final String refName) {
		this(refName, "");
	}

	public UXButton(final String refNameStr, final String defaultText) {
		buttonStyle = UXStyleFactory.createUXStyle();

		//  initComponents();
		//setBackground(FG_COLOR);
		setOpaque(false);
		//System.out.println("Image Cache "+ImageIO.getUseCache());
		//setBackground(new Color(0, 0, 0, 0));
		setLayout(new BorderLayout());

		label.setFont(new Font("Sans", Font.BOLD, 12));
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.refName = refNameStr;
//        this.refScript = refScriptStr;
		label.setText(defaultText);//TODO
		add(label, BorderLayout.CENTER);

		myAWTEventListener = new AWTEventListener() {
			//Allows a mouse event to originate from anywhere
			// and activate only the item it was released on
			@Override
			public void eventDispatched(final AWTEvent event) {
				if (event.getID() == MouseEvent.MOUSE_RELEASED && myMouseFocus) {
					final long curTime = System.currentTimeMillis();
					if (curTime - lastMouseEventTime > 250) {//stop accidental double click
						lastMouseEventTime = curTime;
						System.out.println("Mouse clicked: " + refName);
						runScript();
						if (getAction() != null && actionEnabled) {
							System.out.println("Running Action");
							getAction().run();
							//UXItemMouseClicked();
						}
						if (!defaultMenuAction.isEmpty()) {
							UXMenu.tempDefaultAction = defaultMenuAction;
						}
					}
				}
			}
		};
		Toolkit.getDefaultToolkit().addAWTEventListener(myAWTEventListener, eventMask);
		myMouseMotionListener = new MouseMotionAdapter() {

			@Override
			public void mouseMoved(final MouseEvent e) {
//				buttonStyle.currentBorderThickness = buttonStyle.hoverBorderSize;
//				repaint();
//				borderTimer.restart();
			}

		};
		addMouseMotionListener(myMouseMotionListener);
		myMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(final MouseEvent e) {
				myMouseFocus = true;
				//System.out.println("Mouse entered: " + refName + ", setting mouse focus TRUE");
				buttonStyle.currentBorderThickness = buttonStyle.hoverBorderSize;
				if (execsRunning > 0 && activeImageIcon != null) {
//                    currentImage = activeImage;
				}
				if (hoverImageIcon != null && execsRunning == 0) {
					label.setIcon(hoverImageIcon);
				}
				repaint();
				borderTimer.restart();
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				myMouseFocus = false;
				//System.out.println("Mouse exited: " + refName + ", setting mouse focus FALSE");
				// super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
				buttonStyle.currentBorderThickness = buttonStyle.borderSize;
				if (execsRunning == 0 && stdImageIcon != null) {
					label.setIcon(stdImageIcon);
				}
				repaint();
			}

			//            @Override
//            public void mouseMoved(MouseEvent e) {
//            borderThickness = activeThickness;
//                borderTimer.restart();
//            }
			@Override
			public void mouseReleased(final MouseEvent e) {
				if (execsRunning == 0 && stdImageIcon != null) {
					label.setIcon(stdImageIcon);
				}
			}
		};
		addMouseListener(myMouseListener);
	}

	public final static UXButton createKeyBoardButton(final char symbol) {
		final String label;
		label = "" + symbol;
		final UXButton pi = new UXButton(label, label);
		//pi.actionEnabled=true; //need here?

		return pi;
	}

	public final static UXButton createButton(final int i) {
		return createButton(i, "");
	}

	public final static UXButton createButton(final int i, final String defaultText) {
		final UXButton pi = new UXButton(BUTTON + i + POST_TEXT, defaultText);

		addDefaultButtonFeatures(pi, BUTTON + i);
		addStyle(pi, BUTTON + i);

		pi.addHandler(new PropertyMonitor.PropHandler(BUTTON + i + MENU_CHANGE_ACTION) {
			@Override
			public void updateProperty(final String value) {
				pi.setDefaultMenuAction(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(BUTTON + i + POST_IMAGE) {
			@Override
			public void updateProperty(final String value) {
				pi.setImagePath(value);
			}
		});

		return pi;
	}

	private static void addDefaultButtonFeatures(final UXButton pi, final String prefix) {
		String buttonTextPrefix = prefix;
		if (buttonTextPrefix.endsWith(POST_TEXT)) {
			buttonTextPrefix = buttonTextPrefix.replace(POST_TEXT, "");
		}
		pi.addHandler(new PropertyMonitor.PropHandler(buttonTextPrefix + POST_TEXT) {
			@Override
			public void updateProperty(final String value) {
				pi.setText(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(prefix + POST_SCRIPT) {
			@Override
			public void updateProperty(final String value) {
				pi.setScript(value);
			}
		});
	}

	private static void addStyle(final UXButton pi, final String buttonNamePrefix) {
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_BACKGROUND_COLOR) {
			@Override
			public void updateProperty(final String value) {
				pi.setBackgroundColor(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_BORDER_COLOR) {
			@Override
			public void updateProperty(final String value) {
				pi.setBorderColor(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_FONT_COLOR) {
			@Override
			public void updateProperty(final String value) {
				pi.setFontColor(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_ACTIVE_BACKGROUND_COLOR) {
			@Override
			public void updateProperty(final String value) {
				pi.setActiveBackgroundColor(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_ACTIVE_BORDER_COLOR) {
			@Override
			public void updateProperty(final String value) {
				pi.setActiveBorderColor(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_ACTIVE_FONT_COLOR) {
			@Override
			public void updateProperty(final String value) {
				pi.setActiveFontColor(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_BORDER_ROUNDNESS) {
			@Override
			public void updateProperty(final String value) {
				pi.setBorderRoundness(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_BORDER_SIZE) {
			@Override
			public void updateProperty(final String value) {
				pi.setBorderSize(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_HOVER_BORDER_SIZE) {
			@Override
			public void updateProperty(final String value) {
				pi.setHoverBorderSize(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(buttonNamePrefix + POST_CLASS) {
			@Override
			public void updateProperty(final String value) {
				pi.setStyleClass(value);
			}
		});
	}

	public final static UXButton createImage() {
		return createImage(0, "");
	}

	public final static UXButton createImage(final int i) {
		return createImage(i, "");
	}

	public final static UXButton createImage(final int i, final String noLoadText) {
		final UXButton pi = new UXButton(IMAGE + i + POST_TEXT, noLoadText);
//        final UXPanelItem pi = new UXPanelItem(IMG_PATH,"Graphic");
		//pi.setImagePath(path);
		addDefaultButtonFeatures(pi, IMAGE + i);
		pi.addHandler(new PropertyMonitor.PropHandler(IMG_PATH) {
			@Override
			public void updateProperty(final String value) {
				pi.setImagePath(value);
			}
		});
		pi.addHandler(new PropertyMonitor.PropHandler(IMAGE + i) {
			@Override
			public void updateProperty(final String value) {
				pi.setImagePath(value);
			}
		});

		return pi;
	}

	public final static UXButton createTitle(final String text) {
		final UXButton pi = new UXButton(TOP_TEXT, text);
		pi.label.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 2, 8));
		pi.label.setHorizontalAlignment(SwingConstants.LEFT);
		addDefaultButtonFeatures(pi, TOP_TEXT);
		addStyle(pi, TOP_TEXT);
		return pi;
	}

	public final static UXButton createSubTitle(final String text) {
		final UXButton pi = new UXButton(BOTTOM_TEXT, text);
		pi.label.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 2, 8));
		pi.label.setHorizontalAlignment(SwingConstants.LEFT);
		addDefaultButtonFeatures(pi, BOTTOM_TEXT);
		addStyle(pi, BOTTOM_TEXT);
		return pi;
	}

	public final static UXButton createText(final int i) {
		return createText("", i);
	}

	public final static UXButton createText(final String text) {
		return createText(text, 0);
	}

	public final static UXButton createText(final String text, final int i) {
		final UXButton pi = new UXButton(TEXT + i, text);
		final JPanel dummy = new JPanel();// TODO looks suspicious
		dummy.setSize(new Dimension(0, 0));
		dummy.setBackground(new Color(0, 0, 0, 0));
		pi.add(dummy, BorderLayout.LINE_START);
		pi.label.setHorizontalAlignment(SwingConstants.LEFT);


		addDefaultButtonFeatures(pi, TEXT + i);
		addStyle(pi, TEXT + i);

		return pi;
	}

	void addHandler(final PropHandler handler) {

		final String key = handler.getPropKey();

		List<PropHandler> handlers = PropertyMonitor.getMyHandlerMap().get(key);
		if (handlers == null) {
			handlers = new LinkedList<>();
			PropertyMonitor.getMyHandlerMap().put(key, handlers);
		}
		if (!handlers.contains(handler)) {
			handlers.add(handler);
		}
	}

	@Override
	public void disposeItem() {
		myMouseFocus = false;
		Toolkit.getDefaultToolkit().removeAWTEventListener(myAWTEventListener);
		removeMouseListener(myMouseListener);
		removeMouseMotionListener(myMouseMotionListener);
	}

	@Override
	public void reinstateItem() {
		Toolkit.getDefaultToolkit().addAWTEventListener(myAWTEventListener, eventMask);
		addMouseListener(myMouseListener);
		addMouseMotionListener(myMouseMotionListener);
	}

	@Override
	public void runScript() {
		System.out.println("  Preparing to run Script: " + script);
		if (!script.isEmpty()) {

			System.out.println("  Running Script: " + script);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						execsRunning++;
						if (activeImageIcon != null) {
							label.setIcon(activeImageIcon);
						}
						repaint();
//                        System.out.println("Running: " + script);
						final Process process = Runtime.getRuntime().exec(script);
						// exhaust input stream
						// any error message?
						final StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(),
								"ERROR");

						// any output?
						final StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(),
								"OUTPUT");

						// kick them off
						errorGobbler.start();
						outputGobbler.start();
						// wait for completion
						process.waitFor();

					} catch (final IOException ex) {
						System.err.println("Cannot execute script:" + script);
					} catch (final InterruptedException ex) {
						org.slf4j.LoggerFactory.getLogger(UXButton.class).error(ex.getMessage(), ex);
					}
					if (execsRunning > 0) {
						execsRunning--;
					}
					System.out.println("Script Done, runcount:" + execsRunning);
					if (execsRunning == 0 && stdImageIcon != null) {
						label.setIcon(stdImageIcon);

					}
					repaint();
				}
			}).start();

		}
	}

	@Override
	protected void paintComponent(final Graphics superGraphics) {
		UXStyle renderedButtonStyle = buttonStyle;
		if (shouldBeDisabled()) {
			renderedButtonStyle = UXStyle.buttonStyleClasses.get(UXStyle.DISABLED_STYLE);
		}

		if (myMouseFocus && borderTimer.isRunning()) {
			renderedButtonStyle.currentBorderThickness = renderedButtonStyle.hoverBorderSize;
		} else {
			renderedButtonStyle.currentBorderThickness = renderedButtonStyle.borderSize;
		}

		final boolean buttonIsActive = execsRunning > 0;
		final boolean usingImage = currentImage != null;

		final Color renderBackgroundColor;
		final Color renderBorderColor;
		final Color renderFontColor;
		if (buttonIsActive) {
			renderBackgroundColor = renderedButtonStyle.activeBackgroundColor;
			renderBorderColor = renderedButtonStyle.activeBorderColor;
			renderFontColor = renderedButtonStyle.activeFontColor;
		} else {
			renderBackgroundColor = renderedButtonStyle.backgroundColor;
			renderBorderColor = renderedButtonStyle.borderColor;
			renderFontColor = renderedButtonStyle.fontColor;
		}
		label.setForeground(renderFontColor);

		super.paintComponent(superGraphics);
		final Graphics2D graphics = (Graphics2D) superGraphics;

		final int panelItemWidth = getWidth();
		final int panelItemHeight = getHeight();

		final int borderThickness = renderedButtonStyle.currentBorderThickness;
		final int borderRoundness = renderedButtonStyle.borderRoundness;

		if (usingImage) {
			final int imageWidth = currentImage.getWidth();
			final int imageHeight = currentImage.getHeight();

			if ((float) panelItemWidth / panelItemHeight < (float) imageWidth / imageHeight) {
				final int tw = panelItemHeight * imageWidth / imageHeight;
				final int th = panelItemHeight;
				graphics.drawImage(currentImage, (panelItemWidth - tw) / 2, 0, tw, th, null);
			} else {
				final int tw = panelItemWidth;
				final int th = panelItemWidth * imageHeight / imageWidth;
				graphics.drawImage(currentImage, 0, (panelItemHeight - th) / 2, tw, th, null);
			}

		} else {
			graphics.setColor(renderBorderColor);
			graphics.fillRoundRect(gap, gap, panelItemWidth-gap, panelItemHeight-gap, borderRoundness, borderRoundness);

			graphics.setColor(renderBackgroundColor);
			graphics.fillRoundRect(borderThickness, borderThickness, panelItemWidth - 2 * borderThickness, panelItemHeight - 2 * borderThickness, borderRoundness - borderThickness, borderRoundness - borderThickness);

		}
	}

	private boolean shouldBeDisabled() {
		final boolean hasButtonText = !label.getText().isEmpty();
		final boolean shouldBeDisabled = !styleSetByUser && !hasButtonText && buttonStyle.equals(UXStyle.buttonStyleClasses.get(UXStyle.DEFAULT_STYLE));
		return shouldBeDisabled;
	}

	public void setImagePath(final String imgPath) {
		if (!imgPath.isEmpty()) {
			//System.out.println("Reading Image: " + imgPath);
			try {
				ImageIO.setUseCache(false);

				final String filePathWithoutProtocol = imgPath.replace("file://", "").replace("file:", "");
				if (new File(filePathWithoutProtocol).exists()) {
					currentImage = ImageIO.read(new URL(imgPath));
				} else {
					currentImage = Resources.loadResourceAsBufferedImage(imgPath);
				}
				//BG_COLOR = new Color(0xFFFFFF);
				label.setVisible(false);
			} catch (final IOException ex) {
				System.err.println("[error] cannot read image path '" + imgPath + "'");
				label.setVisible(true);
				ex.printStackTrace();
			}
		}
		repaint();
	}

	public void setStdImageIconPath(final String imgPath) {
		if (!imgPath.isEmpty()) {
			//System.out.println("Reading Image: " + imgPath);
			try {
				stdImageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
						new URL(imgPath)));
				label.setIcon(stdImageIcon);
				label.setVisible(true);
				//BG_COLOR = new Color(0xFFFFFF);
			} catch (final IOException ex) {
				System.err.println("[error] cannot read image path '" + imgPath + "'");
			}
		}
	}

	public void setHoverImageIconPath(final String imgPath) {
		if (!imgPath.isEmpty()) {
			//System.out.println("Reading Image: " + imgPath);
			try {
				hoverImageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(new URL(
						imgPath)));
				//BG_COLOR = new Color(0xFFFFFF);
			} catch (final IOException ex) {
				System.err.println("[error] cannot read image path '" + imgPath + "'");
			}
		}
	}

	public void setActiveImageIconPath(final String imgPath) {
		if (!imgPath.isEmpty()) {
			//System.out.println("Reading Image: " + imgPath);
			try {
				activeImageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(new URL(
						imgPath)));
				//BG_COLOR = new Color(0xFFFFFF);
			} catch (final IOException ex) {
				System.err.println("[error] cannot read image path '" + imgPath + "'");
			}
		}
	}

	// -------------------------------------------------------------------------
	// Tween Accessor
	// -------------------------------------------------------------------------
	public static class Accessor extends SLAnimator.ComponentAccessor {

		public static final int BORDER_THICKNESS = 100;

		@Override
		public int getValues(final Component target, final int tweenType, final float[] returnValues) {
			final UXButton tp = (UXButton) target;

			final int ret = super.getValues(target, tweenType, returnValues);
			if (ret >= 0) {
				return ret;
			}

			switch (tweenType) {
				case BORDER_THICKNESS:
					returnValues[0] = tp.buttonStyle.currentBorderThickness;
					return 1;
				default:
					return -1;
			}
		}

		@Override
		public void setValues(final Component target, final int tweenType, final float[] newValues) {
			final UXButton tp = (UXButton) target;

			super.setValues(target, tweenType, newValues);

			switch (tweenType) {
				case BORDER_THICKNESS:
					tp.buttonStyle.currentBorderThickness = Math.round(newValues[0]);
					tp.repaint();
					break;
			}
		}
	}

	@Override
	public String toString() {
		return "UXButton{" + "refName=" + refName + ", label=" + label.getText() + ", buttonStyle=" + buttonStyle + '}';
	}


	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		setOpaque(false);

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 268, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 217, Short.MAX_VALUE)
		);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables
}
