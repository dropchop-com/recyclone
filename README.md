# Recyclone

Dropchop's Common Library for rapid Java RESTful project development.

It provides common model, REST, service and repository semantics with already implemented features:
- localization,
- security,
- common parameters
- ...

Designed for JAX-RS CDI environment so it's best to run with Quarkus.

![Overview](docs/imag/Recyclone.svg)

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
