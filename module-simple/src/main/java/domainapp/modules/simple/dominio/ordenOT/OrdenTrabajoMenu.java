package domainapp.modules.simple.dominio.ordenOT;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;


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
            @ParameterLayout(named = "Estado OT") final EstadoOT estadoOT


    ) {

        return repositoryOT.create(nroOT,estadoOT);
    }

    @javax.inject.Inject
    OrdenTrabajoRepository repositoryOT;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}