# toaster (SDN typical example)

```
git init
git add README.md
git remote add origin https://github.com/jdleegui/toaster.git
git commit -m "toaster example"
git add README.md && git commit -m "Update README.md" && git push -u origin master
git push -u origin master
```
- [x] Creating project
```
guide : https://github.com/opendaylight/docs/blob/master/manuals/developer-guide/src/main/asciidoc/developing-app.adoc 
mvn archetype:generate -DarchetypeGroupId=org.opendaylight.controller -DarchetypeArtifactId=opendaylight-startup-archetype \
-DarchetypeRepository=https://nexus.opendaylight.org/content/repositories/public/ \
-DarchetypeCatalog=https://nexus.opendaylight.org/content/repositories/public/archetype-catalog.xml
```
- [ ] Step 1
- [ ] Send state

- See [toaster guide] (https://github.com/opendaylight/coretutorials/tree/master/toaster)
- See [CISCO toaster] (https://github.com/opendaylight/controller/blob/master/opendaylight/md-sal/samples/toaster-provider/src/main/java/org/opendaylight/controller/sample/toaster/provider/OpendaylightToaster.java)
- [hello_world1] ( https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Application_Development_Tutorial)
- [hello_world2] [https://wiki.opendaylight.org/view/Controller_Core_Functionality_Tutorials:Tutorials:Starting_A_Project:ch2)
- [YouTube] (https://www.youtube.com/watch?v=s-Yddyieq6s)
- [SDNTUTORIAL] (http://sdntutorials.com/yang-to-java-conversion-how-to-create-maven-project-in-eclipse/)

- [gerrit setup] ( https://wiki.opendaylight.org/view/OpenDaylight_Controller:Gerrit_Setup )
