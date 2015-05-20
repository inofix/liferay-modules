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

The Contact Manager Portlet is a vCard 4.0 compliant contact-manager with the following features: 

* Import- and export of vCards from file
* Editing of all standard fields and the extensions defined in <a href="https://tools.ietf.org/html/rfc6715" target="_blank">RFC 6715</a>
* Integration into Liferay's asset-framework
