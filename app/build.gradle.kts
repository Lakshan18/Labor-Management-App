plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.labor_management_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.labor_management_app"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.lifecycle.viewmodel.ktx.v251)
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("me.zhanghai.android.materialratingbar:library:1.4.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.daimajia.easing:library:2.4@aar")
    implementation ("com.daimajia.androidanimations:library:2.4@aar")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

//    firebase implementation......
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage:21.0.1")
}