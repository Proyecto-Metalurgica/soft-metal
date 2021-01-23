package domainapp.modules.simple.dominio.pagos;


import com.google.common.collect.ComparisonChain;
import domainapp.modules.simple.dominio.item.Item;
import domainapp.modules.simple.dominio.ordenCompra.OrdenCompra;
import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
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
import java.math.BigInteger;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})


@javax.jdo.annotations.Unique(name="Pago_name_UNQ", members = {"fechaPago"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class Pago implements Comparable<Pago> {

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @PropertyLayout(named="Nro de Pago")
    @Title(prepend = "Nro Pago: ")
    private String nroPago;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    @Title(prepend = "Pago: ")
    private LocalDate fechaPago;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private Double monto;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private FormaPago formaPago;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private OrdenCompra ordenCompra;

    @Override
    public String toString() {
        return getNroPago();
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



