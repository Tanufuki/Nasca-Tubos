/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDT;

import java.sql.Date;

/**
 *
 * @author Angelo
 */
public class DetalleRevisionDT {
    private int id;
    private int idRevision;
    private int numero;
    private int idTipoCilindro;
    private Date fechaFabricacion;
    private int idFabricante;
    private String nombreFabricante;
    private int idNorma;
    private String norma;
    private Date ultimaPrueba;
    private int presionServicio;
    private int presionPruebaEstampado;
    private double volCargaIndicada;
    private int presionPrueba;
    private int deformTotal;
    private int deformPermanente;
    private int elasticidad;
    private int deforPermPorcentaje;
    private int pintura;
    private int idFoto;
    private String rutaFoto;
    private int cambioValvula;
    private int valvula;
    private int manilla;
    private int volante;
    private int visible;
    private String cuando;
    private int ivHiloCuello;
    private int ivExterior;
    private int ivInterior;
    private int aprobado;

    public DetalleRevisionDT() {
        
        this.presionServicio = 0;
        this.presionPruebaEstampado = 0;
        this.volCargaIndicada = 0.0;
        this.presionPrueba = 0;
        this.deformTotal = 0;
        this.deformPermanente = 0;
        this.elasticidad = 0;
        this.deforPermPorcentaje = 0;
        this.cambioValvula = 0;
        this.pintura = 0;
        this.valvula = 0;
        this.manilla = 0;
        this.volante = 0;
        this.ivExterior = 0;
        this.ivHiloCuello = 0;
        this.ivInterior = 0;
        this.aprobado = 0;
        
       
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRevision() {
        return idRevision;
    }

    public void setIdRevision(int idRevision) {
        this.idRevision = idRevision;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getIdTipoCilindro() {
        return idTipoCilindro;
    }

    public void setIdTipoCilindro(int idTipoCilindro) {
        this.idTipoCilindro = idTipoCilindro;
    }

    public Date getFechaFabricacion() {
        return fechaFabricacion;
    }

    public void setFechaFabricacion(Date fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }

    public int getIdFabricante() {
        return idFabricante;
    }

    public void setIdFabricante(int idFabricante) {
        this.idFabricante = idFabricante;
    }

    public String getNombreFabricante() {
        return nombreFabricante;
    }

    public void setNombreFabricante(String nombreFabricante) {
        this.nombreFabricante = nombreFabricante;
    }

    public int getIdNorma() {
        return idNorma;
    }

    public void setIdNorma(int idNorma) {
        this.idNorma = idNorma;
    }

    public String getNorma() {
        return norma;
    }

    public void setNorma(String norma) {
        this.norma = norma;
    }

    public Date getUltimaPrueba() {
        return ultimaPrueba;
    }

    public void setUltimaPrueba(Date ultimaPrueba) {
        this.ultimaPrueba = ultimaPrueba;
    }

    public int getPresionServicio() {
        return presionServicio;
    }

    public void setPresionServicio(int presionServicio) {
        this.presionServicio = presionServicio;
    }

    public int getPresionPruebaEstampado() {
        return presionPruebaEstampado;
    }

    public void setPresionPruebaEstampado(int presionPruebaEstampado) {
        this.presionPruebaEstampado = presionPruebaEstampado;
    }

    public double getVolCargaIndicada() {
        return volCargaIndicada;
    }

    public void setVolCargaIndicada(double volCargaIndicada) {
        this.volCargaIndicada = volCargaIndicada;
    }

    public int getPresionPrueba() {
        return presionPrueba;
    }

    public void setPresionPrueba(int presionPrueba) {
        this.presionPrueba = presionPrueba;
    }

    public int getDeformTotal() {
        return deformTotal;
    }

    public void setDeformTotal(int deformTotal) {
        this.deformTotal = deformTotal;
    }

    public int getDeformPermanente() {
        return deformPermanente;
    }

    public void setDeformPermanente(int deformPermanente) {
        this.deformPermanente = deformPermanente;
    }

    public int getElasticidad() {
        return elasticidad;
    }

    public void setElasticidad(int elasticidad) {
        this.elasticidad = elasticidad;
    }

    public int getDeforPermPorcentaje() {
        return deforPermPorcentaje;
    }

    public void setDeforPermPorcentaje(int deforPermPorcentaje) {
        this.deforPermPorcentaje = deforPermPorcentaje;
    }

    public int getPintura() {
        return pintura;
    }

    public void setPintura(int pintura) {
        this.pintura = pintura;
    }

    public int getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(int idFoto) {
        this.idFoto = idFoto;
    }

    public int getCambioValvula() {
        return cambioValvula;
    }

    public void setCambioValvula(int cambioValvula) {
        this.cambioValvula = cambioValvula;
    }

    public int getValvula() {
        return valvula;
    }

    public void setValvula(int valvula) {
        this.valvula = valvula;
    }

    public int getManilla() {
        return manilla;
    }

    public void setManilla(int manilla) {
        this.manilla = manilla;
    }

    public int getVolante() {
        return volante;
    }

    public void setVolante(int volante) {
        this.volante = volante;
    }

    public String getCuando() {
        return cuando;
    }

    public void setCuando(String cuando) {
        this.cuando = cuando;
    }

    public int getIvHiloCuello() {
        return ivHiloCuello;
    }

    public void setIvHiloCuello(int ivHiloCuello) {
        this.ivHiloCuello = ivHiloCuello;
    }

    public int getIvExterior() {
        return ivExterior;
    }

    public void setIvExterior(int ivExterior) {
        this.ivExterior = ivExterior;
    }

    public int getIvInterior() {
        return ivInterior;
    }

    public void setIvInterior(int ivInterior) {
        this.ivInterior = ivInterior;
    }

    public int getAprobado() {
        return aprobado;
    }

    public void setAprobado(int aprobado) {
        this.aprobado = aprobado;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
    
    
  
    
}
