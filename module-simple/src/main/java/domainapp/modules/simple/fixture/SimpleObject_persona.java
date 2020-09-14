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

import domainapp.modules.simple.dominio.cliente.ClienteMenu;
import org.apache.isis.applib.fixturescripts.PersonaWithBuilderScript;
import org.apache.isis.applib.fixturescripts.PersonaWithFinder;
import org.apache.isis.applib.fixturescripts.setup.PersonaEnumPersistAll;
import org.apache.isis.applib.services.registry.ServiceRegistry2;

import domainapp.modules.simple.dominio.cliente.Cliente;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SimpleObject_persona implements PersonaWithBuilderScript<Cliente, SimpleObjectBuilder>,
        PersonaWithFinder<Cliente> {

    FOO("Foo"),
    BAR("Bar"),
    BAZ("Baz"),
    FRODO("Frodo"),
    FROYO("Froyo"),
    FIZZ("Fizz"),
    BIP("Bip"),
    BOP("Bop"),
    BANG("Bang"),
    BOO("Boo");

    private final String name;

//    @Override
    public SimpleObjectBuilder builder() {
        return new SimpleObjectBuilder().setName(name);
    }

    //@Override
    public Cliente findUsing(final ServiceRegistry2 serviceRegistry) {
        ClienteMenu clienteMenu = serviceRegistry.lookupService(ClienteMenu.class);
        return clienteMenu.findByNameExact(name);
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<SimpleObject_persona, Cliente, SimpleObjectBuilder> {
        public PersistAll() {
            super(SimpleObject_persona.class);
        }
    }
}
