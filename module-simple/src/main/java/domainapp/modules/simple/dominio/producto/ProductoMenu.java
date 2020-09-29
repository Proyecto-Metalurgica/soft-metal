
package domainapp.modules.simple.dominio.producto;

        import java.util.List;


        import org.datanucleus.query.typesafe.TypesafeQuery;

        import org.apache.isis.applib.annotation.Action;
        import org.apache.isis.applib.annotation.ActionLayout;
        import org.apache.isis.applib.annotation.BookmarkPolicy;
        import org.apache.isis.applib.annotation.DomainService;
        import org.apache.isis.applib.annotation.DomainServiceLayout;
        import org.apache.isis.applib.annotation.MemberOrder;
        import org.apache.isis.applib.annotation.NatureOfService;
        import org.apache.isis.applib.annotation.ParameterLayout;
        import org.apache.isis.applib.annotation.Programmatic;
        import org.apache.isis.applib.annotation.SemanticsOf;
        import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
        import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
        import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.SimpleMaterialMenu",
        repositoryFor = Producto.class
)
@DomainServiceLayout(
        named = "ProductoMenu",
        menuOrder = "10"
)
public class ProductoMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Producto> listAll() {
        return repositoryService.allInstances(Producto.class);
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Producto> findByName(
            @ParameterLayout(named = "Tipo") final String tipo
    ) {
        TypesafeQuery<Producto> q = isisJdoSupport.newTypesafeQuery(Producto.class);
        final QProducto cand = QProducto.candidate();
        q = q.filter(
                cand.tipo.indexOf(q.stringParameter("tipo")).ne(-1)
        );
        return q.setParameter("tipo", tipo)
                .executeList();
    }

    @Programmatic
    public Producto findByNameExact(final String tipo) {
        TypesafeQuery<Producto> q = isisJdoSupport.newTypesafeQuery(Producto.class);
        final QProducto cand = QProducto.candidate();
        q = q.filter(
                cand.tipo.eq(q.stringParameter("tipo"))
        );
        return q.setParameter("tipo", tipo)
                .executeUnique();
    }

    @Programmatic
    public void ping() {
        TypesafeQuery<Producto> q = isisJdoSupport.newTypesafeQuery(Producto.class);
        final QProducto candidate = QProducto.candidate();
        q.range(0, 2);
        q.orderBy(candidate.tipo.asc());
        q.executeList();
    }

    public static class CreateDomainEvent extends ActionDomainEvent<ProductoMenu> {
    }

    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "3")
    public Producto create(
            @ParameterLayout(named = "Tipo") final String tipo,
            @ParameterLayout(named = "Medida") final String medida,
            @ParameterLayout(named = "Unidad") final String unidad,
            @ParameterLayout(named = "Precio") final String precio

    ) {
        return repositoryService.persist(new Producto(tipo, medida, unidad, precio));
    }

    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}