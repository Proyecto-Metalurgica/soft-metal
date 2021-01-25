package domainapp.modules.simple.dominio.ordenOT;


import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

import java.util.List;


@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.OrdenTrabajoMenu",
        repositoryFor = OrdenTrabajo.class
)
@DomainServiceLayout(
        named = "Ordenes de trabajo",
        menuOrder = "10"
)
public class OrdenTrabajoMenu {

    @Action()
    @ActionLayout(named = "Cargar Orden de trabajo")
    @MemberOrder(sequence = "1")
    public OrdenTrabajo create(

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Nro de Orden de trabajo") final String nroOT,
            @ParameterLayout(named = "Estado OT") final EstadoOT estadoOT,
            @ParameterLayout(named="Fecha",cssClass="x-key" ) final LocalDate fecha

            ) {

        return repositoryOT.create(nroOT,estadoOT,fecha);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de Ordenes de Trabajo")
    @MemberOrder(sequence = "2")
    public List<OrdenTrabajo> listAll() {
        List<OrdenTrabajo> ordenesOT = repositoryOT.Listar();
        return ordenesOT;
    }

    @javax.inject.Inject
    OrdenTrabajoRepository repositoryOT;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}