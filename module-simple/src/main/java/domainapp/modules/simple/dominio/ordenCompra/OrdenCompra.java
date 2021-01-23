package domainapp.modules.simple.dominio.ordenCompra;


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
import java.util.List;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})



@javax.jdo.annotations.Unique(name="OrdenCompra_name_UNQ", members = {"nroCompra"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class OrdenCompra {
    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @PropertyLayout(named="Nro Orden de Compra")
    @Title(prepend = "Nro OC: ")
    private BigInteger nroCompra;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fechaInicio;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fechaEntrega;

    @javax.jdo.annotations.Column(allowsNull = "true",name = "asig_pago_Id")
    @Property()
    @PropertyLayout(named="Pago")
    private Pago pago;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private Presupuesto presupuesto;

    //Metodo para asignar el pago a Orden de compra
    @Action()
    @ActionLayout(named = "Asignar Pago")
    public OrdenCompra AgregarPago(
            @Parameter(optionality = Optionality.MANDATORY)
            @ParameterLayout(named = "Pago")
            final Pago pagos) {

        this.pago = pagos;
        return this;
    }

    public List<Pago> choices0AgregarPago() { return repositoryPago.Listar(); }

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
