# toaster (SDN typical example)
- [Toaster OLD] (https://wiki.opendaylight.org/view/Special:Search/Controller_Core_Functionality_Tutorials)
- [Toaster List] (https://github.com/opendaylight/coretutorials/tree/stable/beryllium/toaster)
- [HelloWorld](https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Application_Development_Tutorial)
```
git init
git add README.md
git remote add origin https://github.com/jdleegui/toaster.git
git commit -m "toaster example"
git add README.md && git commit -m "Update README.md" && git push -u origin master
git push -u origin master
git add README.md && git commit -m "Update README.md" && git push -u origin master
```
## 0. prepare project
- [x] [ Setp0 ] (https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch0)
- [x] [ Setp2 ] (https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch0-old)
```
1.  guide : https://github.com/opendaylight/docs/blob/master/manuals/developer-guide/src/main/asciidoc/developing-app.adoc
mvn archetype:generate \
-DarchetypeGroupId=org.opendaylight.controller \
-DarchetypeArtifactId=opendaylight-startup-archetype \
-DarchetypeRepository=https://nexus.opendaylight.org/content/repositories/public/ \
-DarchetypeCatalog=https://nexus.opendaylight.org/content/repositories/public/archetype-catalog.xml 
2. 
Define value for property 'groupId': : org.opendaylight.toaster
Define value for property 'artifactId': : toaster
[INFO] Using property: version = 1.0.0-SNAPSHOT
Define value for property 'package':  org.opendaylight.toaster: : 
Define value for property 'classPrefix':  Toaster: : ${artifactId.substring(0,1).toUpperCase()}${artifactId.substring(1)}
Define value for property 'copyright': : Copyright (c) 2015 Yoyodyne, Inc.  
3. git add toaster && git add README.md && git commit -m "Update README.md" && git push -u origin master
4. [Guide] ( https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch0#Introduction )
5. toaster$ mvn clean install -DskipTests
6. toaster$ ./karaf/target/assembly/bin/karaf 
7. opendaylight-user@root>log:display|grep 'Toaster'
   2016-06-22 20:04:46,837 | INFO  | config-pusher    | ToasterProvider | 154 - org.opendaylight.toaster.impl - 1.0.0.SNAPSHOT | ToasterProvider Session Initiated
opendaylight-user@root>
```
## 1. Windup with skeleton
- [X] [Step 1] (https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch1)
```
1.Check generated code and test if the log appeared in time.

toaster$ cat toaster/impl/src/main/java/org/opendaylight/toaster/impl/ToasterProvider.java 

/*
 * Copyright © 2015 Copyright (c) 2015 Yoyodyne, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.toaster.impl;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToasterProvider implements BindingAwareProvider, AutoCloseable {

  private static final Logger LOG = LoggerFactory.getLogger(ToasterProvider.class);

  @Override
  public void onSessionInitiated(ProviderContext session) {
    LOG.info("ToasterProvider Session Initiated");
  }

  @Override
  public void close() throws Exception {
    LOG.info("ToasterProvider Closed");
  } 
}

2.mvn clean generate-sources
  mvn clean install -DskipTests
3.toaster$ ./karaf/target/assembly/bin/karaf 
  > display
  > feature:list
4.git add README.md && git add impl/src/main/java/org/opendaylight/toaster/impl/ToasterImpl.java 
```
## 2. Create RPC
- [X] [Step 2](https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch2)
- LeeJD~/Documents/workspace/toaster$ cat toaster/api/src/main/yang/toaster.yang 
```
module toaster {
  yang-version 1;
  namespace "urn:opendaylight:params:xml:ns:yang:toaster";
   
  prefix "toaster";

  revision "2015-01-05" {
    description "Initial revision of toaster model";
  }
    
  identity toast-type {
    description
      "Base for all bread types supported by the toaster.
       New bread types not listed here nay be added in the
       future.";
  }
  
  identity white-bread {
    base toaster:toast-type;
    description "White bread.";
  }

  identity wheat-bread {
    base toast-type;
    description "Wheat bread.";
  }

  identity wonder-bread {
    base toast-type;
    description "Wonder bread.";
  }

  identity frozen-waffle {
    base toast-type;
    description "Frozen waffle.";
  }

  identity frozen-bagel {
    base toast-type;
    description "Frozen bagel.";
  }

  identity hash-brown {
    base toast-type;
    description "Hash browned potatos.";
  }

  typedef DisplayString {
    type string {
      length "0 .. 255";
    }
    description
      "YANG version of the SMIv2 DisplayString TEXTUAL-CONVENTION.";
    reference
      "RFC 2579, section 2.";
  }

  container toaster {
    presence
      "Indicates the toaster service is available";
    description
      "Top-level container for all toaster database objects.";
    leaf toasterManufacturer {
      type DisplayString;
      config false;
      mandatory true;
      description
        "The name of the toaster's manufacturer. For instance,
         Microsoft Toaster.";
    }

    leaf toasterModelNumber {
      type DisplayString;
      config false;
      mandatory true;
      description
        "The name of the toaster's model. For instance,
         Radiant Automatic.";
    }

    leaf toasterStatus {
      type enumeration {
        enum "up" {
          value 1;
          description
            "The toaster knob position is up.
             No toast is being made now.";
        }
        enum "down" {
          value 2;
          description
            "The toaster knob position is down.
             Toast is being made now.";
        }
      }
      config false;
      mandatory true;
      description
        "This variable indicates the current state of
         the toaster.";
    }

    leaf darknessFactor {
      type uint32;
      config true;
      default 1000;
      description
        "The darkness factor. Basically, the number of ms to multiple the doneness value by.";
    }
  } // container toaster
 
  rpc make-toast {
    input {
      leaf toasterDoneness {
        type uint32 {
          range "1 .. 10";
        }
        default '5';
      }
      leaf toasterToastType {
        type identityref {
          base toaster:toast-type;
        }
        default 'wheat-bread';
      }
    }
  } // rpc make-toast
 
  rpc cancel-toast {
  } // rpc cancel-toast

  rpc restock-toaster {
    input {
      leaf amountOfBreadToStock {
        type uint32;
      }
    }
  }

  notification toasterOutOfBread {
  } // notification toasterOutOfStock

  notification toasterRestocked {
    leaf amountOfBread {
      type uint32;
      description
        "Indicates the amount of bread that was re-stocked";
    }
  } // notification toasterOutOfStock  

  rpc guest-seat {
    input {
      leaf name {
        type string;
      }
    }
    output {
      leaf table {
        type string;
      }
    }
  }
}
``` 
- mvn clean install -DskipTests
- curl --verbose -u admin:admin http://localhost:8181/restconf/config/toaster:toaster
## 3. Create basic skeleton code
- [X][Step 3] (https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Application_Development_Tutorial) :Exersice 3

-Create ToasterImpl class in eclipse.
```
toaster$ cat toaster/impl/src/main/java/org/opendaylight/toaster/impl/ToasterImpl.java 
/*
 * Copyright © 2015 Copyright (c) 2015 Yoyodyne, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.toaster.impl;

import java.util.concurrent.Future;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.MakeToastInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.RestockToasterInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToasterImpl implements ToasterService {

  private static final Logger LOG = LoggerFactory.getLogger(ToasterProvider.class);

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
    GuestSeatOutput output = new GuestSeatOutputBuilder()
      .setTable("Table of Toaster Guest:" + input.getName())
      .build();
    return RpcResultBuilder.success(output).buildFuture();
  }
}
```
- Modify ToasterProvider to initiate toasterImpl class
```
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
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToasterProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ToasterProvider.class);
    RpcRegistration < ToasterService > toasterService;
    
    @Override
    public void onSessionInitiated(ProviderContext session) {
        LOG.info("ToasterProvider Session Initiated");
        toasterService = session.addRpcImplementation(ToasterService.class, new ToasterImpl());
    }

    @Override
    public void close() throws Exception {
        LOG.info("ToasterProvider Closed");
        if (toasterService != null)
        	toasterService.close();
    }
}

2.mvn clean generate-sources
  mvn clean install -DskipTests > /tmp/error.txt
  $tail -f /tmp/error.txt
3.toaster$ ./karaf/target/assembly/bin/karaf 
  > display
  > feature:list
4.git add README.md && git add impl/src/main/java/org/opendaylight/toaster/impl/ToasterImpl.java 
```
## Test YANG container
- Modify YANG 'toaster/api/src/main/yang/toaster.yang’
```
module toaster {
  yang-version 1;
  namespace "urn:opendaylight:params:xml:ns:yang:toaster";
   
  prefix "toaster";

  revision "2015-01-05" {
    description "Initial revision of toaster model";
  }
    
  identity toast-type {
    description
      "Base for all bread types supported by the toaster.
       New bread types not listed here nay be added in the
       future.";
  }
  
  identity white-bread {
    base toaster:toast-type;
    description "White bread.";
  }

  identity wheat-bread {
    base toast-type;
    description "Wheat bread.";
  }

  identity wonder-bread {
    base toast-type;
    description "Wonder bread.";
  }

  identity frozen-waffle {
    base toast-type;
    description "Frozen waffle.";
  }

  identity frozen-bagel {
    base toast-type;
    description "Frozen bagel.";
  }

  identity hash-brown {
    base toast-type;
    description "Hash browned potatos.";
  }

  typedef DisplayString {
    type string {
      length "0 .. 255";
    }
    description
      "YANG version of the SMIv2 DisplayString TEXTUAL-CONVENTION.";
    reference
      "RFC 2579, section 2.";
  }
  
  container guest-chair {
    list guest-chair-entry {
      key "name";
      leaf name {
        type string;
      }
      leaf chair {
        type string;
      }
    }
  }
  
  container toaster {
    presence
      "Indicates the toaster service is available";
    description
      "Top-level container for all toaster database objects.";
    leaf toasterManufacturer {
      type DisplayString;
      config false;
      mandatory true;
      description
        "The name of the toaster's manufacturer. For instance,
         Microsoft Toaster.";
    }

    leaf toasterModelNumber {
      type DisplayString;
      config false;
      mandatory true;
      description
        "The name of the toaster's model. For instance,
         Radiant Automatic.";
    }

    leaf toasterStatus {
      type enumeration {
        enum "up" {
          value 1;
          description
            "The toaster knob position is up.
             No toast is being made now.";
        }
        enum "down" {
          value 2;
          description
            "The toaster knob position is down.
             Toast is being made now.";
        }
      }
      config false;
      mandatory true;
      description
        "This variable indicates the current state of
         the toaster.";
    }

    leaf darknessFactor {
      type uint32;
      config true;
      default 1000;
      description
        "The darkness factor. Basically, the number of ms to multiple the doneness value by.";
    }
  } // container toaster
 
  rpc make-toast {
    input {
      leaf toasterDoneness {
        type uint32 {
          range "1 .. 10";
        }
        default '5';
      }
      leaf toasterToastType {
        type identityref {
          base toaster:toast-type;
        }
        default 'wheat-bread';
      }
    }
  } // rpc make-toast
 
  rpc cancel-toast {
  } // rpc cancel-toast

  rpc restock-toaster {
    input {
      leaf amountOfBreadToStock {
        type uint32;
      }
    }
  }

  notification toasterOutOfBread {
  } // notification toasterOutOfStock

  notification toasterRestocked {
    leaf amountOfBread {
      type uint32;
      description
        "Indicates the amount of bread that was re-stocked";
    }
  } // notification toasterOutOfStock  

  rpc guest-seat {
    input {
      leaf name {
        type string;
      }
    }
    output {
      leaf table {
        type string;
      }
    }
  }
}
```
- Add DB handler in ToasterProvider class ‘toaster/impl/src/main/java/org/opendaylight/toaster/impl/ToasterProvider.java’
```
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
    RpcRegistration < ToasterService > toasterService;
    
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
```
- Call database handler and also assign seat name to local db handler. ‘toaster/impl/src/main/java/org/opendaylight/toaster/impl/ToasterImpl.java’
```
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
```
- Check Logger ‘toaster/impl/src/main/java/org/opendaylight/toaster/impl/LoggingFuturesCallBack.java’
```
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
```
- Add test routine ‘/toaster/it/src/test/java/org/opendaylight/toaster/it/ToasterIT.java’
```
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.mdsal.it.base.AbstractMdsalTestBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestChair;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntry;
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
}
```
## Add test code
- Change callback ‘/toaster/impl/src/main/java/org/opendaylight/toaster/impl/LoggingFuturesCallBack.java’
```
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

```
-Add ToasterImpl ‘/toaster/impl/src/main/java/org/opendaylight/toaster/impl/ToasterImpl.java’
```
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
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
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

import com.google.common.base.Optional;
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
				.setTable(readFromGreetingRegistry(input))
				.build();
		writeToGreetingRegistry(input, output);
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
    
    private String readFromGreetingRegistry(GuestSeatInput input) {
        String result = "Guest seat " + input.getName();
        ReadOnlyTransaction transaction = db.newReadOnlyTransaction();
        InstanceIdentifier<GuestChairEntry> iid = toInstanceIdentifier(input);
        CheckedFuture<Optional<GuestChairEntry>, ReadFailedException> future =
                transaction.read(LogicalDatastoreType.CONFIGURATION, iid);
        Optional<GuestChairEntry> optional = Optional.absent();
        try {
            optional = future.checkedGet();
        } catch (ReadFailedException e) {
            LOG.warn("Reading GeustSeat Failed:",e);
        }
        if(optional.isPresent()) {
            result = optional.get().getChair();
        }
        return result;
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
```
-Channge test code ‘/toaster/it/src/test/java/org/opendaylight/toaster/it/ToasterIT.java’
```
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
```
## Add default callback stuff to check toast maker
[x] [Step 5] (https://github.com/opendaylight/coretutorials/blob/stable/beryllium/toaster/ch5-ToasterApiRPCs/toaster-impl/src/main/java/org/opendaylight/toaster/ToasterImpl.java)
```
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
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.DisplayString;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestChair;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestChairBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.GuestSeatOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.MakeToastInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.RestockToasterInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.Toaster;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.Toaster.ToasterStatus;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.ToasterService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntryBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.toaster.rev150105.guest.chair.GuestChairEntryKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class ToasterImpl implements ToasterService {

    private static final Logger LOG = LoggerFactory.getLogger(ToasterProvider.class);
    private DataBroker db;
    public static final InstanceIdentifier<Toaster> TOASTER_IID = InstanceIdentifier.builder(Toaster.class).build();
    private static final DisplayString TOASTER_MANUFACTURER = new DisplayString("Opendaylight");
    private static final DisplayString TOASTER_MODEL_NUMBER = new DisplayString("Model 1 - Binding Aware");  
    
    public ToasterImpl(DataBroker dba)
    {
    	db = dba;
    	initializeDataTree(db);
    }

    /**
     * Restconf RPC call implemented from the ToasterService interface.
     * Cancels the current toast.
     * Implementation to be filled in a later chapter.
     * in postman, http://localhost:8181/restconf/operations/toaster:cancel-toast
     */
	@Override
	public Future<RpcResult<Void>> cancelToast() {
		// TODO_Always return success from the cancel toast call.
		LOG.info("cancelToast");
		return Futures.immediateFuture( RpcResultBuilder.<Void> success().build() );
	}
    
    /**
     * RestConf RPC call implemented from the ToasterService interface.
     * Attempts to make toast.
     * Implementation to be filled in a later chapter.
     * in postman, http://localhost:8181/restconf/operations/toaster:make-toast
     * { "input" : { "toaster:toasterDoneness" : "10", "toaster:toasterToastType":"wheat-bread" } }
     */
	@Override
	public Future<RpcResult<Void>> makeToast(MakeToastInput input) {
		// TODO_Auto-generated method stub
		LOG.info("makeToast: {}", input);		
		return Futures.immediateFuture( RpcResultBuilder.<Void> success().build() );
	}

	/**
	 * RestConf RPC call implemented from the ToasterService interface.
	 * Restocks the bread for the toaster
	 * Implementation to be filled in a later chapter.
	 * in postman, http://localhost:8181/restconf/operations/toaster:restock-toaster
	 * { "input" : { "toaster:amountOfBreadToStock" : "3" } }
	 */
	@Override
	public Future<RpcResult<Void>> restockToaster(RestockToasterInput input) {
		// TODO_Auto-generated method stub
		LOG.info( "restockToaster: {}", input );
		return Futures.immediateFuture( RpcResultBuilder.<Void> success().build() );
	}

	@Override
	public Future<RpcResult<GuestSeatOutput>> guestSeat(GuestSeatInput input) {
		// TODO_Auto-generated method stub
		LOG.info("ToasterImpl:geustSeat input name:"+input.getName());
		GuestSeatOutput output = new GuestSeatOutputBuilder()
				.setTable(readFromGreetingRegistry(input))
				.build();
		writeToGreetingRegistry(input, output);
		return RpcResultBuilder.success(output).buildFuture();
	}
	
    private void initializeDataTree(DataBroker db) {
        LOG.info("Preparing to initialize the guest-chair");
        WriteTransaction transaction = db.newWriteOnlyTransaction();
        InstanceIdentifier<GuestChair> iid = InstanceIdentifier.create(GuestChair.class);
        GuestChair guestChair = new GuestChairBuilder().build();
        transaction.put(LogicalDatastoreType.OPERATIONAL, iid, guestChair);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        
        // Initialize operational and default config data in MD-SAL data store
        // https://github.com/opendaylight/coretutorials/blob/stable/beryllium/toaster/ 
        // ch4-ToasterConfigAndOperationalDataStore/toaster-impl/src/main/java/org/opendaylight/toaster/ToasterImpl.java
        initToasterOperational();
        initToasterConfiguration();
        
        Futures.addCallback(future, new LoggingFuturesCallBack<>("Failed to create greeting registry", LOG));
    }
    
    /**
     * Populates toaster's initial operational data into the MD-SAL operational
     * data store.
     * Note - we are simulating a device whose manufacture and model are fixed
     * (embedded) into the hardware. / This is why the manufacture and model
     * number are hardcoded.
     */
    private void initToasterOperational() {
    	// Build the initial toaster operational data
    	Toaster toaster = new ToasterBuilder().setToasterManufacturer( TOASTER_MANUFACTURER )
    			.setToasterModelNumber( TOASTER_MODEL_NUMBER )
    			.setToasterStatus( ToasterStatus.Up )
    			.build();

    	// Put the toaster operational data into the MD-SAL data store
    	WriteTransaction tx = db.newWriteOnlyTransaction();
    	tx.put(LogicalDatastoreType.OPERATIONAL, TOASTER_IID, toaster);

    	// Perform the tx.submit asynchronously
    	Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
    		@Override
    		public void onSuccess(final Void result) {
    			LOG.info("initToasterOperational: transaction succeeded");
    		}
    		
    		@Override
    		public void onFailure(final Throwable t) {
    			LOG.error("initToasterOperational: transaction failed");
    		}
    	});

    	LOG.info("initToasterOperational: operational status populated: {}", toaster);
	}

    /**
     * Populates toaster's default config data into the MD-SAL configuration
     * data store.  Note the database write to the tree are done in a synchronous fashion
     */
	private void initToasterConfiguration() {
		// Build the default toaster config data
		Toaster toaster = new ToasterBuilder().setDarknessFactor((long)1000).build();

        // Place default config data in data store tree
        WriteTransaction tx = db.newWriteOnlyTransaction();
        tx.put(LogicalDatastoreType.CONFIGURATION, TOASTER_IID, toaster);
        // Perform the tx.submit synchronously
        tx.submit();
        
        LOG.info("initToasterConfiguration: default config populated: {}", toaster);
	}

	private String readFromGreetingRegistry(GuestSeatInput input) {
        String result = "Guest seat " + input.getName();
        ReadOnlyTransaction transaction = db.newReadOnlyTransaction();
        InstanceIdentifier<GuestChairEntry> iid = toInstanceIdentifier(input);
        CheckedFuture<Optional<GuestChairEntry>, ReadFailedException> future =
                transaction.read(LogicalDatastoreType.CONFIGURATION, iid);
        Optional<GuestChairEntry> optional = Optional.absent();
        try {
            optional = future.checkedGet();
        } catch (ReadFailedException e) {
            LOG.warn("Reading GeustSeat Failed:",e);
        }
        if(optional.isPresent()) {
            result = optional.get().getChair();
        }
        return result;
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
```
## Skip transaction notificator algorithm.
[x] Skip transaction chain in part 15 of [helloWorld] (https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Application_Development_Tutorial)
[x] Continue with another toaster example guide. [https://wiki.opendaylight.org/view/OpenDaylight_Controller:MD-SAL:Toaster_Step-By-Step] (https://wiki.opendaylight.org/view/OpenDaylight_Controller:MD-SAL:Toaster_Step-By-Step)
## Reference
- See [toaster guide] (https://github.com/opendaylight/coretutorials/tree/master/toaster)
- See [CISCO toaster] (https://github.com/opendaylight/controller/blob/master/opendaylight/md-sal/samples/toaster-provider/src/main/java/org/opendaylight/controller/sample/toaster/provider/OpendaylightToaster.java)
- [hello_world1] ( https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Application_Development_Tutorial)
- [hello_world2] [https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch2)
- [YouTube] (https://www.youtube.com/watch?v=s-Yddyieq6s)
- [SDNTUTORIAL] (http://sdntutorials.com/yang-to-java-conversion-how-to-create-maven-project-in-eclipse/)

- [gerrit setup] ( https://wiki.opendaylight.org/view/OpenDaylight_Controller:Gerrit_Setup )
- [ SDN example ] (  https://wiki.opendaylight.org/view/Special:Search/Controller_Core_Functionality_Tutorials )]
- [ Recent DOC ] (https://wiki.opendaylight.org/view/Category:RECENTDOC)
