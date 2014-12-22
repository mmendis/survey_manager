mySCILHS Survey Manager
==============

An application that provides a web service to retrieve a survey/form record from a REDCap instance and push it to
the mySCILHS survey message queue for further processing.  The web service is designed to be invoked by REDCap’s
Data Entry Trigger (DET) functionality.

### Web service endpoint (surveymanager/trigger/pull)

**Input (passed directly through REDCap via HTTP POST)**
* record: The record ID of the saved entry. (This should correspond to mySCILHS subject_id)
* project_url: The URL to the mySCILHS project
* redcap_event_name: The name of the longitudinal event of this response (unused in this release)
* redcap_url: The URL to the REDCap server
* instrument: The survey form that was completed

**Output (HTTP status)**
* 200 (OK): Success
* 400 (Bad Request): Invalid input (either through misconfiguration of REDCap parameters or invalid record/form request
* 401/403: Missing or invalid REDCap API token
* 500: Some other internal server error (e.g. message queue connection error)

### Web service endpoint (surveymanager/admin/config)
Brings up administration page for configuring Survey Manager parameters (requires authentication)

# Build
This is a Maven project that produces a WAR file to be dropped in an application server (e.g. Tomcat).  It can be built
using the following command:

`mvn clean package -P [build-profile]`

where `[build-profile]` is equivalent to one of the following:
* *static*: To run tests that are not dependent on an active REDCap and Survey Message queue. Tests basic survey_manager
functionality
* *live*: Includes *static* tests, then adds additional tests against active REDCap and Survey Manager queue.
Requires access to active, running modules of the above, as well as a valid REDCap API token (configuration found in
 test.properties).


