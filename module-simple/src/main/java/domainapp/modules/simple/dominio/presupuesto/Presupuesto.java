package domainapp.modules.simple.dominio.presupuesto;


import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import com.google.common.collect.ComparisonChain;


import org.apache.isis.applib.annotation.*;

import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.AccessLevel;

import java.time.LocalDateTime;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;


@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")

@javax.jdo.annotations.Unique(name="Presupuesto_name_UNQ", members = {"nroPresupuesto"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor

public class Presupuesto {

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property()
    @Title(prepend = "Presupuesto: ")

    private String nroPresupuesto;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private LocalDateTime fecha;


    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @Property
    private String producto;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private Integer cantidad;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private String medida;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private String tipoMaterial;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private String precio;


    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "nroPresupuesto")
    public Presupuesto updatePresupuesto(
            @ParameterLayout(named = "Numero de Presupuesto: ") final String nroPresupuesto,

            @ParameterLayout(named = "Fecha: ") final java.time.LocalDateTime fecha,

            @ParameterLayout(named = "Item: ") final String producto,

            @ParameterLayout(named = "Cantidad: ") final Integer cantidad,

            @ParameterLayout(named = "Medidas: ") final String medida,

            @ParameterLayout(named = "Tipo de Item: ") final String tipoMaterial,

            @ParameterLayout(named = "Precio: ") final String precio

    ) {

        setNroPresupuesto(nroPresupuesto);
        setFecha(fecha);
        setProducto(producto);
        setCantidad(cantidad);
        setMedida(medida);
        setTipoMaterial(tipoMaterial);
        setPrecio(precio);

        return this;
    }

    public String default0UpdatePresupuesto() {
        return getNroPresupuesto();
    }

    public TranslatableString validate0UpdatePresupuesto(final String nroPresupuesto) {
        return nroPresupuesto != null && nroPresupuesto.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.remove(this);
    }

    @Override
    public String toString() {
        return getNroPresupuesto();
    }


    public int compareTo(final Presupuesto other) {
        return ComparisonChain.start()
                .compare(this.getNroPresupuesto(), other.getNroPresupuesto())
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


