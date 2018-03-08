/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import ModeloDAO.ClienteDAO;
import ModeloDAO.RevisionDAO;
import ModeloDT.ClienteDT;
import ModeloDT.RevisionDT;
import ModeloDT.conexion;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import desktop.login;
import desktop.confirmarEliminar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
/**
 *
 * @author Angelo
 */
public class revision extends javax.swing.JFrame {

    /**
     * Creates new form revision
     */
    Statement instru;
    JTable tabla;
    private final String ruta = System.getProperties().getProperty("user.home");
    public int acceso;
    JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
    JMenuItem JMItem = new JMenuItem("Llenar A Formulario"); //Texto De Esa Ventan
    
    public revision() {
        try
        {
          initComponents();
          txtId.setEnabled(false);
          conexion c = new conexion();
          instru = c.getStatement();
          txtCorreo.setEnabled(false);
          txtDireccion.setEnabled(false);
          txtNombre.setEnabled(false);
          txtRut.setEnabled(false);
          setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
          actualizarJComboBoxCliente();
          actualizarJTable();
          popup.add(JMItem); //Agregar el MenuItem al Popup
          JMItem.addActionListener((new ActionListener() {
        public void actionPerformed(ActionEvent evt)
        {
          llenarFormulario();
        }
       }));
       
        tabla.setComponentPopupMenu(popup);
        acceso = login.accesoLogin; 
        ocultarId();
        cerrar();
        }catch(Exception ex)
        {
            
        }
    }
    
    public String obtenerRut(int rut)
    {
       int largoRut =  (rut+"").length();
        int[]digitos = new int[largoRut];
        int[]multiplicar = new int[largoRut];
        int numMultiplicar = 2;
        int ultimo = largoRut-1;
        int total = 0;
        int resto= 0;
        int comprobar = 0;
        String cadenaRut = rut+"";
        for(int i=0; i<largoRut; i++)
        {
            digitos[i] = Integer.parseInt(cadenaRut.charAt(ultimo)+"");
            ultimo--;
        }
        
        for(int i=0; i<largoRut; i++)
        {
            multiplicar[i] = digitos[i]*numMultiplicar;
            numMultiplicar++;
            if(numMultiplicar==8)
            {
                numMultiplicar = 2;
            }
        }
        
        for(int i=0; i<largoRut; i++)
        {
            total += multiplicar[i];
        }
        resto = total % 11;
        comprobar = 11 - resto;
        
        if(comprobar==11)
        {
            comprobar = 0;
        }else if(comprobar==10)
        {
            return cadenaRut+"-K";
        }
        return  cadenaRut+"-"+comprobar;

    }
    
    public void llenarFormulario()
    {
        try
        {
           //Para Obtener El Id de la fila de la Tabla seleccionada
           int i = tabla.getSelectedRow(); // La Fila Selecciona
           int idRevision = (int) tabla.getValueAt(i,0); // 
           RevisionDAO dao = new RevisionDAO();
           ArrayList<RevisionDT>lista = dao.obtenerRevision(idRevision);
           ddlIdCliente.setSelectedItem(lista.get(0).getIdCliente()+"");
           txtNumero.setText(lista.get(0).getNumero()+"");
           dcTermino.setDate(lista.get(0).getFechaTermino());
           txtId.setText(lista.get(0).getId()+"");
           JOptionPane.showMessageDialog(null,"Extraccion Realizado Correctamente");

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
        }
    }
    
        public void ocultarId()
    {
        tablaRevision.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaRevision.getColumnModel().getColumn(0).setMinWidth(0);
        tablaRevision.getColumnModel().getColumn(0).setPreferredWidth(0);
        tablaRevision.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaRevision.getColumnModel().getColumn(1).setMinWidth(0);
        tablaRevision.getColumnModel().getColumn(1).setPreferredWidth(0);
    }
    
    public void Limpiar()
    {
      pbBarraProgreso.setString("");
      txtNumero.setText("");
      ddlIdCliente.setSelectedIndex(0);
      dcTermino.setDate(null);
      txtId.setText("");
    }
    
