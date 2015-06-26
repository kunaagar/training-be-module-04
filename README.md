# Backbase Training Exercises

## Portal Backend - Module 4: Targeting

In this module we will create a custom targeting collector and see the power of CXP's targeting feature.

This is a custom targeting module which can be plugged into your portal setup. Once integrated, this targeting collector will fetch the date of birth of the logged-in user from the Training Server. It calculates the age and you can target your content based on that criteria.

### Prerequisites

All exercises use the [standard portal set-up for backbase training](https://my.backbase.com/resources/how-to-guides/getting-your-first-launchpad-based-portal-set-up/).

### Contents

This module contains following components 

1. targeting-service-module : This module contains custom AgeCollector which will define age and targeting segment of current user. For details check 
[targeting-service-module](targeting-service-module).
