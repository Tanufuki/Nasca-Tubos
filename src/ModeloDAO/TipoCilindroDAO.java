/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

import ModeloDT.TipoCilindroDT;
import ModeloDT.conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Angelo
 */
public class TipoCilindroDAO {
    
    Statement instru;
    
    public TipoCilindroDAO() throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
    }
    
    public int guardarTipoCilindro(TipoCilindroDT tipoCilindro) throws Exception
    {
        int res;
        try
        {
            String  sql = "INSERT INTO tipo_cilindro "+
                          "(codigo,nombre) "+
                          "VALUES('"+tipoCilindro.getCodigo()+"','"+tipoCilindro.getNombre()+"')";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public int eliminarTipoCilindro(int id) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE tipo_cilindro "+
                         "SET visible = 0  "+
                         "WHERE id LIKE "+id+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int actualizarTipoCilindro(TipoCilindroDT tipoCilindro) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE  tipo_cilindro "+
                         "SET codigo = '"+tipoCilindro.getCodigo()+"',"+
                         "    nombre = '"+tipoCilindro.getNombre()+"' "+
                         "WHERE id LIKE "+tipoCilindro.getId()+"";
            res = instru.executeUpdate(sql);
                    
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public ArrayList buscarTipoCilindro(int id) throws Exception
    {
        ArrayList<TipoCilindroDT> lista = new ArrayList<>();
        ResultSet dato;
        TipoCilindroDT dt = new TipoCilindroDT();
        try
        {
         String sql = "SELECT * "+
                      "FROM vw_tipo_cilindro "+ // Siempre dejar un espacio antes de cerrar la cadena de texto
                      "WHERE id LIKE "+id+" ";
         dato = instru.executeQuery(sql);
         while(dato.next())
         {
             String codigo = dato.getString("codigo");
             String nombre = dato.getString("nombre");
             int idTipo = dato.getInt("id");
             dt.setCodigo(codigo);
             dt.setNombre(nombre);
             dt.setId(idTipo);
             lista.add(dt);
         }
         
        }catch(SQLException ex)
        {
            throw  new  Exception(ex.getMessage());
        }
        return lista;
    }
    
    public ArrayList buscarTipoCilindroConCodigo(String codigo) throws Exception
    {
        ArrayList<TipoCilindroDT> lista = new ArrayList<>();
        ResultSet dato;
        TipoCilindroDT dt = new TipoCilindroDT();
        try
        {
         String sql = "SELECT * "+
                      "FROM vw_tipo_cilindro "+ // Siempre dejar un espacio antes de cerrar la cadena de texto
                      "WHERE codigo LIKE '"+codigo+"' ";
         dato = instru.executeQuery(sql);
         while(dato.next())
         {
             String nombre = dato.getString("nombre");
             int id = dato.getInt("id");
             dt.setCodigo(codigo);
             dt.setNombre(nombre);
             dt.setId(id);
             lista.add(dt);
         }
         
        }catch(SQLException ex)
        {
            throw  new  Exception(ex.getMessage());
        }
        return lista;
    }
    
    public ResultSet imprimirTipoCilindro() throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT * FROM vw_tipo_cilindro ORDER BY codigo";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public int actualizarCodigoTipoCilindro(int id , String actual , String anterior)throws Exception
    {
        int res;
        String sql;
        try
        {
            sql = "UPDATE tipo_cilindro "+
                  "SET codigo = '"+actual+"' "+
                  "WHERE id LIKE "+id+" AND codigo LIKE '"+anterior+"'";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public int actualizarNombreTipoCilindro(int id , String actual , String anterior) throws Exception
    {
        int res;
        String sql;
        try
        {
            sql = "UPDATE tipo_cilindro "+
                  "SET nombre = '"+actual+"' "+
                  "WHERE id LIKE "+id+" AND nombre LIKE '"+anterior+"'";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public ResultSet validarEliminarTipoCilindro(int id) throws Exception
    {
        ResultSet dato;
        String sql;
        try
        {
            sql = "SELECT COUNT(id_tipo_cilindro) AS 'IdTipoCilindro' "+
                  "FROM tipo_cilindro t LEFT OUTER JOIN detalle_revision r "+
                  "ON (t.id = r.id_tipo_cilindro ) "+
                  "WHERE id_tipo_cilindro LIKE "+id+" ";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public int buscarCodigoRepetido(int codigo) throws Exception
    {
      int res = 0;
      String sql;
      ResultSet dato;
      try
      {
          sql = "SELECT * FROM tipo_cilindro WHERE codigo LIKE "+codigo+" ";
          dato = instru.executeQuery(sql);
          while(dato.next())
          {
              res++;
          }
      }catch(SQLException ex)
      {
          throw new Exception(ex.getMessage());
      }
      return res;
    }
    
    public int buscarNombreRepetido(String nombre) throws Exception
    {
      int res = 0;
      String sql;
      ResultSet dato;
      try
      {
          sql = "SELECT * FROM tipo_cilindro WHERE nombre LIKE '"+nombre+"' ";
          dato = instru.executeQuery(sql);
          while(dato.next())
          {
              res++;
          }
      }catch(SQLException ex)
      {
          throw new Exception(ex.getMessage());
      }
      return res;
    }    
}
