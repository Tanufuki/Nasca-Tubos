/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

import ModeloDT.DetalleRevisionDT;
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
public class DetalleRevisionDAO {
    
    Statement instru;
    public DetalleRevisionDAO() throws Exception
    {
        conexion c = new conexion();
        instru = c.getStatement();
    }
    
 
    public int guardarDetalleRevision(DetalleRevisionDT detalleRevision) throws Exception
    {
        int res;
        String sql;
        try
        {
            if(detalleRevision.getIdFoto()==0)
            {
                            sql = "INSERT INTO detalle_revision "+
                         "(id_revision,numero,id_tipo_cilindro,fabricacion,id_fabricante,id_norma,ultimaprueba,"+
                         "PresionDeServicio,PresionDePruebaEstampado,VolCargaIndicada,PresionPrueba,DeformTotal,"+
                         "DeformPermanente,Elasticidad,DeformPermPorcentaje,pintura,cambio_valvula,valvula,manilla,"+
                         "volante,IVhiloCuello,IVExterior,IVInterior,Aprobado,id_foto) "+
                         "VALUES("+detalleRevision.getIdRevision()+","+detalleRevision.getNumero()+","+
                         ""+detalleRevision.getIdTipoCilindro()+",'"+detalleRevision.getFechaFabricacion()+"',"+detalleRevision.getIdFabricante()+","+
                         " "+detalleRevision.getIdNorma()+", "+
                         "'"+detalleRevision.getUltimaPrueba()+"',"+detalleRevision.getPresionServicio()+","+detalleRevision.getPresionPruebaEstampado()+","+
                         ""+detalleRevision.getVolCargaIndicada()+","+detalleRevision.getPresionPrueba()+","+detalleRevision.getDeformTotal()+","+
                         ""+detalleRevision.getDeformPermanente()+","+detalleRevision.getElasticidad()+","+detalleRevision.getDeforPermPorcentaje()+","+
                         ""+detalleRevision.getPintura()+","+detalleRevision.getCambioValvula()+","+
                         ""+detalleRevision.getValvula()+","+detalleRevision.getManilla()+","+detalleRevision.getVolante()+","+
                         ""+detalleRevision.getIvHiloCuello()+","+detalleRevision.getIvExterior()+","+
                         ""+detalleRevision.getIvInterior()+","+detalleRevision.getAprobado()+",NULL)";
            }else
            {
            
                sql = "INSERT INTO detalle_revision "+
                         "(id_revision,numero,id_tipo_cilindro,fabricacion,id_fabricante,id_norma,ultimaprueba,"+
                         "PresionDeServicio,PresionDePruebaEstampado,VolCargaIndicada,PresionPrueba,DeformTotal,"+
                         "DeformPermanente,Elasticidad,DeformPermPorcentaje,pintura,cambio_valvula,valvula,manilla,"+
                         "volante,IVhiloCuello,IVExterior,IVInterior,Aprobado,id_foto) "+
                         "VALUES("+detalleRevision.getIdRevision()+","+detalleRevision.getNumero()+","+
                         ""+detalleRevision.getIdTipoCilindro()+",'"+detalleRevision.getFechaFabricacion()+"',"+detalleRevision.getIdFabricante()+","+
                         " "+detalleRevision.getIdNorma()+", "+
                         "'"+detalleRevision.getUltimaPrueba()+"',"+detalleRevision.getPresionServicio()+","+detalleRevision.getPresionPruebaEstampado()+","+
                         ""+detalleRevision.getVolCargaIndicada()+","+detalleRevision.getPresionPrueba()+","+detalleRevision.getDeformTotal()+","+
                         ""+detalleRevision.getDeformPermanente()+","+detalleRevision.getElasticidad()+","+detalleRevision.getDeforPermPorcentaje()+","+
                         ""+detalleRevision.getPintura()+","+detalleRevision.getCambioValvula()+","+
                         ""+detalleRevision.getValvula()+","+detalleRevision.getManilla()+","+detalleRevision.getVolante()+","+
                         ""+detalleRevision.getIvHiloCuello()+","+detalleRevision.getIvExterior()+","+
                         ""+detalleRevision.getIvInterior()+","+detalleRevision.getAprobado()+","+detalleRevision.getIdFoto()+")";
            }
        res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int eliminarDetalleRevision(int id) throws Exception
    {
        int res;
        try
        {
            String sql = "UPDATE detalle_revision "+
                         "SET visible = 0 "+
                         "WHERE id LIKE "+id+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public int actualizarDetalleRevision(DetalleRevisionDT detalleRevision) throws Exception//Pendiente
    {
        int res;
        try
        {
            String sql = "UPDATE detalle_revision "+
                         "SET numero = "+detalleRevision.getNumero()+", "+
                         " fabricacion = '"+detalleRevision.getFechaFabricacion()+"', "+
                         " IVhiloCuello = "+detalleRevision.getIvHiloCuello()+", "+
                         " IVExterior = "+detalleRevision.getIvExterior()+", "+
                         " IVInterior = "+detalleRevision.getIvInterior()+", "+
                         " ultimaprueba = '"+detalleRevision.getUltimaPrueba()+"', "+
                         " PresionDeServicio = "+detalleRevision.getPresionServicio()+", "+
                         " PresionDePruebaEstampado = "+detalleRevision.getPresionPruebaEstampado()+", "+
                         " VolCargaIndicada = "+detalleRevision.getVolCargaIndicada()+", "+
                         " PresionPrueba = "+detalleRevision.getPresionPrueba()+", "+
                         " DeformTotal = "+detalleRevision.getDeformTotal()+", "+
                         " DeformPermanente = "+detalleRevision.getDeformPermanente()+", "+
                         " Elasticidad = "+detalleRevision.getElasticidad()+", "+
                         " DeformPermPorcentaje = "+detalleRevision.getDeforPermPorcentaje()+", "+
                         " pintura = "+detalleRevision.getPintura()+", "+
                         " cambio_valvula = "+detalleRevision.getCambioValvula()+", "+
                         " valvula = "+detalleRevision.getValvula()+", "+
                         " manilla = "+detalleRevision.getManilla()+", "+
                         " volante = "+detalleRevision.getVolante()+", "+
                         " Aprobado = "+detalleRevision.getAprobado()+" "+
                         " WHERE id LIKE "+detalleRevision.getId()+"";
            res = instru.executeUpdate(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return res;
    }
    
    public ResultSet imprimirDetalleRevision(int idRevision) throws Exception
    {
        ResultSet dato;
        try
        {
            String sql = "SELECT * "+
                         "FROM vw_detalle_revision "+
                         "WHERE id_revision LIKE "+idRevision+"";
            dato = instru.executeQuery(sql);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return dato;
    }
    
    public ArrayList buscarDetalleRevision(int id) throws Exception
    {
        ArrayList<DetalleRevisionDT>lista = new ArrayList<>();
        DetalleRevisionDT dt = new DetalleRevisionDT();
        ResultSet dato;
        int  idRevision, numero ,idTipoCilindro , idFabricante , idNorma,
                presionDeServicio,presionPruebaEstampado,presionPrueba,
                deformTotal , deformPermanente , elasticidad,
                deformPermPorcentaje,pintura,cambioValvula,
                valvula,manilla,volante,ivHiloCuello,ivExterior,
                ivInterior,aprobado , idFoto;
        Date  fabricacion ,ultimaPrueba;
        double volCargaIndicada;
        String rutaFoto;
        try
        {
            String sql = "SELECT * "+
                         "FROM vw_detalle_revision "+
                         "WHERE id LIKE "+id+"";
            dato = instru.executeQuery(sql);
         while(dato.next())
         {
             idRevision = dato.getInt("id_revision");
             dt.setIdRevision(idRevision);
             numero = dato.getInt("numero");
             dt.setNumero(numero);
             idTipoCilindro = dato.getInt("id_tipo_cilindro");
             dt.setIdTipoCilindro(idTipoCilindro);
             fabricacion = dato.getDate("fabricacion");
             dt.setFechaFabricacion(fabricacion);
             idFabricante = dato.getInt("id_fabricante");
             dt.setIdFabricante(idFabricante);
             idNorma = dato.getInt("id_norma");
             dt.setIdNorma(idNorma);
             ultimaPrueba = dato.getDate("ultimaprueba");
             dt.setUltimaPrueba(ultimaPrueba);
             presionDeServicio = dato.getInt("PresionDeServicio");
             dt.setPresionServicio(presionDeServicio);
             presionPruebaEstampado = dato.getInt("PresionDePruebaEstampado");
             dt.setPresionPruebaEstampado(presionPruebaEstampado);
             volCargaIndicada = dato.getDouble("VolCargaIndicada");
             dt.setVolCargaIndicada(volCargaIndicada);
             presionPrueba = dato.getInt("PresionPrueba");
             dt.setPresionPrueba(presionPrueba);
             deformTotal = dato.getInt("DeformTotal");
             dt.setDeformTotal(deformTotal);
             deformPermanente = dato.getInt("DeformPermanente");
             dt.setDeformPermanente(deformPermanente);
             elasticidad = dato.getInt("Elasticidad");
             dt.setElasticidad(elasticidad);
             deformPermPorcentaje = dato.getInt("DeformPermPorcentaje");
             dt.setDeforPermPorcentaje(deformPermPorcentaje);
             pintura = dato.getInt("pintura");
             dt.setPintura(pintura);
             idFoto = dato.getInt("id_foto");
             dt.setIdFoto(idFoto);
             rutaFoto = dato.getString("ruta_foto");
             dt.setRutaFoto(rutaFoto);
             cambioValvula = dato.getInt("cambio_valvula");
             dt.setCambioValvula(cambioValvula);
             valvula = dato.getInt("valvula");
             dt.setValvula(valvula);
             manilla = dato.getInt("manilla");
             dt.setManilla(manilla);
             volante = dato.getInt("volante");
             dt.setVolante(volante);
             ivHiloCuello = dato.getInt("IVhiloCuello");
             dt.setIvHiloCuello(ivHiloCuello);
             ivExterior = dato.getInt("IVExterior");
             dt.setIvExterior(ivExterior);
             ivInterior = dato.getInt("IVInterior");
             dt.setIvInterior(ivInterior);
             aprobado = dato.getInt("Aprobado");
             dt.setAprobado(aprobado);
             dt.setId(id);
             lista.add(dt);
         }
        }catch(Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }
   
}
