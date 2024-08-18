import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    js(IR) {
        browser()
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            api(compose.material3)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.bundles.ktor.common)
            implementation(libs.kotlinx.coroutines.core)

            api(libs.koin.core)
            implementation(libs.koin.compose.multiplatform)
            implementation(libs.kotlin.serialization)

            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

            api(libs.decompose.decompose)
            api(libs.essenty.lifecycle.coroutines)
            implementation(libs.decompose.extensions.compose)

            implementation(libs.coil.compose)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.mp)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.slf4j)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
            implementation(libs.sqldelight.sqlite.driver)
            implementation(libs.slf4j)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.koin.core.js)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
    }
}

android {
    namespace = "ph.mart.shopper.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

sqldelight {
    databases {
        create("ShopperDatabase") {
            packageName.set("ph.mart.shopper.db")
        }
    }
}