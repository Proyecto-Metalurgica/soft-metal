package domainapp.modules.simple.dominio.producto;

import javax.jdo.annotations.*;

import com.google.common.collect.ComparisonChain;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.AccessLevel;

import java.math.BigInteger;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@Sequence(name="productoseq", datastoreSequence="YOUR_SEQUENCE_NAME3", strategy=SequenceStrategy.CONTIGUOUS, initialValue = 1, allocationSize = 1)

@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})


@javax.jdo.annotations.Unique(name="Producto_name_UNQ", members = {"nombre"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class Producto implements Comparable<Producto> {

    @Column(allowsNull = "true", length = 10)
    @Property(editing = Editing.DISABLED)
    @Persistent(valueStrategy=IdGeneratorStrategy.SEQUENCE, sequence="productoseq")
    private BigInteger codigo;

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Producto: ")
    private String nombre;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String medida;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private Double precioUnitario;



    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "nombre")
    public Producto updateNombre(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Nombre") final String nombre,
            @ParameterLayout(named = "Medida") final String medida,
            @ParameterLayout(named = "Precio Unitario") final Double precioUnitario){
        setNombre(nombre);
        setMedida(medida);
        setPrecioUnitario(precioUnitario);
        return this;
    }

    public String default0UpdateNombre() {
        return getNombre();
    }

    public TranslatableString validate0UpdateNombre(final String Nombre) {
        return nombre != null && nombre.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }


   /* @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.remove(this);
    }
*/

    @Override
    public String toString() {
        return getNombre();
    }



    public int compareTo(final Producto other) {
        return ComparisonChain.start()
                .compare(this.getNombre(), other.getNombre())
                .result();
    }


    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    ProductoRepository repositoryProducto;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}