# InventoryApp
This app help keep track of the products you have in your warehouse or store

#Screenshots

<img src="https://github.com/ajibadeseun/InventoryApp/commit/c2bb266ca83c30e1f021b22ae42b12234f7ff22d" width="400" height="700 />

#Gradle

apply plugin: 'com.android.application'

android {

    compileSdkVersion 26
    defaultConfig {
        applicationId "com.treble.inventoryapp"
        minSdkVersion 17
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {

    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    compile 'com.android.support:design:26.1.0'
    compile 'de.hdodenhof:circleimageview:2.2.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:0.5'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:2.2.2'
}

