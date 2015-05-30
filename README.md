# liferay-modules

liferay-modules is a collection of portlets, hooks and other plugins which customize or extend 
the Liferay Portal Platform. 

## Installation

If you want to use Inofix' modules for liferay in your portal installation, 
please use the upcoming releases of the modules from the liferay marketplace.

## Compile from source

1. clone the git repo
1. adjust the the parent-pom configuration in liferay-modules/pom.xml to your needs
1. cd to the module directory
1. (if necessary) run the service-builder goal (mvn liferay:build-service install)
1. install or auto-deploy the module to your liferay-installation (mvn package liferay:deploy)

## Module Description

### Contact Manager ###

The Contact Manager Portlet is a contact-manager based on the vCard 4.0 standard with the following features: 

* Import- and export of vCards from file
* Editing of all standard fields and the extensions defined in <a href="https://tools.ietf.org/html/rfc6715" target="_blank">RFC 6715</a>
* Integration into Liferay's Asset-Framework
* Integration into Liferay's Activity-Framework

#### Third Party Libraries

* <a href="https://github.com/mangstadt/ez-vcard" target="_blank">ez-vcard</a>

### cDAV Manager ###

The CDav Manager allows you to connect your Liferay Calendar to and synchronize with CalDAV-Servers like Kerio, Google Calendar, etc.

#### Compile from source ####

1. clone cdav-connector from <a href="https://github.com/Kerio/cdav-connector" target="_blank">https://github.com/Kerio/cdav-connector</a> and 
2. install it to your local maven repo (mvn package install)
3. when the cdav-manager's dependency is fulfilled (see steps 1 & 2) you build the cdav-manager with mvn package

#### Third Party Libraries

* <a href="https://github.com/Kerio/cdav-connector" target="_blank">cdav-connector</a>
