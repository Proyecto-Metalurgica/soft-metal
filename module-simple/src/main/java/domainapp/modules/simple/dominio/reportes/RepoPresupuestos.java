package domainapp.modules.simple.dominio.reportes;


import java.math.BigInteger;

import domainapp.modules.simple.dominio.cliente.Cliente;
import domainapp.modules.simple.dominio.presupuesto.Estado;
import org.joda.time.LocalDate;


@lombok.Getter @lombok.Setter
public class RepoPresupuestos {


    private BigInteger nroPresupuesto;
    private String fecha;
    private String cliente;
    private Double precio;
    private String estado;



    public RepoPresupuestos(BigInteger nroPresupuesto,String fecha,String cliente,Double precio, String estado){
        this.nroPresupuesto=nroPresupuesto;
        this.fecha=fecha;
        this.cliente=cliente;
        this.precio=precio;
        this.estado=estado;


    }

    public RepoPresupuestos(){}

    public Number getNroPresupuesto() { return this.nroPresupuesto;}

    public String getFecha(){ return  this.fecha;}


    public String getCliente(){
        return this.cliente;
    }

    public Double getPrecio(){return  this.precio;}


    public String getEstado(){
        return this.estado;
    }


}
