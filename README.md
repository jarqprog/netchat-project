Simple ChatRoom
------
java network, threads

to compile: mvn package

to run:

    (as server) java -jar target/net-chat-1.0-SNAPSHOT-shaded.jar server CHOSEN-PORT-NUMBER
        eg. java -jar target/net-chat-1.0-SNAPSHOT-shaded.jar server 8000
        
    (as client) java -jar target/net-chat-1.0-SNAPSHOT-shaded.jar client CHOSEN-PORT-NUMBER IP-ADDRESS USER-NAME
        eg. java -jar target/net-chat-1.0-SNAPSHOT-shaded.jar client 8000 localhost Mark
        eg. java -jar target/net-chat-1.0-SNAPSHOT-shaded.jar client 8000 192.168.2.12 Jerry


    