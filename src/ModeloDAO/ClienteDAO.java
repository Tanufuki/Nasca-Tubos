/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

//librerias
import ModeloDT.ClienteDT;
import ModeloDT.conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Angelo
 */
public class ClienteDAO {
    
     Statement instru;
     int modificar = 0; //codigo basura para testear github
     String otroModificar = null;
     //
     //
     
    public ClienteDAO() throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
    }
    
    public int guardarCliente(ClienteDT cliente) throws Exception
    {
        int res = 0;
        String sql = "";
        try
        {
            if(cliente.getEmail().equals("null"))
            {
                sql = "INSERT INTO cliente "+
                         "(rut,nombre,direccion,email) "+
                         "VALUES("+cliente.getRut()+",'"+cliente.getNombre()+"','"+cliente.getDireccion()+"',NULL)";
            }else if(cliente.getEmail()!="null")
            {
                sql = "INSERT INTO cliente "+
                         "(rut,nombre,direccion,email) "+
                         "VALUES("+cliente.getRut()+",'"+cliente.getNombre()+"','"+cliente.getDireccion()+"','"+cliente.getEmail()+"')";
            }
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public ArrayList buscarCliente(int id) throws Exception
    {
        ArrayList<ClienteDT>lista = new ArrayList<ClienteDT>();
        ResultSet dato;
        try
        {
            String sql = "SELECT * "+
                         "FROM vw_cliente "+
                         "WHERE id LIKE "+id+"";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                int idCliente = Integer.parseInt(dato.getString("id"));
                int rut = Integer.parseInt(dato.getString("rut"));
                String nombre = dato.getString("nombre");
                String direccion = dato.getString("direccion");
                String email = dato.getString("email");
                ClienteDT dt = new ClienteDT();
                dt.setId(idCliente);
                dt.setRut(rut);
                dt.setNombre(nombre);
                dt.setDireccion(direccion);
                dt.setEmail(email);
                lista.add(dt);
            }
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
    
     public ArrayList buscarClienteConRut(String rutIngresado) throws Exception
    {
        ArrayList<ClienteDT>lista = new ArrayList<ClienteDT>();
        ResultSet dato;
        try
        {
            String sql = "SELECT * "+
                         "FROM vw_cliente "+
                         "WHERE rut LIKE "+rutIngresado+"";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                int idCliente = Integer.parseInt(dato.getString("id"));
                int rut = Integer.parseInt(dato.getString("rut"));
                String nombre = dato.getString("nombre");
                String direccion = dato.getString("direccion");
                String email = dato.getString("email");
                ClienteDT dt = new ClienteDT();
                dt.setId(idCliente);
                dt.setRut(rut);
                dt.setNombre(nombre);
                dt.setDireccion(direccion);
                dt.setEmail(email);
                lista.add(dt);
            }
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
     
    public ArrayList buscarClienteEliminado(int id) throws Exception
    {
        ArrayList<ClienteDT>lista = new ArrayList<>();
        String sql = null;
        ResultSet dato;
        ClienteDT dt = new ClienteDT();
        try
        {
            sql = "SELECT * "+
                  "FROM cliente "+
                  "WHERE id LIKE "+id+"";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                int rut = dato.getInt("rut");
                String nombre = dato.getString("nombre");
                String direccion = dato.getString("direccion");
                String email = dato.getString("email");
                int idCliente = dato.getInt("id");
                dt.setRut(rut);
                dt.setNombre(nombre);
                dt.setDireccion(direccion);
                dt.setEmail(email);
                dt.setId(idCliente);
                lista.add(dt);
            }
        }
        catch(SQLException ex)
        {
        throw new Exception(ex.getMessage());
        }
        return lista;
    }
    public int eliminarCliente(int id) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE cliente "+
                         "SET visible = 0 "+
                         "WHERE id LIKE "+id+"";
            res = instru.executeUpdate(sql);
        }catch(Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int recuperarCliente(int id) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE cliente "+
                         "SET visible = 1 "+
                        "WHERE id LIKE "+id+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int actualizarCliente(ClienteDT cliente) throws Exception
    {
        int res;
        String sql = "";
        try
        {
            if(cliente.getEmail().equals("null"))
            {
                sql =  "UPDATE cliente "+
                         "SET rut = "+cliente.getRut()+", "+
                             "nombre = '"+cliente.getNombre()+"' ,"+
                             "direccion = '"+cliente.getDireccion()+"' ,"+
                             "email = NULL "+
                        "WHERE id LIKE "+cliente.getId()+"";
            }else
            {
                sql =  "UPDATE cliente "+
                         "SET rut = "+cliente.getRut()+", "+
                             "nombre = '"+cliente.getNombre()+"' ,"+
                             "direccion = '"+cliente.getDireccion()+"' ,"+
                             "email = '"+cliente.getEmail()+"' "+
                        "WHERE id LIKE "+cliente.getId()+"";
            }
            res = instru.executeUpdate(sql);
        }catch(Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ResultSet imprimirCliente() throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT * FROM vw_cliente ORDER BY rut";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public int actualizarRutCliente(int id , int rutActual , int rutAnterior) throws Exception
    {
        int res = 0;
        String sql;
        try
        {
            sql = "UPDATE cliente "+
                  "SET rut = "+rutActual+" "+
                  "WHERE id LIKE "+id+" AND rut LIKE "+rutAnterior+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int actualizarNombreCliente(int id , String nombreActual , String nombreAnterior) throws Exception
    {
        int res = 0;
        String sql;
        try
        {
            sql = "UPDATE cliente "+
                  "SET nombre = '"+nombreActual+"' "+
                  "WHERE id LIKE "+id+" AND nombre LIKE '"+nombreAnterior+"'";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public int actualizarDireccionCliente(int id , String direccionActual , String direccionAnterior) throws Exception
    {
        int res = 0;
        String sql;
        try
        {
            sql = "UPDATE cliente "+
                  "SET direccion = '"+direccionActual+"' "+
                   "WHERE id LIKE "+id+" AND direccion LIKE '"+direccionAnterior+"'";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    public int actualizarEmailCliente(int id , String emailActual , String emailAnterior) throws Exception
    {
        int res = 0;
        String sql;
        try
        {
            sql = "UPDATE cliente "+
                  "SET email = '"+emailActual+"' "+
                  "WHERE id LIKE "+id+" AND email LIKE '"+emailAnterior+"'";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getErrorCode()+"");
        }
        return res;
    }
    
    
    public ArrayList buscarRutRepetido(int rut) throws Exception
    {
      String sql;
      ResultSet dato;
      String nombre , direccion , correo;
      int rutCliente , visible , id;
      ArrayList<ClienteDT> lista = new ArrayList<>();
      ClienteDT dt = new ClienteDT();
      try
      {
          sql = "SELECT * FROM cliente WHERE rut LIKE "+rut+" ";
          dato = instru.executeQuery(sql);
          while(dato.next())
          {
              nombre = dato.getString("nombre");
              direccion = dato.getString("direccion");
              correo = dato.getString("email");
              rutCliente = dato.getInt("rut");
              visible = dato.getInt("visible");
              id = dato.getInt("id");
              dt.setDireccion(direccion);
              dt.setEmail(correo);
              dt.setId(id);
              dt.setNombre(nombre);
              dt.setRut(rut);
              dt.setVisible(visible);
              lista.add(dt);
          }
      }catch(SQLException ex)
      {
          throw new Exception(ex.getMessage());
      }
      return lista;
    }
    
    public ArrayList buscarNombreRepetido(String nombre) throws Exception
    {
        
      String sql;
      ResultSet dato;
      ArrayList<ClienteDT>lista = new ArrayList<>();
      String nombreCliente , direccion , correo;
      int rutCliente , visible , id;
      ClienteDT dt = new ClienteDT();
      try
      {
          sql = "SELECT * FROM cliente WHERE nombre LIKE '"+nombre+"' ";
          dato = instru.executeQuery(sql);
          while(dato.next())
          {
              nombreCliente = dato.getString("nombre");
              direccion = dato.getString("direccion");
              correo = dato.getString("email");
              rutCliente = dato.getInt("rut");
              visible = dato.getInt("visible");
              id = dato.getInt("id");
              dt.setDireccion(direccion);
              dt.setEmail(correo);
              dt.setId(id);
              dt.setNombre(nombreCliente);
              dt.setRut(rutCliente);
              dt.setVisible(visible);
              lista.add(dt);
          }
      }catch(SQLException ex)
      {
          throw new Exception(ex.getMessage());
      }
      return lista;
    }
     
    public ResultSet validarEliminarCliente(int id) throws Exception
    {
        ResultSet dato;
        String sql;
        try
        {
            sql = "SELECT COUNT(id_cliente) AS 'Idcliente' "+
                  "FROM cliente c LEFT OUTER JOIN revision r "+
                  "ON (c.id = r.id_cliente ) "+
                  "WHERE id_cliente LIKE "+id+" ";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
}
