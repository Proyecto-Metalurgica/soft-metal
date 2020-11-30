package domainapp.modules.simple.dominio.presupuesto;


import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;

import com.google.common.collect.ComparisonChain;


import domainapp.modules.simple.dominio.cliente.Cliente;
import domainapp.modules.simple.dominio.item.Item;
import org.apache.isis.applib.annotation.*;

import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.AccessLevel;
import org.apache.isis.schema.utils.jaxbadapters.JodaDateTimeStringAdapter;
import org.joda.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;


@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})

@javax.jdo.annotations.Unique(name="Presupuesto_name_UNQ", members = {"nroPresupuesto"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class Presupuesto implements Comparable<Presupuesto> {

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property()
    @Title(prepend = "Presupuesto: ")
    private String nroPresupuesto;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    @Title()
    private LocalDate fecha;

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @lombok.Getter @lombok.Setter
    @Property(editing = Editing.ENABLED)
    private Cliente cliente;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property()
    private String precio;

    @javax.jdo.annotations.Persistent(
            mappedBy = "presupuesto",
            dependentElement = "false"
    )
    @Collection
    @lombok.Getter @lombok.Setter
    private SortedSet<Item> items = new TreeSet<Item>();

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            associateWith = "simple"
    )
    public Item newItem(@ParameterLayout(named = "Nombre producto") final String producto) {
        return repositoryService.persist(new Item(this, producto));
    }

//    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "nroPresupuesto")
//    public Presupuesto updatePresupuesto(
//            @ParameterLayout(named = "Numero de Presupuesto: ") final String nroPresupuesto,
//
//            @ParameterLayout(named = "Fecha: ") final LocalDate fecha,
//
//            @ParameterLayout(named = "Item: ") final String producto,
//
//            @ParameterLayout(named = "Cantidad: ") final Integer cantidad,
//
//            @ParameterLayout(named = "Medidas: ") final String medida,
//
//            @ParameterLayout(named = "Tipo de Item: ") final String tipoMaterial,
//
//            @ParameterLayout(named = "Precio: ") final String precio
//
//    ) {
//
//        setNroPresupuesto(nroPresupuesto);
//        setFecha(fecha);
//        setProducto(producto);
//        setCantidad(cantidad);
//        setMedida(medida);
//        setTipoMaterial(tipoMaterial);
//        setPrecio(precio);
//
//        return this;
//    }
//
//    public String default0UpdatePresupuesto() {
//        return getNroPresupuesto();
//    }
//
//    public TranslatableString validate0UpdatePresupuesto(final String nroPresupuesto) {
//        return nroPresupuesto != null && nroPresupuesto.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
//    }
//
//    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
//    public void delete() {
//        final String title = titleService.titleOf(this);
//        messageService.informUser(String.format("'%s' deleted", title));
//        repositoryService.remove(this);
//    }
//
//    @Override
//    public String toString() {
//        return getNroPresupuesto();
//    }
//
//
//    public int compareTo(final Presupuesto other) {
//        return ComparisonChain.start()
//                .compare(this.getNroPresupuesto(), other.getNroPresupuesto())
//                .result();
//    }




    @javax.inject.Inject
        @javax.jdo.annotations.NotPersistent
        @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
        RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    PresupuestoRepository repositoryPresupuesto;

        @javax.inject.Inject
        @javax.jdo.annotations.NotPersistent
        @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
        TitleService titleService;

        @javax.inject.Inject
        @javax.jdo.annotations.NotPersistent
        @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
        MessageService messageService;

    public Presupuesto(Cliente cliente, String nroPresupuesto) {
        this.nroPresupuesto = nroPresupuesto;
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return getNroPresupuesto();
    }

    @Override
    public int compareTo(final Presupuesto other) {
        return ComparisonChain.start()
                .compare(this.getNroPresupuesto(), other.getNroPresupuesto())
                .result();
    }
}


