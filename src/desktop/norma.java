/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import ModeloDAO.NormaDAO;
import ModeloDT.NormaDT;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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


/**
 *
 * @author Angelo
 */
public class norma extends javax.swing.JFrame {

    /**
     * Creates new form norma
     */
    
   Statement instru;    
   JTable tabla;
   private final String ruta = System.getProperties().getProperty("user.home");
   public int acceso = 0;
   JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
   JMenuItem JMItem = new JMenuItem("Llenar A Formulario"); //Texto De Esa Ventan
   
    public norma() {
        try
        {
         conexion c = new conexion();
         instru = c.getStatement();
         acceso = login.accesoLogin;
         setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
         initComponents();
         txtId.setEnabled(false);
         actualizarTabla();
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
           int idNorma = (int) tabla.getValueAt(i,0); // 
           NormaDAO dao = new NormaDAO();
           ArrayList<NormaDT>lista = dao.obtenerNorma(idNorma);
           txtNorma.setText(lista.get(0).getNorma());
           txtId.setText(lista.get(0).getId()+"");
           JOptionPane.showMessageDialog(null,"Extraccion Realizado Correctamente");

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
        }
    }
    
    public void ocultarId()
    {
        tablaNorma.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaNorma.getColumnModel().getColumn(0).setMinWidth(0);
        tablaNorma.getColumnModel().getColumn(0).setPreferredWidth(0);
      
        tablaNorma.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaNorma.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
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
    
     public void actualizarTabla() throws Exception
    {
        try
        {
            NormaDAO dao = new NormaDAO();
            ResultSet dato;
            DefaultTableModel dtm = new DefaultTableModel();
            tabla = this.tablaNorma;
            tabla.setModel(dtm);
            dtm.setColumnIdentifiers(new Object[]{"Id","Norma"});
            dato = dao.imprimirNorma();
            while(dato.next())
            {
                dtm.addRow(new Object[]{
                                    dato.getInt("id"),
                                    dato.getString("norma")
                                       });
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
                           String normaActual = tabla.getValueAt(fila,columna).toString();
                           int id = (int)tabla.getValueAt(fila,0);
                           String sql = "SELECT norma FROM vw_norma WHERE id LIKE "+id+"";
                           
                           try {
                               dato = instru.executeQuery(sql);
                               String anterior = "";
                               while(dato.next())
                               {
                                  anterior = dato.getString("norma");
                               }
                               int res = dao.actualizarNormaTabla(normaActual,id,anterior);

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Norma Actualizada");
                                   actualizarTabla();
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Norma");
                                   actualizarTabla();
                                   throw new Exception();
                               }
                               actualizarTabla();
                           } catch (Exception ex) {
                           
                               
                               if(ex.getMessage().toString().equals("1406")){
                               JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(40)");
                               }
                               actualizarTabla();
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
            throw new Exception();
        }
    }
     
     public void limpiar()
     {
         txtNorma.setText("");
         pbBarraProgreso.setString("");
         txtId.setText("");
         txtNorma.requestFocus();
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
        txtNorma = new javax.swing.JTextField();
        jScrollPane = new javax.swing.JScrollPane();
        tablaNorma = new javax.swing.JTable();
        pbBarraProgreso = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        miMenu = new javax.swing.JMenuItem();
        miRevision = new javax.swing.JMenuItem();
        mtCliente = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        mtTipoCilindro = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Norma");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Formulario Norma");

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

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Norma:");

        tablaNorma.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane.setViewportView(tablaNorma);

        pbBarraProgreso.setStringPainted(true);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Id:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pbBarraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNorma, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                            .addComponent(txtId))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNorma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExcel)
                .addContainerGap(210, Short.MAX_VALUE))
        );

        jMenu1.setText("Procesos");

        jMenuItem2.setText("Cerrar Sesion");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mantenedores");

        miMenu.setText("Menu");
        miMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMenuActionPerformed(evt);
            }
        });
        jMenu2.add(miMenu);

        miRevision.setText("Revision");
        miRevision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRevisionActionPerformed(evt);
            }
        });
        jMenu2.add(miRevision);

        mtCliente.setText("Cliente");
        mtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtClienteActionPerformed(evt);
            }
        });
        jMenu2.add(mtCliente);

        jMenuItem5.setText("Fabricante");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        mtTipoCilindro.setText("Tipo Cilindro");
        mtTipoCilindro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtTipoCilindroActionPerformed(evt);
            }
        });
        jMenu2.add(mtTipoCilindro);

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
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mtTipoCilindroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtTipoCilindroActionPerformed
       tipoCilindro tipo = new tipoCilindro();
       tipo.setVisible(true);
       norma.this.dispose();
    }//GEN-LAST:event_mtTipoCilindroActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        fabricante f = new fabricante();
        f.setVisible(true);
        norma.this.dispose();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
       try
       {
           NormaDAO dao = new NormaDAO();
           NormaDT dt = new NormaDT();
            if(acceso==1)
            {
              JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
              throw new Exception();
           }
           if(txtNorma.getText().length()==0|| txtNorma.getText().equals(" "))
           {
             JOptionPane.showMessageDialog(null,"Ingrese Norma");
             txtNorma.requestFocus();
             throw new Exception();
           }else
           {
               if(txtNorma.getText().length()>=41)
               {
                   JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Norma(40)");
                   txtNorma.requestFocus();
                   throw new Exception();
               }else
               {
                    dt.setNorma(txtNorma.getText());
               }
           }
           int res = dao.guardarNorma(dt);
           if(res>0)
           {
               JOptionPane.showMessageDialog(null,"Norma Ingresada Correctamente");
               actualizarTabla();
               limpiar();
           }
       }catch(Exception ex)
       {

       }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
    try
    {
        NormaDAO dao = new NormaDAO();
        NormaDT dt = new NormaDT();
        int id;
        int res;
         if(acceso==1)
         {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
         }
        if(txtId.getText().length()==0)
        {
            JOptionPane.showMessageDialog(null,"Busque La Norma Antes De Actualizar");
            throw new Exception();
        }else
        {
            id = Integer.parseInt(txtId.getText());
        if(txtNorma.getText().length()==0 || txtNorma.getText().equals(" "))
        {
            JOptionPane.showMessageDialog(null,"Ingrese Norma");
            txtNorma.requestFocus();
            throw new Exception();
        }else
        {
            if(txtNorma.getText().length()>=41)
            {
                JOptionPane.showMessageDialog(null,"Minimo Caracteres Para Nombre(40)");
                txtNorma.requestFocus();
                throw new Exception();
            }else
            {
                dt.setNorma(txtNorma.getText());
            }
        }

            dt.setId(id);
            res = dao.actualizarNorma(dt);
            if(res>0)
            {
             JOptionPane.showMessageDialog(null,"Norma Actualizada");
             actualizarTabla();
             limpiar();
            }else{
              JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Norma");
            }
            
        }
    }catch(Exception ex)
    {
    }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
       try
       {
           NormaDAO dao = new NormaDAO();
           NormaDT dt = new NormaDT();
           ResultSet validarTotal;
           int total = 0;
           int id;
           int res;
           if(acceso==1)
         {
            JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
            throw new Exception();
         }
           if(txtId.getText().length()==0)
           {
               JOptionPane.showMessageDialog(null,"Use Buscar Antes De Eliminar Norma");
               throw new Exception();
           }else
           {
               id = Integer.parseInt(txtId.getText());
               
               validarTotal = dao.validarEliminarNorma(id);
               
               while(validarTotal.next())
               {
                   total = validarTotal.getInt("IdNorma");
               }
               
               if(total>0)
               {
                   JOptionPane.showMessageDialog(null,"No Puede Eliminar Esta Norma Porque Esta Asociada a "+total+" Revisiones");
                   throw new Exception();
               }
               
               confirmarEliminar eliminar = new confirmarEliminar(this,true);
               eliminar.setVisible(true);
             if(confirmarEliminar.aceptar>0)
             {
                dt.setId(id);
                res = dao.eliminarNorma(dt);
                if(res==0)
                {
                   JOptionPane.showMessageDialog(null,"Norma No Eliminada");
                   desktop.confirmarEliminar.aceptar = 0;
                }else
                {
                   JOptionPane.showMessageDialog(null,"Norma Eliminada");
                   actualizarTabla();
                   limpiar();
                   desktop.confirmarEliminar.aceptar = 0;
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

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    cerrarSesion cs = new cerrarSesion(this,true,norma.this);
    cs.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void mtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtClienteActionPerformed
      cliente c = new cliente();
      c.setVisible(true);
      norma.this.dispose();
    }//GEN-LAST:event_mtClienteActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
     try
     {limpiar();
     actualizarTabla();
     JOptionPane.showMessageDialog(null,"Formulario Limpio");
     }catch(Exception ex)
     {
         
     }
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void miMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMenuActionPerformed
       menu m = new menu();
       m.setVisible(true);
       norma.this.dispose();
    }//GEN-LAST:event_miMenuActionPerformed

    private void miRevisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRevisionActionPerformed
       revision r = new revision();
       r.setVisible(true);
       norma.this.dispose();
    }//GEN-LAST:event_miRevisionActionPerformed

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
                   fila.createCell(0).setCellValue("Norma");
                   
                   pbBarraProgreso.setMaximum(tablaNorma.getRowCount());
                   XSSFRow filas;
                   Rectangle rect;
                   
                   for(int i=0; i<tablaNorma.getRowCount(); i++)
                   {
                       rect = tablaNorma.getCellRect(i,0,true);
                       try
                       {
                           tablaNorma.scrollRectToVisible(rect);
                       }catch(java.lang.ClassCastException e)
                       {
                           
                       }
                       tablaNorma.setRowSelectionInterval(i, i);
                       pbBarraProgreso.setValue((i+1));
                       
                       filas = hoja.createRow((i+1));
                       filas.createCell(0).setCellValue(tablaNorma.getValueAt(i,1).toString());
                       
                     try
                       {
                           Thread.sleep(20);
                       }catch(InterruptedException ex)
                       {
                           Logger.getLogger(formularioDetalleRevision.class.getName()).log(Level.SEVERE,null,ex);
                       }
                   }
                   pbBarraProgreso.setValue(0);
                   pbBarraProgreso.setString("Abriendo Excel...");
                   
                   try
                   {
                     workbook.write(new FileOutputStream(new File(ruta+"//ExcelNorma.xlsx")));
                     Desktop.getDesktop().open(new File(ruta+"//ExcelNorma.xlsx"));
                     
                   }catch(Exception ex)
                   {
                     JOptionPane.showMessageDialog(null,"Cierre Excel Para Actualizar!!");
                   }
               }
          };
          t.start();
          
      }catch(Exception ex)
      {
          JOptionPane.showMessageDialog(null,ex);
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
            java.util.logging.Logger.getLogger(norma.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(norma.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(norma.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(norma.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run()  {
              try
              {
                new norma().setVisible(true);
              }catch(Exception ex)
              {
                  
              }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JMenuItem miMenu;
    private javax.swing.JMenuItem miRevision;
    private javax.swing.JMenuItem mtCliente;
    private javax.swing.JMenuItem mtTipoCilindro;
    private javax.swing.JProgressBar pbBarraProgreso;
    private javax.swing.JTable tablaNorma;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNorma;
    // End of variables declaration//GEN-END:variables
}
