package domainapp.modules.simple.dominio.ordenOT;

import com.google.common.collect.ComparisonChain;
import domainapp.modules.simple.dominio.item.Item;
import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import domainapp.modules.simple.dominio.producto.Producto;
import domainapp.modules.simple.dominio.producto.ProductoMenu;
import lombok.AccessLevel;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.List;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout(cssClassFa="list-alt")  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class ItemOT implements Comparable<ItemOT> {

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property()
    private Integer nroItem;

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
        this.nroItem = item.getNroItem();
        this.producto = item.getProducto();
        this.medida = item.getMedida();
        this.cantidad = item.getCantidad();
    }

    @Override
    public String toString() {
        return getNroItem().toString();
    }

    public int compareTo(final ItemOT other) {
        return ComparisonChain.start()
                .compare(this.getNroItem(), other.getNroItem())
                .result();
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