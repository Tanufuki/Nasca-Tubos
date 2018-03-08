/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import ModeloDAO.DetalleRevisionDAO;
import ModeloDAO.FabricanteDAO;
import ModeloDAO.FotoDAO;
import ModeloDAO.NormaDAO;
import ModeloDAO.RevisionDAO;
import ModeloDAO.TipoCilindroDAO;
import ModeloDT.DetalleRevisionDT;
import ModeloDT.FabricanteDT;
import ModeloDT.FotoDT;
import ModeloDT.NormaDT;
import ModeloDT.TipoCilindroDT;
import ModeloDT.conexion;
import com.sun.glass.events.KeyEvent;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import desktop.menu;
import desktop.login;
import desktop.confirmarEliminar;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author Angelo
 */
public class formularioDetalleRevision extends javax.swing.JFrame {

    /**
     * Creates new form formularioDetalleRevision
     */
    Statement instru;
    public int idRevisionTabla;
    public int acceso = 0;
    private final String ruta = System.getProperties().getProperty("user.home");
    JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
    JMenuItem JMItem = new JMenuItem("Llenar A Formulario"); //Texto De Esa Ventan
    
    public formularioDetalleRevision() {
      try
      {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
        actualizarJComboBoxTipoCilindro();
        actualizarJComboBoxNorma();
        actualizarJComboBoxFabricante();
        actualizarJComboBoxDetalleRevision();
        actualizarJComboBoxFoto();
        actualizarJTable(menu.idRevision);
        actualizarJTableRevision(menu.idRevision);
        popup.add(JMItem); //Agregar el MenuItem al Popup
        JMItem.addActionListener((new ActionListener() {
        public void actionPerformed(ActionEvent evt)
        {
          llenarFormulario();
        }
       }));
        tablaDetalleRevision.setComponentPopupMenu(popup);
        cerrar();
        ocultarId();
        acceso = login.accesoLogin;
        txtRevision.setText(menu.idRevision+"");
        txtRevision.setEnabled(false);
        txtNormaT.setEnabled(false);
        txtCodigoT.setEnabled(false);
        txtNombreT.setEnabled(false);
        txtCodigoF.setEnabled(false);
        txtNombreF.setEnabled(false);
        txtRuta.setEnabled(false);
        txtIdDetalle.setEnabled(false);
        idRevisionTabla = menu.idRevision;
      }catch(Exception ex)
      {
          
      }
    }
    
    public void llenarFormulario()
    {
        try
        {
           //Para Obtener El Id de la fila de la Tabla seleccionada
           int i = tablaDetalleRevision.getSelectedRow(); // La Fila Selecciona
           int idDetalle = (int) tablaDetalleRevision.getValueAt(i,0); // 
           DetalleRevisionDAO dao = new DetalleRevisionDAO();
           
           ArrayList<DetalleRevisionDT>lista = dao.buscarDetalleRevision(idDetalle);
           
           
           ddlIdNorma.setSelectedItem(lista.get(0).getIdNorma()+"");
           ddlIdFabricante.setSelectedItem(lista.get(0).getIdFabricante()+"");
           ddlIdTipoCilindro.setSelectedItem(lista.get(0).getIdTipoCilindro()+"");
           dcUltimaPrueba.setDate(lista.get(0).getUltimaPrueba());
           dcFabricacion.setDate(lista.get(0).getFechaFabricacion());
           txtNumeroEstampado.setText(lista.get(0).getNumero()+"");
           
           if(lista.get(0).getCambioValvula()==0)
           {
               jcbCambioValvula.setSelected(false);
           }else
           {
               jcbCambioValvula.setSelected(true);
           }
           
           if(lista.get(0).getPintura()==0)
           {
               jcbPintura.setSelected(false);
           }else
           {
               jcbPintura.setSelected(true);
           }
           
           if(lista.get(0).getValvula()==0)
           {
               jcbValvula.setSelected(false);
           }else
           {
               jcbValvula.setSelected(true);
           }
           
           if(lista.get(0).getManilla()==0)
           {
               jcbManilla.setSelected(false);
           }else
           {
               jcbManilla.setSelected(true);
           }
           
           if(lista.get(0).getVolante()==0)
           {
               jcbVolante.setSelected(false);
           }else
           {
               jcbVolante.setSelected(true);
           }
           
           if(lista.get(0).getIvHiloCuello()==0)
           {
               jcbIvHiloCuello.setSelected(false);
           }else
           {
               jcbIvHiloCuello.setSelected(true);
           }
           
           if(lista.get(0).getIvInterior()==0)
           {
               jcboIvInterior.setSelected(false);
           }else
           {
               jcboIvInterior.setSelected(true);
           }
           
           if(lista.get(0).getIvExterior()==0)
           {
               jcbIvExterior.setSelected(false);
           }else
           {
               jcbIvExterior.setSelected(true);
           }
           
           if(lista.get(0).getAprobado()==0)
           {
               jcbAprobado.setSelected(false);
           }else
           {
               jcbAprobado.setSelected(true);
           }
           
           txtPresionDeServicio.setText(lista.get(0).getPresionServicio()+"");
           txtPresionPrueba.setText(lista.get(0).getPresionPrueba()+"");
           txtVolCargaIndicada.setText(lista.get(0).getVolCargaIndicada()+"");
           txtPresionPruebaEstampado.setText(lista.get(0).getPresionPruebaEstampado()+"");
           txtDeformTotal.setText(lista.get(0).getDeformTotal()+"");
           txtDeformPermanente.setText(lista.get(0).getDeforPermPorcentaje()+"");
           txtElasticidad.setText(lista.get(0).getElasticidad()+"");
           txtDeformPermPorcentaje.setText(lista.get(0).getDeforPermPorcentaje()+"");
           
           txtRuta.setText(lista.get(0).getRutaFoto());
           txtIdDetalle.setText(lista.get(0).getId()+"");
           JOptionPane.showMessageDialog(null,"Extraccion Realizado Correctamente");

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
        }
    }

