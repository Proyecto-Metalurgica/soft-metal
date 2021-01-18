package domainapp.modules.simple.dominio.cliente;


import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;


import java.math.BigInteger;
import java.util.List;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Cliente.class)


public class ClienteRepository {

    @Programmatic
    public Cliente create(
            final String cuil,
            final String name,
            final String telefono,
            final String email,
            final String direccion) {

        final Cliente cliente = new Cliente(cuil,name,telefono,email,direccion);
        repositoryService.persist(cliente);
        return cliente;
    }

    @Programmatic
    public List<Cliente> Listar() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Cliente.class,
                        "find"));
    }



    @javax.inject.Inject
    RepositoryService repositoryService;

}



