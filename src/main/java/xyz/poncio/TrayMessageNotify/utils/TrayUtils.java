package xyz.poncio.TrayMessageNotify.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import xyz.poncio.TrayMessageNotify.screens.MainScreen;
import xyz.poncio.TrayMessageNotify.service.ClienteMQTT;
import xyz.poncio.TrayMessageNotify.utils.ImageUtils.Images;

public class TrayUtils {

	private static TrayUtils instance;
	private static TrayIcon trayIcon;
	private static MainScreen mainScreen = MainScreen.getInstance();
	private static ClienteMQTT clienteMqtt;

	private TrayUtils() {

	}

	public static TrayUtils getInstance() {
		if (instance == null)
			instance = new TrayUtils();
		return instance;
	}

	public void checkCompatibility() throws Exception {
		if (!SystemTray.isSupported())
			throw new Exception("SystemTray is not supported");

		final SystemTray tray = SystemTray.getSystemTray();
		final PopupMenu popup = new PopupMenu();

		Image image = ImageUtils.getIconImage(Images.ICON_BRANCO);
		TrayUtils.trayIcon = new TrayIcon(image, "TrayNotify", popup);
		MenuItem exitItem = new MenuItem("Sair");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TrayUtils.getInstance().clienteMQTT().finalizar();
				System.exit(1);
			}
		});
		popup.add(exitItem);
		trayIcon.setPopupMenu(popup);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() >= 2 && !TrayUtils.mainScreen.isVisible())
					TrayUtils.mainScreen.setVisible(true);
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	public void sendNotify(String title, String message) {
		try {
			TrayUtils.trayIcon.displayMessage(title, message, MessageType.INFO);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void clearScreen() {
		TrayUtils.mainScreen = null;
	}

	public void loadClienteMQTT(ClienteMQTT cliente) {
		TrayUtils.clienteMqtt = cliente;
	}

	public ClienteMQTT clienteMQTT() {
		return TrayUtils.clienteMqtt;
	}

	public boolean isScreenVisible() {
		return TrayUtils.mainScreen.isVisible();
	}

	public void setDefaultUser(String defaultUser) {
		TrayUtils.mainScreen.changeDefaultUser(defaultUser);
	}

	public void setDefaultTopic(String defaultTopic) {
		TrayUtils.mainScreen.changeDefaultTopic(defaultTopic);
	}

}
