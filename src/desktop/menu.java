/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import ModeloDAO.RevisionDAO;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author Angelo
 */
public class menu extends javax.swing.JFrame  {

    /**
     * Creates new form menu
     */
    JTable tabla;
    JPopupMenu popup = new JPopupMenu(); //Ventana Al hacer click Drecho en una Fila del JTable
    JMenuItem JMItem = new JMenuItem("Abrir Detalle Revision"); //Texto De Esa Ventan
    private final String ruta = System.getProperties().getProperty("user.home");
    public static int idRevision = 0;
    public int acceso = 0;
    
    public menu() {
      try
      {
        
        initComponents();
        acceso = login.accesoLogin;
        setIconImage(new ImageIcon(getClass().getResource("/img/nasca48.jpg")).getImage());
        llenarJTable();
        
        popup.add(JMItem); //Agregar el MenuItem al Popup
        
        //Metodo Para Abrir Formulario Detalle Revision
        
        JMItem.addActionListener((new ActionListener() {
        public void actionPerformed(ActionEvent evt)
        {
          abrir(evt); //Metodo que abre detalle Revision
        }
       }));
       
        tabla.setComponentPopupMenu(popup); //Agregarlo a la Tabla ("DEBE HACERLO DESPUES DE LLENAR LA TABLA"
        ocultarId();
        cerrar();
      }catch(Exception ex)
      {
  
      }
    }
    
    public void ocultarId()
    {
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(1).setMaxWidth(0);
        tabla.getColumnModel().getColumn(1).setMinWidth(0);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(0);
    }
    
    //Metodo Abrir que Abre DetalleRevision
    public void abrir(ActionEvent event)
    {
       try
        {
       //Para Obtener El Id de la fila de la Tabla seleccionada
       int i = tabla.getSelectedRow(); // La Fila Selecciona
       int fila = (int) tabla.getValueAt(i,0); // 
   
  
         idRevision = fila;
        formularioDetalleRevision dt = new formularioDetalleRevision();
        dt.setVisible(true);
        menu.this.dispose();

        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Seleccione Una Fila");
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
    
    public void llenarJTable()
    {
        try
        {
        RevisionDAO dao = new RevisionDAO();
        ResultSet dato;
        String termino = "";
        DefaultTableModel dtm = new DefaultTableModel();
        tabla = this.TRevisionResult;
        tabla.setModel(dtm);
        dtm.setColumnIdentifiers(new Object[] {"Id","Id Cliente","Nombre","Inicio","Termino","Numero","Total"});
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
            dtm.addRow(new Object[]{dato.getInt("id"),dato.getInt("id_cliente"),dato.getString("cliente_nombre"),dato.getString("inicio"),termino,dato.getInt("numero"),dato.getString("total")});
        }
        ocultarId();
        
        dtm.addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e){
                try
                {
                    if(acceso==1)
                      {
                       JOptionPane.showMessageDialog(null,"Sin Privilegios Para Modificar Datos");
                       llenarJTable();
                       throw new Exception();
                      }
                    if(e.getType() == TableModelEvent.UPDATE)
                    {
                       ResultSet dato;
                       int columna = e.getColumn();
                       int fila = e.getFirstRow();
                       if(columna == 2)
                       {
                           JOptionPane.showMessageDialog(null,"No Se Puede Actualizar");
                           llenarJTable();
                       }else if(columna == 3)
                       {
                           JOptionPane.showMessageDialog(null,"No Se Puede Actualizar");
                           llenarJTable();
                       }else if(columna == 4)
                       {
                           JOptionPane.showMessageDialog(null,"No Se Puede Actualizar");
                           llenarJTable();
                       }else if(columna == 5)
                       {
                           JOptionPane.showMessageDialog(null,"No Se Puede Actualizar");
                           llenarJTable();
                       }else if(columna == 6)
                       {
                           JOptionPane.showMessageDialog(null,"No Se Puede Actualizar");
                           llenarJTable();
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
        btnRevision = new javax.swing.JButton();
        JScrollPane = new javax.swing.JScrollPane();
        TRevisionResult = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        pbBarraProgreso = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmtLogin = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jmtCliente = new javax.swing.JMenuItem();
        jmtFabricante = new javax.swing.JMenuItem();
        jmtNorma = new javax.swing.JMenuItem();
        jmtTipoCilindro = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Lista Revisiones");

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        btnRevision.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/formulario.png"))); // NOI18N
        btnRevision.setText("Registrar Revision");
        btnRevision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevisionActionPerformed(evt);
            }
        });

        TRevisionResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TRevisionResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                TRevisionResultMouseReleased(evt);
            }
        });
        JScrollPane.setViewportView(TRevisionResult);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/16x16-excel.png"))); // NOI18N
        jButton1.setText("Generar Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        pbBarraProgreso.setStringPainted(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnRevision)
                        .addGap(18, 18, 18)
                        .addComponent(pbBarraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pbBarraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addComponent(btnRevision, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jmtCliente.setText("Cliente");
        jmtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtClienteActionPerformed(evt);
            }
        });
        jMenu2.add(jmtCliente);

