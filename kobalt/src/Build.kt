import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.kotlin.*

val repos = repos()

val p = kotlinProject {

    name = "bismarck4"
    group = "com.levelmoney"
    artifactId = name
    version = "0.1"

    sourceDirectories {
        path("src/main/java")
        path("src/main/resources")
        path("src/main/res")
    }

    sourceDirectoriesTest {
        path("src/test/java")
        path("src/test/resources")
        path("src/test/res")
    }

    dependencies {
        compile("io.reactivex:rxjava:1.1.0")
        provided("com.squareup.wire:wire-runtime:1.8.0")
    }

    dependenciesTest {
        compile("junit:junit:4.12")
    }

    assemble {
        jar {
        }
    }
}
