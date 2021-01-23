package domainapp.modules.simple.dominio.pagos;


import domainapp.modules.simple.dominio.ordenCompra.OrdenCompra;
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

            final String nroPago,
            final OrdenCompra ordenCompra

            ) {

        final Pago pago = new Pago(nroPago,ordenCompra);
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
