package domainapp.modules.simple.dominio.reportes;


import java.math.BigInteger;

import domainapp.modules.simple.dominio.cliente.Cliente;
import domainapp.modules.simple.dominio.presupuesto.Estado;
import org.joda.time.LocalDate;


@lombok.Getter @lombok.Setter
public class RepoPresupuestos {


    private BigInteger nroPresupuesto;
    private LocalDate fecha;
    private Cliente cliente;
    private Double precio;
    private Estado estado;



    public RepoPresupuestos(BigInteger nroPresupuesto,LocalDate fecha,Cliente cliente,Double precio, Estado estado){
        this.nroPresupuesto=nroPresupuesto;
        this.fecha=fecha;
        this.cliente=cliente;
        this.precio=precio;
        this.estado=estado;


    }

    public RepoPresupuestos(){}

    public BigInteger getNroPresupuesto() { return this.nroPresupuesto;}

    public LocalDate getFecha(){ return  this.fecha;}


    public Cliente getCliente(){
        return this.cliente;
    }

    public Double getPrecio(){return  this.precio;}


    public Estado getEstado(){
        return this.estado;
    }


}
