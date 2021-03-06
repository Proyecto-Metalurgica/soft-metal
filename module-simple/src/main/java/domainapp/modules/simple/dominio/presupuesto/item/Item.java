package domainapp.modules.simple.dominio.presupuesto.item;

import java.util.List;
import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import domainapp.modules.simple.dominio.producto.Producto;
import domainapp.modules.simple.dominio.producto.ProductoMenu;
import com.google.common.collect.ComparisonChain;
import lombok.AccessLevel;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

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
public class Item implements Comparable<Item> {

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
    private Double precio = 0.0;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private Integer cantidad = 0;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private Double precioTotal = 0.0;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 800)
    @Property(editing = Editing.ENABLED)
    private String detalle;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @lombok.Getter @lombok.Setter
    @Property(editing = Editing.DISABLED)
    private Presupuesto presupuesto;

    public Item(Presupuesto presupuesto, Integer nroItem) {
        this.presupuesto = presupuesto;
        this.nroItem = nroItem;
    }

    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "producto")
    public Item updateCantidad(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Cantidad") final Integer cantidad){
        setCantidad(cantidad);
        if(this.precio !=null){
            setPrecioTotal(this.precio * cantidad);
            this.presupuesto.updatePreciosItems();
        }
        return this;
    }

    public Integer default0UpdateCantidad() {
        return getCantidad();
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public Presupuesto delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.remove(this);
        return this.presupuesto;
    }


    @Override
    public String toString() {
        return getNroItem().toString();
    }



    public int compareTo(final Item other) {
        return ComparisonChain.start()
                .compare(this.getNroItem(), other.getNroItem())
                .result();
    }

    @Action(semantics=SemanticsOf.IDEMPOTENT)
    public Item addProducto(Producto producto) {
        this.producto = producto.getNombre();
        this.medida = producto.getMedida();
        this.precio = producto.getPrecioUnitario();
        this.cantidad = 1;
        this.precioTotal = cantidad * precio;
        this.presupuesto.updatePreciosItems();
        return this;
    }
    public List<Producto> autoComplete0AddProducto(
            @MinLength(1)
                    String searchTerm) {
        return productoMenu.findByName(searchTerm);
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