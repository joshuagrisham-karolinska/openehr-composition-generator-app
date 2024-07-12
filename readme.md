# openEHR Composition Generator

Embryo of a small utility app which can currently:

- convert an openEHR OPT Template to a Web Template
- generate example openEHR Compositions given an openEHR Template
- validate a given composition string against a template (supports JSON, XML, Simple Flat JSON, and Simple Structured JSON)
- convert a given composition string to other formats (supports JSON, XML, Simple Flat JSON, and Simple Structured JSON)

## Run locally

```sh
mvn clean compile quarkus:dev
```

## Build as container image

```sh
mvn clean package
docker run --rm -p 8080:8080 ${USER}/composition-generator-app:1.0-SNAPSHOT
```
