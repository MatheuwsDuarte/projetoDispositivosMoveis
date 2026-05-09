plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.projetodispositivosmoveis"

    // compileSdk é apenas um número inteiro — sem bloco, sem chaves
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.projetodispositivosmoveis"
        minSdk = 24
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //RecyclerView — necessário para a lista de Trilhas
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //CoordinatorLayout — necessário para o layout da MainActivity
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // --- ROOM ---
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // --- LIFECYCLE ---
    val lifecycleVersion = "2.8.3"
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycleVersion")

    // --- MARKWON: renderiza Markdown em TextView nativo do Android ---
// Core: renderização básica (negrito, itálico, listas, headers)
    val markwonVersion = "4.6.2"
    implementation("io.noties.markwon:core:$markwonVersion")
}