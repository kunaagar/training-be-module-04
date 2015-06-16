# Backbase Training Exercises

## Portal Backend - Module 4: Targeting

In this module we will create a custom targeting collector and see the power of  CXP's targeting feature.

This is a custom targeting module which can be plugged in to your portal setup.
Once integrated, this targeting collector will fetch the date of birth of the logged in user via the training server.
It calculates the age and you can target your content based on the age/segment.

### Installation & Configuration

This would be done in the following steps 

- **Build and configure targeting-service-module with services  module of your CXP project**
 -  Add a services folder at the root of exercises-environment and add the targeting-service-module to it.
    Run mvn clean install to build the targeting-service-module
 
-  **Configure portal project to include the targeting-service-module as a dependency**

 -  Add the dependency in the pom.xml file of the portal module so that the targeting framework can detect your collector.

         ```
             <dependency>
                 <groupId>com.backbase.expert.training</groupId>
                 <artifactId>targeting-service-module</artifactId>
                 <version>1.0-SNAPSHOT</version>
             </dependency>
         ```


- Run the portal with mvn jetty:run. 
 - Create a page and drop a targeting container.
 - You should now be able to see the Age collector as an option in the targeting container.
 
