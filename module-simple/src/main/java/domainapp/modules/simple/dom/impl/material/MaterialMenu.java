
package domainapp.modules.simple.dom.impl.material;

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
        objectType = "simple.SimpleObjectMenus",
        repositoryFor = Material.class
)
@DomainServiceLayout(
        named = "MaterialMenu",
        menuOrder = "10"
)
public class MaterialMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Material> listAll() {
        return repositoryService.allInstances(Material.class);
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Material> findByName(
            @ParameterLayout(named = "Tipo") final String tipo
    ) {
        TypesafeQuery<Material> q = isisJdoSupport.newTypesafeQuery(Material.class);
        final QMaterial cand = QMaterial.candidate();
        q = q.filter(
                cand.tipo.indexOf(q.stringParameter("tipo")).ne(-1)
        );
        return q.setParameter("tipo", tipo)
                .executeList();
    }

    @Programmatic
    public Material findByNameExact(final String tipo) {
        TypesafeQuery<Material> q = isisJdoSupport.newTypesafeQuery(Material.class);
        final QMaterial cand = QMaterial.candidate();
        q = q.filter(
                cand.tipo.eq(q.stringParameter("tipo"))
        );
        return q.setParameter("tipo", tipo)
                .executeUnique();
    }

    @Programmatic
    public void ping() {
        TypesafeQuery<Material> q = isisJdoSupport.newTypesafeQuery(Material.class);
        final QMaterial candidate = QMaterial.candidate();
        q.range(0, 2);
        q.orderBy(candidate.tipo.asc());
        q.executeList();
    }

    public static class CreateDomainEvent extends ActionDomainEvent<MaterialMenu> {
    }

    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "3")
    public Material create(
            @ParameterLayout(named = "Tipo") final String tipo,
            @ParameterLayout(named = "Medida") final String medida,
            @ParameterLayout(named = "Unidad") final String unidad,
            @ParameterLayout(named = "Precio") final String precio

    ) {
        return repositoryService.persist(new Material(tipo, medida, unidad, precio));
    }

    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}