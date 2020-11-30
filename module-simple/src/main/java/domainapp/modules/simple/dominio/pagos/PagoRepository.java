package domainapp.modules.simple.dominio.pagos;


import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

import java.util.List;


@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Pago.class)

public class PagoRepository {

    @Programmatic
    public Pago create(

            final LocalDate fechaPago,
            final  Double monto,
            final FormaPago fpago

    ) {

        final Pago pago = new Pago(fechaPago,monto,fpago);
        repositoryService.persist(pago);
        return pago;
    }

    @Programmatic
    public List<Pago> Listar() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Pago.class,
                        "find"));
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
}
