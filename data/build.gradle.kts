plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies{
    //Access to other modules
    implementation(project(":domain"))

    /*Provides annotations @Inject and @Singleton
    for injecting dependencies directly to constructor*/
    implementation(libs.javax.inject)
}
