plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.apo"
    compileSdk = 33  // Target Android 13 (Stable)

    defaultConfig {
        applicationId = "com.example.apo"
        minSdk = 26
        targetSdk = 33
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

    // Standard Java 1.8 compatibility
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // !!! IMPORTANT: These versions are compatible with SDK 33 !!!

    // Core KTX 1.10.1 is the last version to support SDK 33
    implementation("androidx.core:core-ktx:1.10.1")

    // Appcompat 1.6.1 is stable for SDK 33
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Material 1.9.0 matches SDK 33 (1.11.0 requires SDK 34)
    implementation("com.google.android.material:material:1.9.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}