/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * @author josh
 */
public class NetworkInfo extends UXPanelItem {

	private boolean myMouseFocus = false;
	private final int execsRunning = 0;
	private final long eventMask = AWTEvent.MOUSE_EVENT_MASK;
	private final AWTEventListener myAWTEventListener;
	private MouseMotionListener myMouseMotionListener;
	private final MouseListener myMouseListener;
	private long lastMouseEventTime = 0;

	//private UXPanelItem pi = new UXPanelItem("NetworkInfo");

	/**
	 * Creates new form NetworkInfo
	 */
	public NetworkInfo() {
		initComponents();
		refName = "Network Info";
		refreshNetworkInfo();

		myAWTEventListener = new AWTEventListener() {
			//Allows a mouse event to originate from anywhere
			// and activate only the item it was released on
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event.getID() == MouseEvent.MOUSE_RELEASED && myMouseFocus) {
					long curTime = System.currentTimeMillis();
					if (curTime - lastMouseEventTime > 250) {//stop accidental double click
						lastMouseEventTime = curTime;
						System.out.println("Mouse clicked: " + refName);
						runScript();
						if (getAction() != null && actionEnabled) {
							System.out.println("Running Action");
							getAction().run();
							UXItemMouseClicked();
						}
						if (!defaultMenuAction.isEmpty()) {
							UXMenu.tempDefaultAction = defaultMenuAction;
						}
					}
				}
			}
		};
		Toolkit.getDefaultToolkit().addAWTEventListener(myAWTEventListener, eventMask);

		myMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				myMouseFocus = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				myMouseFocus = false;
			}

		};
		addMouseListener(myMouseListener);
	}

	//    public UXButton() {
//        this("", "");
//    }
//
//    public UXButton(String refName) {
//        this(refName, "");
//    }
//
//    public UXButton(String refNameStr, String defaultText) {
	private void refreshNetworkInfo() {
		try {
			Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
			while (list.hasMoreElements()) {
				NetworkInterface netface = list.nextElement();
				switch (netface.getDisplayName()) {
					case "eth0":
						if (netface.getInterfaceAddresses().size() >= 2) {
							System.out.println("Eth: " + netface.getInterfaceAddresses());
							EthIPAddress.setText(netface.getInterfaceAddresses().get(1).getAddress().getHostAddress());
							EthMac.setText(printMac(netface.getHardwareAddress()));
						} else {
							EthIPAddress.setText("Not Connected");
							EthMac.setText("");
						}
						break;
					case "wlan0":
						if (netface.getInterfaceAddresses().size() >= 2) {
							System.out.println("Wifi: " + netface.getInterfaceAddresses());
							WifiIPAddress.setText(netface.getInterfaceAddresses().get(1).getAddress().getHostAddress());
							WifiMac.setText(printMac(netface.getHardwareAddress()));
						} else {
							EthIPAddress.setText("Not Connected");
							EthMac.setText("");
						}
						break;
					default:
						break;
				}
				//System.out.println(netface.getDisplayName()+" "+netface.getInterfaceAddresses().get(1).getAddress().getHostAddress());
				//System.out.println("  "+(netface.isUp()?"Connected":"Disconnected"));
			}
		} catch (SocketException ex) {
			org.slf4j.LoggerFactory.getLogger(NetworkInfo.class).error(ex.getMessage(), ex);
		}
	}

	private String printMac(byte[] maddr) {
		if (maddr != null) {
			StringBuilder sb = new StringBuilder();
			for (byte b : maddr) {
				sb.append(String.format("%02X.", b));
			}
			sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		} else {
			return "";
		}
	}

	void UXItemMouseClicked() {
		System.out.println("Refreshing NetworkInfo");
		refreshNetworkInfo();
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

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//    }

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		Name = new javax.swing.JLabel();
		WifiIPName = new javax.swing.JLabel();
		WifiIPAddress = new javax.swing.JLabel();
		WifiMac = new javax.swing.JLabel();
		EthIPName = new javax.swing.JLabel();
		EthIPAddress = new javax.swing.JLabel();
		EthMac = new javax.swing.JLabel();

		setBackground(new java.awt.Color(208, 19, 19));

		Name.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
		Name.setForeground(UXMenu.style.fontColor);
		Name.setText("Network");

		WifiIPName.setForeground(UXMenu.style.fontColor);
		WifiIPName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		WifiIPName.setText("Wifi");

		WifiIPAddress.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
		WifiIPAddress.setForeground(UXMenu.style.fontColor);
		WifiIPAddress.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		WifiIPAddress.setText("Not Connected");

		WifiMac.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
		WifiMac.setForeground(UXMenu.style.fontColor);
		WifiMac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		WifiMac.setText("  ");

		EthIPName.setForeground(UXMenu.style.fontColor);
		EthIPName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		EthIPName.setText("Ethernet IP");

		EthIPAddress.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
		EthIPAddress.setForeground(UXMenu.style.fontColor);
		EthIPAddress.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		EthIPAddress.setText("Not Connected");

		EthMac.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
		EthMac.setForeground(UXMenu.style.fontColor);
		EthMac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		EthMac.setText("  ");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(WifiIPName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(WifiIPAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(EthIPName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(EthIPAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addGap(0, 139, Short.MAX_VALUE)
												.addComponent(Name))
										.addComponent(WifiMac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(EthMac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGap(6, 6, 6)
								.addComponent(Name)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(WifiIPName)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(WifiIPAddress)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(WifiMac)
								.addGap(18, 18, 18)
								.addComponent(EthIPName)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(EthIPAddress)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(EthMac)
								.addContainerGap(48, Short.MAX_VALUE))
		);
	}// </editor-fold>//GEN-END:initComponents


	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel EthIPAddress;
	private javax.swing.JLabel EthIPName;
	private javax.swing.JLabel EthMac;
	private javax.swing.JLabel Name;
	private javax.swing.JLabel WifiIPAddress;
	private javax.swing.JLabel WifiIPName;
	private javax.swing.JLabel WifiMac;
	// End of variables declaration//GEN-END:variables

}
