# Graylog command line publisher

## Overview
This project implements a command line application that reads events from a text file and publish them on Graylog by using the [GELF](https://docs.graylog.org/en/4.0/pages/gelf.html) message format.

## Technical implementation overview
The application relies on the following 2 features:
 - Parser 
   - in charge of parsing the file
   - Implemented in FileParser.java class 
 - Publisher
    - in charge of publishing an event into Graylog
    - Implemented in EventPublisher.java class 
  
The application solution is structured as follows:

1. Read the file from the file system
2. Parse the file by returning a list of Events
3. For each event:
    * convert the event to GELF format
    * Use /gelf API endpoint to publish the message on Graylog
