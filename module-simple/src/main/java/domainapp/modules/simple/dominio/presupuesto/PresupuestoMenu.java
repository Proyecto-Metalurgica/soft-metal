package domainapp.modules.simple.dominio.presupuesto;

import domainapp.modules.simple.dominio.reportes.EjecutarReportes;
import net.sf.jasperreports.engine.JRException;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.value.Blob;
import org.datanucleus.query.typesafe.TypesafeQuery;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.PresupuestoMenu",
        repositoryFor = Presupuesto.class
)
@DomainServiceLayout(
        named = "Presupuestos",
        menuOrder = "10"
)
public class PresupuestoMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT,named = "Buscar por Nro Presupuesto")
    @MemberOrder(sequence = "2")
    public List<Presupuesto> findByNroPresupuesto(
            @Parameter(optionality = Optionality.MANDATORY)
            @ParameterLayout(named = "Nro de Presupuesto") final BigInteger nroPresupuesto
    ) {
        TypesafeQuery<Presupuesto> q = isisJdoSupport.newTypesafeQuery(Presupuesto.class);
        final QPresupuesto cand = QPresupuesto.candidate();
        q = q.filter(
                cand.nroPresupuesto.eq(q.integerParameter("nroPresupuesto"))
        );
        return q.setParameter("nroPresupuesto", nroPresupuesto)
                .executeList();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de Presupuestos")
    @MemberOrder(sequence = "2")
    public List<Presupuesto> listAll() {
        List<Presupuesto> presupuestos = repositoryPresupuesto.Listar();
        return presupuestos;
    }

    @Action()
    public Blob ExportarListado() throws JRException, IOException {
        EjecutarReportes ejecutar = new EjecutarReportes();
        return ejecutar.ListadoPresupuestosPDF(repositoryPresupuesto.Listar());
    }



    @javax.inject.Inject
    PresupuestoRepository repositoryPresupuesto;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}