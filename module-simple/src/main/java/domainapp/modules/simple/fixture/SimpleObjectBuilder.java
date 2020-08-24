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

package domainapp.modules.simple.fixture;

import org.apache.isis.applib.fixturescripts.BuilderScriptAbstract;

import domainapp.modules.simple.dom.impl.cliente.ClienteMenu;
import domainapp.modules.simple.dom.impl.cliente.Cliente;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class SimpleObjectBuilder extends BuilderScriptAbstract<Cliente, SimpleObjectBuilder> {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String apellido;

    @Getter @Setter
    private String dni;

    @Getter @Setter
    private String telefono;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String direccion;

    @Getter
    private Cliente object;

    @Override
    protected void execute(final ExecutionContext ec) {

        checkParam("name", ec, String.class);

        object = wrap(clienteMenu).create(name, apellido, dni, telefono, email, direccion);
    }

    @javax.inject.Inject
    ClienteMenu clienteMenu;

}
