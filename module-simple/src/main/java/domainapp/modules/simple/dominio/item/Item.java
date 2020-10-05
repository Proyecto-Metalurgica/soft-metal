package domainapp.modules.simple.dominio.item;

import com.google.common.collect.ComparisonChain;
import lombok.AccessLevel;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.i18n.TranslatableString;
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

@javax.jdo.annotations.Unique(name="Item_name_UNQ", members = {"producto"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor

public class Item implements Comparable<Item> {

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Item: ")
    private String producto;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String cantidad;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String precioTotal;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 800)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String detalle;



    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "producto")
    public Item updateProducto(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Producto") final String producto,
            @ParameterLayout(named = "Cantidad") final String cantidad,
            @ParameterLayout(named = "PrecioTotal") final String precioTotal,
            @ParameterLayout(named = "Detalle") final String detalle){
        setProducto(producto);
        setCantidad(cantidad);
        setPrecioTotal(precioTotal);
        setDetalle(detalle);

        return this;
    }

    public String default0UpdateProducto() {
        return getProducto();
    }

    public TranslatableString validate0UpdateProducto(final String Producto) {
        return producto != null && producto.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }


    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.remove(this);
    }


    @Override
    public String toString() {
        return getProducto();
    }



    public int compareTo(final Item other) {
        return ComparisonChain.start()
                .compare(this.getProducto(), other.getProducto())
                .result();
    }


    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}