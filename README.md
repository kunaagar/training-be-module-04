# Backbase Training Exercises

## Targeting Service Module

In this module we will create a custom targeting collector and see the power of CXP's targeting capability.

This is a custom targeting module which can be plugged in to your portal setup.
Once integrated, this targeting collector will fetch the date of birth of the logged in user via the Training Server.
It calculates the age and you can target your content based on the age/segment.

### Installation & Configuration

This would be done in the following steps 

- **Copy targeting-service-module from training-modules into the services folder of your Launchpad 0.12.x project.**

- **Include targeting-service-module module to the build.** Open `pom.xml` from *exercises-environment/services/*. Add `<module>targeting-service-module</module>` into  `<modules>` section
	```xml
	    <modules>
	        ...	    
	        <module>targeting-service-module</module>
	        ...
	    </modules>
	```	
	Re-compile *exercises-environment/services/* executing `mvn clean install` command.
	
- **Enable newly created module in Portal application.** Add the following dependency to your `portal/pom.xml` file in `<dependencies>` section:

	```xml
	    <dependency>
	        <groupId>com.backbase.expert.training</groupId>
	        <artifactId>targeting-service-module</artifactId>
	        <version>1.0-SNAPSHOT</version>
	    </dependency>
	```
 
- **Configure Portal properties.** Add following elements to the configuration into *backbase.properties* file.
    ```
    training.server.host=localhost
    training.server.http.port=9999
    ```
### Build & Run

- Build Portal module with executing `mvn clean install` command from *portal* directory.
- Start Portal application with executing `mvn jetty:run` command from *portal* directory.
- Create a page and drop a targeting container.
- You should now be able to see the Age collector as an option in the targeting container.
 
