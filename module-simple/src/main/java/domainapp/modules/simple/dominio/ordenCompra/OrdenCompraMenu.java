package domainapp.modules.simple.dominio.ordenCompra;



import domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo;
import domainapp.modules.simple.dominio.pagos.Pago;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

import java.math.BigInteger;
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
