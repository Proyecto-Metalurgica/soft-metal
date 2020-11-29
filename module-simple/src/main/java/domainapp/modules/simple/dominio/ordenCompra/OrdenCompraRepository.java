package domainapp.modules.simple.dominio.ordenCompra;



import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.repository.RepositoryService;


@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = OrdenCompra.class)
public class OrdenCompraRepository {

    @Programmatic
    public OrdenCompra create(
            final String nroCompra

    ) {

        final OrdenCompra ordenCompra = new OrdenCompra(nroCompra);
        repositoryService.persist(ordenCompra);
        return ordenCompra;
    }


    @javax.inject.Inject
    RepositoryService repositoryService;

}
