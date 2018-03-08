/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

/**
 *
 * @author Angelo
 */

import ModeloDT.FotoDT;
import ModeloDT.conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FotoDAO {
   
   Statement instru;
    
    public FotoDAO() throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
    }
    
    public int GuardarFoto(FotoDT foto) throws Exception
    {
        int res = 0;
        try
        {
            String sql = "INSERT INTO foto(dir1,dir2) "+
                         "VALUES("+foto.getDir1()+","+foto.getDir2()+")";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int EliminarFoto(int id) throws Exception
    {
        int res = 0;
        try
        {
            String sql = "UPDATE foto "+
                         "SET visible = 0 "+
                         "WHERE id LIKE "+id+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int ActualizarFoto(FotoDT foto) throws Exception
    {
        int res = 0;
        try
        {
            String sql = "UPDATE foto "+
                         "SET dir1 = "+foto.getDir1()+", "+
                         " dir2 = "+foto.getDir2()+" "+
                         "WHERE id LIKE "+foto.getId()+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ResultSet imprimirFoto() throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT * FROM vw_foto ORDER by ruta";
            dato = instru.executeQuery(sql);

        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public ArrayList buscarFoto(int id) throws Exception
    {
        ResultSet dato;
        ArrayList<FotoDT>lista = new ArrayList<FotoDT>();
        FotoDT fotoDT = new FotoDT();
        try
        {
            String sql = "SELECT * FROM vw_foto "+
                         "WHERE id LIKE "+id+"";
            dato = instru.executeQuery(sql);
            while(dato.next())
            {
                int dir1 = dato.getInt("dir1");
                int dir2 = dato.getInt("dir2");
                String ruta = dato.getString("ruta");
                int idFoto = dato.getInt("id");
                fotoDT.setDir1(dir1);
                fotoDT.setDir2(dir2);
                fotoDT.setRuta(ruta);
                fotoDT.setId(idFoto);
                lista.add(fotoDT);
            }
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
    
    public int actualizarDir1(int id, int actual , int anterior) throws Exception
    {
        int res = 0;
        String sql;
        try
        {
            sql = "UPDATE foto "+
                  "SET dir1 = "+actual+" "+
                  "WHERE id LIKE "+id+" AND dir1 LIKE "+anterior+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int actualizarDir2(int id , int actual , int anterior) throws Exception
    {
        int res = 0;
        String sql;
        try
        {
            sql = "UPDATE foto " +
                  "SET dir2 = "+actual+" "+
                  "WHERE id LIKE "+id+" AND dir2 LIKE "+anterior+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ResultSet validarEliminarFoto(int id) throws Exception
    {
        ResultSet dato;
        String sql;
        try
        {
            sql = "SELECT COUNT(id_foto) AS 'IdFoto' "+
                  "FROM foto f LEFT OUTER JOIN detalle_revision r "+
                  "ON (f.id = r.id_foto ) "+
                  "WHERE id_foto LIKE "+id+" ";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
       
}
