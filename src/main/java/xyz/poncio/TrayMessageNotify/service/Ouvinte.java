package xyz.poncio.TrayMessageNotify.service;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.poncio.TrayMessageNotify.messages.MessageCenter;
import xyz.poncio.TrayMessageNotify.objects.Mensagem;
import xyz.poncio.TrayMessageNotify.utils.TrayUtils;

public class Ouvinte implements IMqttMessageListener {

	public Ouvinte(ClienteMQTT clienteMQTT, String topico, int qos) {
		clienteMQTT.subscribe(qos, this, topico);
	}

	public void messageArrived(String topico, MqttMessage mm) throws Exception {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		Mensagem mensagem = gson.fromJson(mm.toString(), Mensagem.class);
		MessageCenter.getInstance().addChatMessage(mensagem);
		
		if(!TrayUtils.getInstance().isScreenVisible())
			TrayUtils.getInstance().sendNotify("Mensagem de " + mensagem.getUsername(), mensagem.getMensagem());
	}

}