    public void ocultarId()
    {
        tablaDetalleRevision.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaDetalleRevision.getColumnModel().getColumn(0).setMinWidth(0);
        tablaDetalleRevision.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public  void actualizarJTableRevision(int idRevisionTabla)
    {
        try
        {
        RevisionDAO dao = new RevisionDAO();
        ResultSet dato;
        DefaultTableModel dtm = new DefaultTableModel();
        JTable tabla = this.JtablaRevision;
        tabla.setModel(dtm);
        dtm.setColumnIdentifiers(new Object[] {"Nombre","Inicio","Termino","Numero","Total"});
        dato = dao.imprimirUnicaRevision(idRevisionTabla);
        while(dato.next())
        {
            dtm.addRow(new Object[]{dato.getString("cliente_nombre"),dato.getString("inicio"),dato.getString("termino"),dato.getInt("numero"),dato.getString("total")});
        }
        }catch(Exception ex)
        {
           
        }
    }
    
     public void actualizarJTable(int idRevision) throws Exception
    {
        try
        {
            
            DetalleRevisionDAO dao = new DetalleRevisionDAO();
            ResultSet dato;
            DefaultTableModel dtm = new DefaultTableModel();
            JTable tabla = this.tablaDetalleRevision;
            tabla.setModel(dtm);
                        //Codigo para activar el JScrollPanel Horinzontal
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tabla.doLayout();
            dtm.setColumnIdentifiers(new Object[]{"Id","Numero","Fecha Fabricacion",
                                                  "Nombre Fabricante","Norma","Fecha Ultima Prueba",
                                                  "Presion De Servicio","Presion De Prueba Estampado","Vol Carga Indicada","Presion De Prueba",
                                                  "Deform Total","Deform Permanente","Elasticidad","Deform Perm Porcentaje",
                                                  "Pintura","Ruta Foto","Cambio Valvula","Valvula","Manilla","Volante","IV Hilo Cuello",
                                                  "IV Exterior","IV Interior","Aprobado"});
            dato = dao.imprimirDetalleRevision(idRevision);
            while(dato.next())
            {
                dtm.addRow(new Object[]{
                                    dato.getInt("id"),
                                    dato.getInt("numero"),
                                    dato.getDate("fabricacion"),
                                    dato.getString("fabricante_nombre"),
                                    dato.getString("norma"),
                                    dato.getDate("ultimaPrueba"),
                                    dato.getInt("PresionDeServicio"),
                                    dato.getInt("PresionDePruebaEstampado"),
                                    dato.getDouble("VolCargaIndicada"),
                                    dato.getInt("PresionPrueba"),
                                    dato.getInt("DeformTotal"),
                                    dato.getInt("DeformPermanente"),
                                    dato.getInt("Elasticidad"),
                                    dato.getInt("DeformPermPorcentaje"),
                                    dato.getInt("pintura"),
                                    dato.getString("ruta_foto"),
                                    dato.getInt("cambio_valvula"),
                                    dato.getInt("valvula"),
                                    dato.getInt("manilla"),
                                    dato.getInt("volante"),
                                    dato.getInt("IVhiloCuello"),
                                    dato.getInt("IVExterior"),
                                    dato.getInt("IVInterior"),
                                    dato.getInt("Aprobado")
                                       });
            }
            ocultarId();           
        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
    
     public void actualizarJComboBoxDetalleRevision() throws Exception
    {
         conexion c = new conexion();
        instru = c.getStatement();
        ResultSet dato = instru.executeQuery("SELECT id FROM vw_detalle_revision "+
                                             "WHERE id_revision LIKE "+menu.idRevision+"");
        ddlBuscarIdDetalle.removeAllItems();
        ddlBuscarIdDetalle.addItem("-Seleccione-");
        while(dato.next())
        {
            ddlBuscarIdDetalle.addItem(dato.getString("id"));
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
    
      public void actualizarJComboBoxTipoCilindro() throws Exception
    {
         conexion c = new conexion();
        instru = c.getStatement();
        ResultSet dato = instru.executeQuery("SELECT id FROM vw_tipo_cilindro ORDER BY codigo");
        ddlIdTipoCilindro.removeAllItems();
        ddlIdTipoCilindro.addItem("-Seleccione-");
        while(dato.next())
        {
            ddlIdTipoCilindro.addItem(dato.getString("id"));
        }
    }
      
        public void actualizarJComboBoxNorma() throws Exception
    {
         conexion c = new conexion();
        instru = c.getStatement();
        ResultSet dato = instru.executeQuery("SELECT id FROM vw_norma ORDER BY norma");
        ddlIdNorma.removeAllItems();
        ddlIdNorma.addItem("-Seleccione-");
        while(dato.next())
        {
            ddlIdNorma.addItem(dato.getString("id"));
        }
    }
        
         public void actualizarJComboBoxFabricante() throws Exception
    {
         conexion c = new conexion();
        instru = c.getStatement();
        ResultSet dato = instru.executeQuery("SELECT id FROM vw_fabricante ORDER BY codigo");
        ddlIdFabricante.removeAllItems();
        ddlIdFabricante.addItem("-Seleccione-");
        while(dato.next())
        {
            ddlIdFabricante.addItem(dato.getString("id"));
        }
    }
         
       public void actualizarJComboBoxFoto() throws Exception
    {
         conexion c = new conexion();
        instru = c.getStatement();
        ResultSet dato = instru.executeQuery("SELECT id FROM vw_foto ORDER BY ruta");
        ddlIdFoto.removeAllItems();
        ddlIdFoto.addItem("-Seleccione-");
        while(dato.next())
        {
            ddlIdFoto.addItem(dato.getString("id"));
        }
    }   
         
    public void limpiar()
    {
            txtDeformPermPorcentaje.setText("");
          txtDeformPermanente.setText("");
          txtDeformTotal.setText("");
          txtElasticidad.setText("");
          dcFabricacion.setDate(null);
          txtNumeroEstampado.setText("");
          txtPresionDeServicio.setText("");
          txtPresionPrueba.setText("");
          txtPresionPruebaEstampado.setText("");
          dcUltimaPrueba.setDate(null);
          txtVolCargaIndicada.setText("");
          txtIdDetalle.setText("");
          ddlBuscarIdDetalle.setSelectedIndex(0);
          ddlIdFabricante.setSelectedIndex(0);
          ddlIdNorma.setSelectedIndex(0);
          ddlIdTipoCilindro.setSelectedIndex(0);
          ddlIdFoto.setSelectedIndex(0);
          
          jcbAprobado.setSelected(false);
          jcbCambioValvula.setSelected(false);
          jcbIvExterior.setSelected(false);
          jcbIvHiloCuello.setSelected(false);
          jcbManilla.setSelected(false);
          jcbPintura.setSelected(false);
          jcbValvula.setSelected(false);
          jcbVolante.setSelected(false);
          jcboIvInterior.setSelected(false);
          
          pbBarraProgreso.setString("");
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPresionDeServicio = new javax.swing.JTextField();
        txtPresionPrueba = new javax.swing.JTextField();
        txtVolCargaIndicada = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jcbVolante = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        jcbCambioValvula = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jcbPintura = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jcbValvula = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jcbManilla = new javax.swing.JCheckBox();
        lblFoto = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtRuta = new javax.swing.JTextField();
        ddlIdFoto = new javax.swing.JComboBox();
        btnFoto = new javax.swing.JButton();
        JPanel8 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jcbAprobado = new javax.swing.JCheckBox();
        txtDeformPermPorcentaje = new javax.swing.JTextField();
        txtElasticidad = new javax.swing.JTextField();
        txtDeformPermanente = new javax.swing.JTextField();
        txtDeformTotal = new javax.swing.JTextField();
        txtPresionPruebaEstampado = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ddlIdTipoCilindro = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        ddlIdNorma = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ddlIdFabricante = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNumeroEstampado = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dcUltimaPrueba = new com.toedter.calendar.JDateChooser();
        dcFabricacion = new com.toedter.calendar.JDateChooser();
        txtRevision = new javax.swing.JTextField();
        txtNormaT = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtCodigoT = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtNombreT = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtCodigoF = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtNombreF = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtIdDetalle = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        ddlBuscarIdDetalle = new javax.swing.JComboBox();
        btnBuscar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jcbIvHiloCuello = new javax.swing.JCheckBox();
        jcbIvExterior = new javax.swing.JCheckBox();
        jcboIvInterior = new javax.swing.JCheckBox();
        JScrollPane = new javax.swing.JScrollPane();
        tablaDetalleRevision = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        JtablaRevision = new javax.swing.JTable();
        jLabel38 = new javax.swing.JLabel();
        pbBarraProgreso = new javax.swing.JProgressBar();
        jPanel7 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmLogin = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mtMenu = new javax.swing.JMenuItem();
        mtRevison = new javax.swing.JMenuItem();
        jmCliente = new javax.swing.JMenuItem();
        jmFabricante = new javax.swing.JMenuItem();
        jmNorma = new javax.swing.JMenuItem();
        jmTipoCilindro = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalle Revision");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setText("Formulario Detalle Revision");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel28.setText("Datos Del Cilindro");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Presion Prueba");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Vol Carga Indicada:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Presion De Servicio:");

        txtPresionDeServicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPresionDeServicioKeyTyped(evt);
            }
        });

        txtPresionPrueba.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPresionPruebaKeyTyped(evt);
            }
        });

        txtVolCargaIndicada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVolCargaIndicadaKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setText("Volante:");

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setText("Cambio Valvula:");

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setText("Pintura:");

        jLabel20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel20.setText("Valvula:");

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setText("Manilla:");

        jLabel33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel33.setText("Foto Directorio:");

        ddlIdFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddlIdFotoActionPerformed(evt);
            }
        });

        btnFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/formulario.png"))); // NOI18N
        btnFoto.setText("Abrir Formulario Foto");
        btnFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel17)
                                    .addGap(18, 18, 18)
                                    .addComponent(jcbPintura))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel19)
                                    .addGap(18, 18, 18)
                                    .addComponent(jcbCambioValvula))))
                        .addGap(70, 70, 70)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtPresionDeServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtVolCargaIndicada, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(txtPresionPrueba, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbValvula))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jcbManilla)
                                    .addComponent(jcbVolante)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddlIdFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFoto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jcbValvula)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcbCambioValvula)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19)
                                        .addComponent(jLabel20)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jcbPintura)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21)
                                .addComponent(txtPresionPrueba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPresionDeServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jcbManilla)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbVolante))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtVolCargaIndicada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ddlIdFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFoto))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        JPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel29.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel29.setText("Prueba Hidrostatica:");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setText("Presion De Prueba Estampado:");

        jLabel13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel13.setText("Deform Total:");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel14.setText("Deform Permanente:");

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setText("Elasticidad:");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel16.setText("Deform Perm Porcentaje:");

        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setText("Aprobado:");

        txtDeformPermPorcentaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDeformPermPorcentajeKeyTyped(evt);
            }
        });

        txtElasticidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtElasticidadKeyTyped(evt);
            }
        });

        txtDeformPermanente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDeformPermanenteKeyTyped(evt);
            }
        });

        txtDeformTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDeformTotalKeyTyped(evt);
            }
        });

        txtPresionPruebaEstampado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPresionPruebaEstampadoActionPerformed(evt);
            }
        });
        txtPresionPruebaEstampado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPresionPruebaEstampadoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout JPanel8Layout = new javax.swing.GroupLayout(JPanel8);
        JPanel8.setLayout(JPanel8Layout);
        JPanel8Layout.setHorizontalGroup(
            JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addGroup(JPanel8Layout.createSequentialGroup()
                        .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(4, 4, 4)
                        .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDeformPermanente, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(txtDeformTotal)
                            .addComponent(txtPresionPruebaEstampado))
                        .addGap(57, 57, 57)
                        .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbAprobado)
                            .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtDeformPermPorcentaje, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                .addComponent(txtElasticidad)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanel8Layout.setVerticalGroup(
            JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel15)
                    .addComponent(txtElasticidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPresionPruebaEstampado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel16)
                    .addComponent(txtDeformPermPorcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDeformTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbAprobado, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(JPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel26)
                        .addComponent(txtDeformPermanente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Revision:");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Tipo Cilindro:");

        ddlIdTipoCilindro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddlIdTipoCilindroActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setText(" Norma:");

        ddlIdNorma.setForeground(new java.awt.Color(255, 255, 255));
        ddlIdNorma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddlIdNormaActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel32.setText("Datos Requeridos:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText(" Fabricante:");

        ddlIdFabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddlIdFabricanteActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Ultima Prueba:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Numero:");

        txtNumeroEstampado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroEstampadoKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Fabricacion:");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel18.setText("Codigo:");

        jLabel35.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel35.setText("Nombre:");

        jLabel36.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel36.setText("Codigo:");

        jLabel37.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel37.setText("Nombre:");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setText("Detalle");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtRevision, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIdDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(ddlIdNorma, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNormaT, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ddlIdTipoCilindro, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel18))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(ddlIdFabricante, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel36))
                                    .addComponent(txtNumeroEstampado, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dcUltimaPrueba, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dcFabricacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCodigoT)
                            .addComponent(txtCodigoF, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreT, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreF))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel32)
                .addGap(22, 22, 22)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtRevision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)
                        .addComponent(txtIdDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ddlIdNorma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNormaT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ddlIdTipoCilindro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18)
                        .addComponent(txtCodigoT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35)
                        .addComponent(txtNombreT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(ddlIdFabricante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCodigoF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37)
                        .addComponent(txtNombreF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(dcUltimaPrueba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(dcFabricacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNumeroEstampado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));

        jLabel30.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel30.setText("Buscar Detalle Revision");

        jLabel31.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel31.setText("Id:");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddlBuscarIdDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(ddlBuscarIdDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel27.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel27.setText("Inspeccion Visual Cilindro:");

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setText("IV Hilo Cuello:");

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setText("Iv Exterior:");

        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setText("IV Interior:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addComponent(jcbIvHiloCuello)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(jcboIvInterior)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(jcbIvExterior)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jcbIvHiloCuello)
                    .addComponent(jLabel25)
                    .addComponent(jcboIvInterior, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24)
                    .addComponent(jcbIvExterior))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaDetalleRevision.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        JScrollPane.setViewportView(tablaDetalleRevision);

        JtablaRevision.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(JtablaRevision);

        jLabel38.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel38.setText("Revision:");

        pbBarraProgreso.setStringPainted(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JScrollPane)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pbBarraProgreso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel38)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbBarraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jScrollPane1.setViewportView(jPanel2);

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExcel)
                .addContainerGap(1205, Short.MAX_VALUE))
        );

        jMenu1.setText("Procesos");

        jmLogin.setText("Cerrar Sesion");
        jmLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmLoginActionPerformed(evt);
            }
        });
        jMenu1.add(jmLogin);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mantenedores");

        mtMenu.setText("Menu");
        mtMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtMenuActionPerformed(evt);
            }
        });
        jMenu2.add(mtMenu);

        mtRevison.setText("Revison");
        mtRevison.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtRevisonActionPerformed(evt);
            }
        });
        jMenu2.add(mtRevison);

        jmCliente.setText("Cliente");
        jmCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmClienteActionPerformed(evt);
            }
        });
        jMenu2.add(jmCliente);

        jmFabricante.setText("Fabricante");
        jmFabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmFabricanteActionPerformed(evt);
            }
        });
        jMenu2.add(jmFabricante);

        jmNorma.setText("Norma");
        jmNorma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmNormaActionPerformed(evt);
            }
        });
        jMenu2.add(jmNorma);

        jmTipoCilindro.setText("Tipo Cilindro");
        jmTipoCilindro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmTipoCilindroActionPerformed(evt);
            }
        });
        jMenu2.add(jmTipoCilindro);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmClienteActionPerformed
      cliente c = new cliente();
      c.setVisible(true);
      formularioDetalleRevision.this.dispose();
    }//GEN-LAST:event_jmClienteActionPerformed

    private void jmNormaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmNormaActionPerformed
       norma n = new norma();
       n.setVisible(true);
       formularioDetalleRevision.this.dispose();
    }//GEN-LAST:event_jmNormaActionPerformed

    private void jmLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmLoginActionPerformed
     cerrarSesion cs = new cerrarSesion(this,true,formularioDetalleRevision.this);
    cs.setVisible(true);
    }//GEN-LAST:event_jmLoginActionPerformed

    private void jmTipoCilindroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmTipoCilindroActionPerformed
      tipoCilindro tc = new tipoCilindro();
      tc.setVisible(true);
      formularioDetalleRevision.this.dispose();
    }//GEN-LAST:event_jmTipoCilindroActionPerformed

    private void jmFabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmFabricanteActionPerformed
     fabricante f = new fabricante();
     f.setVisible(true);
     formularioDetalleRevision.this.dispose();
    }//GEN-LAST:event_jmFabricanteActionPerformed

    private void mtRevisonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtRevisonActionPerformed
      revision r = new revision();
      r.setVisible(true);
      formularioDetalleRevision.this.dispose();
    }//GEN-LAST:event_mtRevisonActionPerformed

    private void mtMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtMenuActionPerformed
       menu m = new menu();
      m.setVisible(true);
      formularioDetalleRevision.this.dispose();
    }//GEN-LAST:event_mtMenuActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
      try
      {
          //Variables
          DetalleRevisionDAO dao = new DetalleRevisionDAO();
          DetalleRevisionDT dt = new DetalleRevisionDT();
          int idRevision , idNorma , idTipoCilindro ,   
              idFabricante , numeroEstampado , presionPrueba,
                  presionServicio , presionPruebaEstampado,
                  deformTotal , deformPermanente , elasticidad,
                  deformPermPorcentaje , idFoto;
          double volCargaIndicada;
          int res = 0;
          if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
          //Validacion Datos Requeridos
          idRevision = Integer.parseInt(txtRevision.getText()) ;
          dt.setIdRevision(idRevision);
          
          if(ddlIdNorma.getSelectedItem().toString().equals("-Seleccione-"))
          {
              JOptionPane.showMessageDialog(null,"Seleccione Una Norma");
              ddlIdNorma.requestFocus();
              throw new Exception();
          }else
          {
              idNorma = Integer.parseInt(ddlIdNorma.getSelectedItem().toString());
              dt.setIdNorma(idNorma);
          }
          
          if(ddlIdTipoCilindro.getSelectedItem().toString().equals("-Seleccione-"))
          {
              JOptionPane.showMessageDialog(null,"Seleccione Tipo Cilindro");
              ddlIdTipoCilindro.requestFocus();
              throw new Exception();
          }else
          {
              idTipoCilindro = Integer.parseInt(ddlIdTipoCilindro.getSelectedItem().toString());
              dt.setIdTipoCilindro(idTipoCilindro);
          }
          
          if(ddlIdFabricante.getSelectedItem().toString().equals("-Seleccione-"))
          {
              JOptionPane.showMessageDialog(null,"Seleccione Un Fabricante");
              ddlIdFabricante.requestFocus();
              throw new Exception();
          }else
          {
              idFabricante = Integer.parseInt(ddlIdFabricante.getSelectedItem().toString());
              dt.setIdFabricante(idFabricante);
          }

          if(txtNumeroEstampado.getText().length()==0|| txtNumeroEstampado.getText().equals(" "))
          {
              JOptionPane.showMessageDialog(null,"Ingrese Numero Estampado");
              txtNumeroEstampado.requestFocus();
              throw new Exception();
          }else
          {
              if(txtNumeroEstampado.getText().length()>=10)
              {
                  JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Numero Estampado(9)");
                  txtNumeroEstampado.requestFocus();
                  throw new Exception();
              }else
              {
                    numeroEstampado = Integer.parseInt(txtNumeroEstampado.getText());
                    dt.setNumero(numeroEstampado);
              }
          }
          
          //Validar Fecha
          if(dcUltimaPrueba.getDate()== null)
          {
              JOptionPane.showMessageDialog(null,"Ingrese Fecha Ultima Prueba");
              dcUltimaPrueba.requestFocus();
              throw new Exception();
          }else
          {
              dt.setUltimaPrueba(new java.sql.Date(dcFabricacion.getDate().getTime()));
          }
          
          if(dcFabricacion.getDate()==null)
          {
              JOptionPane.showMessageDialog(null,"Ingrese Fecha Fabricacion");
              dcFabricacion.requestFocus();
              throw new Exception();
          }else
          {
             dt.setFechaFabricacion(new java.sql.Date(dcUltimaPrueba.getDate().getTime()));
          }
          //Validacion De ChechkBox
          if(jcbCambioValvula.isSelected())
          {
              dt.setCambioValvula(1);
          }
          
          if(jcbPintura.isSelected())
          {
              dt.setPintura(1);
          }
          
          if(jcbValvula.isSelected())
          {
              dt.setValvula(1);
          }
          if(jcbManilla.isSelected())
          {
              dt.setManilla(1);
          }
          
          if(jcbVolante.isSelected())
          {
              dt.setVolante(1);
          }
          if(jcbIvHiloCuello.isSelected())
          {
              dt.setIvHiloCuello(1);
          }
          if(jcboIvInterior.isSelected())
          {
              dt.setIvInterior(1);
          }
          if(jcbIvExterior.isSelected())
          {
              dt.setIvExterior(1);
          }
          if(jcbAprobado.isSelected())
          {
              dt.setAprobado(1);
          }
          
          /*Validacion De Enteros 
            si el usuario no ingresa un valor mayor a 0 == .length()>0
            se usara el valor defecto al crear la clase dt en las variables 0
          */
          if(txtPresionPrueba.getText().length()>0)
          {
              if(txtPresionPrueba.getText().length()>=10)
              {
                  JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Presion Prueba(9)");
                  txtPresionPrueba.requestFocus();
                  throw new Exception();
              }else
              {
                presionPrueba = Integer.parseInt(txtPresionPrueba.getText());
                dt.setPresionPrueba(presionPrueba);
              }
          }
          
          if(txtPresionDeServicio.getText().length()>0)
          {
            presionServicio = Integer.parseInt(txtPresionDeServicio.getText());
            dt.setPresionServicio(presionServicio);
          }
          
            if(txtVolCargaIndicada.getText().length()>0)
            {
               if(txtVolCargaIndicada.getText().equals("."))
               {
                   JOptionPane.showMessageDialog(null,"Ingrese Vol Carga Indicada Valido");
                   txtVolCargaIndicada.requestFocus();
                   txtVolCargaIndicada.setText("0");
                   throw new Exception();
               }else
               {
                 volCargaIndicada = Double.parseDouble(txtVolCargaIndicada.getText());
                 dt.setVolCargaIndicada(volCargaIndicada);
               }
            }
          
          if(txtPresionPruebaEstampado.getText().length()>0)
          {
            presionPruebaEstampado = Integer.parseInt(txtPresionPruebaEstampado.getText()); 
            dt.setPresionPruebaEstampado(presionPruebaEstampado);
          }
          
          if(txtDeformTotal.getText().length()>0)
          {
            deformTotal = Integer.parseInt(txtDeformTotal.getText()); 
            dt.setDeformTotal(deformTotal);
          }
          
          if(txtDeformPermanente.getText().length()>0)
          {
            deformPermanente = Integer.parseInt(txtDeformPermanente.getText());
            dt.setDeformPermanente(deformPermanente);
          }
          
          if(txtElasticidad.getText().length()>0)
          {
            elasticidad = Integer.parseInt(txtElasticidad.getText());
            dt.setElasticidad(elasticidad);
          }
          
          if(txtDeformPermPorcentaje.getText().length()>0)
          {
            deformPermPorcentaje = Integer.parseInt(txtDeformPermPorcentaje.getText());
            dt.setDeforPermPorcentaje(deformPermPorcentaje);
          }
          
          if(ddlIdFoto.getSelectedIndex()==0)
          {
              dt.setIdFoto(0);
          }else
          {
            idFoto = Integer.parseInt(ddlIdFoto.getSelectedItem().toString());
            dt.setIdFoto(idFoto);
          }
          
          res = dao.guardarDetalleRevision(dt);
          if(res>0)
          {
              actualizarJComboBoxDetalleRevision();
              actualizarJTable(menu.idRevision);
              actualizarJTableRevision(menu.idRevision);
              JOptionPane.showMessageDialog(null,"Detalle Guardado");
              limpiar();
              throw new Exception();
             
          }else
          {
              JOptionPane.showMessageDialog(null,"No Se Pudo Guardar Detalle Revision");
              throw new Exception();
          }
      }catch(Exception ex)
      {
          
      }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
    try
    {
        int id , idRevision=0 , numero =0 , idTipoCilindro = 0,idFabricante = 0 ,idNorma=0,
                presionDeServicio = 0,presionDePruebaEstampado = 0,presionPrueba =0,deformTotal=0,
                deformPermanente=0,elasticidad = 0,deformPermPorcentaje = 0,pintura = 0 ,
                cambioValvula = 0,valvula = 0,manilla = 0,volante =0,ivHiloCuello = 0
                ,ivExterior =0,ivInterior =0 ,aprobado =0 , idFoto = 0;
        Date fabricacion = null ,ultimaPrueba = null ;
        String rutaFoto = "";
        double volCargaIndicada =0.0;
        DetalleRevisionDAO dao = new DetalleRevisionDAO();
        ArrayList<DetalleRevisionDT>lista = new ArrayList<DetalleRevisionDT>();
        if(ddlBuscarIdDetalle.getSelectedItem().equals("-Seleccione-"))
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Id");
            ddlBuscarIdDetalle.requestFocus();
            throw new Exception();
        }else
        {
            id = Integer.parseInt(ddlBuscarIdDetalle.getSelectedItem().toString());
        }
        lista = dao.buscarDetalleRevision(id);
        
        for(int i=0; i<lista.size(); i++)
        {
            idRevision = lista.get(i).getIdRevision();
            numero = lista.get(i).getNumero();
            idTipoCilindro = lista.get(i).getIdTipoCilindro();
            idFabricante = lista.get(i).getIdFabricante();
            idNorma = lista.get(i).getIdNorma();
            presionDeServicio = lista.get(i).getPresionServicio();
            presionDePruebaEstampado = lista.get(i).getPresionPruebaEstampado();
            presionPrueba =lista.get(i).getPresionPrueba();
            deformTotal =lista.get(i).getDeformTotal();
            deformPermanente = lista.get(i).getDeformPermanente();
            elasticidad = lista.get(i).getElasticidad();
            deformPermPorcentaje = lista.get(i).getDeforPermPorcentaje();
            pintura = lista.get(i).getPintura();
            cambioValvula = lista.get(i).getCambioValvula();
            valvula = lista.get(i).getValvula();
            manilla = lista.get(i).getManilla();
            volante = lista.get(i).getVolante();
            ivHiloCuello = lista.get(i).getIvHiloCuello();
            ivExterior = lista.get(i).getIvExterior();
            ivInterior = lista.get(i).getIvInterior();
            aprobado = lista.get(i).getAprobado();
            fabricacion = lista.get(i).getFechaFabricacion();
            ultimaPrueba = lista.get(i).getUltimaPrueba();
            volCargaIndicada = lista.get(i).getVolCargaIndicada();
            idFoto = lista.get(i).getIdFoto();
            rutaFoto = lista.get(i).getRutaFoto();
        }
        
        txtNumeroEstampado.setText(numero+"");
        ddlIdTipoCilindro.setSelectedItem(idTipoCilindro+"");
        ddlIdFabricante.setSelectedItem(idFabricante+"");
        ddlIdNorma.setSelectedItem(idNorma+"");
        txtPresionDeServicio.setText(presionDeServicio+"");
        txtPresionPruebaEstampado.setText(presionDePruebaEstampado+"");
        txtPresionPrueba.setText(presionPrueba+"");
        txtDeformTotal.setText(deformTotal+"");
        txtDeformPermanente.setText(deformPermanente+"");
        txtElasticidad.setText(elasticidad+"");
        txtDeformPermPorcentaje.setText(deformPermPorcentaje+"");
        
        dcFabricacion.setDate(fabricacion);
        dcUltimaPrueba.setDate(ultimaPrueba);
        txtVolCargaIndicada.setText(volCargaIndicada+"");
        ddlIdFoto.setSelectedItem(idFoto+"");
        txtRuta.setText(rutaFoto);
        if(pintura == 1)
        {
         jcbPintura.setSelected(true); 
        }else
        {
            jcbPintura.setSelected(false);
        }
        if(cambioValvula == 1)
        {
            jcbCambioValvula.setSelected(true);
        }else
        {
            jcbCambioValvula.setSelected(false);
        }
        if(valvula == 1)
        {
            jcbValvula.setSelected(true);
        }else
        {
            jcbValvula.setSelected(false);
        }
        if(manilla == 1)
        {
            jcbManilla.setSelected(true);
        }else
        {
            jcbManilla.setSelected(false);
        }
        if(volante == 1)
        {
            jcbVolante.setSelected(true);
        }else
        {
            jcbVolante.setSelected(false);
        }
        if(ivHiloCuello ==1)
        {
            jcbIvHiloCuello.setSelected(true);
        }else
        {
            jcbIvHiloCuello.setSelected(false);
        }
        if(ivExterior==1)
        {
            jcbIvExterior.setSelected(true);
        }else
        {
            jcbIvExterior.setSelected(false);
        }
        if(ivInterior==1)
        {
            jcboIvInterior.setSelected(true);
        }else
        {
            jcboIvInterior.setSelected(false);
        }
        if(aprobado==1)
        {
            jcbAprobado.setSelected(true);
        }else
        {
            jcbAprobado.setSelected(false);
        }
        JOptionPane.showMessageDialog(null,"Detalle Encontrado");
        throw new Exception();
        
    }catch(Exception ex)
    {
      
    }
     }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
     try
     {
        limpiar();
         actualizarJTable(idRevisionTabla);
         actualizarJTableRevision(idRevisionTabla);
     JOptionPane.showMessageDialog(null,"Formulario Limpio");
     }catch(Exception ex)
     {
         
     }
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        try
        {
            DetalleRevisionDAO dao = new DetalleRevisionDAO();
            int id = 0;
            int res = 0;
            if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
            if(ddlBuscarIdDetalle.getSelectedItem().equals("-Seleccione-"))
            {
                JOptionPane.showMessageDialog(null,"Seleccione Una Id");
                ddlBuscarIdDetalle.requestFocus();
                throw new Exception();
            }else
            {
                confirmarEliminar eliminar = new confirmarEliminar(this,true);
                eliminar.setVisible(true);
              if(confirmarEliminar.aceptar >0)
              {
                id = Integer.parseInt(ddlBuscarIdDetalle.getSelectedItem().toString());
                res = dao.eliminarDetalleRevision(id);
                if(res>0)
                {
                    actualizarJComboBoxDetalleRevision();
                    actualizarJTable(menu.idRevision);
                    actualizarJTableRevision(menu.idRevision);
                    JOptionPane.showMessageDialog(null,"Detalle Eliminado");
                    confirmarEliminar.aceptar = 0;
                    throw new Exception();
                }else
                {
                    JOptionPane.showMessageDialog(null,"Problemas Al Eliminar Detalle");
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
            DetalleRevisionDAO dao = new DetalleRevisionDAO();
            DetalleRevisionDT dt = new DetalleRevisionDT();
            int   numeroEstampado , presionPrueba,
              presionServicio , presionPruebaEstampado,
              deformTotal , deformPermanente , elasticidad,
              deformPermPorcentaje;
            Date ultimaPrueba , fechaFabricacion;
            double volCargaIndicada;
            int res = 0;
            int id = 0;
            if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
            if(ddlBuscarIdDetalle.getSelectedItem().toString().equals("-Seleccione-"))
            {
                JOptionPane.showMessageDialog(null,"Seleccione Una Id");
                ddlBuscarIdDetalle.requestFocus();
                throw new Exception();
            }else
            {
                id = Integer.parseInt(ddlBuscarIdDetalle.getSelectedItem().toString());
                dt.setId(id);
                    //Validacion Datos Requeridos
                
          if(ddlIdNorma.getSelectedIndex()>0)
          {
              JOptionPane.showMessageDialog(null,"No Se Puede Actualizar Norma");
              ddlIdNorma.setSelectedIndex(0);
              throw new Exception();
          }
          if(ddlIdTipoCilindro.getSelectedIndex()>0)
          {
              JOptionPane.showMessageDialog(null,"No Se Puede Actualizar Tipo Cilindro");
              ddlIdTipoCilindro.setSelectedIndex(0);
              throw new Exception();
          }
          
          if(ddlIdFabricante.getSelectedIndex()>0)
          {
              JOptionPane.showMessageDialog(null,"No Se Puede Actualizar Fabricante");
              ddlIdFabricante.setSelectedIndex(0);
              throw new Exception();
          }
          if(dcUltimaPrueba.getDate()==null)
          {
              JOptionPane.showMessageDialog(null,"Ingrese Fecha Ultima Prueba Para Actualizar");
              dcUltimaPrueba.requestFocus();
              throw new Exception();
          }else
          {
              ultimaPrueba = new java.sql.Date(dcUltimaPrueba.getDate().getTime());
              dt.setUltimaPrueba(ultimaPrueba);
          }
          if(txtNumeroEstampado.getText().length()==0|| txtNumeroEstampado.getText().equals(" "))
          {
              JOptionPane.showMessageDialog(null,"Ingrese Numero Estampado Para Actualizar");
              txtNumeroEstampado.requestFocus();
              throw new Exception();
          }else
          {
              numeroEstampado = Integer.parseInt(txtNumeroEstampado.getText());
              dt.setNumero(numeroEstampado);
          }
          if(dcFabricacion.getDate()==null)
          {
              JOptionPane.showMessageDialog(null,"Ingrese Fecha Fabricacion Para Actualizar");
              dcFabricacion.requestFocus();
              throw new Exception();
          }else
          {
              fechaFabricacion = new java.sql.Date(dcFabricacion.getDate().getTime());
              dt.setFechaFabricacion(fechaFabricacion);
          }
          
           //Validacion De ChechkBox
          if(jcbCambioValvula.isSelected())
          {
              dt.setCambioValvula(1);
          }
          
          if(jcbPintura.isSelected())
          {
              dt.setPintura(1);
          }
          
          if(jcbValvula.isSelected())
          {
              dt.setValvula(1);
          }
          if(jcbManilla.isSelected())
          {
              dt.setManilla(1);
          }
          
          if(jcbVolante.isSelected())
          {
              dt.setVolante(1);
          }
          if(jcbIvHiloCuello.isSelected())
          {
              dt.setIvHiloCuello(1);
          }
          if(jcboIvInterior.isSelected())
          {
              dt.setIvInterior(1);
          }
          if(jcbIvExterior.isSelected())
          {
              dt.setIvExterior(1);
          }
          if(jcbAprobado.isSelected())
          {
              dt.setAprobado(1);
          }
          
          //Validar campos numericos , si al actualizar vacia el campo de texto
          //Se le asignara 0
          if(txtPresionPrueba.getText().length()==0)
          {
              presionPrueba = 0;
          }else
          {
              presionPrueba = Integer.parseInt(txtPresionPrueba.getText());
          }
          
          if(txtPresionDeServicio.getText().length()==0)
          {
              presionServicio = 0;
          }else
          {
              presionServicio = Integer.parseInt(txtPresionDeServicio.getText());
          }
          
          if(txtVolCargaIndicada.getText().length()==0)
          {
              volCargaIndicada = 0;
          }else
          {
              volCargaIndicada = Double.parseDouble(txtVolCargaIndicada.getText());
          }
          
          if(txtPresionPruebaEstampado.getText().length()==0)
          {
              presionPruebaEstampado = 0;
          }else
          {
              presionPruebaEstampado = Integer.parseInt(txtPresionPruebaEstampado.getText());
          }
          
          if(txtDeformTotal.getText().length()==0)
          {
              deformTotal = 0;
          }else
          {
              deformTotal = Integer.parseInt(txtDeformTotal.getText());
          }
          
          if(txtDeformPermanente.getText().length()==0)
          {
              deformPermanente = 0;
          }else
          {
              deformPermanente = Integer.parseInt(txtDeformPermanente.getText());
          }
          
          if(txtElasticidad.getText().length()==0)
          {
              elasticidad = 0;
          }else
          {
              elasticidad = Integer.parseInt(txtElasticidad.getText());
          }
          
          if(txtDeformPermPorcentaje.getText().length()==0)
          {
              deformPermPorcentaje = 0;
          }else
          {
              deformPermPorcentaje = Integer.parseInt(txtDeformPermPorcentaje.getText());
          }
  
          if(ddlIdFoto.getSelectedIndex()>0)
          {
              JOptionPane.showMessageDialog(null,"No Se Puede Actualizar Id Foto");
              ddlIdFoto.requestFocus();
              ddlIdFoto.setSelectedIndex(0);
              throw new Exception();
          }
          
          dt.setPresionPrueba(presionPrueba);
          dt.setPresionServicio(presionServicio);
          dt.setVolCargaIndicada(volCargaIndicada);
          dt.setPresionPruebaEstampado(presionPruebaEstampado);
          dt.setDeformTotal(deformTotal);
          dt.setDeformPermanente(deformPermanente);
          dt.setElasticidad(elasticidad);
          dt.setDeforPermPorcentaje(deformPermPorcentaje);
          
          res = dao.actualizarDetalleRevision(dt);
          if(res>0)
          {
              actualizarJTable(menu.idRevision);
              JOptionPane.showMessageDialog(null,"Detalle Actualizado");
              throw new Exception();
          }else
          {
              JOptionPane.showMessageDialog(null,"No Se Puedo Actualizar Detalle");
              throw new Exception();
          }
        }
            
        }catch(Exception ex)
        {
           
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void ddlIdNormaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddlIdNormaActionPerformed
    try
    {
        NormaDAO dao = new NormaDAO();
        String norma = "";
        if(ddlIdNorma.getSelectedItem().equals("-Seleccione-"))
        {
            norma = "";
            txtNormaT.setText(norma);
        }else
        {
         int id = Integer.parseInt(ddlIdNorma.getSelectedItem().toString());
         ArrayList<NormaDT> lista = dao.obtenerNorma(id);
         for(int i=0; i<lista.size(); i++)
         {
            norma = lista.get(i).getNorma();
         }
         txtNormaT.setText(norma);
        }
    }catch(Exception ex)
    {
        
    }
    }//GEN-LAST:event_ddlIdNormaActionPerformed

    private void ddlIdTipoCilindroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddlIdTipoCilindroActionPerformed
         try
    {
        TipoCilindroDAO dao = new TipoCilindroDAO();
        String codigo = "";
        String nombre = "";
        if(ddlIdTipoCilindro.getSelectedItem().equals("-Seleccione-"))
        {
            codigo = "";
            nombre = "";
            txtCodigoT.setText(codigo);
            txtNombreT.setText(nombre);
        }else
        {
         int id = Integer.parseInt(ddlIdTipoCilindro.getSelectedItem().toString());
         ArrayList<TipoCilindroDT> lista = dao.buscarTipoCilindro(id);
         for(int i=0; i<lista.size(); i++)
         {
            codigo = lista.get(i).getCodigo();
            nombre = lista.get(i).getNombre();
         }
         txtNombreT.setText(nombre);
         txtCodigoT.setText(codigo);
        }
    }catch(Exception ex)
    {
        
    }
    }//GEN-LAST:event_ddlIdTipoCilindroActionPerformed

    private void ddlIdFabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddlIdFabricanteActionPerformed
         try
    {
        FabricanteDAO dao = new FabricanteDAO();
        String codigo = "";
        String nombre = "";
        if(ddlIdFabricante.getSelectedItem().equals("-Seleccione-"))
        {
            codigo = "";
            nombre = "";
            txtCodigoF.setText(codigo);
            txtNombreF.setText(nombre);
        }else
        {
         int id = Integer.parseInt(ddlIdFabricante.getSelectedItem().toString());
         ArrayList<FabricanteDT> lista = dao.buscarFarbicante(id);
         for(int i=0; i<lista.size(); i++)
         {
            codigo = lista.get(i).getCodigo();
            nombre = lista.get(i).getNombre();
         }
         txtNombreF.setText(nombre);
         txtCodigoF.setText(codigo);
        }
    }catch(Exception ex)
    {
        
    }
    }//GEN-LAST:event_ddlIdFabricanteActionPerformed

    private void txtNumeroEstampadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroEstampadoKeyTyped
        char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtNumeroEstampadoKeyTyped

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
                    fila.createCell(0).setCellValue("Numero");
                    fila.createCell(1).setCellValue("Fecha Fabricacion");
                    fila.createCell(2).setCellValue("Nombre Fabricante");
                    fila.createCell(3).setCellValue("Norma");
                    fila.createCell(4).setCellValue("Fecha Ultima Prueba");
                    fila.createCell(5).setCellValue("Presion De Servicio");
                    fila.createCell(6).setCellValue("Presion De Prueba Estampado");
                    fila.createCell(7).setCellValue("Vol Carga Indicada");
                    fila.createCell(8).setCellValue("Presion De Prueba");
                    fila.createCell(9).setCellValue("Deform Total");
                    fila.createCell(10).setCellValue("Deform Permanente");
                    fila.createCell(11).setCellValue("Elasticidad");
                    fila.createCell(12).setCellValue("Deform Perm Porcentaje");
                    fila.createCell(13).setCellValue("Pintura");
                    fila.createCell(14).setCellValue("Ruta Foto");
                    fila.createCell(15).setCellValue("Cambio Valvula");
                    fila.createCell(16).setCellValue("Valvula");
                    fila.createCell(17).setCellValue("Manilla");
                    fila.createCell(18).setCellValue("Volante");
                    fila.createCell(19).setCellValue("IV Hilo Cuello");
                    fila.createCell(20).setCellValue("IV Exterior");
                    fila.createCell(21).setCellValue("IV Interior");
                    fila.createCell(22).setCellValue("Aprobado");
                    
                    pbBarraProgreso.setMaximum(tablaDetalleRevision.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;
                    
                    for(int i=0; i<tablaDetalleRevision.getRowCount(); i++)
                    {
                        rect = tablaDetalleRevision.getCellRect(i,0,true);
                        try
                        {
                            tablaDetalleRevision.scrollRectToVisible(rect);
                        }catch(java.lang.ClassCastException e)
                        {
                            
                        }
                        tablaDetalleRevision.setRowSelectionInterval(i, i);
                        pbBarraProgreso.setValue((i+1));
                        
                        filas = hoja.createRow((i+1));
                        filas.createCell(0).setCellValue(tablaDetalleRevision.getValueAt(i,1).toString());
                        filas.createCell(1).setCellValue(tablaDetalleRevision.getValueAt(i,2).toString());
                        filas.createCell(2).setCellValue(tablaDetalleRevision.getValueAt(i,3).toString());
                        filas.createCell(3).setCellValue(tablaDetalleRevision.getValueAt(i,4).toString());
                        filas.createCell(4).setCellValue(tablaDetalleRevision.getValueAt(i,5).toString());
                        filas.createCell(5).setCellValue(tablaDetalleRevision.getValueAt(i,6).toString());
                        filas.createCell(6).setCellValue(tablaDetalleRevision.getValueAt(i,7).toString());
                        filas.createCell(7).setCellValue(tablaDetalleRevision.getValueAt(i,8).toString());
                        filas.createCell(8).setCellValue(tablaDetalleRevision.getValueAt(i,9).toString());
                        filas.createCell(9).setCellValue(tablaDetalleRevision.getValueAt(i,10).toString());
                        filas.createCell(10).setCellValue(tablaDetalleRevision.getValueAt(i,11).toString());
                        filas.createCell(11).setCellValue(tablaDetalleRevision.getValueAt(i,12).toString());
                        filas.createCell(12).setCellValue(tablaDetalleRevision.getValueAt(i,13).toString());
                        filas.createCell(13).setCellValue(tablaDetalleRevision.getValueAt(i,14).toString());
                        filas.createCell(14).setCellValue(tablaDetalleRevision.getValueAt(i,15).toString());    
                        filas.createCell(15).setCellValue(tablaDetalleRevision.getValueAt(i,16).toString());
                        filas.createCell(16).setCellValue(tablaDetalleRevision.getValueAt(i,17).toString());
                        filas.createCell(17).setCellValue(tablaDetalleRevision.getValueAt(i,18).toString());
                        filas.createCell(18).setCellValue(tablaDetalleRevision.getValueAt(i,19).toString());
                        filas.createCell(19).setCellValue(tablaDetalleRevision.getValueAt(i,20).toString());
                        filas.createCell(20).setCellValue(tablaDetalleRevision.getValueAt(i,21).toString());
                        filas.createCell(21).setCellValue(tablaDetalleRevision.getValueAt(i,22).toString());
                        filas.createCell(22).setCellValue(tablaDetalleRevision.getValueAt(i,23).toString());
                     
                        try
                        {
                            Thread.sleep(20);
                        }catch(InterruptedException ex)
                        {
                            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE,null,ex);
                        }
                    }
                    pbBarraProgreso.setValue(0);
                    pbBarraProgreso.setString("Abriendo Excel...");
                    
                    try
                    {
                        String revision = desktop.menu.idRevision+"";
                        workbook.write(new FileOutputStream(new File(ruta+"//DetalleRevisionNum"+revision+".xlsx")));
                        Desktop.getDesktop().open(new File(ruta+"//DetalleRevisionNum"+revision+".xlsx"));
                        
                    }catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,"Cierre El Excel Para Actualizar!!");
                    }
                }
            };
          t.start();
          
      }catch(Exception ex)
      {
         JOptionPane.showMessageDialog(null,ex.getMessage());
      }
    }//GEN-LAST:event_btnExcelActionPerformed

    private void txtVolCargaIndicadaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVolCargaIndicadaKeyTyped
       int k = (int) evt.getKeyChar();
       if(k>=46 && k<=57)
       {

           if(k==46)
           {
               String dato = txtVolCargaIndicada.getText();
               int largo = dato.length();
               for(int i=0; i<largo; i++)
               {
                   if(dato.charAt(0)==('.'))
                   {
                       evt.setKeyChar((char)KeyEvent.VK_CLEAR);
                   }
                   if(dato.contains("."))
                   {
                       evt.setKeyChar((char)KeyEvent.VK_CLEAR);
                   }
                   
               }
           }
           if(k==47)
            {
                evt.setKeyChar((char)KeyEvent.VK_CLEAR);  
            }
       }else
       {
           evt.setKeyChar((char)KeyEvent.VK_CLEAR);
           evt.consume();
       }
    }//GEN-LAST:event_txtVolCargaIndicadaKeyTyped

    private void txtPresionDeServicioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPresionDeServicioKeyTyped
    char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtPresionDeServicioKeyTyped

    private void txtPresionPruebaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPresionPruebaKeyTyped
        char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtPresionPruebaKeyTyped

    private void txtPresionPruebaEstampadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPresionPruebaEstampadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPresionPruebaEstampadoActionPerformed

    private void txtPresionPruebaEstampadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPresionPruebaEstampadoKeyTyped
    char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtPresionPruebaEstampadoKeyTyped

    private void txtDeformTotalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeformTotalKeyTyped
    char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtDeformTotalKeyTyped

    private void txtDeformPermanenteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeformPermanenteKeyTyped
    char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtDeformPermanenteKeyTyped

    private void txtElasticidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtElasticidadKeyTyped
    char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtElasticidadKeyTyped

    private void txtDeformPermPorcentajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeformPermPorcentajeKeyTyped
    char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtDeformPermPorcentajeKeyTyped

    private void ddlIdFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddlIdFotoActionPerformed
         try
    {
        FotoDAO dao = new FotoDAO();
        String ruta = "";
        if(ddlIdFoto.getSelectedItem().equals("-Seleccione-"))
        {
            ruta = "";
            txtRuta.setText(ruta);
        }else
        {
         int id = Integer.parseInt(ddlIdFoto.getSelectedItem().toString());
         ArrayList<FotoDT> lista = dao.buscarFoto(id);
         for(int i=0; i<lista.size(); i++)
         {
             ruta = lista.get(i).getRuta();
         }
         txtRuta.setText(ruta);
        }
    }catch(Exception ex)
    {
        
    }
    }//GEN-LAST:event_ddlIdFotoActionPerformed

    private void btnFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoActionPerformed
      fotoDialog j = new fotoDialog(this,true);
      j.setVisible(true);
    }//GEN-LAST:event_btnFotoActionPerformed

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
            java.util.logging.Logger.getLogger(formularioDetalleRevision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formularioDetalleRevision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formularioDetalleRevision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formularioDetalleRevision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formularioDetalleRevision().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanel8;
    private javax.swing.JScrollPane JScrollPane;
    private javax.swing.JTable JtablaRevision;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnFoto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private com.toedter.calendar.JDateChooser dcFabricacion;
    private com.toedter.calendar.JDateChooser dcUltimaPrueba;
    private javax.swing.JComboBox ddlBuscarIdDetalle;
    private javax.swing.JComboBox ddlIdFabricante;
    private javax.swing.JComboBox ddlIdFoto;
    private javax.swing.JComboBox ddlIdNorma;
    private javax.swing.JComboBox ddlIdTipoCilindro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox jcbAprobado;
    private javax.swing.JCheckBox jcbCambioValvula;
    private javax.swing.JCheckBox jcbIvExterior;
    private javax.swing.JCheckBox jcbIvHiloCuello;
    private javax.swing.JCheckBox jcbManilla;
    private javax.swing.JCheckBox jcbPintura;
    private javax.swing.JCheckBox jcbValvula;
    private javax.swing.JCheckBox jcbVolante;
    private javax.swing.JCheckBox jcboIvInterior;
    private javax.swing.JMenuItem jmCliente;
    private javax.swing.JMenuItem jmFabricante;
    private javax.swing.JMenuItem jmLogin;
    private javax.swing.JMenuItem jmNorma;
    private javax.swing.JMenuItem jmTipoCilindro;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JMenuItem mtMenu;
    private javax.swing.JMenuItem mtRevison;
    private javax.swing.JProgressBar pbBarraProgreso;
    private javax.swing.JTable tablaDetalleRevision;
    private javax.swing.JTextField txtCodigoF;
    private javax.swing.JTextField txtCodigoT;
    private javax.swing.JTextField txtDeformPermPorcentaje;
    private javax.swing.JTextField txtDeformPermanente;
    private javax.swing.JTextField txtDeformTotal;
    private javax.swing.JTextField txtElasticidad;
    private javax.swing.JTextField txtIdDetalle;
    private javax.swing.JTextField txtNombreF;
    private javax.swing.JTextField txtNombreT;
    private javax.swing.JTextField txtNormaT;
    private javax.swing.JTextField txtNumeroEstampado;
    private javax.swing.JTextField txtPresionDeServicio;
    private javax.swing.JTextField txtPresionPrueba;
    private javax.swing.JTextField txtPresionPruebaEstampado;
    public static javax.swing.JTextField txtRevision;
    private javax.swing.JTextField txtRuta;
    private javax.swing.JTextField txtVolCargaIndicada;
    // End of variables declaration//GEN-END:variables
}
