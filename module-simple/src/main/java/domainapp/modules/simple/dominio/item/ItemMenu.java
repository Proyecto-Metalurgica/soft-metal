
package domainapp.modules.simple.dominio.item;

import domainapp.modules.simple.dominio.item.QItem;
import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.datanucleus.query.typesafe.TypesafeQuery;

import java.util.List;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.SimpleItemMenu",
        repositoryFor = Item.class
)
@DomainServiceLayout(
        named = "ItemMenu",
        menuOrder = "10"
)
public class ItemMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Item> listAll() {
        return repositoryService.allInstances(Item.class);
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Item> findByName(
            @ParameterLayout(named = "Producto") final String producto
    ) {
        TypesafeQuery<Item> q = isisJdoSupport.newTypesafeQuery(Item.class);
        final QItem cand = QItem.candidate();
        q = q.filter(
                cand.producto.indexOf(q.stringParameter("producto")).ne(-1)
        );
        return q.setParameter("producto", producto)
                .executeList();
    }

    @Programmatic
    public Item findByNameExact(final String producto) {
        TypesafeQuery<Item> q = isisJdoSupport.newTypesafeQuery(Item.class);
        final QItem cand = QItem.candidate();
        q = q.filter(
                cand.producto.eq(q.stringParameter("producto"))
        );
        return q.setParameter("producto", producto)
                .executeUnique();
    }

    @Programmatic
    public void ping() {
        TypesafeQuery<Item> q = isisJdoSupport.newTypesafeQuery(Item.class);
        final QItem candidate = QItem.candidate();
        q.range(0, 2);
        q.orderBy(candidate.producto.asc());
        q.executeList();
    }

    public static class CreateDomainEvent extends ActionDomainEvent<ItemMenu> {
    }

    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "3")
    public Item create(
            @ParameterLayout(named = "Producto") final String producto,
            @ParameterLayout(named = "Medida") final String medida,
            @ParameterLayout(named = "Unidad") final String unidad,
            @ParameterLayout(named = "Precio") final String precio,
            @ParameterLayout(named = "Cantidad") final String cantidad,
            @ParameterLayout(named = "PrecioTotal") final String precioTotal,
            @ParameterLayout(named = "Detalle") final String detalle,
            @ParameterLayout(named = "Presupuesto") final Presupuesto presupuesto

    ) {
        return repositoryItem.create(producto,medida,unidad,precio, cantidad, precioTotal, detalle, presupuesto);
    }

    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    ItemRepository repositoryItem;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}