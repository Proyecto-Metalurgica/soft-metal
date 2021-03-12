package domainapp.modules.simple.dominio.ordenOT;



import domainapp.modules.simple.dominio.cliente.Cliente;
import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

import java.math.BigInteger;
import java.util.List;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = OrdenTrabajo.class)

public class OrdenTrabajoRepository {

    @Programmatic
    public OrdenTrabajo create(
            final BigInteger nroOT,
            final Presupuesto presupuesto

            ) {

        final OrdenTrabajo ordenTrabajo = new OrdenTrabajo(nroOT,presupuesto);
        repositoryService.persist(ordenTrabajo);
        return ordenTrabajo;
    }

    @Programmatic
    public List<OrdenTrabajo> ListarActivos() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        OrdenTrabajo.class,
                        "findAllActives"));
    }

    @Programmatic
    public List<OrdenTrabajo> ListarInactivos() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        OrdenTrabajo.class,
                        "findAllInactives"));
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
}
