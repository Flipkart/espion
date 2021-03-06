Espion - Scala Metrics
======================

*Capturing JVM- and application-level metrics. So you know what's going on.*


### Usage

Espion provides an easy way to create metrics and health checks in Scala. It requires an application wide `MetricRegistry`. Create an **`Instrumented`** trait that refers to that registry and extends the `InstrumentedBase` trait.

```scala
object YourAppRegistry {
  /** The application wide metrics registry. */
  val metricRegistry = new com.codahale.metrics.MetricRegistry()
  
  /** You might want to publish to jmx **/
  val jmxReporter: JmxReporter = JmxReporter
      .forRegistry(REGISTRY)
      .inDomain("metrics")
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .convertRatesTo(TimeUnit.SECONDS)
      .build()
  
  jmxReporter.start()
}
trait Instrumented extends com.flipkart.metrics.InstrumentedBase {
  val metricRegistry = YourAppRegistry.metricRegistry
}
```

Now you can create profile by using the `profile` method. You can also use the `@Timed` annotation.

```scala
class Example extends Instrumented {

  def readAll(): Seq[Row] = profile("readAll") {
    db.fetchRows()
  }

  @Timed("write")
  def writeData(obj:MyClass) {
    db.write(obj)
  }
  
  @Timed("writeAsync")
  def writeDataAsync(obj:MyClass):Future[Boolean]= {
    db.asyncWrite(obj)
  }

}
```

## Download

SBT:
```
resolvers += Resolver.jcenterRepo
libraryDependencies += "com.flipkart" %% "espion" % "1.0.4"
```

Then following in build.sbt
```
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
```

Maven:
```
<properties>
    <scala.version>2.11.7</scala.version>
    <scala.dep.version>2.11</scala.dep.version>
</properties>
<dependency>
    <groupId>com.flipkart</groupId>
    <artifactId>espion_${scala.dep.version}</artifactId>
    <version>1.0.4</version>
</dependency>
<repositories>
  <repository>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
  </repository>
</repositories>
<!-- You will also need to add this compiler plugin http://docs.scala-lang.org/overviews/macros/paradise.html -->
```



## Support

If you find a bug, please open an [issue](https://github.com/Flipkart/espion/issues), or send a pull request with your changes :)

## Contributors

[Kinshuk Bairagi](mailto:me@kinshuk.in)


