# firehosenozzle

This project was created to provide an annotation-based wrapper around the ReactorDopplerClient in order to facilitate writing of firehose nozzle Java classes.

There are four annotations to use:

`@EnableFirehoseNozzle`

This annotation belongs at the application level and is required.  This will set the initial configuration of the doppler client and prepare the application for the other annotations.  It takes these four optional attributes:

apiEndpoint:  The doppler endpoint.  For Cloud Foundry implementations, this can point to the api endpoint (usually api.<system domain>) (defaults to https://api.bosh-lite.com)

username:  a user with the doppler.client permissions (defaults to 'admin')

password:  the password for the user described above (defaults to 'admin')

skipSslValidation:  whether or not to skip validation of ssl certificates.  (defaults to true)

`@FirehoseNozzle`

Add this annotation to the class implementing the firehose logic, similar to the `@RestController` annotation.  It takes a single attribute subscriptionId with no default.  This subscriptionId should be unique across all nozzles on the endpoint used above.

`@OnFirehoseEvent`

This annotation is applied to the method that will receive the doppler events.  It *must* be of the signature

`<method name>(org.cloudfoundry.doppler.Envelope e)`

There will be a BeanCreationException thrown if the annotated method does not have this signature.  

The single eventTypes attribute for this annotation is the list of EVENT_TYPEs that will be passed to this method.  Not populating this attribute will imply that all event types should be passed to this method.  For example:

`@OnFirehoseEvent(eventTypes={EventType.COUNTER_EVENT})`

Current list of EVENT_TYPEs are:

EventType.COUNTER_EVENT

EventType.VALUE_METRIC

EventType.CONTAINER_METRIC

EventType.HTTP_START_STOP

EventType.LOG_MESSAGE

`@OnFirehoseEventError`

This annotation is applied to a method that will receive the doppler error events.  It *must* be of the signature

`<method name>(Throwable T)`

There will be a BeanCreationException thrown is the annotated method does not have this signature.

This annotation has no attributes.  


## Bound Services

This project also takes advantage of the Spring Cloud Connector architecture to automatically recognize bound services and populate the connection and subscription information from those.  If there exists a bound service with the following parameters:

1. apiEndpoint
1. password
1. name
1. serviceType
1. skipSslValidation
1. subscriptionId
1. username

*and* the serviceType parameter contains the string "firehose" then the package will use the credentials of this bound service to initialize the firehose nozzle connection, and the attributes of the above `EnableFirehoseNozzle` and `FirehoseNozzle` annotations need not be populated.
