# Backbase Training Exercises

## Portal Backend - Module 4: Custom Targeting Collector

This exercise is part of [Module 4: Targeting](https://github.com/Backbase/training-be-module-04/tree/code-migration)

In this module we will create a custom targeting collector and see the power of CXP's targeting capability.

This is a custom targeting module which can be plugged into your portal setup. Once integrated, this targeting collector will fetch the date of birth of the logged-in user from the Training Server. It calculates the age and you can target your content based on that criteria.

### Prerequisites

You need to have the player management service before performing this excerise.
Follow the instructions from [Enterprise Integration Module](https://github.com/Backbase/training-be-module-01/tree/code-migration/enterprise-integration-module).

### Installation & Configuration

- Copy **targeting-service-module** into the **services** folder of your Launchpad 0.12.x project.

- Include the **targeting-service-module** module to the build. Open `services/pom.xml` and add **targeting-service-module** in the `<modules>` section:
	```xml
	    <modules>
	        ...	    
	        <module>targeting-service-module</module>
	        ...
	    </modules>
	```	
	Re-compile **services** by executing `mvn clean install` in the **services** folder.
	
- Enable the newly created module in the Portal application. In the `<dependencies>` section of `portal/pom.xml`, add the following dependency:

	```xml
	    <dependency>
	        <groupId>com.backbase.training</groupId>
	        <artifactId>targeting-service-module</artifactId>
	        <version>1.0-SNAPSHOT</version>
	    </dependency>
	```

### Build & Run

- Start Portal application by executing `mvn jetty:run` command from the **portal** directory.
- Create a page and drop a targeting container.
- You should now be able to see the Age collector as an option in the targeting container.
