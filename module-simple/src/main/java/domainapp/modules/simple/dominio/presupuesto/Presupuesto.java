package domainapp.modules.simple.dominio.presupuesto;


import javax.jdo.annotations.*;

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
@DomainObjectLayout()
@lombok.Getter
@lombok.Setter
@lombok.RequiredArgsConstructor
public class Presupuesto implements Comparable<Presupuesto> {

    @Column(allowsNull = "true", length = 10)
    @Property(editing = Editing.DISABLED)
    @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE, sequence = "presupuestoseq")
    @Title(prepend = "Nro: ")
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
    @Title()
    private Cliente cliente;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property(editing = Editing.DISABLED)
    private Double precio;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private Estado estado = Estado.Espera;

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
    public Item newItem(@ParameterLayout(named = "Nro Item") final Integer nroItem) {
        return repositoryService.persist(new Item(this, nroItem));
    }

    public Presupuesto(Cliente cliente) {
        this.cliente = cliente;
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


