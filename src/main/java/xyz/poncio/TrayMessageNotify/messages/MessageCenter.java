package xyz.poncio.TrayMessageNotify.messages;

import java.util.ArrayList;
import java.util.List;

import xyz.poncio.TrayMessageNotify.objects.Mensagem;
import xyz.poncio.TrayMessageNotify.objects.Mensagem.TipoMensagem;
import xyz.poncio.TrayMessageNotify.screens.MainScreen;

public class MessageCenter {

	private static MessageCenter instance;
	private List<Mensagem> mensagens = new ArrayList<Mensagem>();

	private MessageCenter() {

	}

	public static MessageCenter getInstance() {
		if (instance == null)
			instance = new MessageCenter();
		return instance;
	}

	public void addSystemMessage(String message) {
		Mensagem mensagem = new Mensagem(null, message, TipoMensagem.SISTEMA);
		this.mensagens.add(mensagem);
		MainScreen.getInstance().addMensagemTela(mensagem);
	}

	public void addChatMessage(String user, String message) {
		Mensagem mensagem = new Mensagem(user, message, TipoMensagem.CHAT);
		this.mensagens.add(mensagem);
		MainScreen.getInstance().addMensagemTela(mensagem);
	}
	
	public void addChatMessage(Mensagem mensagem) {
		mensagem.setTipo(TipoMensagem.CHAT);
		this.mensagens.add(mensagem);
		MainScreen.getInstance().addMensagemTela(mensagem);
	}

}
