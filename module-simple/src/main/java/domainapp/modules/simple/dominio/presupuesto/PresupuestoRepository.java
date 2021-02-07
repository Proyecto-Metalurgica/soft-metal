package domainapp.modules.simple.dominio.presupuesto;

import domainapp.modules.simple.dominio.cliente.Cliente;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import java.util.List;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Presupuesto.class)

public class PresupuestoRepository {

    @Programmatic
    public Presupuesto create(
            final Cliente cliente
    ) {
        final Presupuesto presupuesto = new Presupuesto(cliente);
        repositoryService.persist(presupuesto);
        return presupuesto;
    }

    @Programmatic
    public List<Presupuesto> Listar() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Presupuesto.class,
                        "find"));
    }

    @Programmatic
    public List<Presupuesto> ListarUltimos() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Presupuesto.class,
                        "findLast"));
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
}
