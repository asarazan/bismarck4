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
        //        compile("com.beust:jcommander:1.48")
        compile("io.reactivex:rxjava:1.1.0")
//        compile("org.jetbrains.kotlin:kotlin-stdlib:1.0.0-beta-4583")
    }

    dependenciesTest {
        //        compile("org.testng:testng:6.9.5")
        compile("junit:junit:4.12")
    }

    assemble {
        jar {
        }
    }
}
