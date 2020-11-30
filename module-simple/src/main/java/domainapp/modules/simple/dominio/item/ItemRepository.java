package domainapp.modules.simple.dominio.item;

import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import java.util.List;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Item.class)

public class ItemRepository {

    @Programmatic
    public Item create(
            final String producto,
            final String medida,
            final Double precio,
            final Integer cantidad,
            final Double precioTotal,
            final String detalle,
            final Presupuesto presupuesto

    ) {

        final Item item = new Item(producto,medida,precio,cantidad,precioTotal,detalle,presupuesto);
        repositoryService.persist(item);
        return item;
    }

    @Programmatic
    public List<Item> Listar() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Item.class,
                        "find"));
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
}
