/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

import ModeloDT.RevisionDT;
import ModeloDT.conexion;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Angelo
 */


public class RevisionDAO {

    Statement instru;
    public RevisionDAO()throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
        
    }
    
    
    public int guardarRevision(RevisionDT revision) throws Exception
    {
        int res;
        String sql;
        try
        {
            if(revision.getFechaTermino()==null)
            {
                  sql = "INSERT INTO revision "+
                         "(id_cliente,inicio,termino,numero) "+
                         "VALUES("+revision.getIdCliente()+",now(),NULL,"+revision.getNumero()+")";
            }else
            {
                  sql = "INSERT INTO revision "+
                         "(id_cliente,inicio,termino,numero) "+
                         "VALUES("+revision.getIdCliente()+",now(),'"+revision.getFechaTermino()+"',"+revision.getNumero()+")";
            }
          
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ArrayList obtenerRevision(int id) throws Exception
    {
        ArrayList<RevisionDT>lista = new ArrayList();
        ResultSet dato;
        try
        {
            String sql = "SELECT * FROM vw_revision "+
                         "WHERE id LIKE "+id+"";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                int idCliente = Integer.parseInt(dato.getString("id_cliente"));
                String nombreCliente = dato.getString("cliente_nombre");
                Date termino = dato.getDate("termino");
                Date incio = dato.getDate("inicio");
                int numero = Integer.parseInt(dato.getString("numero"));
                int idRevision = dato.getInt("id");
                RevisionDT dt = new RevisionDT();
                dt.setIdCliente(idCliente);
                dt.setId(idRevision);
                dt.setNombreCliente(nombreCliente);
                dt.setFechaTermino(termino);
                dt.setFechaInicio(incio);
                dt.setNumero(numero);
                lista.add(dt);
                       
            }
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
    
    public int eliminarRevision(int id) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE revision "+
                         "SET visible = 0 "+
                         "WHERE id LIKE "+id+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int actualizarRevision(RevisionDT revision) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE revision "+
                         "SET termino = '"+revision.getFechaTermino()+"', "+
                              " numero = "+revision.getNumero()+" " +
                         "WHERE id LIKE "+revision.getId()+" ";
            res = instru.executeUpdate(sql);
                                      
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ResultSet imprimirRevision() throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT r.id , id_cliente , cliente_nombre , inicio , termino , r.numero , "+ 
                         "       COUNT(d.id_revision) AS total "+
                         "FROM vw_revision r LEFT OUTER JOIN vw_detalle_revision d "+
                         "ON(r.id = d.id_revision) "+
                         "JOIN cliente c "+
                         "ON(r.id_cliente = c.id) "+
                         "GROUP BY r.id "+
                         "ORDER BY c.nombre , r.inicio ASC";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public ResultSet imprimirUnicaRevision(int id) throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT r.id , id_cliente , cliente_nombre , inicio , termino , r.numero , "+ 
                         "       COUNT(d.id_revision) AS total "+
                         "FROM vw_revision r LEFT OUTER JOIN vw_detalle_revision d "+
                         "ON(r.id = d.id_revision) "+
                         "WHERE r.id LIKE "+id+"";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public ResultSet validarEliminarRevision(int id) throws Exception
    {
        ResultSet dato;
        String sql;
        try
        {
            sql = "SELECT COUNT(id_revision) AS 'IdRevision' "+
                  "FROM revision r LEFT OUTER JOIN detalle_revision rr "+
                  "ON (r.id = rr.id_revision ) "+
                  "WHERE id_revision LIKE "+id+" ";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
}
