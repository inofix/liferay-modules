# liferay-modules

liferay-modules is a collection of portlets, hooks and other plugins which customize or extend 
the Liferay Portal Platform. 

This collection is created and maintained by Inofix GmbH, a Luzern bases company specialized on the development and operation of Liferay Portal Solutions. Visit us at <a href="http://www.inofix.ch" target="_blank">www.inofix.ch</a>.

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

### Asset Categories Summary Hook

Wrap the vocabulary-title of Liferay's asset-categories-summary tag into a span with class="vocabulary-title".

### Asset Display Hook 

The Asset Display Hook adds custom display styles to Liferay's Asset Publisher Portlet. The hook currently comprises the following styles

* ifx-abstract: A slight modification of Liferay's abstracts style where the metadata-section is displayed BEFORE title and summary (instead of after).
* 
### CKEditor Hook

The CKEditor Hook provides a patch for https://issues.liferay.com/browse/LPS-43513.

### Contact Manager

The Contact Manager Portlet is a contact-manager based on the vCard 4.0 standard with the following features: 

* Import- and export of vCards from file
* Editing of all standard fields and the extensions defined in <a href="https://tools.ietf.org/html/rfc6715" target="_blank">RFC 6715</a>
* Integration into Liferay's Asset-Framework
* Integration into Liferay's Activity-Framework

#### Third Party Libraries

* <a href="https://github.com/mangstadt/ez-vcard" target="_blank">ez-vcard</a>

### CDAV Manager

The CDav Manager allows you to connect your Liferay Calendar to and synchronize with CalDAV-Servers like Kerio, Google Calendar, etc.

#### Compile from source

1. clone cdav-connector from <a href="https://github.com/Kerio/cdav-connector" target="_blank">https://github.com/Kerio/cdav-connector</a> and 
2. install it to your local maven repo (mvn package install)
3. when the cdav-manager's dependency is fulfilled (see steps 1 & 2) build the cdav-manager with mvn package

#### Third Party Libraries

* <a href="https://github.com/Kerio/cdav-connector" target="_blank">cdav-connector</a>

### Display Page Hook

Display Page Hook is a first approach to solve the issue described under https://issues.liferay.com/browse/LPS-30115. 

It allows you to define a default display page for a structure by adding a field named layoutUuid to the structure and set it's default value to the uuid of the respective layout.

### Inofix Theme

A full responsive theme which makes intensive use of Bootstrap and Alloy UI.

### Portlet Title Hook

Include the portlet-title-default node also for unregistered users. The title can then be used for dynamic scrollspies like those included for example in the <a href="https://github.com/inofix/flussbad-modules/tree/master/flussbad-theme" target="_blank">flussbad-theme</a>.

### Social Media Portlet

Social Media Portlet provides a set of social media buttons which respect the user's privacy. 

It's based on heiseonline's shariff library, which can be found at <a href="https://github.com/heiseonline/shariff" target="_blank">https://github.com/heiseonline/shariff</a>. 

#### Third Party Libraries

* <a href="https://github.com/heiseonline/shariff" target="_blank">heiseonline's shariff</a>

### Taglib Util

taglib-util is a collection of utility tags, which are used by various liferay-portlets but can also be used in other contexts.

#### Tags

* build-info: Extract build-info parameters from META-INF/MANIFEST.MF and display them in a definition list. The properties are configured via common settings of a project's or parent project's pom.xml file.

### URL Title Hook
* edit a JournalArticle's urlTitle attribute.

