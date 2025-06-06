plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.graficadorapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.graficadorapp"
        minSdk = 25
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.guava:guava:30.1-android")

    // Configuración para evitar duplicados
    testImplementation(libs.junit) {
        exclude(group = "org.hamcrest") // Excluye Hamcrest de JUnit
    }
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Si encuentras que Guava es necesario, añade esta configuración
    configurations.all {
        resolutionStrategy {
            force("com.google.guava:guava:30.1.1-android")
            exclude(group = "com.google.guava", module = "listenablefuture")
        }
    }
}