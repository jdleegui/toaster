/*
 * Copyright © 2015 Copyright (c) 2015 Yoyodyne, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.toaster.impl;

import org.slf4j.Logger;

import com.google.common.util.concurrent.FutureCallback;

public class LoggingFuturesCallBack <V> implements FutureCallback <V> {

	private Logger LOG;
	private String message;

	public LoggingFuturesCallBack(String message, Logger LOG) {
		this.message = message;
		this.LOG = LOG;
	}
	
	@Override
	public void onFailure(Throwable e) {
		// TODO_Auto-generated method stub
		LOG.warn(message, e);
	}

	@Override
	public void onSuccess(V arg0) {
		// TODO_Auto-generated method stub
        LOG.info("Success! {} ", arg0);
	}
}
