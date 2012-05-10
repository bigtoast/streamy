

organization := "com.github.bigtoast"

name := "streamy"

version := "0.1"

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

mainClass := Some("com.github.bigtoast.streamy.Streamy")

libraryDependencies += "net.databinder" %% "unfiltered" % "0.6.2"

libraryDependencies += "net.databinder" %% "unfiltered-netty" % "0.6.2"

libraryDependencies += "net.databinder" %% "unfiltered-netty-server" % "0.6.2"
 
libraryDependencies += "commons-codec" %  "commons-codec" % "1.4"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.1"


