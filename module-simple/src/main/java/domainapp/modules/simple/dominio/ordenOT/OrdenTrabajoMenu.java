package domainapp.modules.simple.dominio.ordenOT;


import domainapp.modules.simple.dominio.cliente.Cliente;
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

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de OT Activas")
    @MemberOrder(sequence = "3")
    public List<OrdenTrabajo> listAllActive() {
        List<OrdenTrabajo> ordenesOT = repositoryOT.ListarActivos();
        return ordenesOT;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de OT Inactivas")
    @MemberOrder(sequence = "3")
    public List<OrdenTrabajo> listAllInactive() {
        List<OrdenTrabajo> ordenesOT = repositoryOT.ListarInactivos();
        return ordenesOT;
    }

    @javax.inject.Inject
    OrdenTrabajoRepository repositoryOT;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}