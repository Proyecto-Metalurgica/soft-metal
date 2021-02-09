package domainapp.modules.simple.dominio.ordenCompra;


import com.google.common.collect.ComparisonChain;
import domainapp.modules.simple.dominio.pagos.Pago;
import domainapp.modules.simple.dominio.pagos.PagoRepository;
import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import lombok.AccessLevel;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.schema.utils.jaxbadapters.JodaDateTimeStringAdapter;
import org.joda.time.LocalDate;

import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})

@javax.jdo.annotations.Unique(name="OrdenCompra_name_UNQ", members = {"nroCompra"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout(cssClassFa="file-o" )  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class OrdenCompra implements Comparable<OrdenCompra>  {

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @PropertyLayout(named="Nro Orden de Compra")
    @Title(prepend = "Nro OC: ")
    private BigInteger nroCompra;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @PropertyLayout(named="Fecha Creacion de OC: ")
    @Property(editing = Editing.DISABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fechaCreacionOC = LocalDate.now();

    @javax.jdo.annotations.Column(allowsNull = "true")
    @PropertyLayout(named="Fecha Inicio de Trabajos: ")
    @Property(editing = Editing.ENABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fechaInicio;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @PropertyLayout(named="Fecha Entrega de Trabajos: ")
    @Property(editing = Editing.ENABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fechaEntrega;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @PropertyLayout(named="Valor Total de Compra: ")
    @Property(editing = Editing.DISABLED)
    private Double valorTotalOC;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @PropertyLayout(named="Total Abonado: ")
    @Property(editing = Editing.DISABLED)
    private Double totalAbonado = 0.0;

    @javax.jdo.annotations.Persistent(
            mappedBy = "ordenCompra",
            dependentElement = "false"
    )
    @CollectionLayout(defaultView = "table")
    @lombok.Getter @lombok.Setter
    private SortedSet<Pago> pagosRecibidos = new TreeSet<Pago>();

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private Presupuesto presupuesto;

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            associateWith = "simple"
    )
    public Pago newPago() {
        //Se numeran los pagos de esta manera "103-1", el segundo pago "103-2", etc.
        int size = pagosRecibidos.size() + 1;
        String nroCompra = this.nroCompra.toString()+'-'+ size;
        return repositoryService.persist(new Pago( nroCompra,this));
    }

    public void updatePagos() {
            if (!this.pagosRecibidos.isEmpty()) {
                Double suma = 0.0;
                for (Pago pago : pagosRecibidos) {
                    suma += pago.getMonto();
                }
                setTotalAbonado(suma);
            } else {
                messageService.warnUser("El Listado de Pagos se encuentra vacio");
                setTotalAbonado(0.0);
            }
    }

    @Override
    public String toString() {
        return getNroCompra().toString();
    }



    public int compareTo(final OrdenCompra other) {
        return ComparisonChain.start()
                .compare(this.getNroCompra(), other.getNroCompra())
                .result();
    }

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    OrdenCompraRepository repositoryOC;

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
