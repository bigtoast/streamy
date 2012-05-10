Streamy
=======

This is a stupid simple project to play with streaming apis.

The project requires [sbt](https://github.com/harrah/xsbt/wiki) to be installed

Build with sbt

    > sbt one-jar
    
This will produce an executable jar in target/scala_2.9.1/streamy_2.9.1_0.1-one-jar.jar

It can be run with 

    > java -jar streamy_2.9.1_0.1-one-jar.jar [optional port number]
    
The default port number it will run on is 6666

When it is running hit

    > curl http://localhost:6666/
    
and you should see some streaming output. Feel free to connect as many clients as you want.

- The end, love andy