plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ph.mart.shopper"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ph.mart.shopper"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isMinifyEnabled = true

            proguardFiles(
                // Includes the default ProGuard rules files that are packaged with
                // the Android Gradle plugin. To learn more, go to the section about
                // R8 configuration files.
                getDefaultProguardFile("proguard-android-optimize.txt"),

                // Includes a local, custom Proguard rules file
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
}