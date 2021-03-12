package domainapp.modules.simple.dominio.pagos;




import domainapp.modules.simple.dominio.ordenOT.OrdenTrabajo;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

import java.util.List;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.PagoMenu",
        repositoryFor = Pago.class
)
@DomainServiceLayout(
        named = "Pagos",
        menuOrder = "10"
)

public class PagoMenu {


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de Pagos")
    @MemberOrder(sequence = "2")
    public List<Pago> listAll() {
        List<Pago> pagos = repositoryPago.Listar();
        return pagos;
    }

    @javax.inject.Inject
    PagoRepository repositoryPago;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
