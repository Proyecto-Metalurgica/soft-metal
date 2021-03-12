package domainapp.modules.simple.dominio.pagos;


import com.google.common.collect.ComparisonChain;
import domainapp.modules.simple.dominio.ordenCompra.OrdenCompra;
import lombok.AccessLevel;
import org.apache.isis.applib.annotation.*;


import javax.jdo.annotations.IdentityType;

import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.schema.utils.jaxbadapters.JodaDateTimeStringAdapter;
import org.joda.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})

@javax.jdo.annotations.Unique(name="Pago_name_UNQ", members = {"nroPago"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout( cssClassFa="money")  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class Pago implements Comparable<Pago> {

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @PropertyLayout(named="Nro de Pago")
    @Title(prepend = "Nro de Pago: ")
    private String nroPago;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @PropertyLayout(named="Fecha de Pago: ")
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    @Property(editing = Editing.DISABLED)
    private LocalDate fechaPago;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @PropertyLayout(named="Monto Abonado: ")
    @Property(editing = Editing.DISABLED)
    private Double monto;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @PropertyLayout(named="Forma de Pago: ")
    @Property(editing = Editing.DISABLED)
    private FormaPago formaPago;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @PropertyLayout(named="Orden de Compra: ")
    @Property(editing = Editing.DISABLED)
    private OrdenCompra ordenCompra;

    @Override
    public String toString() {
        return getNroPago();
    }

    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "monto")
    public Object updateMonto(
            @ParameterLayout(named = "Monto: ") final Double monto,
            @ParameterLayout(named = "Forma de Pago: ") final FormaPago formaPago,
            @ParameterLayout(named = "Fecha de Pago: ") final LocalDate fechaPago) {
        if(this.ordenCompra.getTotalAbonado()+monto <= this.ordenCompra.getValorTotalOC()){
            this.setMonto(monto);
            this.setFormaPago(formaPago);
            this.setFechaPago(fechaPago);
            this.ordenCompra.updatePagos();
        }
        else{
            messageService.warnUser("El monto a pagar supera el valor adeudado de la OC");
        }
        return this;
    }

    public int compareTo(final Pago other) {
        return ComparisonChain.start()
                .compare(this.getNroPago(), other.getNroPago())
                .result();
    }

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    PagoRepository repositoryPago;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}



