plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "phongtaph31865.poly.stayserene"
    compileSdk = 34

    defaultConfig {
        applicationId = "phongtaph31865.poly.stayserene"
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    viewBinding { enable = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //Picasso
    implementation ("com.squareup.picasso:picasso:2.8")
    //Circle image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation ("com.firebaseui:firebase-ui-storage:8.0.2")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    //Facebook
    //implementation ("com.facebook.android:facebook-android-sdk:[4,5)")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    //Gson
    implementation ("com.google.code.gson:gson:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.10.0")
    //PopUp dialog
    implementation ("com.saadahmedev.popup-dialog:popup-dialog:2.0.0")
    implementation ("org.imaginativeworld.oopsnointernet:oopsnointernet:2.0.0")
    //Location
    implementation ("io.ipgeolocation:ipgeolocation:1.0.16")
    //Handle image
    implementation ("com.google.mlkit:text-recognition:16.0.1")
}