package domainapp.modules.simple.dominio.ordenCompra;


import domainapp.modules.simple.dominio.ordenOT.EstadoOT;
import domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo;
import domainapp.modules.simple.dominio.ordenOT.OrdenTrabajoRepository;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.OrdenCompraMenu",
        repositoryFor = OrdenCompra.class
)
@DomainServiceLayout(
        named = "Ordenes de compra",
        menuOrder = "10"
)
public class OrdenCompraMenu {

    @Action()
    @ActionLayout(named = "Cargar Orden de Compra")
    @MemberOrder(sequence = "1")
    public OrdenCompra create(

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Nro de Orden de compra") final String nroCompra


    ) {

        return repositoryOC.create(nroCompra);
    }


    @javax.inject.Inject
    OrdenCompraRepository repositoryOC;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
