/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import ModeloDAO.FabricanteDAO;
import ModeloDT.FabricanteDT;
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
public class fabricante extends javax.swing.JFrame {

    /**
     * Creates new form fabricante
     */
    
    Statement instru;
    JTable tabla;
    public int acceso = 0;
    private final String ruta = System.getProperties().getProperty("user.home");
    JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
    JMenuItem JMItem = new JMenuItem("Llenar A Formulario"); //Texto De Esa Ventan
    
    public fabricante() {
       try
       {
        initComponents();
        conexion c = new conexion();
        instru = c.getStatement();
        txtId.setEnabled(false);
        acceso = login.accesoLogin;
        setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
        actualizarJTable();
        popup.add(JMItem); //Agregar el MenuItem al Popup
        JMItem.addActionListener((new ActionListener() {
        public void actionPerformed(ActionEvent evt)
        {
          llenarFormulario();
        }
       }));
       
        tabla.setComponentPopupMenu(popup);
        ocultarId();
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
           int idFabricante = (int) tabla.getValueAt(i,0); // 
           FabricanteDAO dao = new FabricanteDAO();
           ArrayList<FabricanteDT>lista = dao.buscarFarbicante(idFabricante);
           txtNombre.setText(lista.get(0).getNombre());
           txtCodigo.setText(lista.get(0).getCodigo());
           txtId.setText(lista.get(0).getId()+"");
           JOptionPane.showMessageDialog(null,"Extraccion Realizado Correctamente");

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
        }
    }
    
