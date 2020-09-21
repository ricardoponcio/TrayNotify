package xyz.poncio.TrayMessageNotify;

import javax.swing.JOptionPane;

import xyz.poncio.TrayMessageNotify.service.ClienteMQTT;
import xyz.poncio.TrayMessageNotify.utils.PropertiesUtils;
import xyz.poncio.TrayMessageNotify.utils.StringUtils;
import xyz.poncio.TrayMessageNotify.utils.TrayUtils;

public class TrayMain {

	public static void main(String[] args) {
		try {
			TrayUtils.getInstance().checkCompatibility();
			// TrayUtils.getInstance().sendNotify("Teste", "Aquiiiiiiiiii");

			String host = PropertiesUtils.getInstance().readProperty("mqtt.host", String.class);
			String user = PropertiesUtils.getInstance().readProperty("mqtt.user", String.class);
			String pass = PropertiesUtils.getInstance().readProperty("mqtt.pass", String.class);

			TrayUtils.getInstance().loadClienteMQTT(new ClienteMQTT(host, user, pass));
			TrayUtils.getInstance().clienteMQTT().iniciar();

			TrayUtils.getInstance().setDefaultUser(TrayUtils.getInstance().clienteMQTT().getDefaultName());
			String userTela = PropertiesUtils.getInstance().readProperty("tela.usuario", String.class);
			if (userTela != null && !userTela.isEmpty())
				TrayUtils.getInstance().setDefaultUser(userTela);
			String topicoTela = PropertiesUtils.getInstance().readProperty("tela.topico", String.class);
			if (topicoTela != null && !topicoTela.isEmpty())
				TrayUtils.getInstance().setDefaultTopic(topicoTela);
			else {
				String topic = StringUtils.randomAlphaNumeric(8);
				TrayUtils.getInstance().setDefaultTopic(topic);
				PropertiesUtils.getInstance().setProperty("tela.topico", topic);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

}
