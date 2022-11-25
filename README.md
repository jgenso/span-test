## sbt project compiled with Scala 3


### Usage

The following alternatives are available to run the app:

* This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt` and then  `run <file_path>`, and `sbt console` will start a Scala 3 REPL.

* You can compile the project and the run using scala binary, for this you will need Scala 3 installed(this is explained in Install Scala 3 section), 
  for this you need to compile and package the project, for this run `sbt compile` then `sbt package` and then 
  `scala target/scala-3.1.3/span-test_3-0.1.0-SNAPSHOT.jar "./src/test/resources/input.txt"`

### Scala 3 Installation

Run `curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz | gzip -d > cs && chmod +x cs && ./cs setup`

Run `scala -version`

You should see "Scala code runner version 3.1.3 -- Copyright 2002-2022, LAMP/EPFL"


