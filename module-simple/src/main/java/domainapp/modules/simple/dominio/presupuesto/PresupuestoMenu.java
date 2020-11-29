package domainapp.modules.simple.dominio.presupuesto;

import domainapp.modules.simple.dominio.cliente.Cliente;
import domainapp.modules.simple.dominio.cliente.ClienteRepository;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
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
            @ParameterLayout(named = "Cantidad") final String cantidad,
            @ParameterLayout(named = "Medida") final String medida,
            @ParameterLayout(named = "Tipo Material") final String tipoMaterial,
            @ParameterLayout(named = "precio") final String precio


    ) {
        return repositoryPresupuesto.create(nroPresupuesto,fecha,cliente,cantidad,medida,tipoMaterial,precio);
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