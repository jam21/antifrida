plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.tg.antifrida"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tg.antifrida"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

    publishing {
        singleVariant("release") {
            // Customize the "release" component if needed (e.g., withSourcesJar())
        }
        singleVariant("debug") {
            // Customize the "release" component if needed (e.g., withSourcesJar())
        }
    }
}

dependencies {
    implementation(project(":antifrida"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.tg.antifrida"
                artifactId = "antifrida"
                version = "0.1"
            }

            create<MavenPublication>("debug") {
                from(components["debug"])
                groupId = "com.tg.antifrida"
                artifactId = "antifrida-debug"
                version = "1.0"
            }
        }
    }
}