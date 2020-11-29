package domainapp.modules.simple.dominio.ordenOT;


import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = OrdenTrabajo.class)

public class OrdenTrabajoRepository {

    @Programmatic
    public OrdenTrabajo create(
            final String nroOT,
            final EstadoOT estadoOT,
            final LocalDate fecha

            ) {

        final OrdenTrabajo ordenTrabajo = new OrdenTrabajo(nroOT,estadoOT,fecha);
        repositoryService.persist(ordenTrabajo);
        return ordenTrabajo;
    }


    @javax.inject.Inject
    RepositoryService repositoryService;
}
