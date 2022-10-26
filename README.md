# How to Run
1. Navigate to the project folder and run the Active MQ Artemis by executing the command `docker-compose -f broker-compose.yml up mq`
2. Run the class `WebSocketApplication`
3. Access the Client page at http://localhost:8080
4. On IntelliJ duplicate the Runner for `WebSocketApplication` and add the VM parameter `-Dserver.port=8282` to open a new instance in other port.



