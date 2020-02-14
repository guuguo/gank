plugins {
    id(BuildPlugins.androidApplication)
//    kotlin("android")
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
}
fun gitVersionCode() =
    currentTime()//Date().format("yyMMdd", java.util.TimeZone.getTimeZone("UTC")).toInteger()

android {
    compileSdkVersion(AndroidSdk.compile)
    defaultConfig {
        applicationId = "com.guuguo.gank"
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = gitVersionCode()
        versionName = "1.7"
        ndk {
            //设置支持的SO库架构
            abiFilters("armeabi", "x86", "armeabi-v7a", "x86_64", "arm64-v8a")
        }
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }
    signingConfigs {
        create("config") {
            keyAlias = "mimi"
            keyPassword = "guuguo123"
            storeFile = file("mimi.jks")
            storePassword = "android"
        }
    }
    packagingOptions {
        exclude("META-INF/library_release.kotlin_module")
        exclude("META-INF/*_release.kotlin_module")
    }
    buildTypes {

        getByName("debug") {
            versionNameSuffix = "d"
            isMinifyEnabled = true
            isShrinkResources = false
            isZipAlignEnabled = true
            signingConfig = signingConfigs.getByName("config")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            isZipAlignEnabled = true
            signingConfig = signingConfigs.getByName("config")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    applicationVariants.all { variant ->
        variant.outputs.all { output ->
            val fileName = output.outputFile.name
            fileName.replace("app", "gank-${variant.versionName}-${variant.versionCode}")

            true
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
    dataBinding {
        isEnabled = true
    }
    lintOptions {
        isAbortOnError = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_1_8.toString()
//    }
//    buildFeatures {
//        compose = true
//    }
}

dependencies {
    implementation(fileTree(hashMapOf("dir" to "libs", "include" to arrayOf("*.jar"))))

    implementation(project(":baselib"))
    implementation(project(":fuliba"))

    testImplementation(Deps.mockito.core)
    testImplementation(Deps.mockito.all)
    testImplementation(Deps.junit)

    androidTestImplementation(Deps.atsl.runner)
    androidTestImplementation(Deps.atsl.rules)
    androidTestImplementation(Deps.espresso.core)
    androidTestImplementation("org.hamcrest:hamcrest-library:2.1")

    implementation(Deps.navigation.runtime_ktx)
    implementation(Deps.navigation.fragment_ktx)
    implementation(Deps.navigation.ui)

    implementation(Deps.room.runtime)
    implementation(Deps.room.rxjava2)
    kapt(Deps.room.compiler)


    implementation(Deps.multidex)

    implementation(Deps.dagger.runtime)
    implementation(Deps.dagger.android_support)
    kapt(Deps.dagger.compiler)

    implementation("com.github.florent37:viewanimator:1.1.1")

    implementation("com.github.Lauzy:LBehavior:1.0.3")
    implementation("com.github.jrvansuita:MaterialAbout:0.1.9")
    implementation("com.github.castorflex.smoothprogressbar:library:1.1.0")
    implementation("com.github.iielse:ImageWatcher:1.1.5")
    implementation("com.tapadoo.android:alerter:3.0.1")
    implementation("com.mikepenz:materialdrawer:6.1.2")
    {
        exclude("com.google.android.material")
    }
}
