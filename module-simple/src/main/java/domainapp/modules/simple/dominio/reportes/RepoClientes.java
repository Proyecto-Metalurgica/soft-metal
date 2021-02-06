package domainapp.modules.simple.dominio.reportes;

@lombok.Getter @lombok.Setter
public class RepoClientes {

    private String cuil;
    private String name;
    private String telefono;
    private String email;
    private String direccion;



    public RepoClientes(String cuil, String name, String telefono, String email, String direccion){
        this.cuil=cuil;
        this.name=name;
        this.telefono=telefono;
        this.email=email;
        this.direccion=direccion;


    }

    public RepoClientes(){}

    public String getCuil() { return this.cuil;}



    public String getName(){
        return this.name;
    }



    public String getTelefono(){
        return this.telefono;
    }



    public String getEmail(){
        return this.email;
    }



    public String getDireccion(){
        return this.direccion;
    }







}
