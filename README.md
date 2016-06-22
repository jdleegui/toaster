# toaster (SDN typical example)
- [Table of contents] (https://wiki.opendaylight.org/view/Special:Search/Controller_Core_Functionality_Tutorials)
- [Another hidden contents] (https://github.com/opendaylight/coretutorials/tree/stable/beryllium/toaster)
- [Basic guide] (https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Application_Development_Tutorial)
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
mvn archetype:generate -DarchetypeGroupId=org.opendaylight.controller -DarchetypeArtifactId=opendaylight-startup-archetype \
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
```
## 1. Windup with skeleton
- [X] [Step 1] (https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch1)
```
1.Create ToasterImpl class in eclipse.

toaster$ cat toaster/impl/src/main/java/org/opendaylight/toaster/impl/ToasterImpl.java 
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

public class ToasterImpl implements BindingAwareProvider, AutoCloseable {

	private static final Logger LOG = LoggerFactory.getLogger(ToasterImpl.class);
	@Override
	public void close() throws Exception {
		// TODO_Auto-generated method stub
		LOG.info("Toaster Closed!");
	}

	@Override
	public void onSessionInitiated(ProviderContext arg0) {
		// TODO_Auto-generated method stub
		LOG.info("Toaster World!");
	}
}
toaster$ 

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
  module toaster {
      yang-version 1;
      namespace "urn:opendaylight:params:xml:ns:yang:toaster";
      prefix "toaster";
  
      revision "2015-01-05" {
          description "Initial revision of toaster model";
      }
  
      organization "Netconf Central";
      contact
        "Andy Bierman <andy@netconfcentral.org>";
  
      description
        "YANG version of the TOASTER-MIB.";
  
      identity toast-type {
        description
           "Base for all bread types supported by the toaster.
            New bread types not listed here nay be added in the future.";
      }
  
      identity white-bread {
         base toaster:toast-type;
         description "White bread.";
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
      }  // rpc cancel-toast
  
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
  }

- mvn clean install -DskipTests
- curl --verbose -u admin:admin http://localhost:8181/restconf/config/toaster:toaster

- See [toaster guide] (https://github.com/opendaylight/coretutorials/tree/master/toaster)
- See [CISCO toaster] (https://github.com/opendaylight/controller/blob/master/opendaylight/md-sal/samples/toaster-provider/src/main/java/org/opendaylight/controller/sample/toaster/provider/OpendaylightToaster.java)
- [hello_world1] ( https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Application_Development_Tutorial)
- [hello_world2] [https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch2)
- [YouTube] (https://www.youtube.com/watch?v=s-Yddyieq6s)
- [SDNTUTORIAL] (http://sdntutorials.com/yang-to-java-conversion-how-to-create-maven-project-in-eclipse/)

- [gerrit setup] ( https://wiki.opendaylight.org/view/OpenDaylight_Controller:Gerrit_Setup )
- [ SDN example ] (  https://wiki.opendaylight.org/view/Special:Search/Controller_Core_Functionality_Tutorials )]
