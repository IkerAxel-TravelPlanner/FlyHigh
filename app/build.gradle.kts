plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // HILT
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")

    // Firebase
    id("com.google.gms.google-services") version "4.4.2"
}

android {
    namespace = "com.example.FlyHigh"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.flyhigh"
        minSdk = 26
        targetSdk = 35
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
    // AndroidX + Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // BOM de Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.adapters)
    implementation(libs.androidx.runtime.livedata)

    // Coil para cargar imágenes en Compose ✅ AÑADIDO
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.firestore.ktx)

    // Gson
    implementation("com.google.code.gson:gson:2.8.8")

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.google.transportation.consumer)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.guava)
    implementation(libs.androidx.room.paging)
    testImplementation(libs.androidx.room.testing)

    // Material
    implementation(libs.material)

    // HILT
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Retrofit + OkHttp (API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Mockito y Mockk
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // MaxKeppeler Sheets - Corrección de importación
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.1.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.1.0")
}
