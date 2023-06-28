import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
    kotlin("plugin.serialization") version "1.8.0"
    id("com.codingfeline.buildkonfig")
}

@Suppress("TooGenericExceptionCaught")
configure<BuildKonfigExtension> {
    packageName = "com.santansarah.city_kmm"

    val props = Properties()

    try {
        props.load(file("key.properties").inputStream())
    } catch (e: Exception) {
        // keys are private and can not be committed to git
    }

    defaultConfigs {
        buildConfigField(
            Type.STRING,
            "WEB_CLIENT_ID",
            props["WEB_CLIENT_ID"]?.toString()
        )
        buildConfigField(
            Type.STRING,
            "KTOR_IP_ADDR",
            props["KTOR_IP_ADDR"]?.toString()
        )
    }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.bundles.ktor)
                implementation(libs.koin.core)

                api(libs.shareResources)
                api(libs.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.santansarah.city_kmm"
    compileSdk = 33
    defaultConfig {
        minSdk = 29
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.santansarah.city_kmm"
    multiplatformResourcesClassName = "SharedRes"
}