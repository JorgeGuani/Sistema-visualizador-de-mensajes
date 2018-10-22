/*
 * Alumnos:
    -Aguado Guaní Jorge Enrique
    -Méndez Martínez Natalia
 */
package sistemavisualizadordemensajes;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import jssc.SerialPortException;

public class InterfazGraficaDisplay extends JFrame {
    // Variables                     
    private JMenuItem Abrir;
    private JMenu MenuAcercaDe;
    private JMenuBar barraMenu;
    private JButton btnAgregar;
    private JButton btnCerrar;
    private JButton btnEliminar;
    private JButton btnHumedad;
    private JButton btnLuminosidad;
    private JButton btnMostrar;
    private JButton btnOK;
    private JButton btnTemperatura;
    private JMenuItem itemGuardar;
    private JMenuItem itemGuardarComo;
    private JMenuItem itemNuevo;
    private JLabel lblClima;
    private JLabel lblMensajeAgregar;
    private JLabel lblMensajes;
    private JList<String> listaMensajes;
    private JMenu menuArchivo;
    private JPanel panelAgregarMensaje;
    private JPanel panelInicial;
    private JScrollPane scrollMensajeAgregar;
    private JScrollPane scrollMensajes;
    private JTextArea txaMensaje;   
    private String fechaDisplay;
    private DefaultListModel model = new DefaultListModel();
    private DefaultListModel model2 = new DefaultListModel(); 
    private boolean climaPresionado = false;
    // Fin de la delaración de variables
    
    // Librería para la comunicación serial
    PanamaHitek_Arduino arduino = new PanamaHitek_Arduino(); 
    PanamaHitek_Arduino tecladoMatricial = new PanamaHitek_Arduino();
    

    public InterfazGraficaDisplay() {
        inicializarComponentes();
        posicionarVentana();    

        // Iniciar la comuniación serial:
        try {
            arduino.arduinoTX("/dev/ttyUSB0", 9600);
        } catch (ArduinoException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //tecladoMatricial.arduinoRX("/dev/ttyUSB1", 9600, listener);
    }
    
    private void inicializarComponentes() {
        // Instanciar componentes
        panelInicial = new JPanel();
        btnAgregar = new JButton();
        btnEliminar = new JButton();
        lblMensajes = new JLabel();
        btnMostrar = new JButton();
        scrollMensajes = new JScrollPane();
        listaMensajes = new JList<>();
        lblClima = new JLabel();
        btnTemperatura = new JButton();
        btnHumedad = new JButton();
        btnLuminosidad = new JButton();
        panelAgregarMensaje = new JPanel();
        btnOK = new JButton();
        lblMensajeAgregar = new JLabel();
        btnCerrar = new JButton();
        scrollMensajeAgregar = new JScrollPane();
        txaMensaje = new JTextArea();
        barraMenu = new JMenuBar();
        menuArchivo = new JMenu();
        itemNuevo = new JMenuItem();
        Abrir = new JMenuItem();
        itemGuardar = new JMenuItem();
        itemGuardarComo = new JMenuItem();
        MenuAcercaDe = new JMenu();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // Botón agregar mensaje
        btnAgregar.setBackground(new Color(0, 255, 24));
        btnAgregar.setText("Agregar mensaje");
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        
        // Botón eliminar mensaje
        btnEliminar.setBackground(new Color(255, 0, 0));
        btnEliminar.setForeground(new Color(254, 254, 254));
        btnEliminar.setText("Eliminar mensaje");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        // Botón mostrar mensaje
        btnMostrar.setBackground(new Color(20, 19, 234));
        btnMostrar.setForeground(new Color(254, 254, 254));
        btnMostrar.setText("Mostrar mensaje");
        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });
        
