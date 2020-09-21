package xyz.poncio.TrayMessageNotify.objects;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mensagem {

	public static enum TipoMensagem {
		SISTEMA, CHAT;
	};
	
	@Expose
	private String username;
	@Expose
	private String mensagem;
	private TipoMensagem tipo;
	
}
