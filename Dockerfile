FROM java:8
EXPOSE 8080
ADD target/TicketService-0.0.1-SNAPSHOT.jar TicketService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","TicketService-0.0.1-SNAPSHOT.jar"]