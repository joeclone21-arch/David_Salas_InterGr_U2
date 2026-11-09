package vista;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import controlador.logica_ventana;
import java.util.Locale;  //fase 4
import java.util.ResourceBundle;

public class ventana extends JFrame {

	public JPanel contentPane;
	public JTabbedPane tabbedPane;
	public JPanel panelGestion, panelEstadisticas;
	
	public JTextField txt_nombres, txt_telefono, txt_email, txt_buscar;
	public JCheckBox chb_favorito;
	public JComboBox<String> cmb_categoria;
	public JComboBox<String> cmb_idiomas; 
	public JButton btn_add, btn_modificar, btn_eliminar, btn_exportar;
	
	public JTable tabla_contactos;
	public DefaultTableModel modeloTabla;
	public JScrollPane scrTabla;
	public JProgressBar progressBar;
	public JLabel lbl_estadisticasInfo;
	
	// Componentes globales para actualizar en tiempo real
	public JLabel lblNombres, lblTelefono, lblEmail, lblBuscar;
	public ResourceBundle bundle;
	public Locale localeCurrent;

	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                ventana frame = new ventana();
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	public ventana() {
		// INTERNACIONALIZACION
		localeCurrent = Locale.of("es", "ES");
		bundle = ResourceBundle.getBundle("idiomas.mensajes", localeCurrent);
		
		setTitle(bundle.getString("app.titulo"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1026, 748);		
					
		// Contenedor principal con BorderLayout
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);
		
		// Panel superior para selector de idiomas
		JPanel panelSuperiorGlobal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		cmb_idiomas = new JComboBox<>(new String[]{"Español", "English", "Français"});
		cmb_idiomas.addActionListener(e -> cambiarIdioma(cmb_idiomas.getSelectedIndex()));
		panelSuperiorGlobal.add(new JLabel("Idioma / Language: "));
		panelSuperiorGlobal.add(cmb_idiomas);
		contentPane.add(panelSuperiorGlobal, BorderLayout.NORTH);

		// Panel de Pestañas (JTabbedPane)
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		// --- PESTAÑA 1 GESTION DE CONTACTOS ---
		panelGestion = new JPanel();
		panelGestion.setLayout(new BorderLayout(10, 10));
		tabbedPane.addTab(bundle.getString("tab.gestion"), panelGestion);

		// PANEL NORTE: Formulario, Botones y Opciones
		JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
		panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Formulario de entradas (Izquierda)
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS)); 
		panelFormulario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Fila 1 para Nombres
		JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		lblNombres = new JLabel(bundle.getString("lbl.nombres"));
		lblNombres.setPreferredSize(new Dimension(80, 25)); 
		txt_nombres = new JTextField();
		txt_nombres.setPreferredSize(new Dimension(220, 25)); 
		fila1.add(lblNombres);
		fila1.add(txt_nombres);
		panelFormulario.add(fila1);

		// Fila 2 para Teléfono
		JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		lblTelefono = new JLabel(bundle.getString("lbl.telefono"));
		lblTelefono.setPreferredSize(new Dimension(80, 25));
		txt_telefono = new JTextField();
		txt_telefono.setPreferredSize(new Dimension(220, 25));
		fila2.add(lblTelefono);
		fila2.add(txt_telefono);
		panelFormulario.add(fila2);

		// Fila 3 para Email
		JPanel fila3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		lblEmail = new JLabel(bundle.getString("lbl.email"));
		lblEmail.setPreferredSize(new Dimension(80, 25));
		txt_email = new JTextField();
		txt_email.setPreferredSize(new Dimension(220, 25));
		fila3.add(lblEmail);
		fila3.add(txt_email);
		panelFormulario.add(fila3);

		// PANEL DE BOTONES (Derecha)
		JPanel panelBotonesGrid = new JPanel(new GridLayout(2, 2, 15, 15));
		
		btn_add = crearBotonIcono("src/iconos/agregar-contacto.png", bundle.getString("btn.agregar"));
		btn_modificar = crearBotonIcono("src/iconos/editar-contacto.png", bundle.getString("btn.modificar"));
		btn_eliminar = crearBotonIcono("src/iconos/quitar-contacto.png", bundle.getString("btn.eliminar"));
		btn_exportar = crearBotonIcono("src/iconos/exportar.png", bundle.getString("btn.exportar"));

		panelBotonesGrid.add(btn_add);
		panelBotonesGrid.add(btn_modificar);
		panelBotonesGrid.add(btn_eliminar);
		panelBotonesGrid.add(btn_exportar);

		JPanel panelFormYBotones = new JPanel(new BorderLayout(40, 0));
		panelFormYBotones.add(panelFormulario, BorderLayout.WEST);
		panelFormYBotones.add(panelBotonesGrid, BorderLayout.CENTER);
		panelNorte.add(panelFormYBotones, BorderLayout.CENTER);

		// Panel de opciones (Favorito y Categoría)
		JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		
		ImageIcon iconoCheckOriginal = new ImageIcon("src/iconos/no-favorito.png"); 
		ImageIcon iconoCheckSeleccionado = new ImageIcon("src/iconos/favorito.png"); 

		Image imgOff = iconoCheckOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		Image imgOn = iconoCheckSeleccionado.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

		chb_favorito = new JCheckBox(bundle.getString("lbl.favorito"));
		chb_favorito.setIcon(new ImageIcon(imgOff));
		chb_favorito.setSelectedIcon(new ImageIcon(imgOn));
		chb_favorito.setOpaque(false);
		panelOpciones.add(chb_favorito);

		// Inicialización del ComboBox de Categorías con internacionalización
		cmb_categoria = new JComboBox<>();
		cmb_categoria.setPreferredSize(new Dimension(160, 25));
		actualizarComboCategorias();
		panelOpciones.add(cmb_categoria);

		panelNorte.add(panelOpciones, BorderLayout.SOUTH);
		panelGestion.add(panelNorte, BorderLayout.NORTH);

		// TABLA CENTRAL DE CONTACTOS
		modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "Telefono", "Email", "Categoria", "Favorito"}, 0);
		tabla_contactos = new JTable(modeloTabla);
		scrTabla = new JScrollPane(tabla_contactos);
		panelGestion.add(scrTabla, BorderLayout.CENTER);

		// PANEL INFERIOR (SUR)
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		
		ImageIcon iconoLupaOriginal = new ImageIcon("src/iconos/buscar-contacto.png"); 
		Image imgLupaEscalada = iconoLupaOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		final ImageIcon iconoLupa = new ImageIcon(imgLupaEscalada);

		txt_buscar = new JTextField(20);
		txt_buscar.setBorder(BorderFactory.createCompoundBorder(
			txt_buscar.getBorder(),
			new javax.swing.border.Border() {
				@Override
				public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
					int iconHeight = iconoLupa.getIconHeight();
					int iconX = x + 5;
					int iconY = y + (height - iconHeight) / 2;
					iconoLupa.paintIcon(c, g, iconX, iconY);
				}
				@Override
				public Insets getBorderInsets(Component c) { return new Insets(0, 28, 0, 0); }
				@Override
				public boolean isBorderOpaque() { return false; }
			}
		));

		lblBuscar = new JLabel(bundle.getString("lbl.buscar"));
		panelSur.add(lblBuscar); 
		panelSur.add(txt_buscar);

		progressBar = new JProgressBar(0, 100);
		progressBar.setPreferredSize(new Dimension(300, 25));
		progressBar.setStringPainted(true);
		panelSur.add(progressBar);
		
		panelGestion.add(panelSur, BorderLayout.SOUTH);

		// --- PESTAÑA 2 ESTADISTICAS ---
		panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
		lbl_estadisticasInfo = new JLabel("Resumen general de contactos registrados en el sistema.");
		lbl_estadisticasInfo.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEstadisticas.add(lbl_estadisticasInfo);
		tabbedPane.addTab(bundle.getString("tab.estadisticas"), panelEstadisticas);

		// Enlazar con el controlador
		logica_ventana lv = new logica_ventana(this);
	}

	private JButton crearBotonIcono(String rutaIcono, String tooltip) {
		ImageIcon iconoOriginal = new ImageIcon(rutaIcono);
		Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
		JButton boton = new JButton(new ImageIcon(imagenEscalada));
		boton.setContentAreaFilled(false);
		boton.setBorderPainted(false);
		boton.setFocusPainted(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.setToolTipText(tooltip);
		return boton;
	}
	
	// Método auxiliar para poblar o actualizar los ítems del ComboBox según el idioma actual
	private void actualizarComboCategorias() {
		int seleccionActual = cmb_categoria.getSelectedIndex();
		cmb_categoria.removeAllItems();
		cmb_categoria.addItem(bundle.getString("cat.elija"));
		cmb_categoria.addItem(bundle.getString("cat.familia"));
		cmb_categoria.addItem(bundle.getString("cat.amigos"));
		cmb_categoria.addItem(bundle.getString("cat.trabajo"));
		if (seleccionActual >= 0 && seleccionActual < cmb_categoria.getItemCount()) {
			cmb_categoria.setSelectedIndex(seleccionActual);
		}
	}
	
	// Método para cambiar dinámicamente los textos al seleccionar otro idioma
	public void cambiarIdioma(int opcion) {
	    if (opcion == 0) {
	        localeCurrent = Locale.of("es", "ES");
	    } else if (opcion == 1) {
	        localeCurrent = Locale.of("en", "US");
	    } else if (opcion == 2) {
	        localeCurrent = Locale.of("fr", "FR");
	    }

	    try {
	        bundle = ResourceBundle.getBundle("idiomas.mensajes", localeCurrent);

	        setTitle(bundle.getString("app.titulo"));
	        tabbedPane.setTitleAt(0, bundle.getString("tab.gestion"));
	        tabbedPane.setTitleAt(1, bundle.getString("tab.estadisticas"));
	        
	        // Actualización de etiquetas del formulario
	        lblNombres.setText(bundle.getString("lbl.nombres"));
	        lblTelefono.setText(bundle.getString("lbl.telefono"));
	        lblEmail.setText(bundle.getString("lbl.email"));
	        chb_favorito.setText(bundle.getString("lbl.favorito"));
	        lblBuscar.setText(bundle.getString("lbl.buscar"));
	        
	        // Actualización del ComboBox de categorías
	        actualizarComboCategorias();
	        
	        // Actualización de cabeceras de la tabla de contactos
	        modeloTabla.setColumnIdentifiers(new Object[]{
	            bundle.getString("lbl.nombres").replace(":", ""),
	            bundle.getString("lbl.telefono").replace(":", ""),
	            bundle.getString("lbl.email").replace(":", ""),
	            bundle.getString("cat.elija").replace("Choose a category", "Category").replace("Elija una categoria", "Categoria").replace("Choisir une catégorie", "Catégorie"), 
	            bundle.getString("lbl.favorito")
	        });
	        
	        // Tooltips de botones
	        btn_add.setToolTipText(bundle.getString("btn.agregar"));
	        btn_modificar.setToolTipText(bundle.getString("btn.modificar"));
	        btn_eliminar.setToolTipText(bundle.getString("btn.eliminar"));
	        btn_exportar.setToolTipText(bundle.getString("btn.exportar"));
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo de recursos para el idioma seleccionado.", "Error de Internacionalizacion", JOptionPane.ERROR_MESSAGE);
	    }
	}
}