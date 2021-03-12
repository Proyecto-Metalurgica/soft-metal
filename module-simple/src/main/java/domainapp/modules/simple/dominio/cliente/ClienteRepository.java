package domainapp.modules.simple.dominio.cliente;


import domainapp.modules.simple.dominio.reportes.EjecutarReportes;
import net.sf.jasperreports.engine.JRException;
import org.apache.isis.applib.query.QueryDefault;

import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.value.Blob;


import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
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
    public List<Cliente> ListarActivos() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Cliente.class,
                        "findAllActives"));
    }

    @Programmatic
    public List<Cliente> ListarInactivos() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Cliente.class,
                        "findAllInactives"));
    }

    @Programmatic
    public Blob generarReporteClientes()throws JRException, IOException
    {

        List<Cliente> clientes = new ArrayList<Cliente>();

        EjecutarReportes ejecutarReportes=new EjecutarReportes();

        clientes = repositoryService.allInstances(Cliente.class);

        return ejecutarReportes.ListadoClientesPDF(clientes);
    }

    @javax.inject.Inject
    EjecutarReportes ejecutarReportes;

    @javax.inject.Inject
    RepositoryService repositoryService;



}



