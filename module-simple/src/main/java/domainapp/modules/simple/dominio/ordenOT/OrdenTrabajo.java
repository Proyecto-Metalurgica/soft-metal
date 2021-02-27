package domainapp.modules.simple.dominio.ordenOT;


import domainapp.modules.simple.dominio.ordenOT.itemOT.ItemOT;
import domainapp.modules.simple.dominio.presupuesto.item.Item;
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

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;

@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@Queries({
        @Query(
                name = "findAllActives", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo "
                        + "WHERE activo == true "),
        @Query(
                name = "findAllInactives", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo "
                        + "WHERE activo == false "),
})


@javax.jdo.annotations.Unique(name="OrdenTrabajo_name_UNQ", members = {"nroOT"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout(cssClassFa = "dropbox")  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class OrdenTrabajo {
    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @PropertyLayout(named="Nro Orden de Compra")
    @Title(prepend = "Nro OT: ")
    private BigInteger nroOT;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.DISABLED)
    private EstadoOT estadoOT = EstadoOT.Espera;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @PropertyLayout(named="Fecha Inicio de Trabajos: ")
    @Property(editing = Editing.DISABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fechaInicio;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @PropertyLayout(named="Fecha Entrega de Trabajos: ")
    @Property(editing = Editing.DISABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fechaEntrega;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private Presupuesto presupuesto;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private boolean activo = true;

    @javax.jdo.annotations.Persistent(
            mappedBy = "ordenTrabajo",
            dependentElement = "false"
    )
    @CollectionLayout(defaultView = "table")
    @lombok.Getter
    @lombok.Setter
    private SortedSet<ItemOT> itemsOT = new TreeSet<ItemOT>();

    public OrdenTrabajo(Presupuesto presupuesto) {
        this.nroOT = presupuesto.getNroPresupuesto();
        this.presupuesto = presupuesto;
        this.fechaInicio = presupuesto.getOrdenCompra().getFechaInicio();
        this.fechaEntrega = presupuesto.getOrdenCompra().getFechaEntrega();
        for (Item item : presupuesto.getItems())
        {
            itemsOT.add(new ItemOT(this,item));
        }
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, command = ENABLED, publishing = Publishing.ENABLED)
    public OrdenTrabajo actualizarEstadoOT() {
        boolean itemsEnTerminado = true;
        for (ItemOT itemOT : getItemsOT()) {
            if(!itemOT.getEstadoOT().equals(EstadoOT.Terminado)){
                itemsEnTerminado = false;
            }
        }
        if (getEstadoOT().equals(EstadoOT.Espera)) {
            setEstadoOT(EstadoOT.Ejecucion);
            messageService.warnUser("Se coloca la OT en Ejecucion");
        } else if (getEstadoOT().equals(EstadoOT.Ejecucion) & itemsEnTerminado) {
            setEstadoOT(EstadoOT.Terminado);
            setActivo(false);
            messageService.warnUser("La OT con todos sus items se encuentra Terminada");
        } else {
            messageService.warnUser("Falta finalizar items de la OT");
        }
        return this;
    }

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
