# teamsykmelding-pik
This project contains the application code and infrastructure for teamsykmelding-pik

## Technologies used
* Kotlin
* Ktor
* Gradle


#### Requirements
* JDK 25


### Building the application
#### Compile and package application
To build locally and run the integration tests you can simply run
``` bash
./gradlew shadowJar
 ```
 or  on windows 
`gradlew.bat shadowJar`

#### Creating a docker image
Creating a docker image should be as simple as
``` bash
docker build -t teamsykmelding-pik .
```

#### Running a docker image
``` bash
docker run --rm -it -p 8080:8080 teamsykmelding-pik
```

### Upgrading the Gradle wrapper
Find the newest version of Gradle here: https://gradle.org/releases/ Then run this command:

``` bash
./gradlew wrapper --gradle-version $gradleVersjon
```

### Contact

This project is maintained by [navikt/teamsykmelding](CODEOWNERS)

Questions and/or feature requests? Please create an [issue](https://github.com/navikt/teamsykmelding-pik/issues)

If you work in [@navikt](https://github.com/navikt) you can reach us at the Slack
channel [#team-sykmelding](https://nav-it.slack.com/archives/CMA3XV997)