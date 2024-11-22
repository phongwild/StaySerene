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
    implementation(fileTree(mapOf(
        "dir" to "E:\\DATN\\StaySerene\\app\\src\\main\\java\\phongtaph31865\\poly\\stayserene\\SDK_zalo",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))

    implementation(fileTree(mapOf(
        "dir" to "D:\\Totnghiep\\StaySerenee\\app\\src\\main\\java\\phongtaph31865\\poly\\stayserene\\SDK_zalo",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\buidu\\AndroidStudioProjects\\StaySerene\\app\\src\\main\\java\\phongtaph31865\\poly\\stayserene\\SDK_zalo",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    implementation(fileTree(mapOf(
        "dir" to "D:\\StaySerene\\app\\src\\main\\java\\phongtaph31865\\poly\\stayserene\\SDK_zalo",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))

    implementation(fileTree(mapOf(
        "dir" to "G:\\Hoc\\StaySerene\\app\\src\\main\\java\\phongtaph31865\\poly\\stayserene\\SDK_zalo",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\phong.ta\\StudioProjects\\StaySerene\\app\\src\\main\\java\\phongtaph31865\\poly\\stayserene\\SDK_zalo",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))

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
    //Country picker
    implementation ("com.hbb20:ccp:2.7.1")
    //nhận diện ảnh
    implementation ("androidx.exifinterface:exifinterface:1.3.7")
    //zalopay
    implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")
    //api javamail
    implementation ("com.sun.mail:android-mail:1.6.0")
    implementation ("com.sun.mail:android-activation:1.6.0")
    //pinview
    implementation ("io.github.chaosleung:pinview:1.4.4")


}