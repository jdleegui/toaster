/*
 * Copyright © 2015 Copyright (c) 2015 Yoyodyne, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.toaster.impl;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToasterProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ToasterProvider.class);
    private RpcRegistration < ToasterService > toasterService;
    
    @Override
    public void onSessionInitiated(ProviderContext session) {
        LOG.info("ToasterProvider Session Initiated");
        DataBroker dba = session.getSALService(DataBroker.class);
        toasterService = session.addRpcImplementation(ToasterService.class, new ToasterImpl(dba));
    }

    @Override
    public void close() throws Exception {
        LOG.info("ToasterProvider Closed");
        if (toasterService != null)
        	toasterService.close();
    }
}
