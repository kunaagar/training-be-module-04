# Backbase Training Exercises

## Portal Backend - Module 4: Targeting

In this module we will create a custom targeting collector and see the power of CXP's targeting feature.

This is a custom targeting module which can be plugged in to your portal setup. Once integrated, this targeting collector will fetch the date of birth of the logged in user via the training server. It calculates the age and you can target your content based on the age/segment.

### Contents

This module contains following components 

1. targeting-service-module : This module contains custom AgeCollector which will define age and targeting segment of current user. For details check 
[security-ldap](https://github.com/Backbase/training-be-module-04/blob/code-migration/targeting-service-module/README.md).

To be able to proceed with this training section, you will need to have Exercises Environment prepared and Training Server up and running.