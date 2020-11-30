package domainapp.modules.simple.dominio.presupuesto;

import domainapp.modules.simple.dominio.cliente.Cliente;
import domainapp.modules.simple.dominio.cliente.ClienteRepository;
import domainapp.modules.simple.dominio.presupuesto.QPresupuesto;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.datanucleus.query.typesafe.TypesafeQuery;
import org.joda.time.LocalDate;

import java.util.List;


@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.PresupuestoMenu",
        repositoryFor = Presupuesto.class
)
@DomainServiceLayout(
        named = "Presupuestos",
        menuOrder = "10"
)
public class PresupuestoMenu {

    @Action()
    @ActionLayout(named = "Crear un Presupuesto")
    @MemberOrder(sequence = "1")
    public Presupuesto create(

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Nro de Presupuesto") final String nroPresupuesto,
            @ParameterLayout(named = "Fecha") final LocalDate fecha,
            @ParameterLayout(named = "Cliente") final Cliente cliente,
            @ParameterLayout(named = "precio") final String precio


    ) {
        return repositoryPresupuesto.create(nroPresupuesto,fecha,cliente,precio);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT,named = "Buscar presupuesto")
    @MemberOrder(sequence = "2")
    public List<Presupuesto> findByName(
            @Parameter(optionality = Optionality.MANDATORY)
            @ParameterLayout(named = "Nro del Presupuesto") final String nroPresupuesto
    ) {
        TypesafeQuery<Presupuesto> q = isisJdoSupport.newTypesafeQuery(Presupuesto.class);
        final QPresupuesto cand = QPresupuesto.candidate();
        q = q.filter(
                cand.nroPresupuesto.indexOf(q.stringParameter("name")).ne(-1)
        );
        return q.setParameter("nroPresupuesto", nroPresupuesto)
                .executeList();
    }

    @Programmatic
    public Presupuesto findByNameExact(final String nroPresupuesto) {
        TypesafeQuery<Presupuesto> q = isisJdoSupport.newTypesafeQuery(Presupuesto.class);
        final QPresupuesto cand = QPresupuesto.candidate();
        q = q.filter(
                cand.nroPresupuesto.eq(q.stringParameter("nroPresupuesto"))
        );
        return q.setParameter("nroPresupuesto", nroPresupuesto)
                .executeUnique();
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de Presupuestos")
    @MemberOrder(sequence = "2")
    public List<Presupuesto> listAll() {
        List<Presupuesto> presupuestos = repositoryPresupuesto.Listar();
        return presupuestos;
    }

    @javax.inject.Inject
    PresupuestoRepository repositoryPresupuesto;

    @javax.inject.Inject
    ClienteRepository repositoryCliente;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}