import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.mikepenz.aboutlibraries.plugin")
    id("androidx.room")
}

android {
    namespace = "ua.acclorite.book_story"
    compileSdk = 34

    defaultConfig {
        applicationId = "ua.acclorite.book_story"
        minSdk = 26
        targetSdk = 34
        versionCode = 6
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false

            proguardFiles("proguard-rules.pro")
        }

        create("release-debug") {
            initWith(getByName("release"))
            applicationIdSuffix = ".release.debug"
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

aboutLibraries {
    registerAndroidTasks = false
    prettyPrint = true
    gitHubApiToken = gradleLocalProperties(rootDir, providers)["github-key"] as? String

    filterVariants = arrayOf("debug", "release", "release-debug")
    excludeFields = arrayOf("generated", "funding", "description")
}

dependencies {

    // Default
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")

    // Compose BOM libraries
    // Compose BOM was eliminated - it is recognized as Closed Source in AboutLibraries.
    implementation("androidx.compose.foundation:foundation:1.6.8")
    implementation("androidx.compose.animation:animation:1.6.8")
    implementation("androidx.compose.animation:animation-android:1.6.8")
    implementation("androidx.compose.foundation:foundation-layout:1.6.8")
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.ui:ui-graphics:1.6.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")
    implementation("androidx.compose.ui:ui-android:1.6.8")
    implementation("androidx.compose.material3:material3:1.3.0-beta05")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation("androidx.compose.material:material:1.6.8")

    // All dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.34.0")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-android-compiler:2.52")
    implementation("com.google.dagger:hilt-compiler:2.52")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Permission Handling
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // Pdf parser
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")

    // Epub parser
    implementation("com.positiondev.epublib:epublib-core:3.1") {
        exclude("xmlpull")
    }
    implementation("org.jsoup:jsoup:1.18.1")

    // Fb2 parser
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.1")

    // App Compat for Language Switcher
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.appcompat:appcompat-resources:1.7.0")

    // Coil for loading bitmaps
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // About open source libraries
    implementation("com.mikepenz:aboutlibraries-core:11.2.2")
    implementation("com.mikepenz:aboutlibraries-compose-m3:11.2.2")

    // Drag & Drop
    implementation("sh.calvin.reorderable:reorderable:2.3.0")
}