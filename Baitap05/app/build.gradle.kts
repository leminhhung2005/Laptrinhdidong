plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.btap21_11_2025"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.btap21_11_2025"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.0")
    // Picasso (tải ảnh)
    implementation("com.squareup.picasso:picasso:2.71828")
    // RecyclerView (nếu chưa có)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}