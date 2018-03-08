/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

//Librerias
import ModeloDAO.ClienteDAO;
import ModeloDT.ClienteDT;
import ModeloDT.conexion;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


/**
 *
 * @author Angelo
 */
public final class cliente extends javax.swing.JFrame {

    /**
     * Creates new form cliente
     */
    
    //variables
    Statement instru;
    JTable tabla;
    private final String ruta = System.getProperties().getProperty("user.home");
    public int acceso =0;
    JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
    JMenuItem JMItem = new JMenuItem("Llenar A Formulario"); //Texto De Esa Ventan
    public static int idEliminado = 0;
    //Constructor
    public cliente() {
     try
     {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setSize(new Dimension(800,800));
        this.setResizable(true);
        conexion c = new conexion();
        instru = c.getStatement();
        txtId.setEnabled(false);
        acceso = login.accesoLogin;
        setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
        actualizarTabla();
        ocultarId();
        popup.add(JMItem); //Agregar el MenuItem al Popup
        JMItem.addActionListener((new ActionListener() {
        public void actionPerformed(ActionEvent evt)
        {
          llenarFormulario();
        }
       }));
        tabla.setComponentPopupMenu(popup);
        cerrar();
     }catch(Exception ex)
     {
         
     }
    }
    
    //Metodo Para Ocultar la id de la tabla
    public void ocultarId()
    {
        tablaCliente.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCliente.getColumnModel().getColumn(0).setMinWidth(0);
        tablaCliente.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
    
    public void llenarFormulario()
    {
        try
        {
           //Para Obtener El Id de la fila de la Tabla seleccionada
           int i = tabla.getSelectedRow(); // La Fila Selecciona
           int idCliente = (int) tabla.getValueAt(i,0); // 
           ClienteDAO dao = new ClienteDAO();
           ArrayList<ClienteDT>lista = dao.buscarCliente(idCliente);
           txtNombre.setText(lista.get(0).getNombre());
           txtRut.setText(lista.get(0).getRut()+"");
           txtDireccion.setText(lista.get(0).getDireccion());
           txtCorreo.setText(lista.get(0).getEmail());
           txtId.setText(lista.get(0).getId()+"");
           JOptionPane.showMessageDialog(null,"Extraccion Realizado Correctamente");

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
        }
    }
    
    //Metodo Para Limpiar Formulario Al Guardar , Eliminar , Actualizar
    public void limpiar() throws Exception
    {
        txtRut.setText("");
        txtCorreo.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtId.setText("");
        actualizarTabla();
        pbBarraProgreso.setString("");
        ocultarId();
    }
    
    //Metodo para cerrar jFrame
    public void cerrar()
    {
        try
        {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    confirmarSalida();
                }
            });
            this.setVisible(true);
        }catch(Exception ex)
        {
        }
    }
    
    //Metodo Que Abre La ventana de salida
    public void confirmarSalida()
    {
        int valor = JOptionPane.showConfirmDialog(this,"¿Esta Seguro De Cerrar La Aplicacion?","Adventencia",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(valor == JOptionPane.YES_OPTION)
        {
            JOptionPane.showMessageDialog(null,"Gracias Por su Visita","Gracias",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    
    //Metodo para llenar jtable con los datos de la BD
    public void actualizarTabla() throws Exception
    {
        try
        {
            ClienteDAO dao = new ClienteDAO();
            ResultSet dato;
            DefaultTableModel dtm = new DefaultTableModel();
            tabla = this.tablaCliente;
            tabla.setModel(dtm);
            dtm.setColumnIdentifiers(new Object[]{"Id","Rut","Nombre","Direccion","Email"});
            dato = dao.imprimirCliente();
            while(dato.next())
            {
                String correo = "Sin Correo";
                if(dato.getString("email")==null)
                {
                    correo = "";
                }else
                {
                    correo = dato.getString("email");
                }
                dtm.addRow(new Object[]{
                                    dato.getInt("id"),
                                    obtenerRut(dato.getInt("rut")),
                                    dato.getString("nombre"),
                                    dato.getString("direccion"),
                                    correo
                                       });
            }
            UpdateTable(dtm,dao);
        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
    
    public void UpdateTable(TableModel dtm ,  ClienteDAO dao)
    {
        dtm.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                try
                {
                    if(acceso==1)
                    {
                       JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
                       actualizarTabla();
                       throw new Exception();
                    }
                  
                   if(e.getType() == TableModelEvent.UPDATE)
                   {
                       ResultSet dato;
                       int columna = e.getColumn();
                       int fila = e.getFirstRow();
                       if(columna == 1)
                       {
                           String rutActual = tablaCliente.getValueAt(fila,columna).toString();
                           if(rutActual.length()!=8)
                           {
                               JOptionPane.showMessageDialog(null,"Ingrese 8 Primeros Digitos");
                               throw new Exception();
                           }else
                           {
                            int id = (int)tabla.getValueAt(fila,0);
                            
                            
                            String sql = "SELECT rut FROM vw_cliente WHERE id LIKE "+id+"";
                           
                            try {
                               dato = instru.executeQuery(sql);
                               int rutAnterior = 0;
                               while(dato.next())
                               {
                                  rutAnterior = dato.getInt("rut");
                               }
                               int actual = Integer.parseInt(rutActual);

                               int res = dao.actualizarRutCliente(id,actual, rutAnterior);

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Rut  Actualizado");
                                   throw new Exception();
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Rut");
                                   throw new Exception();
                               }
                           
                               
                            } catch (Exception ex) {
                           
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Rut(8)");
                               }
                               
                           }
                        }
                       }else if(columna == 2)
                       {
                           String nombreActual = tablaCliente.getValueAt(fila, columna).toString();
                           if(nombreActual.length()==0)
                           {
                               JOptionPane.showMessageDialog(null,"Ingrese Nombre Para Actualizar");
                               throw new Exception();
                           }else
                           {
                                int id = (int)tabla.getValueAt(fila,0);
                                String sql = "SELECT nombre FROM vw_cliente WHERE id LIKE "+id+"";
                           
                                try {
                                dato = instru.executeQuery(sql);
                                String nombreAnterior = "";
                                while(dato.next())
                                {
                                  nombreAnterior = dato.getString("nombre");
                                }

                                int res = dao.actualizarNombreCliente(id,nombreActual, nombreAnterior);

                                if(res>0)
                                {
                                   JOptionPane.showMessageDialog(null,"Nombre  Actualizado");
                                }else
                                {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Nombre");
                                }
                           
                               
                                } catch (Exception ex) {
                           
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(240)");

                               }

                                }
                           }
                       }else if(columna==3)
                       {
                           String direccionActual = tablaCliente.getValueAt(fila, columna).toString();
                           if(direccionActual.length()==0)
                           {
                               JOptionPane.showMessageDialog(null,"Ingrese Direccion Para Actualizar");
                               throw new Exception();
                           }else
                           {
                               int id = (int)tabla.getValueAt(fila,0);
                                String sql = "SELECT direccion FROM vw_cliente WHERE id LIKE "+id+"";
                           
                            try {
                                dato = instru.executeQuery(sql);
                                String direccionAnterior = "";
                               while(dato.next())
                               {
                                  direccionAnterior = dato.getString("direccion");
                               }
                               int res = dao.actualizarDireccionCliente(id,direccionActual, direccionAnterior);

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Direccion  Actualizado");
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Direccion");
                               }
                           
                               
                            } catch (Exception ex) {
                           
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Direccion(240)");
                               }
                           }
                           }
                       }else if(columna==4)
                       {
                            String emailActual = tablaCliente.getValueAt(fila, columna).toString();
                           if(emailActual.length()==0)
                           {
                               JOptionPane.showMessageDialog(null,"Ingrese Email Para Actualizar");
                               throw new Exception();
                           }else
                           {
                               int id = (int)tabla.getValueAt(fila,0);
                                String sql = "SELECT email FROM vw_cliente WHERE id LIKE "+id+"";
                           
                            try {
                                dato = instru.executeQuery(sql);
                                String emailAnterior = "";
                               while(dato.next())
                               {
                                  emailAnterior = dato.getString("email");
                               }
                               int res = dao.actualizarEmailCliente(id,emailActual, emailAnterior);

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Email  Actualizado");
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Email");
                               }
                           
                               
                            } catch (Exception ex) {
                           
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Email(80)");
                               }
                           }
                           }
                       }
                   }
                  }catch(Exception ex)
                  {
                  }
                }
            });
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
        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtRut = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCliente = new javax.swing.JTable();
        pbBarraProgreso = new javax.swing.JProgressBar();
        btnBuscar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jtLogin = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jmtMenu = new javax.swing.JMenuItem();
        jmiRevision = new javax.swing.JMenuItem();
        jmFabricante = new javax.swing.JMenuItem();
        jmNorma = new javax.swing.JMenuItem();
        jmTipoCilindro = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cliente");
        setBackground(new java.awt.Color(255, 255, 204));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Formulario Cliente");

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
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnExcel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExcel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Rut:");

        txtRut.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtRut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRutKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Direccion:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Correo:");

        tablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "null", "Title 5"
            }
        ));
        jScrollPane1.setViewportView(tablaCliente);

        pbBarraProgreso.setStringPainted(true);

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel6.setText("Id:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                    .addComponent(pbBarraProgreso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDireccion)
                                        .addGap(10, 10, 10))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNombre))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtRut, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtRut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbBarraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jMenu1.setText("Procesos");

        jtLogin.setText("Cerrar Sesion");
        jtLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtLoginActionPerformed(evt);
            }
        });
        jMenu1.add(jtLogin);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mantenedores");

        jmtMenu.setText("Menu");
        jmtMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtMenuActionPerformed(evt);
            }
        });
        jMenu2.add(jmtMenu);

        jmiRevision.setText("Revision");
        jmiRevision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRevisionActionPerformed(evt);
            }
        });
        jMenu2.add(jmiRevision);

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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtLoginActionPerformed
    cerrarSesion cs = new cerrarSesion(this,true,cliente.this);
    cs.setVisible(true);
    }//GEN-LAST:event_jtLoginActionPerformed

    private void jmTipoCilindroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmTipoCilindroActionPerformed
     tipoCilindro tc  = new tipoCilindro();
     tc.setVisible(true);
     cliente.this.dispose();
    }//GEN-LAST:event_jmTipoCilindroActionPerformed

    private void jmFabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmFabricanteActionPerformed
       fabricante f = new fabricante();
       f.setVisible(true);
       cliente.this.dispose();
    }//GEN-LAST:event_jmFabricanteActionPerformed

    private void jmNormaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmNormaActionPerformed
       norma n = new norma();
       n.setVisible(true);
       cliente.this.dispose();
    }//GEN-LAST:event_jmNormaActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
       try
       { limpiar();
         JOptionPane.showMessageDialog(null,"Formulario Limpio");
       }catch(Exception ex)
       {
           
       }
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try
        {
        if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
            ClienteDAO dao = new ClienteDAO();
            ClienteDT dt = new ClienteDT();
            int id;
            int res;
            int rut = 0;
            String nombre = "" , direccion = "" , correo = "";
            if(txtId.getText().length()==0)
            {
                JOptionPane.showMessageDialog(null,"Busque Cliente Antes De Actualizar");
                throw new Exception();
                
            }else
            {
                id = Integer.parseInt(txtId.getText());
                
                if(txtRut.getText().length()==0 || txtRut.equals(" "))
                {
                    JOptionPane.showMessageDialog(null,"Ingrese Rut Para Actualizar");
                    txtRut.requestFocus();
                    throw new Exception();
                }else
                {
                    if(txtRut.getText().length()!=8)
                    {
                        JOptionPane.showMessageDialog(null,"Ingrese Solo 8 Primeros Digitos");
                        txtRut.requestFocus();
                        throw new Exception();
                    }else{
                            rut = Integer.parseInt(txtRut.getText());
                    }
                }
                
                if(txtNombre.getText().length()==0 || txtNombre.getText().equals(" "))
                {
                    JOptionPane.showMessageDialog(null,"Ingrese Nombre Para Actualizar");
                    txtNombre.requestFocus();
                    throw new Exception();
                }else
                {
                    if(txtNombre.getText().length()>=241)
                    {
                        JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(240)");
                        txtNombre.requestFocus();
                        throw new Exception();
                    }else
                    {
                        nombre = txtNombre.getText();
                    }
                }
                if(txtDireccion.getText().length()==0|| txtDireccion.equals(" "))
                {
                    JOptionPane.showMessageDialog(null,"Ingrese Direccion Para Actualizar");
                    txtDireccion.requestFocus();
                    throw new Exception();
                }else
                {
                    if(txtDireccion.getText().length()>=241)
                    {
                        JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Direccion(240)");
                        txtDireccion.requestFocus();
                        throw new Exception();
                    }else{
                        direccion = txtDireccion.getText();
                    }
                }
                
                if(txtCorreo.getText().length()>0)
                {
                if(txtCorreo.getText().length()>=81)
                {
                    JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Email(80)");
                    txtCorreo.requestFocus();
                    throw new Exception();
                }else{
                    correo = txtCorreo.getText();
                }
                }else
                {
                    correo = "null";
                }
                dt.setId(id);
                dt.setRut(rut);
                dt.setNombre(nombre);
                dt.setDireccion(direccion);
                dt.setEmail(correo);
                res = dao.actualizarCliente(dt);
                if(res>0)
                {
                    JOptionPane.showMessageDialog(null,"Cliente Actualizado");
                    actualizarTabla();
                    limpiar();
                }else
                {
                    JOptionPane.showMessageDialog(null,"Problemas Al Actualizar Cliente");
                }
            }
        }catch(Exception ex)
        {
            
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        try
        {
            if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
            ClienteDAO dao = new ClienteDAO();
            int id;
            int res;
            ResultSet dato;
            int cantidadRevision = 0;
            if(txtId.getText().length()==0)
            {
                JOptionPane.showMessageDialog(null,"Busque Cliente Antes De Eliminar");
                throw new Exception();
            }else
            {
                id = Integer.parseInt(txtId.getText());
                
                //VALIDAR SI CLIENTE ESTA ASIGNADO A UNA REVISION
                dato = dao.validarEliminarCliente(id);
                while(dato.next())
                {
                    cantidadRevision = dato.getInt("Idcliente");
                }
                if(cantidadRevision>0)
                {
                    JOptionPane.showMessageDialog(null,"No Puede Eliminar Este Cliente , Porque Esta Asociado a "+cantidadRevision+" Revisiones !");
                    throw new Exception();
                }
                
                confirmarEliminar eliminar = new confirmarEliminar(this,true);
                eliminar.setVisible(true);
              if(confirmarEliminar.aceptar>0)
              {
                
                res = dao.eliminarCliente(id);
                if(res>0)
                {
                    JOptionPane.showMessageDialog(null,"Cliente Eliminado");
                    actualizarTabla();
                    limpiar();
                    confirmarEliminar.aceptar = 0;
                }else
                {
                    JOptionPane.showMessageDialog(null,"Problemas Al Eliminar Cliente");
                    confirmarEliminar.aceptar = 0;
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

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        //Variables para validar Campos Repetidos
        int rutCopia = 0;
        int nombreCopia = 0;
        int direccionCopia = 0;
        int emailCopia = 0;
        ArrayList<ClienteDT>lista = new ArrayList<>();
        try
        {
            if(acceso==1)
            {
               JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
               throw new Exception();
            }
            ClienteDAO dao = new ClienteDAO();
            //CREAR CLASE CLIENTE PARA RECUPERAR DATOS
            ClienteDT dt = new ClienteDT();
            if(txtRut.getText().length()==0 || txtRut.getText().equals(" "))
            {
                JOptionPane.showMessageDialog(null,"Ingrese Rut");
                txtRut.requestFocus();
                throw new Exception();
            }else{
                if(txtRut.getText().length()!=8)
                {
                    JOptionPane.showMessageDialog(null,"Ingrese 8 Primeros Digitos");
                    txtRut.requestFocus();
                    txtRut.setText("");
                    throw new Exception();
                }else{
                    rutCopia = Integer.parseInt(txtRut.getText());
                    lista = dao.buscarRutRepetido(rutCopia);
                    if(lista.isEmpty()==false)
                    {
                        JOptionPane.showMessageDialog(null,"Rut Repetido No Se Puede Ingresar");
                        int visible = lista.get(0).getVisible();
                        int idClienteEliminado = lista.get(0).getId();
                        if(visible == 0)
                        {
                            idEliminado = idClienteEliminado;
                            recuperarCliente rc = new recuperarCliente(this,true);
                            rc.setVisible(true);
                        }
                        txtRut.requestFocus();
                        limpiar();
                        throw new Exception();
                    }else
                    {
                        dt.setRut(Integer.parseInt(txtRut.getText()));
                    }
                }
                
            }

            if(txtNombre.getText().length()==0|| txtNombre.getText().equals(" "))
            {
                JOptionPane.showMessageDialog(null,"Ingrese Nombre");
                txtNombre.requestFocus();
                throw new Exception();
            }else{
                
                if(txtNombre.getText().length()>=241)
                {
                    JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(240)");
                    txtNombre.requestFocus();
                    throw new Exception();
                }else
                {
                    lista  = dao.buscarNombreRepetido(txtNombre.getText());
                    if(lista.isEmpty()==false)
                    {
                        JOptionPane.showMessageDialog(null,"Nombre Repetido No Se Puede Ingresar");
                        int visible = lista.get(0).getVisible();
                        int idClienteEliminado = lista.get(0).getId();
                        if(visible == 0)
                        {
                            idEliminado = idClienteEliminado;
                            recuperarCliente rc = new recuperarCliente(this,true);
                            rc.setVisible(true);
                        }
                        txtNombre.requestFocus();
                        limpiar();
                        throw new Exception();
                    }else
                    {
                        dt.setNombre(txtNombre.getText());
                    }
                }
                
            }

            if(txtDireccion.getText().length()==0 ||txtDireccion.getText().equals(" "))
            {
             JOptionPane.showMessageDialog(null,"Ingrese Direccion");
             txtDireccion.requestFocus();
             throw new Exception();
            }else{
                
                if(txtDireccion.getText().length()>=241)
                {
                    JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Direccion(240)");
                    txtDireccion.requestFocus();
                    throw new Exception();
                }else
                {
                   dt.setDireccion(txtDireccion.getText());
                }
            }
            
            if(txtCorreo.getText().length()>0)
            {
            if(txtCorreo.getText().length()>=81)
            {
                JOptionPane.showMessageDialog(null,"Minimo Caracteres Para  Email(80)");
                txtCorreo.requestFocus();
                throw new Exception();
            }else
            {
                dt.setEmail(txtCorreo.getText());
            }
            }else
            {
                dt.setEmail("null");
            }
            //LLAMAR A GUARDAR CLIENTE
            int result = dao.guardarCliente(dt);
            if(result>0)
            {
               JOptionPane.showMessageDialog(null,"Cliente Guardado");
               actualizarTabla();
               limpiar();
               txtRut.requestFocus();
            }else
            {
                JOptionPane.showMessageDialog(null,"Problemas Al Guardar Cliente");
            }
        }catch(Exception ex)
        {
           
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try
        {
            ClienteDAO dao = new ClienteDAO();
            ClienteDT dt = new ClienteDT();
            ArrayList<ClienteDT>lista = new ArrayList<ClienteDT>();
            String rut = null;
            if(txtRut.getText().length()==0 || txtRut.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Ingrese Un Rut Para Buscar");
                txtRut.requestFocus();
                throw new Exception();
            }else if(txtRut.getText().length()!=8)
            {
                JOptionPane.showMessageDialog(null,"Ingrese 8 Primeros Digitos");
                txtRut.requestFocus();
                throw new Exception();
            }else{
                rut = txtRut.getText();
                lista = dao.buscarClienteConRut(rut);
                //Validar Si Se Encontro Cliente
                if(lista.isEmpty()==false){
                for(int i=0; i<lista.size(); i++)
                {

                    txtNombre.setText(lista.get(i).getNombre());
                    txtDireccion.setText(lista.get(i).getDireccion());
                    txtCorreo.setText(lista.get(i).getEmail());
                    txtId.setText(lista.get(i).getId()+"");

                }
                JOptionPane.showMessageDialog(null,"Cliente Encontrado");
                }else
                {
                    JOptionPane.showMessageDialog(null,"Cliente No Encontrado");
                    limpiar();
                }
            }
        }catch(Exception ex)
        {
            
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jmtMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtMenuActionPerformed
        menu m = new menu();
        m.setVisible(true);
       cliente.this.dispose();
    }//GEN-LAST:event_jmtMenuActionPerformed

    private void jmiRevisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRevisionActionPerformed
       revision r = new revision();
       r.setVisible(true);
       cliente.this.dispose();
    }//GEN-LAST:event_jmiRevisionActionPerformed

    private void txtRutKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRutKeyTyped
        char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtRutKeyTyped

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
                    fila.createCell(0).setCellValue("Rut");
                    fila.createCell(1).setCellValue("Nombre");
                    fila.createCell(2).setCellValue("Direccion");
                    fila.createCell(3).setCellValue("Email");
                    
                    pbBarraProgreso.setMaximum(tablaCliente.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;
                    String correo = "";
                    for(int i=0; i<tablaCliente.getRowCount(); i++)
                    {
                        rect = tablaCliente.getCellRect(i,0,true);
                        try
                        {
                            if(tablaCliente.getValueAt(i,4)==null)
                            {
                                correo = "NULL";
                            }else
                            {
                                correo = tablaCliente.getValueAt(i,4).toString();
                            }
                            tablaCliente.scrollRectToVisible(rect);
                        }catch(java.lang.ClassCastException e)
                        {
                            
                        }
                        tablaCliente.setRowSelectionInterval(i, i);
                        pbBarraProgreso.setValue((i+1));
                        
                        filas = hoja.createRow((i+1));
                        filas.createCell(0).setCellValue(tablaCliente.getValueAt(i,1).toString());
                        filas.createCell(1).setCellValue(tablaCliente.getValueAt(i,2).toString());
                        filas.createCell(2).setCellValue(tablaCliente.getValueAt(i,3).toString());
                        filas.createCell(3).setCellValue(correo);
                        
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
                        workbook.write(new FileOutputStream(new File(ruta+"//ExcelCliente.xlsx")));
                        Desktop.getDesktop().open(new File(ruta+"//ExcelCliente.xlsx"));
                        
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
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem jmFabricante;
    private javax.swing.JMenuItem jmNorma;
    private javax.swing.JMenuItem jmTipoCilindro;
    private javax.swing.JMenuItem jmiRevision;
    private javax.swing.JMenuItem jmtMenu;
    private javax.swing.JMenuItem jtLogin;
    private javax.swing.JProgressBar pbBarraProgreso;
    private javax.swing.JTable tablaCliente;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRut;
    // End of variables declaration//GEN-END:variables
}
