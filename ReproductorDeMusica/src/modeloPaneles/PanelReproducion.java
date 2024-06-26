package modeloPaneles;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import logica.ControladorDeSonido;
import logica.GestionDeLaInformacion;
import modelo.Cancion;
import modelo.Cliente;
import view.VistaPrincipal;

/**
 * Panel utilizado para la reprodución de las canciones y su respectivo guardado en favoritos
 * @author Kaiet Zarzosa, Ibai Manso
 */
public class PanelReproducion extends JPanel {

	private static final long serialVersionUID = 1L;

	private Cliente cliente;
	private JButton btnPlay, btnAnterior, btnSiguiente, btnMenu, btnMeGusta;
	private JLabel lblAlbum, lblDuracion, lblColaboradores, lblImagenAutor;
	private ArrayList<Cancion> canciones;
	private ArrayList<Cancion> anuncios;
	private int contador;
	private ControladorDeSonido control;
	private JLabel lblNombre;
	private boolean reproduciendo;
	private long tiempo;

	public PanelReproducion(VistaPrincipal ventana, GestionDeLaInformacion gestion, int cont) {
		setBackground(new Color(0, 255, 127));
		gestion.recogerCancionesDeLaBaseDeDatosConAudio();
		canciones = gestion.devolverCanciones();
		this.contador = cont;
		reproduciendo = false;
		control = new ControladorDeSonido(canciones.get(contador));
		tiempo = System.currentTimeMillis();
		cliente = gestion.devolverUsuario();
		anuncios = gestion.recogerAnunciosDeLaBaseDeDatosConAudio();

		setLayout(null);
		setSize(ventana.getSize());

		JPanel panel1 = new JPanel();

		panel1.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
		panel1.setBackground(new Color(0, 255, 0));
		panel1.setBounds(-48, 0, 762, 140);
		add(panel1);
		panel1.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("multimedia/TipografiaAplicacion.png"));
		lblNewLabel.setBounds(179, 9, 313, 118);
		panel1.add(lblNewLabel);

