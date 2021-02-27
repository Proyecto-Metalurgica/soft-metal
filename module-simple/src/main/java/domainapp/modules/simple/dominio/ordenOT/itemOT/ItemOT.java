package domainapp.modules.simple.dominio.ordenOT.itemOT;

import com.google.common.collect.ComparisonChain;
import domainapp.modules.simple.dominio.ordenOT.EstadoOT;
import domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo;
import domainapp.modules.simple.dominio.presupuesto.Estado;
import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import domainapp.modules.simple.dominio.presupuesto.item.Item;
import domainapp.modules.simple.dominio.producto.ProductoMenu;
import lombok.AccessLevel;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout(cssClassFa="cube")  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class ItemOT implements Comparable<ItemOT> {

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property()
    private Integer nroItemOT;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Item: ")
    private String producto;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @Property()
    private String medida;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private Integer cantidad;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 800)
    @Property(editing = Editing.ENABLED)
    private String detalle;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    private EstadoOT estadoOT = EstadoOT.Espera;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @lombok.Getter @lombok.Setter
    @Property(editing = Editing.DISABLED)
    private OrdenTrabajo ordenTrabajo;

    public ItemOT(OrdenTrabajo ordenTrabajo, Item item) {
        this.ordenTrabajo = ordenTrabajo;
        this.nroItemOT = item.getNroItem();
        this.producto = item.getProducto();
        this.medida = item.getMedida();
        this.cantidad = item.getCantidad();
        this.detalle = item.getDetalle();
    }

    @Override
    public String toString() {
        return getNroItemOT().toString();
    }

    public int compareTo(final ItemOT other) {
        return ComparisonChain.start()
                .compare(this.getNroItemOT(), other.getNroItemOT())
                .result();
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, command = ENABLED, publishing = Publishing.ENABLED)
    public ItemOT estadoEjecucion() {
        setEstadoOT(EstadoOT.Ejecucion);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, command = ENABLED, publishing = Publishing.ENABLED)
    public ItemOT estadoEspera() {
        setEstadoOT(EstadoOT.Espera);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, command = ENABLED, publishing = Publishing.ENABLED)
    public ItemOT estadoTerminado() {
        setEstadoOT(EstadoOT.Terminado);
        return this;
    }

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    ProductoMenu productoMenu;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}