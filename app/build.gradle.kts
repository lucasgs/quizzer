plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    id ("kotlin-kapt")
}

android {
    namespace = "com.dendron.quizzer"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dendron.quizzer"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility (JavaVersion.VERSION_17)
        targetCompatibility (JavaVersion.VERSION_17)
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation (libs.androidx.core)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // testing
    implementation(libs.junit)
    androidTestImplementation(libs.bundles.androidx.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Mocks
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.coroutines.test)

    // Turbine for testing flows
    testImplementation(libs.turbine)

    // Datastore
    implementation(libs.androidx.datastore.preferences)
}

kapt {
  correctErrorTypes = true
}