package domainapp.modules.simple.dominio.pagos;




import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

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

    @Action()
    @ActionLayout(named = "Cargar Pago")
    @MemberOrder(sequence = "1")
    public Pago create(

            @Parameter(maxLength = 40)

            @ParameterLayout(named="Ingrese fecha de pago") final LocalDate fechaPago,
            @ParameterLayout(named="Monto") final Double monto

    ) {

        return repositoryPago.create(fechaPago,monto);
    }

    @javax.inject.Inject
    PagoRepository repositoryPago;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}
