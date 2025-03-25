

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.1.1"
}


javafx {
    version = "20.0.2"
    modules("javafx.controls")
}

jlink {
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "Life Drawing"
    }
}

repositories {
    mavenCentral()
}


dependencies {

    implementation("org.apache.commons:commons-imaging:1.0.0-alpha5")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(20)
    }
}

application {
    mainClass = "org.example.App"
    //mainModule = "life drawing"
}

