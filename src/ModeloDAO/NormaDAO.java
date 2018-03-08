/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

import ModeloDT.NormaDT;
import ModeloDT.conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Angelo
 */
public class NormaDAO {
    Statement instru;
    public NormaDAO() throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
    }
    
    public int guardarNorma(NormaDT norma) throws Exception
    {
        int res = 0;
     try
     {
         String sql = "INSERT INTO norma "+
                      "(norma) "+
                      "VALUES('"+norma.getNorma()+"')";
         res = instru.executeUpdate(sql);
     }catch(SQLException ex)
     {
         throw new Exception(ex.getErrorCode()+"");
     }
     return res;
    }
    
    public ArrayList obtenerNorma(int id) throws Exception
    {
        ArrayList<NormaDT>lista = new ArrayList<>();
        ResultSet dato;
        try
        {
            String sql = "SELECT * "+
                    "FROM vw_norma "+
                    "WHERE id LIKE "+id+" ";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                int idNorma = Integer.parseInt(dato.getString("id"));
                String norma = dato.getString("norma");
                NormaDT dt = new NormaDT();
                dt.setId(idNorma);
                dt.setNorma(norma);
                lista.add(dt);
            }
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
            return lista; 
    }
    
    
    public int actualizarNorma(NormaDT norma) throws Exception
    {
        int res;
        try
        {
            String sql ="UPDATE norma " +
                    "SET norma = '"+norma.getNorma()+"'"+
                    "WHERE id like "+norma.getId()+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public int eliminarNorma(NormaDT norma) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE norma "+
                         "SET visible = 0 "+
                         "WHERE id LIKE "+norma.getId()+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
 
    public ResultSet imprimirNorma()throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT * FROM vw_norma ORDER BY norma";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public int actualizarNormaTabla(String normaActual , int id,String normaAnterior) throws Exception
    {
        int res;
        try
        {
           
           String sqlActualizar = "UPDATE norma SET norma ='"+normaActual+"' "+
                                  " WHERE id LIKE "+id+" "+
                                  "AND norma LIKE '"+normaAnterior+"'";
           res = instru.executeUpdate(sqlActualizar);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public ResultSet validarEliminarNorma(int id) throws Exception
    {
        ResultSet dato;
        String sql;
        try
        {
            sql = "SELECT COUNT(id_norma) AS 'IdNorma' "+
                  "FROM norma n LEFT OUTER JOIN detalle_revision r "+
                  "ON (n.id = r.id_norma ) "+
                  "WHERE id_norma LIKE "+id+" ";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
}
