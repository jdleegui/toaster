/*
 * Copyright © 2015 Copyright (c) 2015 Yoyodyne, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.toaster.it;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.controller.mdsal.it.base.AbstractMdsalTestBase;
import org.opendaylight.toaster.impl.GuestChairDataChangeListenerFuture;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestChair;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntryBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntryKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ToasterIT extends AbstractMdsalTestBase {
    private static final Logger LOG = LoggerFactory.getLogger(ToasterIT.class);

    @Override
    public String getModuleName() {
        return "toaster";
    }

    @Override
    public String getInstanceName() {
        return "toaster-default";
    }

    @Override
    public MavenUrlReference getFeatureRepo() {
        return maven()
                .groupId("org.opendaylight.toaster")
                .artifactId("toaster-features")
                .classifier("features")
                .type("xml")
                .versionAsInProject();
    }

    @Override
    public String getFeatureName() {
        return "odl-toaster-ui";
    }

    @Override
    public Option getLoggingOption() {
        Option option = editConfigurationFilePut(ORG_OPS4J_PAX_LOGGING_CFG,
                logConfiguration(ToasterIT.class),
                LogLevel.INFO.name());
        option = composite(option, super.getLoggingOption());
        return option;
    }

    @Test
    public void testtoasterFeatureLoad() {
        Assert.assertTrue(true);
    }

    @Test
    public void testRPC() throws InterruptedException, ExecutionException {
        String name = "Ed Warnicke";
        ToasterService service = getSession().getRpcService(ToasterService.class);

        GuestSeatInput input = new GuestSeatInputBuilder()
                .setName(name)
                .build();
        Future<RpcResult<GuestSeatOutput>> outputFuture = service.guestSeat(input);
        RpcResult<GuestSeatOutput> outputResult = outputFuture.get();
        Assert.assertTrue("RPC was unsuccessful", outputResult.isSuccessful());
        Assert.assertEquals("Did not receive the expected response to helloWorld RPC", "Hello " + name,
                outputResult.getResult().getTable());
    }
    
    @Test
    private void validateGuestChair(String name) {
        InstanceIdentifier<GuestChairEntry> iid = InstanceIdentifier.create(GuestChair.class)
                .child(GuestChairEntry.class, new GuestChairEntryKey(name));
        DataBroker db = getSession().getSALService(DataBroker.class);
        ReadOnlyTransaction transaction = db.newReadOnlyTransaction();
        CheckedFuture<Optional<GuestChairEntry>, ReadFailedException> future =
                transaction.read(LogicalDatastoreType.OPERATIONAL, iid);
        Optional<GuestChairEntry> optional = Optional.absent();
        try {
            optional = future.checkedGet();
        } catch (ReadFailedException e) {
            LOG.warn("Reading greeting failed:",e);
        }
        Assert.assertTrue(name + " not recorded in greeting registry",optional.isPresent());
    }
    
    @Test
    private void validateGuestChair(String name, GuestChairDataChangeListenerFuture future) throws InterruptedException, TimeoutException, ExecutionException {
        future.get(100, TimeUnit.MILLISECONDS);
        Assert.assertTrue(name + " not recorded in greeting registry", future.isDone());
    }
    @Test
    private void validateRPCResponse(String name,String response) throws InterruptedException, ExecutionException {
        ToasterService service = getSession().getRpcService(ToasterService.class);

        GuestSeatInput input = new GuestSeatInputBuilder()
                .setName(name)
                .build();
        Future<RpcResult<GuestSeatOutput>> outputFuture = service.guestSeat(input);
        RpcResult<GuestSeatOutput> outputResult = outputFuture.get();
        Assert.assertTrue("RPC was unsuccessful", outputResult.isSuccessful());
        Assert.assertEquals("Did not receive the expected response to helloWorld RPC", response,
                outputResult.getResult().getTable());
    }
    
    
    private void programResponse(String name, String response) throws TransactionCommitFailedException {
        DataBroker db = getSession().getSALService(DataBroker.class);
        WriteTransaction transaction = db.newWriteOnlyTransaction();
        InstanceIdentifier<GuestChairEntry> iid = InstanceIdentifier.create(GuestChair.class)
                .child(GuestChairEntry.class, new GuestChairEntryKey(name));
        GuestChairEntry entry = new GuestChairEntryBuilder()
                .setName(name)
                .setChair(response)
                .build();
        transaction.put(LogicalDatastoreType.CONFIGURATION, iid, entry);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        future.checkedGet();
    }
    
    @Test
    public void testProgrammableRPC() throws InterruptedException, ExecutionException, TransactionCommitFailedException {
        String name = "Colin Dixon";
        String response = "Hola " + name;
        programResponse(name,response);
        validateRPCResponse(name,response);
        validateGuestChair(name);
    }
}
