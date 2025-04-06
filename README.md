# virtually

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Native Build Configuration

This project is configured to build using native by default. The native build process creates a standalone executable that doesn't require a JVM to run, resulting in faster startup times and lower memory usage.

### Native Build Requirements

To build this project, you'll need the following tools installed:

1. **GraalVM** - A high-performance JDK distribution with native image capabilities
   - Installation guide: <https://www.graalvm.org/latest/docs/getting-started/>
   - For macOS: `brew install --cask graalvm/tap/graalvm-jdk-21`
   - For Linux: Download from <https://github.com/graalvm/graalvm-ce-builds/releases>

2. **UPX** - Ultimate Packer for eXecutables, used to compress the native executable
   - Installation guide: <https://upx.github.io/>
   - For macOS: `brew install upx`
   - For Linux: `apt-get install upx` or equivalent for your distribution

### Building the Native Executable

The project is configured to build a native executable by default:

```shell script
./gradlew build
```

This works because the following settings are enabled in `application.properties`:
```
quarkus.package.jar.enabled=false
quarkus.native.enabled=true
quarkus.native.compression.level=5
quarkus.native.compression.additional-args=-v

# Additional optimizations to reduce binary size
quarkus.native.add-all-charsets=false
quarkus.native.enable-http-url-handler=false
quarkus.native.enable-https-url-handler=false
```

If you don't have GraalVM installed locally, you can run the native executable build in a container:

```shell script
./gradlew build -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/virtually-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and
  Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on
  it.
- REST Client ([guide](https://quarkus.io/guides/rest-client)): Call REST services
- Apache Kafka Client ([guide](https://quarkus.io/guides/kafka)): Connect to Apache Kafka with its native API
- Apache Avro ([guide](https://quarkus.io/guides/kafka-schema-registry-avro)): Provide support for the Avro data
  serialization system
- REST resources for Hibernate Reactive with Panache ([guide](https://quarkus.io/guides/rest-data-panache)): Generate
  Jakarta REST resources for your Hibernate Reactive Panache entities and repositories
- REST JSON-B ([guide](https://quarkus.io/guides/rest#json-serialisation)): JSON-B serialization support for Quarkus
  REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on
  it.
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and
  method parameters for your beans (REST, CDI, Jakarta Persistence)
- Confluent Schema Registry - Avro ([guide](https://quarkus.io/guides/kafka-schema-registry-avro)): Use Confluent as
  Avro schema registry
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus
  REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code
  for Hibernate ORM via the active record or the repository pattern
- Apache Kafka Streams ([guide](https://quarkus.io/guides/kafka-streams)): Implement stream processing applications
  based on Apache Kafka
- Reactive PostgreSQL client ([guide](https://quarkus.io/guides/reactive-sql-clients)): Connect to the PostgreSQL
  database using the reactive pattern
- REST JAXB ([guide](https://quarkus.io/guides/resteasy-reactive#xml-serialisation)): JAXB serialization support for
  Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that
  depend on it.
- Reactive Routes ([guide](https://quarkus.io/guides/reactive-routes)): REST framework offering the route model to
  define non blocking endpoints

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)

### REST Client

Invoke different services through REST with JSON

[Related guide section...](https://quarkus.io/guides/rest-client)

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
