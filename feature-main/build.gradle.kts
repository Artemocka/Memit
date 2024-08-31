plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.dracul.feature_main"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(
                "proguard-rules.pro"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    buildFeatures.compose = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":components:notes"))
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose.v191)
    implementation(libs.koin.android)
    implementation(libs.decompose.jetbrains)
    implementation(libs.decompose)
    implementation(libs.richeditor.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.animation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(project(":feature-reminder"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.ui.tooling)
}