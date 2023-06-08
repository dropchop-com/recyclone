# Recyclone

Dropchop's Common Library for rapid Java RESTful project development.

It provides common model, REST, service and repository semantics with already implemented features:
- localization,
- security,
- common parameters
- ...

Designed for JAX-RS CDI environment, so it's best to run with Quarkus.

![Overview](https://github.com/ivlcic/recyclone/blob/4ec9266f62f3b34cc231cdcb3d82eaaf5797033c/docs/img/Recyclone.svg)

## Build

```bash 
mvn clean install
```

## Run development mode

```bash
cd test/quarkus
./mvnw compile quarkus:dev

# navigate to http://localhost:8080/api/spec/ui/
```

Test