        // Botón mostrar temperatura en el display
        btnTemperatura.setText("Temperatura");
        btnTemperatura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnTemperaturaActionPerformed(evt);
            }
        });

        // Botón mostrar humedad en el display
        btnHumedad.setText("Humedad");
        btnHumedad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnHumedadActionPerformed(evt);
            }
        });

        // Botón mostrar luminosidad en el display
        btnLuminosidad.setText("Luminosidad");
        btnLuminosidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnLuminosidadActionPerformed(evt);
            }
        });
        
        // Etiquetas
        lblMensajes.setText("Lista de mensajes para mostrar");
        lblClima.setText("Mostrar condiciones climatológicas:");
        lblMensajeAgregar.setText("Escriba su mensaje:");
        
        scrollMensajes.setViewportView(listaMensajes);

        // Botón confirmar (OK) para tras escribir mensaje
        btnOK.setText("OK");
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        // Botón cerrar en el módulo escribir mensaje
        btnCerrar.setBackground(new Color(255, 0, 6));
        btnCerrar.setForeground(new Color(254, 254, 254));
        btnCerrar.setText("X");
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        // Área de texto para escribir mensaje
        txaMensaje.setColumns(20);
        txaMensaje.setRows(5);
        txaMensaje.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                txaMensajeKeyPressed(evt);
            }
        });
        //Añadirle un scroll al área de texto escribir mensajes
        scrollMensajeAgregar.setViewportView(txaMensaje);
        
        // Páneles
        GroupLayout panelInicialLayout = new GroupLayout(panelInicial);
        panelInicial.setLayout(panelInicialLayout);
        panelInicialLayout.setHorizontalGroup(
            panelInicialLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInicialLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, panelInicialLayout.createSequentialGroup()
                        .addComponent(btnEliminar)
                        .addGap(27, 27, 27)
                        .addComponent(btnAgregar)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(btnMostrar))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addGroup(panelInicialLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(panelInicialLayout.createSequentialGroup()
                                .addComponent(lblClima)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE))
                            .addGroup(panelInicialLayout.createSequentialGroup()
                                .addComponent(btnTemperatura)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHumedad, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addGap(68, 68, 68)))
                        .addComponent(btnLuminosidad))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(lblMensajes, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(scrollMensajes))
                .addContainerGap())
        );
        panelInicialLayout.setVerticalGroup(
            panelInicialLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, panelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblClima)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInicialLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTemperatura)
                    .addComponent(btnHumedad)
                    .addComponent(btnLuminosidad))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(lblMensajes, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollMensajes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(panelInicialLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEliminar)
                    .addComponent(btnMostrar))
                .addContainerGap())
        );

        panelAgregarMensaje.setBackground(new Color(194, 227, 237));
        
        GroupLayout panelAgregarMensajeLayout = new GroupLayout(panelAgregarMensaje);
        panelAgregarMensaje.setLayout(panelAgregarMensajeLayout);
        panelAgregarMensajeLayout.setHorizontalGroup(
            panelAgregarMensajeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarMensajeLayout.createSequentialGroup()
                .addContainerGap(142, Short.MAX_VALUE)
                .addGroup(panelAgregarMensajeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, panelAgregarMensajeLayout.createSequentialGroup()
                        .addComponent(btnOK, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9))
                    .addComponent(lblMensajeAgregar, GroupLayout.Alignment.TRAILING))
                .addGap(123, 123, 123)
                .addComponent(btnCerrar, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
            .addGroup(panelAgregarMensajeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(panelAgregarMensajeLayout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(scrollMensajeAgregar, GroupLayout.PREFERRED_SIZE, 358, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(41, Short.MAX_VALUE)))
        );
        panelAgregarMensajeLayout.setVerticalGroup(
            panelAgregarMensajeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarMensajeLayout.createSequentialGroup()
                .addGroup(panelAgregarMensajeLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCerrar)
                    .addGroup(panelAgregarMensajeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblMensajeAgregar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(btnOK, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelAgregarMensajeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(panelAgregarMensajeLayout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(scrollMensajeAgregar, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(64, Short.MAX_VALUE)))
        );

        // Barra de menús
        menuArchivo.setText("Archivo");

        // Elementos del menú
        itemNuevo.setText("Nuevo");
        itemNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                itemNuevoActionPerformed(evt);
            }
        });
        menuArchivo.add(itemNuevo);

        Abrir.setText("Abrir");
        Abrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                AbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(Abrir);

        itemGuardar.setText("Guardar");
        menuArchivo.add(itemGuardar);

        itemGuardarComo.setText("Guardar como...");
        itemGuardarComo.setToolTipText("Guarda los mensajes almacenados para su posterior uso.");
        itemGuardarComo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                itemGuardarComoActionPerformed(evt);
            }
        });
        menuArchivo.add(itemGuardarComo);

        barraMenu.add(menuArchivo);

        MenuAcercaDe.setText("Acerca de");
        barraMenu.add(MenuAcercaDe);

        setJMenuBar(barraMenu);

        // Finalmente se agregan los páneles al Frame
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelAgregarMensaje, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panelInicial, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(panelAgregarMensaje, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panelInicial, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        
        scrollMensajeAgregar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        //Desde el inciio enfocar el botón agregar mensaje
        btnAgregar.requestFocus(true);
        panelAgregarMensaje.setVisible(false);
    } 
    
    private void posicionarVentana() {
        Dimension tamañoPantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Point mitad = new Point(tamañoPantalla.width/2, tamañoPantalla.height/2);
        Point nuevaLocalizacion = new Point(mitad.x - panelInicial.getWidth()/2,
                                            mitad.y - panelInicial.getHeight()/2);
        setLocation(nuevaLocalizacion);
        setResizable(false);
    }
    
    private void validacionMensaje() {
        if (txaMensaje.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "No ha ingresado nada");
        } else if (txaMensaje.getText().length() > 140) {
            JOptionPane.showMessageDialog(null, "Solo 140 caracteres");
        } else {
            boolean existe = existeElemento(txaMensaje.getText());
            if (!existe) {
                formatofecha();
                model.addElement(txaMensaje.getText().trim());
                model2.addElement(fechaDisplay); //Añadir la fecha actual del mensaje emitido
                System.out.println(model2);
                panelInicial.setVisible(true);
                panelAgregarMensaje.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Este mensaje ya existe");
            }
        }
    }
    
    private void formatofecha() {
        Date fecha = new Date();
        Calendar calendario = new GregorianCalendar();
        calendario.setTime(fecha);
        //Formato fecha y 
        fechaDisplay = (calendario.get(Calendar.DAY_OF_MONTH) >= 10 ? 
                        calendario.get(Calendar.DAY_OF_MONTH) : 
                        "0" + calendario.get(Calendar.DAY_OF_MONTH))+"/" 

                     + (calendario.get(Calendar.MONTH) >= 10 ? 
                        calendario.get(Calendar.MONTH) : 
                        "0" + calendario.get(Calendar.MONTH))+"/" 

                     + calendario.get(Calendar.YEAR) + " "

                     + (calendario.get(Calendar.HOUR_OF_DAY) >= 10 ? 
                        calendario.get(Calendar.HOUR_OF_DAY) : "0"
                     +  calendario.get(Calendar.HOUR_OF_DAY))+":"

                     + (calendario.get(Calendar.MINUTE) >= 10 ? 
                        calendario.get(Calendar.MINUTE) : "0" 
                     +  calendario.get(Calendar.MINUTE));
    }
    
    //Eventos
    private void btnOKActionPerformed(ActionEvent evt) {                                      
        listaMensajes.setModel(model);
        validacionMensaje();
        btnAgregar.requestFocus(true);
        listaMensajes.setSelectedIndex(listaMensajes.getModel().getSize() - 1);       
    }                                     

    private boolean existeElemento(String mensaje) {
        boolean existeEnLaLista = false;
        for (int i = 0; i < listaMensajes.getModel().getSize(); i++) {
            if (mensaje.equals(listaMensajes.getModel().getElementAt(i))) {
                existeEnLaLista = true;
            }
        }
        return existeEnLaLista;
    }

    private void btnAgregarActionPerformed(ActionEvent evt) {                                           
        txaMensaje.setText("");
        panelInicial.setVisible(false);
        panelAgregarMensaje.setVisible(true);
        txaMensaje.requestFocus(true);
    }                                          

    private void btnCerrarActionPerformed(ActionEvent evt) {                                          
        panelInicial.setVisible(true);
        panelAgregarMensaje.setVisible(false);
    }                                         

    private void btnMostrarActionPerformed(ActionEvent evt) {                                           
        if(climaPresionado) {
            try {
                arduino.sendData("4");
            } catch (ArduinoException | SerialPortException ex) {
                Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
            climaPresionado = false;
        }
        try {
            if (!listaMensajes.isSelectionEmpty()) {
                char primerLetra = listaMensajes.getSelectedValue().charAt(0);
                arduino.sendData(primerLetra + listaMensajes.getSelectedValue());
                arduino.sendData(" - "+model2.getElementAt(listaMensajes.getSelectedIndex()).toString());                
            } else {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún mensaje");
            }
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }                                          

    private void btnEliminarActionPerformed(ActionEvent evt) {                                            
        //listaMensajes.setModel(model);
        int index = listaMensajes.getSelectedIndex();

        if (!listaMensajes.isSelectionEmpty()) {
            model.removeElementAt(index);
            model2.removeElementAt(index);
        } else {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún mensaje");
        }
    }                                           

    private void itemGuardarComoActionPerformed(ActionEvent evt) {                                                
        if (listaMensajes.getModel().getSize() != 0) {
            try {
                String nombre = "";
                JFileChooser file = new JFileChooser();
                file.showSaveDialog(null);
                File guarda = file.getSelectedFile();

                if (guarda != null) {
                    FileWriter save = new FileWriter(guarda + ".txt");

                    for (int i = 0; i < listaMensajes.getModel().getSize(); i++) {
                        save.write(listaMensajes.getModel().getElementAt(i));
                        save.write("\r\n");
                        save.write(model2.getElementAt(i).toString());
                        save.write("\r\n");
                    }

                    save.close();
                    JOptionPane.showMessageDialog(null,
                            "El archivo se ha guardado Exitosamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Su archivo no se ha guardado",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Añada al menos un mensaje",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }                                               

    private void AbrirActionPerformed(ActionEvent evt) {                                      
        String aux = "";
        try {
            //llamamos el metodo que permite cargar la ventana
            JFileChooser file = new JFileChooser();
            file.showOpenDialog(null);
            //abrimos el archivo seleccionado
            File abre = file.getSelectedFile();

            //recorremos el archivo, lo leemos para plasmarlo
            //en el area de texto
            if (abre != null) {
                listaMensajes.setModel(model);
                model.removeAllElements();
                model2.removeAllElements();
                FileReader archivos = new FileReader(abre);
                BufferedReader lee = new BufferedReader(archivos);
                boolean nombreMensaje = true;
                while ((aux = lee.readLine()) != null) {
                    if(nombreMensaje) {
                        model.addElement(aux.trim());
                    } else {
                        model2.addElement(aux.trim());
                    }
                    nombreMensaje = !nombreMensaje;
                }
                
                lee.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex + ""
                    + "\nNo se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }
    }                                     

    private void itemNuevoActionPerformed(ActionEvent evt) {                                          
        model.removeAllElements();
        model2.removeAllElements();
    }                                         

    private void btnHumedadActionPerformed(ActionEvent evt) { 
        // Se envían 2 señales por si hay un retraso de comunicación
        try {
            arduino.sendData("2");
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            arduino.sendData("2");
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        climaPresionado = true;
    }                                          

    private void btnLuminosidadActionPerformed(ActionEvent evt) {
        // Se envían 2 señales por si hay un retraso de comunicación
        try {
            arduino.sendData("3");
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            arduino.sendData("3");
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        climaPresionado = true;
    }                                                                                  

    private void btnTemperaturaActionPerformed(ActionEvent evt) {
        // Se envían 2 señales por si hay un retraso de comunicación
        try {
            arduino.sendData("1");
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            arduino.sendData("1");
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        climaPresionado = true;
    }                                              

    private void txaMensajeKeyPressed(KeyEvent evt) {                                      
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            listaMensajes.setModel(model);
            validacionMensaje();
            btnAgregar.requestFocus(true);
            listaMensajes.setSelectedIndex(listaMensajes.getModel().getSize() - 1);       
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            panelInicial.setVisible(true);
            panelAgregarMensaje.setVisible(false);
            btnAgregar.requestFocus(true);
        }
    } 
    

    public static void main(String args[]) {
        //Ejecutar aplicación
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazGraficaDisplay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazGraficaDisplay().setVisible(true);
            }
        });
    }
}