        jmtFabricante.setText("Fabricante");
        jmtFabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmtFabricanteActionPerformed(evt);
            }
        });
        jMenu2.add(jmtFabricante);

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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
  llenarJTable();
    }//GEN-LAST:event_formWindowOpened

    private void btnRevisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevisionActionPerformed
     revision r = new revision();
     r.setVisible(true);
     menu.this.dispose();
    }//GEN-LAST:event_btnRevisionActionPerformed

    private void jmtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtLoginActionPerformed
    cerrarSesion cs = new cerrarSesion(this,true,menu.this);
    cs.setVisible(true);
    }//GEN-LAST:event_jmtLoginActionPerformed

    private void jmtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtClienteActionPerformed
        cliente c = new cliente();
        c.setVisible(true);
        menu.this.dispose();
    }//GEN-LAST:event_jmtClienteActionPerformed

    private void jmtFabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtFabricanteActionPerformed
       fabricante f = new fabricante();
       f.setVisible(true);
       menu.this.dispose();
    }//GEN-LAST:event_jmtFabricanteActionPerformed

    private void jmtNormaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtNormaActionPerformed
        norma n = new norma();
        n.setVisible(true);
        menu.this.dispose();
    }//GEN-LAST:event_jmtNormaActionPerformed

    private void jmtTipoCilindroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmtTipoCilindroActionPerformed
       tipoCilindro tc = new tipoCilindro();
       tc.setVisible(true);
       menu.this.dispose();
    }//GEN-LAST:event_jmtTipoCilindroActionPerformed

    private void TRevisionResultMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TRevisionResultMouseReleased

    }//GEN-LAST:event_TRevisionResultMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
                    fila.createCell(4).setCellValue("Total");
                    
                    pbBarraProgreso.setMaximum(TRevisionResult.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;
                    
                    for(int i=0; i<TRevisionResult.getRowCount(); i++)
                    {
                        rect = TRevisionResult.getCellRect(i,0,true);
                        try
                        {
                            TRevisionResult.scrollRectToVisible(rect);
                        }catch(java.lang.ClassCastException e)
                        {
                            
                        }
                        TRevisionResult.setRowSelectionInterval(i, i);
                        pbBarraProgreso.setValue((i+1));
                        
                        filas = hoja.createRow((i+1));
                        filas.createCell(0).setCellValue(TRevisionResult.getValueAt(i,2).toString());
                        filas.createCell(1).setCellValue(TRevisionResult.getValueAt(i,3).toString());
                        filas.createCell(2).setCellValue(TRevisionResult.getValueAt(i,4).toString());
                        filas.createCell(3).setCellValue(TRevisionResult.getValueAt(i,5).toString());
                        filas.createCell(4).setCellValue(TRevisionResult.getValueAt(i,6).toString());
                        try
                        {
                            Thread.sleep(20);
                        }catch(InterruptedException ex)
                        {
                            Logger.getLogger(menu.class.getName()).log(Level.SEVERE,null,ex);
                        }
                    }
                    pbBarraProgreso.setValue(0);
                    pbBarraProgreso.setString("Abriendo Excel...");
                    
                    try
                    {
                        workbook.write(new FileOutputStream(new File(ruta+"//ExcelMenuRevision.xlsx")));
                        Desktop.getDesktop().open(new File(ruta+"//ExcelMenuRevision.xlsx"));
                        pbBarraProgreso.setValue(0);
                        
                    }catch(Exception ex)
                    {
                        Logger.getLogger(formularioDetalleRevision.class.getName()).log(Level.SEVERE,null,ex);
                    }
                }
            };
          t.start();
          
      }catch(Exception ex)
      {
         
      }
    }//GEN-LAST:event_jButton1ActionPerformed
 
   
    
 
    
    
    
    /**
     * @param args the command line arguments
     */
   
    
    public static void main(String args[]) throws Exception{
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
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JScrollPane;
    private javax.swing.JTable TRevisionResult;
    private javax.swing.JButton btnRevision;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JMenuItem jmtCliente;
    private javax.swing.JMenuItem jmtFabricante;
    private javax.swing.JMenuItem jmtLogin;
    private javax.swing.JMenuItem jmtNorma;
    private javax.swing.JMenuItem jmtTipoCilindro;
    private javax.swing.JProgressBar pbBarraProgreso;
    // End of variables declaration//GEN-END:variables
}
