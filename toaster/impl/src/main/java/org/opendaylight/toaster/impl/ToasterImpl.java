/*
 * Copyright © 2015 Copyright (c) 2015 Yoyodyne, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.toaster.impl;

import java.util.concurrent.Future;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestChair;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestChairBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.MakeToastInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.RestockToasterInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntryBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntryKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.Futures;

public class ToasterImpl implements ToasterService {

    private static final Logger LOG = LoggerFactory.getLogger(ToasterProvider.class);
    private DataBroker db;
    
    public ToasterImpl(DataBroker dba)
    {
    	db = dba;
    	initializeDataTree(db);
    }

	@Override
	public Future<RpcResult<Void>> cancelToast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<RpcResult<Void>> makeToast(MakeToastInput input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<RpcResult<Void>> restockToaster(RestockToasterInput input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<RpcResult<GuestSeatOutput>> guestSeat(GuestSeatInput input) {
		// TODO_Auto-generated method stub
		LOG.info("ToasterImpl:geustSeat input name:"+input.getName());
		GuestSeatOutput output = new GuestSeatOutputBuilder()
				.setTable("Table of Toaster Guest:" + input.getName())
				.build();
		this.writeToGreetingRegistry(input, output);
		return RpcResultBuilder.success(output).buildFuture();
	}
	
    private void initializeDataTree(DataBroker db) {
        LOG.info("Preparing to initialize the guest-chair");
        WriteTransaction transaction = db.newWriteOnlyTransaction();
        InstanceIdentifier<GuestChair> iid = InstanceIdentifier.create(GuestChair.class);
        GuestChair guestChair = new GuestChairBuilder()
                .build();
        transaction.put(LogicalDatastoreType.OPERATIONAL, iid, guestChair);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        Futures.addCallback(future, new LoggingFuturesCallBack<>("Failed to create greeting registry", LOG));
    }
    
    private void writeToGreetingRegistry(GuestSeatInput input, GuestSeatOutput output) {
        WriteTransaction transaction = db.newWriteOnlyTransaction();
        InstanceIdentifier<GuestChairEntry> iid = toInstanceIdentifier(input);
        GuestChairEntry greeting = new GuestChairEntryBuilder()
                .setChair(output.getTable())
                .setName(input.getName())
                .build();
        transaction.put(LogicalDatastoreType.OPERATIONAL, iid, greeting);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        Futures.addCallback(future, new LoggingFuturesCallBack<Void>("Failed to write greeting to greeting registry", LOG));
    }

    private InstanceIdentifier<GuestChairEntry> toInstanceIdentifier(GuestSeatInput input) {
        InstanceIdentifier<GuestChairEntry> iid = InstanceIdentifier.create(GuestChair.class)
            .child(GuestChairEntry.class, new GuestChairEntryKey(input.getName()));
        return iid;
    }   
}
