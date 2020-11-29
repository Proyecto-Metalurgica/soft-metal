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

import java.util.List;


import org.apache.isis.applib.annotation.*;
import org.datanucleus.query.typesafe.TypesafeQuery;


import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.SimpleClienteMenu",
        repositoryFor = Cliente.class
)
@DomainServiceLayout(
        named = "Clientes",
        menuOrder = "10"
)
public class ClienteMenu {


    @Action()
    @ActionLayout(named = "Alta Cliente")
    @MemberOrder(sequence = "1")
    public Cliente create(

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Numero de Cliente") final String nroCliente,

            @Parameter(maxLength = 13)
            @ParameterLayout(named = "CUIT/CUIL") final String cuil,

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Nombre del Cliente") final String name,

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Telefono") final String telefono,

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Email") final String email,

            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Direccion") final String direccion) {

        return repositoryCliente.create(nroCliente, cuil, name, telefono, email, direccion);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT,named = "Buscar cliente")
    @MemberOrder(sequence = "2")
    public List<Cliente> findByName(
            @Parameter(optionality = Optionality.MANDATORY)
            @ParameterLayout(named = "Nombre del Cliente") final String name
    ) {
        TypesafeQuery<Cliente> q = isisJdoSupport.newTypesafeQuery(Cliente.class);
        final QCliente cand = QCliente.candidate();
        q = q.filter(
                cand.name.indexOf(q.stringParameter("name")).ne(-1)
        );
        return q.setParameter("name", name)
                .executeList();
    }

    @Programmatic
    public Cliente findByNameExact(final String name) {
        TypesafeQuery<Cliente> q = isisJdoSupport.newTypesafeQuery(Cliente.class);
        final QCliente cand = QCliente.candidate();
        q = q.filter(
                cand.name.eq(q.stringParameter("name"))
        );
        return q.setParameter("name", name)
                .executeUnique();
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listado de clientes")
    @MemberOrder(sequence = "3")
    public List<Cliente> listAll() {
        List<Cliente> clientes = repositoryCliente.Listar();
        return clientes;
    }

    @javax.inject.Inject
    ClienteRepository repositoryCliente;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;
}



