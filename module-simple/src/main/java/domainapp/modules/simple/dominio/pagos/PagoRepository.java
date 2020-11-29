package domainapp.modules.simple.dominio.pagos;


import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;



@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Pago.class)

public class PagoRepository {

    @Programmatic
    public Pago create(

            final LocalDate fechaPago

    ) {

        final Pago pago = new Pago(fechaPago);
        repositoryService.persist(pago);
        return pago;
    }

    /*@Programmatic
    public List<OrdenTrabajo> Listar() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        OrdenTrabajo.class,
                        "find"));
    }*/

    @javax.inject.Inject
    RepositoryService repositoryService;
}
