plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.2.0-alpha08").apply(false)
    id("com.android.library").version("8.2.0-alpha08").apply(false)
    kotlin("android").version("1.8.21").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.22.3")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.13.3")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
