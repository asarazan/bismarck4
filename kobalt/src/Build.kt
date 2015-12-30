import com.beust.kobalt.*
import com.beust.kobalt.plugin.kotlin.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.publish.*

val repos = repos()

val p = kotlinProject {

    group = "com.levelmoney"
    name = "bismarck4"
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

        // I want to switch these to provided but it doesn't seem to be supported
        provided("com.squareup.wire:wire-runtime:1.8.0")
        provided("com.google.code.gson:gson:2.5")
    }

    dependenciesTest {
        compile("junit:junit:4.12")
        compile("org.jetbrains.kotlin:kotlin-test:1.0.0-beta-4584")
    }

    assemble {
        mavenJars {
        }
    }

    jcenter {
        publish = true
    }
}
