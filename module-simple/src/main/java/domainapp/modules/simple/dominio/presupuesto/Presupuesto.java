package domainapp.modules.simple.dominio.presupuesto;


import javax.jdo.annotations.*;

import com.google.common.collect.ComparisonChain;


import domainapp.modules.simple.dominio.cliente.Cliente;
import domainapp.modules.simple.dominio.item.Item;
import domainapp.modules.simple.dominio.ordenCompra.OrdenCompra;
import domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo;
import domainapp.modules.simple.dominio.pagos.Pago;
import org.apache.isis.applib.annotation.*;

import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.AccessLevel;
import org.apache.isis.schema.utils.jaxbadapters.JodaDateTimeStringAdapter;
import org.joda.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;


@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Sequence(name = "presupuestoseq", datastoreSequence = "YOUR_SEQUENCE_NAME2", strategy = SequenceStrategy.CONTIGUOUS, initialValue = 100, allocationSize = 1)
@javax.jdo.annotations.Version(strategy = VersionStrategy.DATE_TIME, column = "version")
@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})

@javax.jdo.annotations.Unique(name = "Presupuesto_name_UNQ", members = {"nroPresupuesto"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout(cssClassFa = "file-text-o")
@lombok.Getter
@lombok.Setter
@lombok.RequiredArgsConstructor
public class Presupuesto implements Comparable<Presupuesto> {

    @Column(allowsNull = "true", length = 10)
    @Property(editing = Editing.DISABLED)
    @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE, sequence = "presupuestoseq")
    @Title(prepend = "Nro Presupuesto: ")
    private BigInteger nroPresupuesto;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    @XmlJavaTypeAdapter(JodaDateTimeStringAdapter.ForJaxb.class)
    private LocalDate fecha = LocalDate.now();

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @lombok.Getter
    @lombok.Setter
    @Property(editing = Editing.DISABLED)
    private Cliente cliente;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private Double precio = 0.0;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private Estado estado = Estado.Espera;

    @javax.jdo.annotations.Persistent(
            mappedBy = "presupuesto",
            dependentElement = "false"
    )
    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.DISABLED)
    @lombok.Getter
    @lombok.Setter
    private OrdenCompra ordenCompra;

    @javax.jdo.annotations.Persistent(
            mappedBy = "presupuesto",
            dependentElement = "false"
    )
    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.DISABLED)
    @lombok.Getter
    @lombok.Setter
    private OrdenTrabajo ordenTrabajo;

    @javax.jdo.annotations.Persistent(
            mappedBy = "presupuesto",
            dependentElement = "false"
    )
    @CollectionLayout(defaultView = "table")
    @lombok.Getter
    @lombok.Setter
    private SortedSet<Item> items = new TreeSet<Item>();

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            associateWith = "simple"
    )
    public Object newItem() {
        if (this.getEstado().equals(Estado.Espera)) {
            return repositoryService.persist(new Item(this, items.size() + 1));
        } else {
            messageService.warnUser("Solo se pueden agregar items a un presupuesto en Espera");
            return this;
        }
    }

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            associateWith = "simple"
    )
    public Object newOrdenCompra() {
        if (this.getEstado().equals(Estado.Espera)) {
            if (this.getOrdenCompra() == null && !this.items.isEmpty()) {
                this.updatePreciosItems();
                this.setEstado(Estado.Aprobado);
                return repositoryService.persist(new OrdenCompra(this.nroPresupuesto, this.precio, this));
            } else if (this.items.isEmpty()) {
                messageService.warnUser("No se puede crear OC sin tener items cargados");
            } else {
                messageService.warnUser("Ya existe una OC para este presupuesto");
            }
        } else if (this.getEstado().equals(Estado.Anulado)) {
            messageService.warnUser("No se pueden crear OC para presupuestos Anulados");
        } else {
            messageService.warnUser("Solo se pueden crear OC para presupuestos en Espera");
        }
        return this;
    }

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            associateWith = "simple"
    )
    public Object newOrdenTrabajo() {
        if (this.getOrdenCompra() != null && this.getOrdenTrabajo() == null) {
            return repositoryService.persist(new OrdenTrabajo(this.nroPresupuesto, this));
        }
        else if (this.getOrdenTrabajo() != null) {
            messageService.warnUser("Ya existe una OT para este presupuesto");
        }
        else {
            messageService.warnUser("No se puede crear una OT sin haber creado antes una OC");
        }

        return this;
    }

    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "precio")
    public Presupuesto updatePrecio() {
        if (this.getEstado().equals(Estado.Espera)) {
            if (!this.items.isEmpty()) {
                Double suma = 0.0;
                for (Item item : items) {
                    suma += item.getPrecioTotal();
                }
                setPrecio(suma);
            } else {
                messageService.warnUser("El Listado de Items se encuentra vacio");
                setPrecio(0.0);
            }
        } else {
            messageService.warnUser("Solo los presupuestos en Espera pueden modificar su precio");
        }
        return this;
    }

    public void updatePreciosItems() {
        if (!this.items.isEmpty()) {
            Double suma = 0.0;
            for (Item item : items) {
                suma += item.getPrecioTotal();
            }
            setPrecio(suma);
        } else {
            messageService.warnUser("El Listado de Items se encuentra vacio");
            setPrecio(0.0);
        }
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, command = ENABLED, publishing = Publishing.ENABLED)
    public Presupuesto anularPresupuesto() {
        if (getEstado().equals(Estado.Espera)) {
            setEstado(Estado.Anulado);
            messageService.warnUser("Se a Anulado el presupuesto");
        } else if (getEstado().equals(Estado.Aprobado)) {
            messageService.warnUser("No se puede Anular un presupuesto Aprobado");
        } else {
            messageService.warnUser("El presupuesto ya estaba previamente Anulado");
        }
        return this;
    }

    @Override
    public String toString() {
        return getNroPresupuesto().toString();
    }

    @Override
    public int compareTo(final Presupuesto other) {
        return ComparisonChain.start()
                .compare(this.getNroPresupuesto().toString(), other.getNroPresupuesto().toString())
                .result();
    }

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE)
    @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE)
    @lombok.Setter(AccessLevel.NONE)
    PresupuestoRepository repositoryPresupuesto;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE)
    @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE)
    @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}


