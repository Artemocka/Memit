plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.dracul.feature_edit"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    buildFeatures.compose = true

}

dependencies {
    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":components:notes"))
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
    implementation(libs.androidx.material3.android)
    implementation(project(":feature-main"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}