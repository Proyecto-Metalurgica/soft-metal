package domainapp.modules.simple.dominio.producto;


import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import java.util.List;

    @DomainService(
            nature = NatureOfService.DOMAIN,
            repositoryFor = Producto.class)


    public class ProductoRepository {

        @Programmatic
        public Producto create(
                final String nombre,
                final String medida,
                final Double precioUnitario
                ) {

            final Producto producto = new Producto(nombre,medida,precioUnitario);
            repositoryService.persist(producto);
            return producto;
        }

        @Programmatic
        public List<Producto> Listar() {
            return repositoryService.allMatches(
                    new QueryDefault<>(
                            Producto.class,
                            "find"));
        }



        @javax.inject.Inject
        RepositoryService repositoryService;

    }







