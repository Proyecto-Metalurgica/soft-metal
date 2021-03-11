
package domainapp.modules.simple.dominio.producto;

        import java.util.List;



        import org.apache.isis.applib.annotation.*;
        import org.datanucleus.query.typesafe.TypesafeQuery;

        import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
        import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
        import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.SimpleProductoMenu",
        repositoryFor = Producto.class
)
@DomainServiceLayout(
        named = "Productos",
        menuOrder = "10"
)
public class ProductoMenu {

    @Action()
    @ActionLayout(named = "Cargar Producto")
    @MemberOrder(sequence = "1")
    public Producto create(

            @Parameter(maxLength = 13)
            @ParameterLayout(named = "Nombre de producto") final String nombre,

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Medida") final String medida,

            @ParameterLayout(named = "Precio Unitario") final Double precioUnitario

            ) {

        return repositoryProducto.create(nombre.toUpperCase(),medida,precioUnitario);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar productos")
    @MemberOrder(sequence = "2")
    public List<Producto> findByName(
            @Parameter(optionality = Optionality.MANDATORY)
            @ParameterLayout(named = "Nombre del producto") final String nombre
    ) {
        TypesafeQuery<Producto> q = isisJdoSupport.newTypesafeQuery(Producto.class);
        final QProducto cand = QProducto.candidate();
        q = q.filter(
                cand.nombre.indexOf(q.stringParameter("nombre")).ne(-1)
        );
        return q.setParameter("nombre", nombre)
                .executeList();
    }

    @Programmatic
    public Producto findByNameExact(final String nombre) {
        TypesafeQuery<Producto> q = isisJdoSupport.newTypesafeQuery(Producto.class);
        final QProducto cand = QProducto.candidate();
        q = q.filter(
                cand.nombre.eq(q.stringParameter("nombre"))
        );
        return q.setParameter("nombre", nombre)
                .executeUnique();
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de productos")
    @MemberOrder(sequence = "3")
    public List<Producto> listAll() {
        List<Producto> productos = repositoryProducto.Listar();
        return productos;
    }

    @javax.inject.Inject
    ProductoRepository repositoryProducto;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}