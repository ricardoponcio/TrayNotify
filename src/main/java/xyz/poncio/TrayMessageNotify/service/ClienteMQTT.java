package xyz.poncio.TrayMessageNotify.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import xyz.poncio.TrayMessageNotify.messages.MessageCenter;

public class ClienteMQTT implements MqttCallbackExtended {

	private final String serverURI;
	private MqttClient client;
	private final MqttConnectOptions mqttOptions;
	private List<String> topicsSubscribed = new ArrayList<>();
	private String defaultClientName;

	public ClienteMQTT(String serverURI, String usuario, String senha) {
		this.serverURI = serverURI;

		mqttOptions = new MqttConnectOptions();
		mqttOptions.setMaxInflight(200);
		mqttOptions.setConnectionTimeout(3);
		mqttOptions.setKeepAliveInterval(10);
		mqttOptions.setAutomaticReconnect(true);
		mqttOptions.setCleanSession(false);

		if (usuario != null && senha != null) {
			mqttOptions.setUserName(usuario);
			mqttOptions.setPassword(senha.toCharArray());
		}
	}

	public IMqttToken subscribe(int qos, IMqttMessageListener gestorMensagemMQTT, String... topicos) {
		if (client == null || topicos.length == 0) {
			return null;
		}
		int tamanho = topicos.length;
		int[] qoss = new int[tamanho];
		IMqttMessageListener[] listners = new IMqttMessageListener[tamanho];

		for (int i = 0; i < tamanho; i++) {
			qoss[i] = qos;
			listners[i] = gestorMensagemMQTT;
		}
		try {
			IMqttToken token = client.subscribeWithResponse(topicos, qoss, listners);
			this.topicsSubscribed.addAll(Arrays.asList(topicos));
			return token;
		} catch (MqttException ex) {
			MessageCenter.getInstance().addSystemMessage(
					String.format("Erro ao se inscrever nos tópicos %s - %s", Arrays.asList(topicos), ex));
			return null;
		}
	}

	public void unsubscribe(String... topicos) {
		if (client == null || !client.isConnected() || topicos.length == 0) {
			return;
		}
		try {
			client.unsubscribe(topicos);
			this.topicsSubscribed.removeAll(Arrays.asList(topicos));
		} catch (MqttException ex) {
			MessageCenter.getInstance().addSystemMessage(
					String.format("Erro ao se desinscrever no tópico %s - %s", Arrays.asList(topicos), ex));
		}
	}

	public void iniciar() {
		try {
			this.defaultClientName = String.format("cliente_java_%d", System.currentTimeMillis());
			MessageCenter.getInstance().addSystemMessage("Conectando no broker MQTT em " + serverURI);
			client = new MqttClient(serverURI, this.defaultClientName,
					new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
			client.setCallback(this);
			client.connect(mqttOptions);
		} catch (MqttException ex) {
			MessageCenter.getInstance()
					.addSystemMessage("Erro ao se conectar ao broker mqtt " + serverURI + " - " + ex);
		}
	}

	public void finalizar() {
		if (client == null || !client.isConnected())
			return;
		
		try {
			unsubscribe(this.topicsSubscribed.toArray(new String[0]));
		} catch (Exception e) {
			// Nada
		}
		
		try {
			client.disconnect();
			client.close();
		} catch (MqttException ex) {
			MessageCenter.getInstance().addSystemMessage("Erro ao desconectar do broker mqtt - " + ex);
		}
	}

	public void publicar(String topic, byte[] payload, int qos) {
		publicar(topic, payload, qos, false);
	}

	public synchronized void publicar(String topic, byte[] payload, int qos, boolean retained) {
		try {
			if (client.isConnected()) {
				client.publish(topic, payload, qos, retained);
				//MessageCenter.getInstance()
				//		.addSystemMessage(String.format("Tópico %s publicado. %dB", topic, payload.length));
			} else {
				MessageCenter.getInstance()
						.addSystemMessage("Cliente desconectado, não foi possível publicar o tópico " + topic);
			}
		} catch (MqttException ex) {
			MessageCenter.getInstance().addSystemMessage("Erro ao publicar " + topic + " - " + ex);
		}
	}

	public void connectionLost(Throwable thrwbl) {
		MessageCenter.getInstance().addSystemMessage("Conexão com o broker perdida -" + thrwbl);
	}

	public void connectComplete(boolean reconnect, String serverURI) {
		MessageCenter.getInstance().addSystemMessage(
				"Cliente MQTT " + (reconnect ? "reconectado" : "conectado") + " com o broker " + serverURI);
	}

	public void deliveryComplete(IMqttDeliveryToken imdt) {
	}

	public void messageArrived(String topic, MqttMessage mm) throws Exception {
	}
	
	public String getDefaultName() {
		return this.defaultClientName;
	}

}