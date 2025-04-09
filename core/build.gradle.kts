import java.io.FileInputStream
import java.util.Base64
import java.util.Properties

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("signing")
}

android {
    compileSdk = 35
    namespace = "com.google.ar.sceneform"

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("lib-proguard-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    androidResources {
        noCompress += listOf("filamat", "ktx")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(files("libs/libsceneform_runtime_schemas.jar"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.jdk8)
    api(libs.filament.android)
    api(libs.gltfio.android)
    api(libs.filament.utils.android)
    implementation(libs.annotation)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.fuel)
    implementation(libs.fuel.android)
    implementation(libs.fuel.coroutines)
}

val localProps = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

val publishKey = String(
    Base64.getDecoder().decode(localProps.getProperty("PUBLISH_KEY_BASE64")), Charsets.UTF_8
)
val publishPassphrase: String = localProps.getProperty("PUBLISH_KEY_PASSPHRASE")
val ossrhUsername: String = localProps.getProperty("OSSRH_USER_NAME")
val ossrhPassword: String = localProps.getProperty("OSSRH_PASSWORD")

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.lascade"
            artifactId = "sceneform"
            version = "2.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Sceneform")
                description.set("A fork of Google Sceneform, but without XR support.")
                url.set("https://github.com/Lascade-Co/sceneform")

                licenses {
                    license {
                        name.set("Apache-2.0 License")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("rohit")
                        name.set("Rohit T P")
                        email.set("rohit@lascade.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/Lascade-Co/sceneform.git")
                    developerConnection.set("scm:git:ssh://github.com:Lascade-Co/sceneform.git")
                    url.set("https://github.com/Lascade-Co/sceneform")
                }
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
        maven {
            name = "local"
            url = layout.buildDirectory.dir("repo").get().asFile.toURI()
        }
    }
}

signing {
    useInMemoryPgpKeys(publishKey, publishPassphrase)
    sign(publishing.publications["release"])
}
