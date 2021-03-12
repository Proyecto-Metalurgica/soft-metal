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
package domainapp.application.services.homepage;

import java.util.List;

import domainapp.modules.simple.dominio.presupuesto.Presupuesto;
import domainapp.modules.simple.dominio.presupuesto.PresupuestoRepository;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.HomePage;



@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "domainapp.application.services.homepage.HomePageViewModel"
)
public class HomePageViewModel {

    public TranslatableString title() {
        //return TranslatableString.tr("Presupuestos Totales: {num} ", "num", getObjects().size());
        return TranslatableString.tr("Ultimos Presupuestos");
    }

    @HomePage()
    @CollectionLayout(named="Ultimos Presupuestos")
    public List<Presupuesto> getObjects() {
        return presupuestoRepository.ListarUltimos();
    }

    @javax.inject.Inject
    PresupuestoRepository presupuestoRepository;
}