    public void ocultarId()
    {
        tablaFabricante.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaFabricante.getColumnModel().getColumn(0).setMinWidth(0);
        tablaFabricante.getColumnModel().getColumn(0).setPreferredWidth(0);
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
            FabricanteDAO dao = new FabricanteDAO();
            ResultSet dato;
            DefaultTableModel dtm = new DefaultTableModel();
            tabla = this.tablaFabricante;
            tabla.setModel(dtm);
            dtm.setColumnIdentifiers(new Object[]{"Id","Codigo","Nombre"});
            dato = dao.imprimirFabricante();
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
                       actualizarJTable();
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
                           String sql = "SELECT codigo FROM vw_fabricante WHERE id LIKE "+id+"";
                           
                           try {
                               dato = instru.executeQuery(sql);
                               String codigoAnterior = "";
                               while(dato.next())
                               {
                                  codigoAnterior = dato.getString("codigo");
                               }
                               int res = dao.actualizarCodigoFabricante(id,codigoActual,codigoAnterior);

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Codigo Actualizado");
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Codigo");
                               }
                               actualizarJTable();
                           } catch (Exception ex) {
                           
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Codigo(10)");
                               }
                               actualizarJTable();
                           }
                       }else if(columna==2)
                       {
                           String nombreActual = tabla.getValueAt(fila,columna).toString();
                           int id = (int)tabla.getValueAt(fila,0);
                           String sql = "SELECT nombre FROM vw_fabricante WHERE id LIKE "+id+"";
                           
                           try {
                               dato = instru.executeQuery(sql);
                               String nombreAnterior = "";
                               while(dato.next())
                               {
                                  nombreAnterior = dato.getString("nombre");
                               }
                               int res = dao.actualizarNormbreFabricante(id, nombreActual, nombreAnterior);

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Nombre Actualizado");
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Nombre");
                               }
                               actualizarJTable();
                           } catch (Exception ex) {
                           
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(40)");
                               }
                               actualizarJTable();
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
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaFabricante = new javax.swing.JTable();
        pbBarraProgreso = new javax.swing.JProgressBar();
        btnBuscar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmtLogin = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mtMenu = new javax.swing.JMenuItem();
        mtRevision = new javax.swing.JMenuItem();
        jmtCliente = new javax.swing.JMenuItem();
        jmtNorma = new javax.swing.JMenuItem();
        jmtTipoCilindro = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fabricante");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Formulario Fabricante:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(329, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Codigo:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Nombre");

        tablaFabricante.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaFabricante);

        pbBarraProgreso.setStringPainted(true);

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Id:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbBarraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombre)
                                    .addComponent(txtId))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar)
                        .addGap(0, 77, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbBarraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jmtCliente.setText("Cliente");
        jmtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtClienteActionPerformed(evt);
            }
        });
        jMenu2.add(jmtCliente);

        jmtNorma.setText("Norma");
        jmtNorma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtNormaActionPerformed(evt);
            }
        });
        jMenu2.add(jmtNorma);

        jmtTipoCilindro.setText("Tipo Cilindro");
        jmtTipoCilindro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtTipoCilindroActionPerformed(evt);
            }
        });
        jMenu2.add(jmtTipoCilindro);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtLoginActionPerformed
        cerrarSesion cs = new cerrarSesion(this,true,fabricante.this);
        cs.setVisible(true);
    }//GEN-LAST:event_jmtLoginActionPerformed

    private void jmtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtClienteActionPerformed
       cliente c = new cliente();
       c.setVisible(true);
       fabricante.this.dispose();
    }//GEN-LAST:event_jmtClienteActionPerformed

    private void jmtNormaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtNormaActionPerformed
        norma n = new norma();
        n.setVisible(true);
        fabricante.this.dispose();
    }//GEN-LAST:event_jmtNormaActionPerformed

    private void jmtTipoCilindroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtTipoCilindroActionPerformed
      tipoCilindro t = new tipoCilindro();
      t.setVisible(true);
      fabricante.this.dispose();
    }//GEN-LAST:event_jmtTipoCilindroActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
      try
      {
          if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
          FabricanteDAO dao = new FabricanteDAO();
          FabricanteDT dt = new FabricanteDT();
          int res;
          if(txtCodigo.getText().length()==0 || txtCodigo.getText().equals(" "))
          {
              JOptionPane.showMessageDialog(null,"Ingrese Codigo");
              txtCodigo.requestFocus();
              throw new Exception();
          }else
          {
              if(txtCodigo.getText().length()>=11)
              {
                  JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Codigo(10)");
                  txtCodigo.requestFocus();
                  throw new Exception();
              }else{
                dt.setCodigo(txtCodigo.getText());
              }
          }
          if(txtNombre.getText().length()==0 || txtNombre.getText().equals(" "))
          {
              JOptionPane.showMessageDialog(null,"Ingrese Nombre");
              txtNombre.requestFocus();
              throw new Exception();
          }else
          { 
              if(txtNombre.getText().length()>=41)
              {
                  JOptionPane.showMessageDialog(null,"Minimo Caracteres Para NOmbre(40)");
                  txtNombre.requestFocus();
                  throw new Exception();
              }else
              {
                    dt.setNombre(txtNombre.getText());
              }
          }
          res = dao.guardarFabricante(dt);
          if(res>0)
          {
              JOptionPane.showMessageDialog(null,"Fabricante Guardado");
              actualizarJTable();
              limpiar();

          }else
          {
              JOptionPane.showMessageDialog(null,"Problemas Al Guardar Fabricante");
          }
      }catch(Exception ex)
      {
         
      }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try
        {
            FabricanteDAO dao = new FabricanteDAO();
            int id = 0;
            String codigo="";
            String nombre="";
            ArrayList<FabricanteDT> lista = new ArrayList<FabricanteDT>();
            if(txtCodigo.getText().length()==0 || txtCodigo.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Ingrese Codigo");
                txtCodigo.requestFocus();
                throw new Exception();
            }
            lista = dao.buscarFarbicantePorCodigo(txtCodigo.getText());
            if(lista.isEmpty()==false)
            {
                for(int i=0; i<lista.size(); i++)
                {
                    id = lista.get(0).getId();
                    nombre = lista.get(i).getNombre();
                }
                txtNombre.setText(nombre);
                txtId.setText(id+"");
                JOptionPane.showMessageDialog(null,"Fabricante Encontrado");
            }else
            {
                JOptionPane.showMessageDialog(null,"Fabricante No Encontrado");
                txtCodigo.setText("");
                txtCodigo.requestFocus();
            }
        }catch(Exception ex)
         {  
            
         } 
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
       try{
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
           if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
           FabricanteDAO dao = new FabricanteDAO();
           int id;
           int res;
           int total = 0;
           ResultSet validar;
           if(txtId.getText().length()==0)
           {
               JOptionPane.showMessageDialog(null,"Busque Fabricante");
               throw new Exception();
           }else
           {
               id = Integer.parseInt(txtId.getText());
               validar = dao.validarEliminarFabricante(id);
               
               while(validar.next())
               {
                   total = validar.getInt("IdFabricante");
               }
               
               if(total>0)
               {
                   JOptionPane.showMessageDialog(null,"No Puede Eliminar Este Fabricante Porque Esta Asociado a "+total+" Detalle Revisiones");
                   throw new Exception();
               }
               
               confirmarEliminar eliminar = new confirmarEliminar(this,true);
               eliminar.setVisible(true);
             if(confirmarEliminar.aceptar>0)
             {
               res = dao.eliminarFabricante(id);
               if(res>0)
               {
                   actualizarJTable();
                   limpiar();
                   JOptionPane.showMessageDialog(null,"Fabricante Eliminado");
                   confirmarEliminar.aceptar =0;
               }else
               {
                   JOptionPane.showMessageDialog(null,"No Se Pudo Eliminar Fabricante");
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

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
     try
     {
         if(acceso==1)
        {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
        }
         FabricanteDAO dao = new FabricanteDAO();
         FabricanteDT dt = new FabricanteDT();
         int id;
         int res;
         String codigo;
         String nombre;
         if(txtId.getText().length()==0)
         {
             JOptionPane.showMessageDialog(null,"Busque Fabricante");
             throw new Exception();
         }else
         {
             id = Integer.parseInt(txtId.getText());
             dt.setId(id);
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
                 }else{
                    codigo = txtCodigo.getText(); 
                 }
                 dt.setCodigo(codigo);
             }
             
             if(txtNombre.getText().length()==0||txtNombre.getText().equals(" "))
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
                 }else{
                    nombre = txtNombre.getText();
                    dt.setNombre(nombre);
                 }
             }
             res = dao.actualizarFabricante(dt);
             if(res>0)
             {
                 actualizarJTable();
                 limpiar();
                 JOptionPane.showMessageDialog(null,"Fabricante Actualizado");
                 throw new Exception();
             }else
             {
                 JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Fabricante");
                 throw new Exception();
             }
             
         }
     }catch(Exception ex)
     {
        
     }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void mtRevisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtRevisionActionPerformed
        revision r = new revision();
        r.setVisible(true);
        fabricante.this.dispose();
    }//GEN-LAST:event_mtRevisionActionPerformed

    private void mtMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtMenuActionPerformed
         menu m = new menu();
         m.setVisible(true);
         fabricante.this.dispose();
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
                    
                    pbBarraProgreso.setMaximum(tablaFabricante.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;
                    
                    for(int i=0; i<tablaFabricante.getRowCount(); i++)
                    {
                        rect = tablaFabricante.getCellRect(i,0,true);
                        try
                        {
                            tablaFabricante.scrollRectToVisible(rect);
                        }catch(java.lang.ClassCastException e)
                        {
                            
                        }
                        tablaFabricante.setRowSelectionInterval(i, i);
                        pbBarraProgreso.setValue((i+1));
                        
                        filas = hoja.createRow((i+1));
                        filas.createCell(0).setCellValue(tablaFabricante.getValueAt(i,1).toString());
                        filas.createCell(1).setCellValue(tablaFabricante.getValueAt(i,2).toString());
                        
                        
                        try
                        {
                            Thread.sleep(20);
                        }catch(InterruptedException ex)
                        {
                            Logger.getLogger(fabricante.class.getName()).log(Level.SEVERE,null,ex);
                        }
                    }
                    pbBarraProgreso.setValue(0);
                    pbBarraProgreso.setString("Abriendo Excel...");
                    
                    try
                    {
                        workbook.write(new FileOutputStream(new File(ruta+"//ExcelFabricante.xlsx")));
                        Desktop.getDesktop().open(new File(ruta+"//ExcelFabricante.xlsx"));
                        
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
            java.util.logging.Logger.getLogger(fabricante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fabricante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fabricante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fabricante.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fabricante().setVisible(true);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem jmtCliente;
    private javax.swing.JMenuItem jmtLogin;
    private javax.swing.JMenuItem jmtNorma;
    private javax.swing.JMenuItem jmtTipoCilindro;
    private javax.swing.JMenuItem mtMenu;
    private javax.swing.JMenuItem mtRevision;
    private javax.swing.JProgressBar pbBarraProgreso;
    private javax.swing.JTable tablaFabricante;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
