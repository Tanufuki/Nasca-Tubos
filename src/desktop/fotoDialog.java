/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import ModeloDAO.FotoDAO;
import ModeloDT.FotoDT;
import ModeloDT.conexion;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import desktop.login;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import desktop.confirmarEliminar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
/**
 *
 * @author Angelo
 */
public class fotoDialog extends javax.swing.JDialog {

    /**
     * Creates new form NewJDialog
     */
    
    JTable tabla;
    Statement instru;
    public int acceso = 0;
    private final String ruta = System.getProperties().getProperty("user.home");
    JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
    JMenuItem JMItem = new JMenuItem("Llenar A Formulario"); //Texto De Esa Ventan
    
    
    public fotoDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
      try
      {
        initComponents();
        conexion c = new conexion();
        instru = c.getStatement();
        txtId.setEnabled(false);
        acceso = login.accesoLogin;
        setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
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
        //cerrar();
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
           int idFoto = (int) tabla.getValueAt(i,0); // 
           FotoDAO dao = new FotoDAO();
           ArrayList<FotoDT>lista = dao.buscarFoto(idFoto);
           txtDir1.setText(lista.get(0).getDir1()+"");
           txtDir2.setText(lista.get(0).getDir2()+"");
           txtId.setText(lista.get(0).getId()+"");
           JOptionPane.showMessageDialog(null,"Extraccion Realizado Correctamente");

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
        }
    }
    
    public void ocultarId()
    {
        tablaFoto.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaFoto.getColumnModel().getColumn(0).setMinWidth(0);
        tablaFoto.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void actualizarTabla() throws Exception
    {
        try
        {
            FotoDAO dao = new FotoDAO();
            ResultSet dato;
            DefaultTableModel dtm = new DefaultTableModel();
            tabla = this.tablaFoto;
            tabla.setModel(dtm);
            dtm.setColumnIdentifiers(new Object[]{"Id","Dir 1","Dir 2","Ruta"});
            dato = dao.imprimirFoto();
            while(dato.next())
            {
                dtm.addRow(new Object[]{
                                    dato.getInt("id"),
                                    dato.getInt("dir1"),
                                    dato.getInt("dir2"),
                                    dato.getString("ruta")
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
                           int dir1Actual = Integer.parseInt(tabla.getValueAt(fila,columna).toString());
                           int idFoto = (int)tabla.getValueAt(fila,0);
                           String sql = "SELECT dir1 FROM vw_foto WHERE id LIKE "+idFoto+"";
                           
                           try {
                               dato = instru.executeQuery(sql);
                               int dir1anterior = 0;
                               int res = 0;
                               while(dato.next())
                               {
                                  dir1anterior = dato.getInt("dir1");
                               }
                               if(dir1Actual>=1000 || dir1Actual<=0)
                               {
                                   JOptionPane.showMessageDialog(null,"Dir1 Ingresado No Valido(1-999)");
                                   actualizarTabla();
                                   throw new Exception();
                               }else
                               {
                                    res = dao.actualizarDir1(idFoto,dir1Actual, dir1anterior);
                               }

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Dir 1 Actualizado");
                                   
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Dir1");
                               }
                               actualizarTabla();
                           } catch (Exception ex) {
                           
                               
                               actualizarTabla();
                           }
                       }else if(columna==2)
                       {
                           int dir2Actual = Integer.parseInt(tabla.getValueAt(fila,columna).toString());
                           int idFoto = (int)tabla.getValueAt(fila,0);
                           String sql = "SELECT dir2 FROM vw_foto WHERE id LIKE "+idFoto+"";
                           try
                           {
                               dato = instru.executeQuery(sql);
                               int dir2Anterior = 0;
                               int res = 0;
                               while(dato.next())
                               {
                                  dir2Anterior = dato.getInt("dir2");
                               }
                               if(dir2Actual>=1000 || dir2Actual<=0)
                               {
                                   JOptionPane.showMessageDialog(null,"Dir2 Ingresado No Valido(1-999)");
                                   actualizarTabla();
                                   throw new Exception();
                               }else
                               {
                                    res = dao.actualizarDir2(idFoto,dir2Actual, dir2Anterior);
                               }

                               if(res>0)
                               {
                                   JOptionPane.showMessageDialog(null,"Dir 2 Actualizado");
                               }else
                               {
                                   JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Dir2");
                               }
                               actualizarTabla();
                           }catch(Exception ex)
                           {
                               actualizarTabla();
                           }
                       
                       }else if(columna==3)
                       {
                           try
                           {
                               JOptionPane.showMessageDialog(null,"No Puede Modificar La Ruta");
                               actualizarTabla();
                           }catch(Exception ex)
                           {
                               
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
    
    public void limpiar()
    {
        txtDir1.setText("");
        txtDir2.setText("");
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

        jPanel3 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnGenerarExcel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDir1 = new javax.swing.JTextField();
        txtDir2 = new javax.swing.JTextField();
        scrollPanel = new javax.swing.JScrollPane();
        tablaFoto = new javax.swing.JTable();
        pbBarraProgreso = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Foto");

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

        btnGenerarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/16x16-excel.png"))); // NOI18N
        btnGenerarExcel.setText("Generar Excel");
        btnGenerarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGenerarExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addGap(11, 11, 11)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGenerarExcel)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Formulario Foto");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(549, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Dir 1:");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Dir 2:");

        txtDir1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDir1KeyTyped(evt);
            }
        });

        txtDir2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDir2KeyTyped(evt);
            }
        });

        tablaFoto.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollPanel.setViewportView(tablaFoto);

        pbBarraProgreso.setStringPainted(true);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Id:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                    .addComponent(pbBarraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDir1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDir2)
                                    .addComponent(txtId))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDir2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(pbBarraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try
        {
            FotoDT dt = new FotoDT();
            FotoDAO dao = new FotoDAO();
            int dir1 = 0;
            int dir2 = 0;
            int res = 0;
            if(acceso==1)
            {
                JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
                throw new Exception();
            }

            if(txtDir1.getText().length()==0 || txtDir1.getText().length()>=4)
            {
                JOptionPane.showMessageDialog(null,"Ingrese Directorio 1 (1-999)");
                txtDir1.requestFocus();
                throw new Exception();
            }else
            {
                dir1 = Integer.parseInt(txtDir1.getText());
                dt.setDir1(dir1);
            }

            if(txtDir2.getText().length()==0 || txtDir2.getText().length()>=4)
            {
                JOptionPane.showMessageDialog(null,"Ingrese Directorio 2 (1-999)");
                txtDir2.requestFocus();
                throw new Exception();
            }else
            {
                dir2 = Integer.parseInt(txtDir2.getText());
                dt.setDir2(dir2);
            }

            res = dao.GuardarFoto(dt);
            if(res>0)
            {
                JOptionPane.showMessageDialog(null,"Directorio Registrado");
                actualizarTabla();
                limpiar();
            }else
            {
                JOptionPane.showMessageDialog(null,"No Se Pudo Registrar Directorio");
            }
        }catch(Exception ex)
        {
            
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        try
        {
            FotoDAO dao = new FotoDAO();
            FotoDT  dt = new FotoDT();
            int id = 0;
            int res = 0;
            ResultSet validar;
            int total = 0;
            if(txtId.getText().length()==0)
            {
                JOptionPane.showMessageDialog(null,"Busque Foto Antes De Eliminar");
                throw new Exception();
            }else
            {
                id = Integer.parseInt(txtId.getText());
            }
            
                validar = dao.validarEliminarFoto(id);
                while(validar.next())
                {
                    total = validar.getInt("IdFoto");
                }
                
                if(total>0)
                {
                    JOptionPane.showMessageDialog(null,"No Puede Eliminar Esta Foto Porque Esta Asociado a "+total+"Detalle Revisones");
                    throw new Exception();
                }   
                
                res = dao.EliminarFoto(id);

                if(res>0)
                {
                    JOptionPane.showMessageDialog(null,"Foto Eliminada");
                    actualizarTabla();
                    limpiar();
                    confirmarEliminar.aceptar=0;
                }else
                {
                    JOptionPane.showMessageDialog(null,"No Se Pudo Eliminar Foto");
                }
        }catch(Exception ex)
        {

        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try
        {
            FotoDAO dao = new FotoDAO();
            FotoDT dt = new FotoDT();
            int res = 0;
            int dir1 = 0;
            int dir2 = 0;
            int id = 0;

            if(txtId.getText().length()==0)
            {
                JOptionPane.showMessageDialog(null,"Busque Foto Antes De Actualizar");
                
                throw new Exception();
            }else
            {
                id = Integer.parseInt(txtId.getText());
                dt.setId(id);
            }

            if(txtDir1.getText().length()==0 || txtDir1.getText().length()>=4)
            {
                JOptionPane.showMessageDialog(null,"Ingrese Dir 1 Para Actualizar (1-999)");
                txtDir1.requestFocus();
                throw new Exception();
            }else
            {
                dir1 = Integer.parseInt(txtDir1.getText());
                dt.setDir1(dir1);
            }

            if(txtDir2.getText().length()==0 || txtDir2.getText().length()>=4)
            {
                JOptionPane.showMessageDialog(null,"Ingrese Dir2 Para Actualizar (1-999)");
                txtDir2.requestFocus();
                throw new Exception();
            }else
            {
                dir2 = Integer.parseInt(txtDir2.getText());
                dt.setDir2(dir2);
            }

            res = dao.ActualizarFoto(dt);

            if(res>0)
            {
                JOptionPane.showMessageDialog(null,"Foto Actualizada");
                actualizarTabla();
                limpiar();
            }else
            {
                JOptionPane.showMessageDialog(null,"No Se Pudo Actualizar Foto");
            }
        }catch(Exception ex)
        {

        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
    try
    {   limpiar();
        JOptionPane.showMessageDialog(null,"Formulario Limpio");
        actualizarTabla();
    }catch(Exception ex)
    {
        
    }
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGenerarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarExcelActionPerformed
        try
        {
            Thread t = new Thread()
            {
                public void run()
                {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet hoja = workbook.createSheet();

                    XSSFRow fila = hoja.createRow(0);
                    fila.createCell(0).setCellValue("Dir1");
                    fila.createCell(1).setCellValue("Dir2");
                    fila.createCell(2).setCellValue("Ruta");

                    pbBarraProgreso.setMaximum(tablaFoto.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;

                    for(int i=0; i<tablaFoto.getRowCount(); i++)
                    {
                        rect = tablaFoto.getCellRect(i,0,true);
                        try
                        {
                            tablaFoto.scrollRectToVisible(rect);
                        }catch(java.lang.ClassCastException e)
                        {

                        }
                        tablaFoto.setRowSelectionInterval(i, i);
                        pbBarraProgreso.setValue((i+1));

                        filas = hoja.createRow((i+1));
                        filas.createCell(0).setCellValue(tablaFoto.getValueAt(i,1).toString());
                        filas.createCell(1).setCellValue(tablaFoto.getValueAt(i,2).toString());
                        filas.createCell(2).setCellValue(tablaFoto.getValueAt(i,3).toString());

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
                        workbook.write(new FileOutputStream(new File(ruta+"//ExcelFoto.xlsx")));
                        Desktop.getDesktop().open(new File(ruta+"//ExcelFoto.xlsx"));

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
    }//GEN-LAST:event_btnGenerarExcelActionPerformed

    private void txtDir1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDir1KeyTyped
        char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtDir1KeyTyped

    private void txtDir2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDir2KeyTyped
        char c = evt.getKeyChar();
        if(c<'0'||c>'9') evt.consume();
    }//GEN-LAST:event_txtDir2KeyTyped

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
            java.util.logging.Logger.getLogger(fotoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fotoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fotoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fotoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                fotoDialog dialog = new fotoDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGenerarExcel;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JProgressBar pbBarraProgreso;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JTable tablaFoto;
    private javax.swing.JTextField txtDir1;
    private javax.swing.JTextField txtDir2;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
