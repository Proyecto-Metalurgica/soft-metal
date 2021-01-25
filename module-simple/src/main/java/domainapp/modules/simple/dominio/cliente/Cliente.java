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
import domainapp.modules.simple.dominio.presupuesto.PresupuestoRepository;
import lombok.NonNull;
import org.apache.isis.applib.annotation.*;

import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;

import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.AccessLevel;

import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy= IdGeneratorStrategy.IDENTITY, column="id")
@Sequence(name="clienteseq", datastoreSequence="YOUR_SEQUENCE_NAME", strategy=SequenceStrategy.CONTIGUOUS, initialValue = 10000, allocationSize = 1)
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@Queries({
        @Query(
                name = "findAllActives", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dominio.cliente.Cliente "
                        + "WHERE activo == true "),
        @Query(
                name = "findAllInactives", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dominio.cliente.Cliente "
                        + "WHERE activo == false "),
})

@javax.jdo.annotations.Unique(name="Cliente_name_UNQ", members = {"cuil"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout(cssClassFa="user-circle" )  // causes UI events to be triggered
@lombok.Getter @lombok.Setter
@lombok.RequiredArgsConstructor
public class Cliente implements Comparable<Cliente> {

    @Column(allowsNull = "true", length = 10)
    @Property(editing = Editing.DISABLED)
    @Persistent(valueStrategy=IdGeneratorStrategy.SEQUENCE, sequence="clienteseq")
    private BigInteger nroCliente;

    @Column(allowsNull = "true", length = 13)
    @NonNull
    @Property()
    private String cuil;

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Cliente: ")
    private String name;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED,
            regexPattern = "[0-9]+",
            regexPatternReplacement = "Solo numeros y sin espacios"
    )
    private String telefono;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED,
            regexPattern = "(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+",
            regexPatternFlags= Pattern.CASE_INSENSITIVE,
            regexPatternReplacement = "Debe ser un email valido (contiene un '@' simbolo)"
    )
    private String email;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String direccion;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property()
    private Boolean activo = true;

    @javax.jdo.annotations.Persistent(
            mappedBy = "cliente",
            dependentElement = "false"
    )
    @CollectionLayout(defaultView = "table")
    @lombok.Getter @lombok.Setter
    private SortedSet<Presupuesto> presupuestos = new TreeSet<Presupuesto>();

    @Action(
            semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE,
            associateWith = "simple"
    )
    public Cliente newPresupuesto() {
        if(activo){
            repositoryPresupuesto.create(this);
        }
        else{
            messageService.warnUser(
                    "El Cliente "+ this.getName() + " se encuentra Inactivo, no puede crear nuevos presupuestos");
        }
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "activo")
    public Cliente updateActivo()
    {
        if(getActivo()){ setActivo(false); }
        else{ setActivo(true); }
        return this;
    }

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
    PresupuestoRepository repositoryPresupuesto;

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