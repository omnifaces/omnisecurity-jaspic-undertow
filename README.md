# omnisecurity-jaspic-undertow

Extension for Undertow that activates JASPIC

This can be used to activate JASPIC on Undertow (JBoss WildFly) from within an application. To do so compile this project as a jar and put it on the class path of the application in question.

For Maven projects, alternatively the following can be added to **pom.xml** as well:

```xml
<dependency>
    <groupId>org.omnifaces</groupId>
    <artifactId>omnifaces-security-jaspic-undertow</artifactId>
    <version>10.0.0.Final-u1-SNAPSHOT</version>
</dependency>
```

Furthermore a dependency on an internal undertow package must be declared by creating a **META-INF/jboss-deployment-structure.xml** file within the application with at least the following
content:

```xml
<?xml version='1.0' encoding='UTF-8'?>
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.2">
	<deployment>
		<dependencies>
			<module name="org.wildfly.extension.undertow" services="export" export="true" />
		</dependencies>
	</deployment>
</jboss-deployment-structure>
```

Note this is somewhat of a hack, but needed for JBoss WildFly 9.0.1. WildFly 10.0.0.Final doesn't need this and just has to declare the jaspitest security domain. 

The current version (10.0.0.Final-u1-SNAPSHOT) does work for WildFly 10.0.0.Final. Older versions of WildFly need a matching older version of this hack.

