/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import ModeloDAO.TipoCilindroDAO;
import ModeloDT.TipoCilindroDT;
import ModeloDT.conexion;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import desktop.login;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public final class tipoCilindro extends javax.swing.JFrame {

    /**
     * Creates new form tipoCilindro
     */
    
    JTable tabla;
    Statement instru;
    public int acceso = 0;
    private final String ruta = System.getProperties().getProperty("user.home");
    JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
    JMenuItem JMItem = new JMenuItem("Llenar A Formulario"); //Texto De Esa Ventan
    
    public tipoCilindro() {
       try
       {
        initComponents();
        conexion c = new conexion();
        instru = c.getStatement();
        txtId.setEnabled(false);
        txtCodigo.requestFocus();
        setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
        actualizarJTable();
        acceso = login.accesoLogin;
        ocultarId();
        popup.add(JMItem); //Agregar el MenuItem al Popup
        JMItem.addActionListener((new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
          llenarFormulario();
        }
       }));
       
        tabla.setComponentPopupMenu(popup);
        acceso = login.accesoLogin;
        cerrar();
       }catch(Exception ex)
       {
           
       }
    }
    
    public void llenarFormulario()
    {
        try
        {
           //Para Obtener El Id de la fila de la Tabla seleccionada
           int i = tabla.getSelectedRow(); // La Fila Selecciona
           int idTipoCilindro = (int) tabla.getValueAt(i,0); // 
           TipoCilindroDAO dao = new TipoCilindroDAO();
           ArrayList<TipoCilindroDT>lista = dao.buscarTipoCilindro(idTipoCilindro);
           txtCodigo.setText(lista.get(0).getCodigo());
           txtNombre.setText(lista.get(0).getNombre());
           txtId.setText(lista.get(0).getId()+"");
           JOptionPane.showMessageDialog(null,"Extraccion Realizado Correctamente");

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
        }
    }
    
    public void ocultarId()
    {
        tablaTipoCilindro.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaTipoCilindro.getColumnModel().getColumn(0).setMinWidth(0);
        tablaTipoCilindro.getColumnModel().getColumn(0).setPreferredWidth(0);
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
    
    public void actualizarJTable()
    {
        try
        {
            TipoCilindroDAO dao = new TipoCilindroDAO();
            ResultSet dato;
            DefaultTableModel dtm = new DefaultTableModel();
            tabla = this.tablaTipoCilindro;
            tabla.setModel(dtm);
            dtm.setColumnIdentifiers(new Object[]{"Id","Codigo","Nombre"});
            dato = dao.imprimirTipoCilindro();
            while(dato.next())
            {
                dtm.addRow(new Object[]{dato.getInt("id"),dato.getString("codigo"),dato.getString("nombre")});
            }
            ocultarId();
            dtm.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                  try
                  {
                      if(acceso==1)
                      {
                       JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
                       throw new Exception();
                      }
                  
                   if(e.getType() == TableModelEvent.UPDATE)
                   {
                       ResultSet dato;
                       int columna = e.getColumn();
                       int fila = e.getFirstRow();
                       if(columna == 1)
                       {
                           String codigoActual = tabla.getValueAt(fila,columna).toString();
                           int id = (int)tabla.getValueAt(fila,0);
                           String sql = "SELECT codigo FROM vw_tipo_cilindro WHERE id LIKE "+id+"";
                           
                           try {
                               dato = instru.executeQuery(sql);
                               String codigoAnterior = "";
                               while(dato.next())
                               {
                                  codigoAnterior = dato.getString("codigo");
                               }
                               int res = dao.actualizarCodigoTipoCilindro(id,codigoActual,codigoAnterior);
                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Codigo Actualizado");
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Codigo");
                               }

                           } catch (Exception ex) {
                              
                              if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(10)");
                               }
                           }
                       }else if(columna==2)
                       {
                           String nombreActual = tabla.getValueAt(fila,columna).toString();
                           int id = (int) tabla.getValueAt(fila,0);
                           String sql = "SELECT nombre FROM vw_tipo_cilindro WHERE id LIKE "+id+"";
                           
                           try{
                               dato = instru.executeQuery(sql);
                               String nombreAnterior = "";
                               while(dato.next()){
                                   nombreAnterior = dato.getString("nombre");
                               }
                               int res = dao.actualizarNombreTipoCilindro(id,nombreActual,nombreAnterior);
                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Nombre Actualizado");
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Nombre");
                               }
                           }catch(Exception ex)
                           {
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(40)");
                               }
                               
                           }
                       }
                   }
                  }catch(Exception ex)
                  {
                      
                  }
                }
            });
        }catch(Exception ex)
        {
            
        }
    }
    
    public void limpiar()
    {
        txtCodigo.setText("");
        txtNombre.setText("");
        pbBarraProgreso.setString("");
        txtId.setText("");
        txtCodigo.requestFocus();
        ocultarId();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTipoCilindro = new javax.swing.JTable();
        pbBarraProgreso = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mtLogin = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mtMenu = new javax.swing.JMenuItem();
        mtRevision = new javax.swing.JMenuItem();
        mtCliente = new javax.swing.JMenuItem();
        mtFabricante = new javax.swing.JMenuItem();
        mtNorma = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tipo Cilindro");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Formulario Tipo Cilindro");

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
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Codigo:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Nombre:");

        tablaTipoCilindro.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaTipoCilindro);

        pbBarraProgreso.setStringPainted(true);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Id:");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                                    .addComponent(txtId))))
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pbBarraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbBarraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
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
                .addContainerGap(363, Short.MAX_VALUE))
        );

        jMenu1.setText("Procesos");

        mtLogin.setText("Cerrar Sesion");
        mtLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtLoginActionPerformed(evt);
            }
        });
        jMenu1.add(mtLogin);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mantenedores");

        mtMenu.setText("Menu");
        mtMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtMenuActionPerformed(evt);
            }
        });
        jMenu2.add(mtMenu);

        mtRevision.setText("Revision");
        mtRevision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtRevisionActionPerformed(evt);
            }
        });
        jMenu2.add(mtRevision);

        mtCliente.setText("Cliente");
        mtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtClienteActionPerformed(evt);
            }
        });
        jMenu2.add(mtCliente);

        mtFabricante.setText("Fabricante");
        mtFabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtFabricanteActionPerformed(evt);
            }
        });
        jMenu2.add(mtFabricante);

        mtNorma.setText("Norma");
        mtNorma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtNormaActionPerformed(evt);
            }
        });
        jMenu2.add(mtNorma);

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
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtLoginActionPerformed
    cerrarSesion cs = new cerrarSesion(this,true,tipoCilindro.this);
    cs.setVisible(true);
    }//GEN-LAST:event_mtLoginActionPerformed

    private void mtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtClienteActionPerformed
       cliente c = new cliente();
       c.setVisible(true);
       tipoCilindro.this.dispose();
    }//GEN-LAST:event_mtClienteActionPerformed

    private void mtNormaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtNormaActionPerformed
       norma n = new norma();
        n.setVisible(true);
        tipoCilindro.this.dispose();
    }//GEN-LAST:event_mtNormaActionPerformed

    private void mtFabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtFabricanteActionPerformed
       fabricante f = new fabricante();
       f.setVisible(true);
       tipoCilindro.this.dispose();
    }//GEN-LAST:event_mtFabricanteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        TipoCilindroDT dt = new TipoCilindroDT();
        int res;
        try
        {
            if(acceso==1)
            {
               JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
               throw new Exception();
            }
            TipoCilindroDAO dao = new TipoCilindroDAO();
            if(txtCodigo.getText().length()==0 || txtCodigo.getText().equals(" "))
            {
                JOptionPane.showMessageDialog(null,"Ingrese Codigo");
                txtCodigo.requestFocus();
                throw new Exception();
            }else
            {
                if(txtCodigo.getText().length()>=11)
                {
                    JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Codigo (10)");
                    txtCodigo.requestFocus();
                    throw new Exception();
                }else
                {
                    dt.setCodigo(txtCodigo.getText());
                }
            }
            if(txtNombre.getText().length()==0||txtNombre.getText().equals(" "))
            {
                JOptionPane.showMessageDialog(null,"Ingrese Nombre");
                txtNombre.requestFocus();
                throw new Exception();
            }else
            {
                if(txtNombre.getText().length()>=41)
                {
                    JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(40)");
                    txtNombre.requestFocus();
                    throw new Exception();
                }else
                {
                    dt.setNombre(txtNombre.getText());
                }
            }
            res = dao.guardarTipoCilindro(dt);
            if(res>0)
            {
                JOptionPane.showMessageDialog(null,"Tipo Cilindro Guardado");
                actualizarJTable();
                limpiar();
            }else
            {
                JOptionPane.showMessageDialog(null,"No Se Pudo Guardar Tipo Cilindro");
            }
            
        }catch(Exception ex)
        {
           
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
    try
    {
       limpiar();
       JOptionPane.showMessageDialog(null,"Formulario Limpio");
       actualizarJTable();
    }catch(Exception ex)
    {
        
    }
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
     try
     {
         int id;
         int res;
         TipoCilindroDAO dao = new TipoCilindroDAO();
         ResultSet validarEliminar;
         int total = 0;
         if(acceso==1)
            {
               JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
               throw new Exception();
            }
         if(txtId.getText().length()==0 )
         {
             JOptionPane.showMessageDialog(null,"Use Opciones Buscar");
             throw new Exception();
         }else
         {
             id = Integer.parseInt(txtId.getText());
             
             validarEliminar = dao.validarEliminarTipoCilindro(id);
             while(validarEliminar.next())
             {
                 total = validarEliminar.getInt("IdTipoCilindro");
             }
             
             if(total>0)
             {
                 JOptionPane.showMessageDialog(null,"No Puede Eliminar Tipo Cilindro Porque esta asociado a "+total+" Revisiones");
                 throw new Exception();
             }
             confirmarEliminar eliminar = new confirmarEliminar(this,true);
             eliminar.setVisible(true);
           if(confirmarEliminar.aceptar>0)
           {
             
             res = dao.eliminarTipoCilindro(id);
             if(res>0)
             {
                 JOptionPane.showMessageDialog(null,"Tipo Cilindro Eliminado");
                 actualizarJTable();
                 limpiar();
                 confirmarEliminar.aceptar = 0;
             }else
             {
                 JOptionPane.showMessageDialog(null,"No Se Pudo Eliminar Tipo Cilindro");
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

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try
        {
         TipoCilindroDAO dao = new TipoCilindroDAO();
         int id;
         ArrayList<TipoCilindroDT> lista = new ArrayList<TipoCilindroDT>();
            if(txtCodigo.getText().length()==0)
            {
                JOptionPane.showMessageDialog(null,"Ingrese Codigo");
                txtCodigo.requestFocus();
            }else
            {
             lista = dao.buscarTipoCilindroConCodigo(txtCodigo.getText());
             if(lista.isEmpty()==false)
             {
                for (TipoCilindroDT x : lista) {
                    txtNombre.setText(x.getNombre());
                    txtId.setText(x.getId()+"");
                }
                 JOptionPane.showMessageDialog(null,"Tipo Cilindro Encontrado");
             }else
             {
                 JOptionPane.showMessageDialog(null,"No Se Encontro Tipo Cilindro");
                 txtCodigo.setText("");
                 txtCodigo.requestFocus();
             }
            }
        }catch(Exception ex)
        {
            
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
       try
       {
           int id;
           String nombre;
           String codigo;
           TipoCilindroDAO dao = new TipoCilindroDAO();
           TipoCilindroDT dt = new TipoCilindroDT();
           int res;
           if(acceso==1)
            {
               JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
               throw new Exception();
            }
           if(txtId.getText().length()==0)
           {
               JOptionPane.showMessageDialog(null,"Use Opciones De Buscar");
               throw new Exception();
           }else
           {
              id = Integer.parseInt(txtId.getText());
             
              if(txtCodigo.getText().length()==0 || txtCodigo.getText().equals(" "))
             {
                 JOptionPane.showMessageDialog(null,"Ingrese Codigo Para Actualizar");
                 txtCodigo.requestFocus();
                 throw new Exception();
             }else
             {
                if(txtCodigo.getText().length()>=11)
                {
                    JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Codigo(10)");
                    txtCodigo.requestFocus();
                    throw new Exception();
                }else
                {
                    codigo = txtCodigo.getText();
                    dt.setCodigo(codigo);
                }
             }
              
             if(txtNombre.getText().length()==0 || txtNombre.getText().equals(" "))
             {
                 JOptionPane.showMessageDialog(null,"Ingrese Nombre Para Actualizar");
                 txtNombre.requestFocus();
                 throw new Exception();
             }else
             {
                 if(txtNombre.getText().length()>=41)
                 {
                     JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(40)");
                     txtNombre.requestFocus();
                     throw new Exception();
                 }else
                 {
                    nombre = txtNombre.getText();
                    dt.setNombre(nombre);
                 }
             }
              dt.setId(id);
              res = dao.actualizarTipoCilindro(dt);
              if(res>0)
              {
                  JOptionPane.showMessageDialog(null,"Tipo Cilindro Actualizado");
                  actualizarJTable();
                  limpiar();
              }else
              {
                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Tipo Cilindro");
              }
           }
       }catch(Exception ex)
       {
           
       }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void mtRevisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtRevisionActionPerformed
      revision r = new revision();
      r.setVisible(true);
      tipoCilindro.this.dispose();
    }//GEN-LAST:event_mtRevisionActionPerformed

    private void mtMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtMenuActionPerformed
      menu m = new menu();
      m.setVisible(true);
      tipoCilindro.this.dispose();
    }//GEN-LAST:event_mtMenuActionPerformed

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
                    fila.createCell(0).setCellValue("Codigo");
                    fila.createCell(1).setCellValue("Nombre");
                    
                    pbBarraProgreso.setMaximum(tablaTipoCilindro.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;
                    
                    for(int i=0; i<tablaTipoCilindro.getRowCount(); i++)
                    {
                        rect = tablaTipoCilindro.getCellRect(i,0,true);
                        try
                        {
                            tablaTipoCilindro.scrollRectToVisible(rect);
                        }catch(java.lang.ClassCastException e)
                        {
                            
                        }
                        tablaTipoCilindro.setRowSelectionInterval(i, i);
                        pbBarraProgreso.setValue((i+1));
                        
                        filas = hoja.createRow((i+1));
                        filas.createCell(0).setCellValue(tablaTipoCilindro.getValueAt(i,1).toString());
                        filas.createCell(1).setCellValue(tablaTipoCilindro.getValueAt(i,2).toString());
                       
                      
                        
                        try
                        {
                            Thread.sleep(20);
                        }catch(InterruptedException ex)
                        {
                            Logger.getLogger(tipoCilindro.class.getName()).log(Level.SEVERE,null,ex);
                        }
                    }
                    pbBarraProgreso.setValue(0);
                    pbBarraProgreso.setString("Abriendo Excel...");
                    
                    try
                    {
                        workbook.write(new FileOutputStream(new File(ruta+"//ExcelTipoCilindro.xlsx")));
                        Desktop.getDesktop().open(new File(ruta+"//ExcelTipoCilindro.xlsx"));
                        
                    }catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,"Cierre El Excel Para Actualizar!!");
                    }
                }
            };
          t.start();
          
      }catch(Exception ex)
      {
         JOptionPane.showMessageDialog(null, ex.getMessage());
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
            java.util.logging.Logger.getLogger(tipoCilindro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tipoCilindro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tipoCilindro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tipoCilindro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tipoCilindro().setVisible(true);
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mtCliente;
    private javax.swing.JMenuItem mtFabricante;
    private javax.swing.JMenuItem mtLogin;
    private javax.swing.JMenuItem mtMenu;
    private javax.swing.JMenuItem mtNorma;
    private javax.swing.JMenuItem mtRevision;
    private javax.swing.JProgressBar pbBarraProgreso;
    private javax.swing.JTable tablaTipoCilindro;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
