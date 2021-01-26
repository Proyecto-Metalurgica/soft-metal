package domainapp.application.fixture.scenarios;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import domainapp.modules.simple.dominio.producto.Producto;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScript;



public class ProductoRecreate extends FixtureScript {

    public final List<String> nombres = Collections.unmodifiableList(Arrays.asList(
            "Mesa",
            "Silla",
            "Puerta",
            "Porton"
    ));

    public final List<String> medidas = Collections.unmodifiableList(Arrays.asList(
            "2x2",
            "1x1",
            "220x3",
            "5x5"

    ));

    public final List<Double> precios = Collections.unmodifiableList(Arrays.asList(
           2000.00,
            100.00,
            60000.00,
            8000.50

    ));

    public ProductoRecreate() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    //region > number (optional input)
    private Integer number;

    /**
     * The number of objects to create, up to 10; optional, defaults to 3.
     */
    public Integer getNumber() {
        return number;
    }

    public ProductoRecreate setNumber(final Integer number) {
        this.number = number;
        return this;
    }
    //endregion

    //region > simpleObjects (output)
    private final List<Producto> simpleObjects = Lists.newArrayList();

    /**
     * The simpleobjects created by this fixture (output).
     */
    @Programmatic
    public List<Producto> getSimpleObjects() {
        return simpleObjects;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext ec) {

        // defaults
        final int number = defaultParam("number", ec, 1);

        // validate
        if(number < 0 || number > nombres.size()) {
            throw new IllegalArgumentException(String.format("number must be in range [0,%d)", nombres.size()));
        }

        //
        // execute
        //
        ec.executeChild(this, new ProductoTearDown());

        for (int i = 0; i < number; i++) {
            final ProductoCreate hc = new ProductoCreate();
            hc.setNombre(nombres.get(i));
            hc.setMedida(medidas.get(i));
            hc.setPrecioUnitario(precios.get(i));

            ec.executeChild(this, hc.getNombre(), hc);
            simpleObjects.add(hc.getSimpleObject());
        }
    }
}

