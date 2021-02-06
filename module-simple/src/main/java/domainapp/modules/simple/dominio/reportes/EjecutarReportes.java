package domainapp.modules.simple.dominio.reportes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domainapp.modules.simple.dominio.cliente.Cliente;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.isis.applib.value.Blob;


import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public class EjecutarReportes {


    public Blob  ListadoClientesPDF(List<Cliente> clientes)throws JRException, IOException{

        List<RepoClientes> clientesDatasource = new ArrayList<RepoClientes>();

        clientesDatasource.add(new RepoClientes());

        for (Cliente cli : clientes) {

            RepoClientes repoClientes = new RepoClientes(cli.ReporCuil(),cli.ReporName(),cli.ReporTelefono(),cli.ReporEmail(),cli.ReporDireccion());

            clientesDatasource.add(repoClientes);

        }

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(clientesDatasource);
        return GenerarArchivoPDF("ListadoCliente.jrxml","ListadoClientes.pdf", ds);
    }


    private Blob GenerarArchivoPDF(String archivoDesing, String nombreSalida, JRBeanCollectionDataSource ds) throws JRException, IOException{

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(archivoDesing);
        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ds", ds);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
        byte[] contentBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return new Blob(nombreSalida, "application/pdf", contentBytes);
    }



}
