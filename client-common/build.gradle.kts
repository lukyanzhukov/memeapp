import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version Versions.KOTLIN
}

repositories {
    maven(url = "https://dl.bintray.com/florent37/maven")
    google()
    jcenter()
}

kotlin {
    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "ClientCommon"
            }
        }
    }

    jvm("android")

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("io.ktor:ktor-client-core:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-websockets:${Versions.KTOR}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${Versions.SERIALIZATION}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.COROUTINES}")
        implementation("io.ktor:ktor-client-json:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-logging:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-serialization:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-auth:${Versions.KTOR}")
        implementation("com.russhwolf:multiplatform-settings:0.5.1")
        implementation(project(":common"))
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("io.ktor:ktor-client-android:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-websockets-jvm:${Versions.KTOR}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.SERIALIZATION}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}")
        implementation("io.ktor:ktor-client-json-jvm:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-core-jvm:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-logging-jvm:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-serialization-jvm:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-auth-jvm:${Versions.KTOR}")
        implementation("org.slf4j:slf4j-simple:1.6.1")
    }

    sourceSets["iosMain"].dependencies {
        implementation("io.ktor:ktor-client-ios:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-websockets-ios:${Versions.KTOR}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${Versions.KTOR}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.COROUTINES}")
        implementation("io.ktor:ktor-client-core-ios:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-json-ios:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-serialization-native:${Versions.KTOR}")
        implementation("io.ktor:ktor-client-auth-native:${Versions.KTOR}")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText(
            "#!/bin/bash\n"
                    + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                    + "cd '${rootProject.rootDir}'\n"
                    + "./gradlew \$@\n"
        )
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)