plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //compose navigation
    alias(libs.plugins.kotlin.serialization)
    //KSP devtools (for room dependencies)
    alias(libs.plugins.devtools.ksp)
    //Hilt dependency injection
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.gabof92.hebrewaudiobible"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gabof92.hebrewaudiobible"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //material design premade composables/styles
    implementation(libs.material3)
    //compose navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    //Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    // Retrofit with Moshi Converter
    implementation(libs.retrofit2.converter.moshi)
    // Moshi
    implementation(libs.moshi.kotlin)
    //interceptor to log retrofit requests
    implementation(libs.okhttp3.logging.interceptor)
    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    //Jetpack DataStore to save app preferences/settings
    implementation(libs.datastore.preferences)

    //modules
    implementation(project(":usecases"))
    implementation(project(":data"))
    implementation(project(":domain"))
}