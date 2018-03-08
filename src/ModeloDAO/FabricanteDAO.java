/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

import ModeloDT.FabricanteDT;
import ModeloDT.conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Angelo
 */
public class FabricanteDAO {
    
    Statement instru;
    public FabricanteDAO() throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
    }
    
    public int guardarFabricante(FabricanteDT fabricante) throws Exception
    {
        int res;
        try
        {
            String sql = "INSERT INTO fabricante "+
                         "(codigo,nombre) "+
                         "VALUES('"+fabricante.getCodigo()+"','"+fabricante.getNombre()+"')";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ArrayList buscarFarbicante(int id) throws Exception
    {
        ArrayList<FabricanteDT>lista = new ArrayList<>();
        ResultSet dato;
        try
        {
            String sql = "SELECT * "+
                         "FROM vw_fabricante "+
                         "WHERE id LIKE "+id+"";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                String codigo = dato.getString("codigo");
                String nombre = dato.getString("nombre");
                FabricanteDT dt = new FabricanteDT();
                dt.setCodigo(codigo);
                dt.setNombre(nombre);
                dt.setId(id);
                lista.add(dt);
            }
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
    
    public ArrayList buscarFarbicantePorCodigo(String codigo) throws Exception
    {
        ArrayList<FabricanteDT>lista = new ArrayList<>();
        ResultSet dato;
        try
        {
            String sql = "SELECT * "+
                         "FROM vw_fabricante "+
                         "WHERE codigo LIKE '"+codigo+"'";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                String codigoFabricante = dato.getString("codigo");
                String nombre = dato.getString("nombre");
                int id = dato.getInt("id");
                FabricanteDT dt = new FabricanteDT();
                dt.setCodigo(codigoFabricante);
                dt.setNombre(nombre);
                dt.setId(id);
                
                lista.add(dt);
            }
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
    
    public int eliminarFabricante(int id) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE fabricante "+
                        "SET visible = 0 "+
                         "WHERE id LIKE "+id+"";
            res = instru.executeUpdate(sql);
        }catch(Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int actualizarFabricante(FabricanteDT fabricante) throws Exception
    {
        int res;
        try
        {
             String sql ="UPDATE fabricante " +
                    "SET codigo = '"+fabricante.getCodigo()+"',"+
                     "   nombre = '"+fabricante.getNombre()+"' "+ 
                    "WHERE id like "+fabricante.getId()+"";
             res = instru.executeUpdate(sql);
        }catch(Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ResultSet imprimirFabricante() throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT * FROM vw_fabricante ORDER BY codigo";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public int actualizarCodigoFabricante(int id , String codigoActual ,String codigoAnterior) throws Exception
    {
        String sql;
        int res;
        try
        {
            sql = "UPDATE fabricante "+
                  "SET codigo = '"+codigoActual+"' " +
                  "WHERE id LIKE "+id+" AND codigo LIKE '"+codigoAnterior+"'";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public int actualizarNormbreFabricante(int id , String nombreActual , String nombreAnterior) throws Exception
    {
        String sql;
        int res;
        try
        {
            sql = "UPDATE fabricante "+
                  "SET nombre = '"+nombreActual+"' "+
                  "WHERE id LIKE "+id+" AND nombre = '"+nombreAnterior+"'";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public ResultSet validarEliminarFabricante(int id) throws Exception
    {
        ResultSet dato;
        String sql;
        try
        {
            sql = "SELECT COUNT(id_fabricante) AS 'IdFabricante' "+
                  "FROM fabricante f LEFT OUTER JOIN detalle_revision r "+
                  "ON (f.id = r.id_fabricante ) "+
                  "WHERE id_fabricante LIKE "+id+" ";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
}
