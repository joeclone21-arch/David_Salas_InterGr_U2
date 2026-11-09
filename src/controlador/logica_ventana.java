package controlador;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import vista.ventana;
import modelo.*;

public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
	private ventana delegado;
	private String nombres, email, telefono, categoria = "";
	private persona persona;
	private List<persona> contactos;
	private boolean favorito = false;

	public logica_ventana(ventana delegado) {
		this.delegado = delegado;
		cargarContactosConProgreso(); 

		// Listeners de Botones
		this.delegado.btn_add.addActionListener(this);
		this.delegado.btn_eliminar.addActionListener(this);
		this.delegado.btn_modificar.addActionListener(this);
		this.delegado.btn_exportar.addActionListener(this);

		// Listeners de Tabla y Componentes
		this.delegado.tabla_contactos.getSelectionModel().addListSelectionListener(this);
		this.delegado.cmb_categoria.addItemListener(this);
		this.delegado.chb_favorito.addItemListener(this);

		// Evento de Teclado para busqueda en tiempo real
		this.delegado.txt_buscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtrarContactos(delegado.txt_buscar.getText());
			}
		});

		// Evento de mouse contextual (clic derecho en la tabla)
		this.delegado.tabla_contactos.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = delegado.tabla_contactos.rowAtPoint(e.getPoint());
					if (row >= 0) {
						delegado.tabla_contactos.setRowSelectionInterval(row, row);
						JPopupMenu menu = new JPopupMenu();
						JMenuItem itemEliminar = new JMenuItem(delegado.bundle.getString("menu.eliminar.contextual"));
						itemEliminar.addActionListener(ev -> eliminarContactoSeleccionado(row));
						menu.add(itemEliminar);
						menu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});
	}

	private void inicializacionCampos() {
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	public void cargarContactosConProgreso() {
		SwingWorker<Void, Integer> worker = new SwingWorker<>() {
			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i <= 100; i += 20) {
					Thread.sleep(500);
					publish(i);
				}
				contactos = new personaDAO(new persona()).leerArchivo();
				return null;
			}

			@Override
			protected void process(List<Integer> chunks) {
				delegado.progressBar.setValue(chunks.get(chunks.size() - 1));
			}

			@Override
			protected void done() {
				actualizarTabla(contactos);
				actualizarEstadisticas();
			}
		};
		worker.execute();
	}

	private void actualizarTabla(List<persona> lista) {
		DefaultTableModel modelo = (DefaultTableModel) delegado.tabla_contactos.getModel();
		modelo.setRowCount(0);
		for (persona p : lista) {
			modelo.addRow(new Object[]{p.getNombre(), p.getTelefono(), p.getEmail(), p.getCategoria(), p.isFavorito()});
		}
	}

	private void actualizarEstadisticas() {
		int total = contactos != null ? contactos.size() : 0;
		long favoritos = contactos != null ? contactos.stream().filter(p -> p.isFavorito()).count() : 0;
		delegado.lbl_estadisticasInfo.setText("<html><b>" + delegado.bundle.getString("lbl.estadisticas.total") + "</b> " + total + " | <b>" + delegado.bundle.getString("lbl.estadisticas.favoritos") + "</b> " + favoritos + "</html>");
	}

	private void filtrarConfig(String filtro) {
		if (contactos == null) return;
		DefaultTableModel modelo = (DefaultTableModel) delegado.tabla_contactos.getModel();
		modelo.setRowCount(0);
		for (persona p : contactos) {
			if (p.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
				modelo.addRow(new Object[]{p.getNombre(), p.getTelefono(), p.getEmail(), p.getCategoria(), p.isFavorito()});
			}
		}
	}
	
	private void filtrarContactos(String texto) {
		filtrarConfig(texto);
	}

	private void limpiarCampos() {
		delegado.txt_nombres.setText("");
		delegado.txt_telefono.setText("");
		delegado.txt_email.setText("");
		categoria = "";
		favorito = false;
		delegado.chb_favorito.setSelected(false);
		delegado.cmb_categoria.setSelectedIndex(0);
		inicializacionCampos();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		inicializacionCampos();

		if (e.getSource() == delegado.btn_add) {
			if (!nombres.isEmpty() && !telefono.isEmpty() && !email.isEmpty()) {
				if (!categoria.equals("Elija una categoria") && !categoria.equals("Choose a category") && !categoria.equals("Choisir une catégorie") && !categoria.isEmpty()) {
					persona = new persona(nombres, telefono, email, categoria, favorito);
					new personaDAO(persona).escribirArchivo();
					
					try {
						contactos = new personaDAO(new persona()).leerArchivo();
						actualizarTabla(contactos);
						actualizarEstadisticas();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					
					limpiarCampos();
					JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.registro.exito"));
				} else {
					JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.categoria.invalida"));
				}
			} else {
				JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.campos.vacios"));
			}
		} else if (e.getSource() == delegado.btn_eliminar) {
			int row = delegado.tabla_contactos.getSelectedRow();
			if (row != -1) {
				eliminarContactoSeleccionado(row);
			} else {
				JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.seleccionar.eliminar"));
			}
		} else if (e.getSource() == delegado.btn_modificar) {
			int row = delegado.tabla_contactos.getSelectedRow();
			if (row != -1) {
				persona pMod = contactos.get(row);
				pMod.setNombre(nombres);
				pMod.setTelefono(telefono);
				pMod.setEmail(email);
				pMod.setCategoria(categoria);
				pMod.setFavorito(favorito);
				
				try {
					new personaDAO(new persona()).actualizarContactos(contactos);
					
					contactos = new personaDAO(new persona()).leerArchivo();
					actualizarTabla(contactos);
					actualizarEstadisticas();
					
					limpiarCampos();
					JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.modificar.exito"));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.seleccionar.modificar"));
			}
		} else if (e.getSource() == delegado.btn_exportar) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle(delegado.bundle.getString("msg.exportar.titulo"));
			javax.swing.filechooser.FileNameExtensionFilter filter = 
					new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV (*.csv)", "csv");
			fileChooser.setFileFilter(filter);

			if (fileChooser.showSaveDialog(delegado) == JFileChooser.APPROVE_OPTION) {
				File archivo = fileChooser.getSelectedFile();
				if (!archivo.getName().toLowerCase().endsWith(".csv")) {
					archivo = new File(archivo.getAbsolutePath() + ".csv");
				}

				boolean exito = new personaDAO(new persona()).exportarCSV(archivo);
				if (exito) {
					JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.exportar.exito") + archivo.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.exportar.error"));
				}
			}
		}
	}

	private void eliminarContactoSeleccionado(int row) {
		int confirm = JOptionPane.showConfirmDialog(
			delegado, 
			delegado.bundle.getString("msg.confirmar.eliminar.pregunta"), 
			delegado.bundle.getString("msg.confirmar.eliminar.titulo"), 
			JOptionPane.YES_NO_OPTION
		);
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				contactos.remove(row);
				new personaDAO(new persona()).actualizarContactos(contactos);
				
				contactos = new personaDAO(new persona()).leerArchivo();
				actualizarTabla(contactos);
				actualizarEstadisticas();
				
				limpiarCampos();
				JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.eliminar.exito"));
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(delegado, delegado.bundle.getString("msg.eliminar.error"));
			}
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int row = delegado.tabla_contactos.getSelectedRow();
		if (row != -1 && row < contactos.size()) {
			persona p = contactos.get(row);
			delegado.txt_nombres.setText(p.getNombre());
			delegado.txt_telefono.setText(p.getTelefono());
			delegado.txt_email.setText(p.getEmail());
			delegado.chb_favorito.setSelected(p.isFavorito());
			delegado.cmb_categoria.setSelectedItem(p.getCategoria());
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == delegado.cmb_categoria && e.getStateChange() == ItemEvent.SELECTED) {
			Object item = delegado.cmb_categoria.getSelectedItem();
			if (item != null) categoria = item.toString();
		} else if (e.getSource() == delegado.chb_favorito) {
			favorito = delegado.chb_favorito.isSelected();
		}
	}
}