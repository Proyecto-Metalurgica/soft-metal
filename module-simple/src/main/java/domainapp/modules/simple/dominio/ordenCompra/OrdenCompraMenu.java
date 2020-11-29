package domainapp.modules.simple.dominio.ordenCompra;



import domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

import java.util.List;

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
            @ParameterLayout(named = "Nro de Orden de compra") final String nroCompra,
            @ParameterLayout(named = "Fecha de inicio ") final LocalDate fechaInicio,
            @ParameterLayout(named = "Fecha de entrega ") final LocalDate fechaEntrega,
            @ParameterLayout(named = "Lista de pagos") final String pagos



    ) {

        return repositoryOC.create(nroCompra,fechaInicio,fechaEntrega,pagos);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de Ordenes de Compra")
    @MemberOrder(sequence = "2")
    public List<OrdenCompra> listAll() {
        List<OrdenCompra> ordenesOC = repositoryOC.Listar();
        return ordenesOC;
    }


    @javax.inject.Inject
    OrdenCompraRepository repositoryOC;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
