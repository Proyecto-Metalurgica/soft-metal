package domainapp.modules.simple.dominio.presupuesto;

import domainapp.modules.simple.dominio.cliente.Cliente;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

import java.util.List;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Presupuesto.class)

public class PresupuestoRepository {

    @Programmatic
    public Presupuesto create(
            final String nroPresupuesto,
            final LocalDate fecha,
            final Cliente cliente,
            final String cantidad,
            final String medida,
            final String tipoMaterial,
            final String precio

    ) {

        final Presupuesto presupuesto = new Presupuesto(nroPresupuesto,fecha,cliente,cantidad,medida,tipoMaterial,precio);
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

    @javax.inject.Inject
    RepositoryService repositoryService;
}
