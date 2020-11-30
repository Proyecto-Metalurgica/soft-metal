package domainapp.modules.simple.dominio.ordenCompra;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

import java.util.List;


@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = OrdenCompra.class)
public class OrdenCompraRepository {

    @Programmatic
    public OrdenCompra create(
            final String nroCompra,
            final LocalDate fechaInicio,
            final LocalDate fechaEntrega

            ) {

        final OrdenCompra ordenCompra = new OrdenCompra(nroCompra,fechaInicio,fechaEntrega);
        repositoryService.persist(ordenCompra);
        return ordenCompra;
    }

    @Programmatic
    public List<OrdenCompra> Listar() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        OrdenCompra.class,
                        "find"));
    }

    @javax.inject.Inject
    RepositoryService repositoryService;

}
