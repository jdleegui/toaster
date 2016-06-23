/*
 * Copyright © 2015 Copyright (c) 2015 Yoyodyne, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.toaster.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker.DataChangeScope;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestChair;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntryKey;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import com.google.common.util.concurrent.AbstractFuture;

public class GuestChairDataChangeListenerFuture extends AbstractFuture<GuestChairEntry> implements DataChangeListener, AutoCloseable {

	private String name;
	private ListenerRegistration<DataChangeListener> registration;

	public GuestChairDataChangeListenerFuture(DataBroker db, String name) {
		super();
		// TODO_Auto-generated constructor stub
		this.name = name;
        InstanceIdentifier<GuestChairEntry> iid =
                InstanceIdentifier.create(GuestChair.class)
                    .child(GuestChairEntry.class);
        this.registration = db.registerDataChangeListener(LogicalDatastoreType.OPERATIONAL,
                iid, this, DataChangeScope.BASE);
	}

	
	@Override
	public void close() throws Exception {
		// TODO_Auto-generated method stub
        if(registration != null) {
            registration.close();
        }		
	}

	@Override
	public void onDataChanged(AsyncDataChangeEvent<InstanceIdentifier<?>, DataObject> event) {
		// TODO_Auto-generated method stub
        InstanceIdentifier<GuestChairEntry> iid =
                InstanceIdentifier.create(GuestChair.class)
                .child(GuestChairEntry.class,new GuestChairEntryKey(this.name));
        if(event.getCreatedData().containsKey(iid) ) {
            if(event.getCreatedData().get(iid) instanceof GuestChairEntry) {
                this.set((GuestChairEntry) event.getCreatedData().get(iid));
            }
            quietClose();
        } else if (event.getUpdatedData().containsKey(iid)) {
            if(event.getUpdatedData().get(iid) instanceof GuestChairEntry) {
                this.set((GuestChairEntry) event.getUpdatedData().get(iid));
            }
            quietClose();
        }
	}
	
    private void quietClose() {
        try {
            this.close();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to close registration",e);
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        quietClose();
        return super.cancel(mayInterruptIfRunning);
    }
}
