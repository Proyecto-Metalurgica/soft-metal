package domainapp.modules.simple.dominio.ordenOT;


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

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})


@javax.jdo.annotations.Unique(name="OrdenTrabajo_name_UNQ", members = {"nroOT"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class OrdenTrabajo {
    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @PropertyLayout(named="Nro Orden de Compra")
    @Title(prepend = "Nro OT: ")
    private BigInteger nroOT;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private EstadoOT estadoOT;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    @Title()
    private LocalDate fecha;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private Presupuesto presupuesto;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    OrdenTrabajoRepository repositoryOT;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;



}
