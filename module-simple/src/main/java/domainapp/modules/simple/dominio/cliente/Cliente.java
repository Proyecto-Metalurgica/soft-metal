/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.modules.simple.dominio.cliente;

import javax.jdo.annotations.*;

import com.google.common.collect.ComparisonChain;

import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import lombok.NonNull;
import org.apache.isis.applib.annotation.*;

import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;

import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.AccessLevel;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "),})

@javax.jdo.annotations.Unique(name="Cliente_name_UNQ", members = {"name"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class Cliente implements Comparable<Cliente> {
    @Column(allowsNull = "true", length = 40)
    @NonNull
    @Property(editing = Editing.ENABLED)
    private String nroCliente;

    @Column(allowsNull = "true", length = 13)
    @NonNull
    @Property(editing = Editing.ENABLED)
    private String cuil;

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Cliente: ")
    private String name;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String telefono;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String email;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String direccion;

    @javax.jdo.annotations.Persistent(
            mappedBy = "cliente",
            dependentElement = "false"
    )
    @Collection
    @lombok.Getter @lombok.Setter
    private SortedSet<Presupuesto> presupuestos = new TreeSet<Presupuesto>();


    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "name")
    public Cliente updateName(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Nro de Cliente") final String nroCliente,
            @ParameterLayout(named = "CUIL/CUIT") final String cuil,
            @ParameterLayout(named = "Name") final String name,
            @ParameterLayout(named = "Telefono") final String telefono,
            @ParameterLayout(named = "Email") final String email,
            @ParameterLayout(named = "Direccion") final String direccion){
        setNroCliente(nroCliente);
        setCuil(cuil);
        setName(name);
        setTelefono(telefono);
        setEmail(email);
        setDireccion(direccion);
        return this;
    }

    public String default0UpdateName() {
        return getName();
    }

    public TranslatableString validate0UpdateName(final String name) {
        return name != null && name.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            associateWith = "simple"
    )
    public Presupuesto newPresupuesto(final String nroPresupuesto) {
        return repositoryService.persist(new Presupuesto(this, nroPresupuesto));
    }
/*

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryCliente.remove(this);
    }
*/

    /*@Action(semantics=SemanticsOf.IDEMPOTENT)
    public Item addProducto(Producto producto) {
        this.producto = producto.getNombre();
        this.medida = producto.getMedida();
        this.unidad = producto.getUnidad();
        this.precio = producto.getPrecio();
        return this;
    }
    public List<Producto> autoComplete0AddProducto(
            @MinLength(1)
                    String searchTerm) {
        return productoMenu.findByName(searchTerm);
    }
*/

    @Override
    public String toString() {
        return getName();
    }

    public int compareTo(final Cliente other) {
        return ComparisonChain.start()
                .compare(this.getName(), other.getName())
                .result();
    }


    @javax.jdo.annotations.NotPersistent
    @javax.inject.Inject
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    ClienteRepository repositoryCliente;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}