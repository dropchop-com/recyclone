# Recyclone

Dropchop's Common Library for rapid Java REST project development.

It provides common model, REST, service and repository semantics with already implemented features:
- localization,
- security,
- common parameters
- ...

Designed for JAX-RS CDI environment so it's best to run with Quarkus.

## Build

```bash 
mvn clean install
```

## Run development mode

```bash
cd test/quarkus
./mvnw compile quarkus:dev
```