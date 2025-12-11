# Recyclone

Dropchop's Common Library for rapid Java RESTful project development.

It provides common model, REST, service and repository semantics with already implemented features:
- localization,
- security,
- common parameters
- JPA and Elasticsearch repositories
- ...

Designed for JAX-RS CDI environment, so it's best to run with Quarkus.

![Overview](https://raw.githubusercontent.com/dropchop-com/recyclone/4ec9266f62f3b34cc231cdcb3d82eaaf5797033c/docs/img/Recyclone.svg  "Overview")

## Build

```bash 
mvn clean install
```

## Run development mode

```shell
cd server/quarkus/integration-tests
mvn compile quarkus:dev

# navigate to http://localhost:8080/api/spec/ui/
```

Find packaging pom artifacts:
```bash
find . -path '*/target' -prune -o -name pom.xml -exec \
  xmlstarlet sel -N x="http://maven.apache.org/POM/4.0.0" \
  -t -m "/x:project[x:packaging='pom']" -v "/x:project/x:artifactId" -n {} \;
```

# Troubleshooting Guide


## Docker issues when running tests

- check if docker is running
- check if user in docker group
- check if docker compose plugin is installed

### Issues with API version

create **~/.docker-java.properties**
add
```
api.version=1.44
```