		JButton btnPerfil = new JButton("");
		btnPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ventana.cambiarDePanel(4, 0);

			}
		});
		btnPerfil.setIcon(new ImageIcon("multimedia/perfil.png"));
		btnPerfil.setBounds(590, 7, 137, 120);
		btnPerfil.setFocusPainted(false);
		btnPerfil.setBorderPainted(false);
		btnPerfil.setContentAreaFilled(false);
		panel1.add(btnPerfil);

		JLabel lblLogo = new JLabel("");
		lblLogo.setBounds(35, 11, 167, 108);
		panel1.add(lblLogo);
		lblLogo.setIcon(new ImageIcon("multimedia/logoAplicacion.png"));

		JLabel lblInformacion = new JLabel("Información");
		lblInformacion.setBounds(89, 507, 238, 30);
		add(lblInformacion);

		lblNombre = new JLabel(canciones.get(contador).getNombreAudio());
		lblNombre.setBounds(89, 532, 238, 30);
		add(lblNombre);

		lblImagenAutor = new JLabel("");
		ImageIcon imageIcon = null;
		if (canciones.get(contador).getImagen() == null) {
			imageIcon = new ImageIcon("multimedia/default_perfil.png");
		} else {
			imageIcon = canciones.get(contador).getImagen();
		}
		Image image = imageIcon.getImage();
		Image newImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon newImageIcon = new ImageIcon(newImage);
		lblImagenAutor.setIcon(newImageIcon);
		lblImagenAutor.setBounds(183, 151, 300, 300);
		lblImagenAutor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(lblImagenAutor);

		JButton bntCerrarSesion = new JButton("");
		bntCerrarSesion.setIcon(new ImageIcon("multimedia/cerrarSesion.png"));
		bntCerrarSesion.setFocusPainted(false);
		bntCerrarSesion.setBorderPainted(false);
		bntCerrarSesion.setContentAreaFilled(false);
		bntCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gestion.devolverAlbum().getAño() != null) {
					ventana.cambiarDePanel(7, 0);
				} else {
					ventana.cambiarDePanel(15, 0);
				}

			}
		});
		bntCerrarSesion.setBounds(575, 151, 153, 62);
		add(bntCerrarSesion);

		lblDuracion = new JLabel("" + canciones.get(contador).getDuracion());
		lblDuracion.setBounds(89, 562, 238, 30);
		add(lblDuracion);

		lblAlbum = new JLabel("" + canciones.get(contador).getNombreAudio());
		lblAlbum.setBounds(340, 532, 238, 30);
		add(lblAlbum);

		lblColaboradores = new JLabel((String) null);
		lblColaboradores.setBounds(340, 535, 238, 30);
		add(lblColaboradores);

		btnPlay = new JButton("▶");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reproduciendo = control.verificarEjecucion();
				if (reproduciendo == false) {
					btnPlay.setText("⏸");
					control.reproducir();
				} else {
					btnPlay.setText("▶");
					control.pausar();
				}
			}
		});
		btnPlay.setBounds(292, 473, 89, 23);
		add(btnPlay);

		btnAnterior = new JButton("<-");
		btnAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancionAnterior();
			}
		});
		btnAnterior.setBounds(237, 473, 45, 23);
		add(btnAnterior);

		btnSiguiente = new JButton("->");
		btnSiguiente.setBounds(391, 473, 45, 23);
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancionSiguiente();
			}
		});
		add(btnSiguiente);

		btnMenu = new JButton("|||");
		btnMenu.setBounds(89, 473, 89, 23);
		add(btnMenu);
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] botones = { " Menu", " Fav" };
				int opcion = JOptionPane.showOptionDialog(null, " Quieres guardar esto en favoritos?", "Advertencia",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/* icono */, botones,
						botones[0]);
				if (opcion == 1) {
					// TODO
					control.pausar();
					gestion.insertarCacionEnPlayList(canciones.get(contador));
					ventana.cambiarDePanel(3, 0);
				} else if (opcion == 0) {
					control.pausar();
					ventana.cambiarDePanel(3, 0);
				}
			}
		});
		btnMeGusta = new JButton("<3");
		btnMeGusta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestion.insertarCacionEnPlayList(canciones.get(contador));
			}
		});
		btnMeGusta.setBounds(500, 473, 89, 23);
		add(btnMeGusta);
	}

	public void mostarAnuncio() {
		Long tiempoAnuncio = System.currentTimeMillis();
		Long tiempoAnuncio30000 = tiempoAnuncio + 30000;
		control.cambiarCancion(anuncios.get(1));
		cambiarInfoAnuncio();
		control.reproducir();
		while (tiempoAnuncio < tiempoAnuncio30000) {
			btnPlay.setEnabled(false);
			btnAnterior.setEnabled(false);
			btnMeGusta.setEnabled(false);
			btnMenu.setEnabled(false);
			btnSiguiente.setEnabled(false);
			tiempoAnuncio = System.currentTimeMillis();
		}
		btnPlay.setEnabled(true);
		btnAnterior.setEnabled(true);
		btnMeGusta.setEnabled(true);
		btnMenu.setEnabled(true);
		btnSiguiente.setEnabled(true);

	}

	public void cancionAnterior() {
		if (cliente.getPremium().equalsIgnoreCase("Free")) {
			System.out.println(tiempo);
			System.out.println(tiempo + 600);
			System.out.println(System.currentTimeMillis());
			if (System.currentTimeMillis() < tiempo + 60000) {
				JOptionPane.showMessageDialog(null, "No han pasado 10 mins");
			} else {
				if (contador <= 0) {
					mostarAnuncio();
					contador = canciones.size() - 1;
					control.cambiarCancion(canciones.get(contador));
					cambiarInfoAudio();
				} else {
					mostarAnuncio();
					contador--;
					control.cambiarCancion(canciones.get(contador));
					cambiarInfoAudio();
				}
				tiempo = System.currentTimeMillis();
			}
		} else {
			if (contador <= 0) {
				contador = canciones.size() - 1;
				control.cambiarCancion(canciones.get(contador));
				cambiarInfoAudio();
			} else {
				contador--;
				control.cambiarCancion(canciones.get(contador));
				cambiarInfoAudio();
			}
			tiempo = System.currentTimeMillis();
		}
	}

	public void cancionSiguiente() {
		if (cliente.getPremium().equalsIgnoreCase("Free")) {
			if (System.currentTimeMillis() < tiempo + 1000) {
				JOptionPane.showMessageDialog(null, "No han pasado 10 mins");
			} else {
				if (contador >= canciones.size() - 1) {
					mostarAnuncio();
					contador = 0;
					control.cambiarCancion(canciones.get(contador));
					cambiarInfoAudio();
				} else {
					mostarAnuncio();
					contador++;
					control.cambiarCancion(canciones.get(contador));
					cambiarInfoAudio();
				}
				tiempo = System.currentTimeMillis();
			}
		} else {
			if (contador >= canciones.size() - 1) {
				contador = 0;
				control.cambiarCancion(canciones.get(contador));
				cambiarInfoAudio();
			} else {
				contador++;
				control.cambiarCancion(canciones.get(contador));
				cambiarInfoAudio();
			}
			tiempo = System.currentTimeMillis();
		}
	}

	public void cambiarInfoAudio() {
		lblAlbum.setText(canciones.get(contador).getIdAlbum());
		lblDuracion.setText("" + canciones.get(contador).getDuracion());
		lblNombre.setText(canciones.get(contador).getNombreAudio());
		ImageIcon imageIcon = null;
		if (canciones.get(contador).getImagen() == null) {
			imageIcon = new ImageIcon("multimedia/default_perfil.png");
		} else {
			imageIcon = canciones.get(contador).getImagen();
		}
		Image image = imageIcon.getImage();
		Image newImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon newImageIcon = new ImageIcon(newImage);
		lblImagenAutor.setIcon(newImageIcon);
	}

	public void cambiarInfoAnuncio() {
		lblAlbum.setText(anuncios.get(0).getIdAlbum());
		lblDuracion.setText("" + anuncios.get(0).getDuracion());
		lblNombre.setText(anuncios.get(0).getNombreAudio());
		ImageIcon imageIcon = null;
		if (canciones.get(0).getImagen() == null) {
			imageIcon = new ImageIcon("multimedia/default_perfil.png");
		} else {
			imageIcon = anuncios.get(0).getImagen();
		}
		Image image = imageIcon.getImage();
		Image newImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon newImageIcon = new ImageIcon(newImage);
		lblImagenAutor.setIcon(newImageIcon);
	}
}
