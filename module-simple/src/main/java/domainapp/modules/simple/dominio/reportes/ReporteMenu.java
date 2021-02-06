package domainapp.modules.simple.dominio.reportes;

import domainapp.modules.simple.dominio.cliente.ClienteRepository;

import lombok.AccessLevel;
import net.sf.jasperreports.engine.JRException;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.value.Blob;


import java.io.IOException;



@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.ReporteMenu"

)
@DomainServiceLayout(
        named = "Reportes",
        menuOrder = "10"
)
public class ReporteMenu {

    @Action(
            semantics = SemanticsOf.SAFE,
            restrictTo = RestrictTo.PROTOTYPING
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT,
            named = "Reporte de Clientes"
    )
    @MemberOrder(sequence = "1")
    /**
     *  reporte de todos los clientes cargados en el sistema
     *
     */
    public Blob generarReporteClientes(
    ) throws JRException, IOException {
        return repositoryCliente.generarReporteClientes();
    }

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    ClienteRepository repositoryCliente;
}