# Query Service

This service provides querying capabilities. It is distinct from the run-time API which is used to perform actions on engine items.

## Approach

The service provides query endpoints with paging and sorting. So an example query might be e.g. /query/processinstances?status=RUNNING&page=0&size=10&sort=lastModified,desc

The approach is based upon https://github.com/spring-projects/spring-data-examples/tree/master/web/querydsl . It supports querying for nested objects and nested collections by specifying the path with '.'

If the Q* classes aren't present in the /target/generated-sources directory then run mvn generate-sources from this project directory

## Database Support

The implementation is using spring data in as agnostic a way as available so that alternative databases could be used. It is intended to verify that elasticsearch could be used if a ElasticsearchRepository were used in place of CrudRepository and an elastic instance were configured (https://www.mkyong.com/spring-boot/spring-boot-spring-data-elasticsearch-example/)

The project has a split structure so that the activiti-cloud-services-query-repo module can be excluded and replaced with an alternative implementation

## How to run

Have the rabbitMQ and keycloak docker containers running - see the docker directory.

Start the Application in the spring-boot-sample-hal-rest-api (e.g. from IDE). 

To start a process submit the relevant post to the sample application. Its logs should show 'started on port(s)' to reveal which port. Use the postman collection called 'rest_calls_postman_collection.json' (elsewhere in Activiti).

After starting a process you can query for tasks by going to e.g. http://localhost:55085/query/tasks/ where the port is that from the logs of the QueryApplication startup.

## Outstanding