     public void actualizarJTable() throws Exception
    {
        try
        {
            RevisionDAO dao = new RevisionDAO();
            ResultSet dato;
            DefaultTableModel dtm = new DefaultTableModel();
            tabla = this.tablaRevision;
            tabla.setModel(dtm);
            String termino = "";
            dtm.setColumnIdentifiers(new Object[]{"Id","Id Cliente","Nombre Cliente","Inicio","Termino","Numero"});
            dato = dao.imprimirRevision();
            while(dato.next())
            {
                if(dato.getString("termino")==null)
                {
                    termino = "";
                }else
                {
                    termino = dato.getString("termino");
                }
                dtm.addRow(new Object[]{
                                    dato.getInt("id"),
                                    dato.getInt("id_cliente"),
                                    dato.getString("cliente_nombre"),
                                    dato.getString("inicio"),
                                    termino,
                                    dato.getInt("numero")
                                       });
            }
            ocultarId();
        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
       //Metodo para cerrar jFrame
    public void cerrar()
    {
        try
        {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e)
                {
                    confirmarSalida();
                }
            });
            this.setVisible(true);
        }catch(Exception ex)
        {
          ex.printStackTrace();
        }
    }
    
    public void confirmarSalida()
    {
        int valor = JOptionPane.showConfirmDialog(this,"¿Esta Seguro De Cerrar La Aplicacion?","Adventencia",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(valor == JOptionPane.YES_OPTION)
        {
            JOptionPane.showMessageDialog(null,"Gracias Por su Visita","Gracias",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
     public void actualizarJComboBoxCliente() throws Exception
    {
         conexion c = new conexion();
        instru = c.getStatement();
        ResultSet dato = instru.executeQuery("SELECT id FROM vw_cliente ORDER BY rut");
        ddlIdCliente.removeAllItems();
        ddlIdCliente.addItem("-Seleccione-");
        while(dato.next())
        {
            ddlIdCliente.addItem(dato.getString("id"));
        }
    }   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ddlIdCliente = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dcTermino = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRevision = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtRut = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        pbBarraProgreso = new javax.swing.JProgressBar();
        jLabel5 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmtLogin = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jmtMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Revision");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Formulario Revision");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Cliente:");

        ddlIdCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddlIdClienteActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Numero");

        txtNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Termino:");

        tablaRevision.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaRevision);

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setText("Rut:");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel8.setText("Nombre:");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setText("Direccion:");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel10.setText("Correo:");

        pbBarraProgreso.setStringPainted(true);

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Id:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ddlIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel9))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(65, 65, 65)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtRut, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDireccion))))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtId))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dcTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pbBarraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ddlIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtRut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtNumero, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)))
                    .addComponent(dcTermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbBarraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save_16.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/del16.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Update.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gnome_edit_clear.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/16x16-excel.png"))); // NOI18N
        btnExcel.setText("Generar Excel");
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExcel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExcel)
                .addContainerGap(296, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMenu1.setText("Procesos");

        jmtLogin.setText("Cerrar Sesion");
        jmtLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtLoginActionPerformed(evt);
            }
        });
        jMenu1.add(jmtLogin);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mantenedores");

        jmtMenu.setText("Menu");
        jmtMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtMenuActionPerformed(evt);
            }
        });
        jMenu2.add(jmtMenu);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
     try
       {
        if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
           RevisionDAO dao = new RevisionDAO();
           RevisionDT dt = new RevisionDT();
           int idCliente;
           int res;
           if(ddlIdCliente.getSelectedItem().toString().equals("-Seleccione-"))
           {
              JOptionPane.showMessageDialog(null,"Seleccione Id Cliente");
              ddlIdCliente.requestFocus();
              throw new Exception();
           }else
           {
               idCliente = Integer.parseInt(ddlIdCliente.getSelectedItem().toString());
              dt.setIdCliente(idCliente);
           }
           if(dcTermino.getDate()==null)
           {
               
           }else
           {
             dt.setFechaTermino(new java.sql.Date(dcTermino.getDate().getTime()));
           }
           if(txtNumero.getText().length()==0 || txtNumero.getText().equals("0") || txtNumero.getText().equals(""))
           {
               JOptionPane.showMessageDialog(null,"Ingrese Numero");
               txtNumero.requestFocus();
               throw new Exception();
           }else
           {
                if(txtNumero.getText().length()>=10)
                {
                    JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Numero(9)");
                    txtNumero.requestFocus();
                    throw new Exception();
                }else
                {
                    dt.setNumero(Integer.parseInt(txtNumero.getText()));
                }
           }
           res = dao.guardarRevision(dt);
           if(res>0)
           {
               actualizarJTable();
               JOptionPane.showMessageDialog(null,"Revision Guardada");
               actualizarJTable();
               Limpiar();
               
           }else
           {
               JOptionPane.showMessageDialog(null,"No Se Pudo Guardar Revision");
           }
       }catch(Exception ex)
       {
         JOptionPane.showMessageDialog(null, ex.getMessage());
       }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jmtMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtMenuActionPerformed
        menu m = new menu();
        m.setVisible(true);
        revision.this.dispose();
    }//GEN-LAST:event_jmtMenuActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        try{
            Limpiar();
            JOptionPane.showMessageDialog(null,"Formulario Limpio");
            actualizarJTable();
        }catch(Exception ex)
        {
        
        }
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void jmtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtLoginActionPerformed
     cerrarSesion cs = new cerrarSesion(this,true,revision.this);
    cs.setVisible(true);
    }//GEN-LAST:event_jmtLoginActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
     try
     {
         if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
         
         RevisionDAO dao = new RevisionDAO();
         int id = 0;
         int res = 0;
         ResultSet totalRevisiones;
         int total = 0;
         
         if(txtId.getText().length()==0)
         {
             JOptionPane.showMessageDialog(null,"Busque Revision Antes de Eliminar");
             throw new Exception();
         }else
         {
             id = Integer.parseInt(txtId.getText());
             
             totalRevisiones = dao.validarEliminarRevision(id);
             
             while(totalRevisiones.next())
             {
                 total = totalRevisiones.getInt("IdRevision");
             }
             
             if(total>0)
             {
                 JOptionPane.showMessageDialog(null,"No Puede Eliminar Esta Revision Porque Esta Asociada a "+total+" Detalle Revisiones");
                 throw new Exception();
             }
             
             confirmarEliminar eliminar = new confirmarEliminar(this,true);
             eliminar.setVisible(true);
             if(confirmarEliminar.aceptar>0)
             {
         
               res = dao.eliminarRevision(id);
              if(res>0)
              {
                actualizarJTable();
                JOptionPane.showMessageDialog(null,"Revision Eliminada");
                Limpiar();
                confirmarEliminar.aceptar = 0;
                throw new Exception();
             
              }else
              {
                JOptionPane.showMessageDialog(null,"No Se Pudo Eliminar Revision");
                confirmarEliminar.aceptar = 0;
                throw new Exception();
              }
             }else
             {
                 JOptionPane.showMessageDialog(null,"Accion Cancelada");
             }
         }
         
     }catch(Exception ex)
     {
      
     }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
     try
     {
         if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
         RevisionDAO dao = new RevisionDAO();
         RevisionDT dt = new RevisionDT();
         int res = 0;
         int id = 0;
         if(txtId.getText().length()==0)
         {
             JOptionPane.showMessageDialog(null,"Busque Revision Antes De Actualizar");
             throw new Exception();
         }else
         {
             id = Integer.parseInt(txtId.getText());
             dt.setId(id);
         }
         if(ddlIdCliente.getSelectedIndex()>0)
         {
             JOptionPane.showMessageDialog(null,"No Se Puede Actualizar Cliente");
             ddlIdCliente.setSelectedIndex(0);
             ddlIdCliente.requestFocus();
             throw new Exception();
         }

         if(dcTermino.getDate()==null)
         {
             dt.setFechaTermino(null);
         }else
         {
             dt.setFechaTermino(new java.sql.Date(dcTermino.getDate().getTime()));
         }
         if(txtNumero.getText().length()==0 || txtNumero.getText().equals("0"))
         {
             JOptionPane.showMessageDialog(null,"Ingrese Numero");
             txtNumero.requestFocus();
             throw new Exception();
         }else
         {
            if(txtNumero.getText().length()>=10)
            {
                JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Numero(9");
            }else
            {
                dt.setNumero(Integer.parseInt(txtNumero.getText()));
            }
         }
         res = dao.actualizarRevision(dt);
         if(res>0)
         {
             JOptionPane.showMessageDialog(null,"Revision Actualizada");
             actualizarJTable();
             Limpiar();
             throw new Exception();
         }else
         {
             JOptionPane.showMessageDialog(null,"Problemas Al Actualizar Revision");
             throw new Exception();
         }
     }catch(Exception ex)
     {
        
     }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtNumeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroKeyTyped
        char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtNumeroKeyTyped

    private void ddlIdClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddlIdClienteActionPerformed
        try
        {
            ClienteDAO dao = new ClienteDAO();
            int id = 0;
            String rut = "";
        if(ddlIdCliente.getSelectedItem().equals("-Seleccione-"))
         {
             txtCorreo.setText("");
             txtDireccion.setText("");
             txtNombre.setText("");
             txtRut.setText("");
             
         }else{
            id = Integer.parseInt(ddlIdCliente.getSelectedItem().toString());
            ArrayList<ClienteDT> lista = dao.buscarCliente(id);
            for(int i=0; i<lista.size(); i++)
            {
                rut = obtenerRut(lista.get(i).getRut());
                txtRut.setText(rut);
                txtNombre.setText(lista.get(i).getNombre()+"");
                txtCorreo.setText(lista.get(i).getEmail()+"");
                txtDireccion.setText(lista.get(i).getDireccion());
            }
        }
        }catch(Exception ex)
        {
            
        }
    }//GEN-LAST:event_ddlIdClienteActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
            try
      {
          Thread t = new Thread()
          {
             public void run()
               {
                   XSSFWorkbook workbook = new XSSFWorkbook();
                   XSSFSheet hoja = workbook.createSheet();
                    
                    XSSFRow fila = hoja.createRow(0);
                    fila.createCell(0).setCellValue("Nombre Cliente");
                    fila.createCell(1).setCellValue("Inicio");
                    fila.createCell(2).setCellValue("Termino");
                    fila.createCell(3).setCellValue("Numero");
                    
                    pbBarraProgreso.setMaximum(tablaRevision.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;
                    
                    for(int i=0; i<tablaRevision.getRowCount(); i++)
                    {
                        rect = tablaRevision.getCellRect(i,0,true);
                        try
                        {
                            tablaRevision.scrollRectToVisible(rect);
                        }catch(java.lang.ClassCastException e)
                        {
                            
                        }
                        tablaRevision.setRowSelectionInterval(i, i);
                        pbBarraProgreso.setValue((i+1));
                        
                        filas = hoja.createRow((i+1));
                        filas.createCell(0).setCellValue(tablaRevision.getValueAt(i,2).toString());
                        filas.createCell(1).setCellValue(tablaRevision.getValueAt(i,3).toString());
                        filas.createCell(2).setCellValue(tablaRevision.getValueAt(i,4).toString());
                        filas.createCell(3).setCellValue(tablaRevision.getValueAt(i,5).toString());
                        
                        try
                        {
                            Thread.sleep(20);
                        }catch(InterruptedException ex)
                        {
                            Logger.getLogger(revision.class.getName()).log(Level.SEVERE,null,ex);
                        }
                    }
                    pbBarraProgreso.setValue(0);
                    pbBarraProgreso.setString("Abriendo Excel...");
                    
                    try
                    {
                        workbook.write(new FileOutputStream(new File(ruta+"//ExcelRevision.xlsx")));
                        Desktop.getDesktop().open(new File(ruta+"//ExcelRevision.xlsx"));
                        
                    }catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,"Cierre El Excel Para Actualizar!!");
                    }
                }
            };
          t.start();
          
      }catch(Exception ex)
      {
         
      }
    }//GEN-LAST:event_btnExcelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new revision().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private com.toedter.calendar.JDateChooser dcTermino;
    private javax.swing.JComboBox ddlIdCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem jmtLogin;
    private javax.swing.JMenuItem jmtMenu;
    private javax.swing.JProgressBar pbBarraProgreso;
    private javax.swing.JTable tablaRevision;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtRut;
    // End of variables declaration//GEN-END:variables
}
