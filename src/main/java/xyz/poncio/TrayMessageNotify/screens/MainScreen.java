package xyz.poncio.TrayMessageNotify.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.poncio.TrayMessageNotify.objects.Mensagem;
import xyz.poncio.TrayMessageNotify.objects.Mensagem.TipoMensagem;
import xyz.poncio.TrayMessageNotify.service.Ouvinte;
import xyz.poncio.TrayMessageNotify.utils.ImageUtils;
import xyz.poncio.TrayMessageNotify.utils.ImageUtils.Images;
import xyz.poncio.TrayMessageNotify.utils.PropertiesUtils;
import xyz.poncio.TrayMessageNotify.utils.TrayUtils;

public class MainScreen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static MainScreen instance;
	private String topicoInscrito;
	@SuppressWarnings("unused")
	private Ouvinte ouvinte;

	private JPanel pSuperiorSuper;

	private JPanel pSuperior1;
	private JLabel lblUsuario;
	private JTextField txtUsuario;
	private String usuarioAnterior;

	private JPanel pSuperior2;
	private JLabel lblTopico;
	private JTextField txtTopico;
	private JButton btnTopico;

	private JTextPane areaTexto;

	private JTextField msg;
	private JButton btnSend;
	private JButton btnClose;
	private JButton btnUsuario;

	public static MainScreen getInstance() {
		if (instance == null)
			instance = new MainScreen();
		return instance;
	}

	private MainScreen() {
		try {
			Image image = ImageUtils.getIconImage(Images.ICON_PRETO);
			setIconImage(image);
		} catch (Exception e) {
			// TODO: handle exception
		}

		setResizable(false);
		Container tela = getContentPane();
		msg = new JTextField(28);
		msg.setHorizontalAlignment(SwingConstants.LEFT);

		areaTexto = new JTextPane();
		areaTexto.setEditable(false);
		areaTexto.setSize(200, 200);
		areaTexto.setPreferredSize(new Dimension(450, 300));
		JScrollPane painelRolagem = new JScrollPane(areaTexto);
		painelRolagem.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		painelRolagem.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JPanel pCentro = new JPanel();
		pCentro.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		pCentro.add(painelRolagem);

		JPanel pInferior = new JPanel();
		FlowLayout fl_pInferior = new FlowLayout(FlowLayout.LEFT);
		pInferior.setLayout(fl_pInferior);
		pInferior.add(msg);

		pSuperiorSuper = new JPanel();
		getContentPane().add(pSuperiorSuper, BorderLayout.NORTH);
		pSuperiorSuper.setLayout(new BorderLayout(0, 0));

		pSuperior1 = new JPanel();
		pSuperiorSuper.add(pSuperior1, BorderLayout.NORTH);
		pSuperior1.setLayout(new FlowLayout(FlowLayout.LEFT));

		lblUsuario = new JLabel("Usu\u00E1rio        ");
		pSuperior1.add(lblUsuario);

		txtUsuario = new JTextField(24);
		txtUsuario.setHorizontalAlignment(SwingConstants.LEFT);
		pSuperior1.add(txtUsuario);

		btnUsuario = new JButton("Alterar Usu\u00E1rio");
		btnUsuario.addActionListener(this);
		pSuperior1.add(btnUsuario);

		pSuperior2 = new JPanel();
		pSuperiorSuper.add(pSuperior2, BorderLayout.SOUTH);
		pSuperior2.setLayout(new FlowLayout(FlowLayout.LEFT));

		lblTopico = new JLabel("T\u00F3pico          ");
		pSuperior2.add(lblTopico);

		txtTopico = new JTextField(24);
		txtTopico.setHorizontalAlignment(SwingConstants.LEFT);
		pSuperior2.add(txtTopico);

		btnTopico = new JButton("Alterar T\u00F3pico  ");
		btnTopico.addActionListener(this);
		pSuperior2.add(btnTopico);

		tela.add(BorderLayout.CENTER, pCentro);
		tela.add(BorderLayout.SOUTH, pInferior);

		btnSend = new JButton("Enviar");
		btnSend.addActionListener(this);
		pInferior.add(btnSend);

		btnClose = new JButton("Fechar");
		btnClose.addActionListener(this);
		pInferior.add(btnClose);

		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		Object fonte = evt.getSource();
		if (fonte == btnSend) {
			if (msg.getText() == null || msg.getText().trim().isEmpty()) {
				return;
			}

			String text = msg.getText();
			Mensagem mensagem = new Mensagem(usuarioAnterior, text, TipoMensagem.CHAT);
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			TrayUtils.getInstance().clienteMQTT().publicar("/" + this.topicoInscrito, gson.toJson(mensagem).getBytes(),
					0);

			msg.setText(null);
		} else if (fonte == btnClose) {
			setVisible(false);
		} else if (fonte == btnTopico) {
			if (txtTopico.getText() == null || txtTopico.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "É necessário informar um tópico!", "Erro",
						JOptionPane.ERROR_MESSAGE);
				txtTopico.setText(this.topicoInscrito);
				return;
			}

			if (topicoInscrito != null)
				TrayUtils.getInstance().clienteMQTT().unsubscribe(this.topicoInscrito);
			this.topicoInscrito = this.txtTopico.getText();
			this.ouvinte = new Ouvinte(TrayUtils.getInstance().clienteMQTT(), "/" + this.topicoInscrito, 0);
		} else if (fonte == btnUsuario) {
			try {
				if (txtUsuario.getText() == null || txtUsuario.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "É necessário informar um usuário!", "Erro",
							JOptionPane.ERROR_MESSAGE);
					txtUsuario.setText(usuarioAnterior);
					return;
				} else
					usuarioAnterior = txtUsuario.getText();

				PropertiesUtils.getInstance().setProperty("tela.usuario", this.txtUsuario.getText());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void addMensagemTela(Mensagem mensagem) {
		if (mensagem.getTipo() == TipoMensagem.CHAT) {
			if (mensagem.getUsername().equals(txtUsuario.getText()))
				append(mensagem.getUsername() + ": ", Color.CYAN);
			else
				append(mensagem.getUsername() + ": ", Color.BLUE);
			append(mensagem.getMensagem() + "\n", Color.BLACK);
		} else if (mensagem.getTipo() == TipoMensagem.SISTEMA) {
			append(mensagem.getMensagem() + "\n", Color.RED);
		}
	}

	private void append(String s, Color c) {
		Integer fonte = null;
		try {
			String fonteStr = PropertiesUtils.getInstance().readProperty("tela.fonte", String.class);
			fonte = Integer.parseInt(fonteStr);
		} catch (Exception e) {
			//
		}

		Font font = new Font("Arial", Font.PLAIN, (fonte != null ? fonte : 15));
		this.areaTexto.setFont(font);

		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		int len = this.areaTexto.getDocument().getLength();
		this.areaTexto.setCaretPosition(len);
		this.areaTexto.setCharacterAttributes(aset, false);
		this.areaTexto.setEditable(true);
		this.areaTexto.replaceSelection(s);
		this.areaTexto.setEditable(false);
	}

	public void changeDefaultUser(String defaultUser) {
		txtUsuario.setText(defaultUser);
		usuarioAnterior = defaultUser;
	}

	public void changeDefaultTopic(String defaultTopic) {
		txtTopico.setText(defaultTopic);
		this.topicoInscrito = defaultTopic;
		this.ouvinte = new Ouvinte(TrayUtils.getInstance().clienteMQTT(), "/" + this.topicoInscrito, 0);
	}

}