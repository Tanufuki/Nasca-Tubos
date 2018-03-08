/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

import ModeloDT.UsuarioDT;
import ModeloDT.conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Angelo
 */
public class UsuarioDAO {
    Statement instru;
    public UsuarioDAO() throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
    }
    
    public ArrayList loginUsuario(String usuario , String password) throws Exception
    {
        ResultSet dato;
        String sql;
        ArrayList<UsuarioDT> lista = new ArrayList<UsuarioDT>();
        
        
        try
        {
            sql ="SELECT * FROM vw_usuario "+
                 " WHERE nombre = '"+usuario+"' and clave = '"+password+"'";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
               int id = Integer.parseInt(dato.getString("id"));
               String codigo = dato.getString("codigo");
               String nombre = dato.getString("nombre");
               String clave = dato.getString("clave");
               int acceso = Integer.parseInt(dato.getString("acceso"));
               String cuando = dato.getString("cuando");
               UsuarioDT dt = new UsuarioDT();
               dt.setId(id);
               dt.setCodigo(codigo);
               dt.setNombre(nombre);
               dt.setClave(clave);
               dt.setAcceso(acceso);
               dt.setCuando(cuando);
               lista.add(dt);
            }
        }catch(SQLException ex )
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
}
