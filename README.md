# Galaxy Garden

To run:

```
$ git clone https://github.com/tedigc/galaxy-garden
$ cd galaxy-garden
$ ./gradlew desktop:run
```

#### Building the HTML distribution
To build the HTML version, go to the project root and run:
```
$ ./gradlew html:dist
```

**Note** - Building a HTML dist is *slow*. During development, you should use `./gradlew html:superDev` which allows you to debug your code directly in the browser. More information about this is available on the [libGDX Wiki](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline#running-the-html-project).

---

Created by [halfcutdev](https://github.com/halfcutdev)
