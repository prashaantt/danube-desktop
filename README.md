<a href="http://projectdanube.org/" target="_blank"><img src="http://peacekeeper.github.com/xdi2/images/projectdanube_logo.png" align="right"></a>

This is a "Personal Cloud Desktop" application for the [XDI2](http://github.com/peacekeeper/xdi2) server. 

### How to build

You have to make sure that the XDI2 library as well as a number of XDI2 connectors are built: 

* [xdi2](https://github.com/peacekeeper/xdi2) - XDI2 library
* [xdi2-connector-facebook](https://github.com/peacekeeper/xdi2-connector-facebook) - Facebook -> XDI Connector
* [xdi2-connector-personal](https://github.com/peacekeeper/xdi2-connector-personal) - Personal.com -> XDI Connector
* [xdi2-connector-allfiled](https://github.com/peacekeeper/xdi2-connector-allfiled) - Allfiled -> XDI Connector

You also have to install a few external dependencies (you only have to do this once):

    cd danube-desktop.jars
    mvn clean install
    cd ..

Then, just run

    mvn clean install

To build all components.

### How to run the Personal Cloud Desktop

    cd danube-desktop.web
    mvn jetty:run

Then go to:

    http://localhost:11110/

### Community

Google Group: http://groups.google.com/group/xdi2
