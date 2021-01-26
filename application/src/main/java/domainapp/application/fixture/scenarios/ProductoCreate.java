package domainapp.application.fixture.scenarios;




import domainapp.modules.simple.dominio.producto.Producto;
import domainapp.modules.simple.dominio.producto.ProductoMenu;
import org.apache.isis.applib.fixturescripts.FixtureScript;



public class ProductoCreate extends FixtureScript {

    private String nombre;

    private String medida;

    private Double precioUnitario;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(final String medida) {
        this.medida = medida;
    }

    public Double getPrecioUnitario (){
        return precioUnitario;
    }

    public void setPrecioUnitario(final Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }


    private Producto producto;

    public Producto getProducto() {
        return producto;
    }


    //region > simpleObject (output)
    private Producto simpleObject;

    /**
     * The created simple object (output).
     * @return
     */
    public Producto getSimpleObject() {
        return simpleObject;
    }
    //endregion


    @Override
    protected void execute(final ExecutionContext ec) {

        String nombre = checkParam("nombre", ec, String.class);

        String medida = checkParam("medida", ec, String.class);

        Double precioUnitario = checkParam("precioUnitario", ec, Double.class);


        this.producto = wrap(menu).create(nombre,medida,precioUnitario);

        // also make available to UI
        ec.addResult(this, producto);
    }

    @javax.inject.Inject
    ProductoMenu menu;
}
