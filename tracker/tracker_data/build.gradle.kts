import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    defaultConfig {
        val apiKey = gradleLocalProperties(rootDir).getProperty("API_KEY")

        buildConfigField("String", "API_KEY", "\"${apiKey}\"")
        buildConfigField("String", "base_url", "\"my.base.url\"")
    }
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.trackerDomain))

    "implementation"(Retrofit.okHttp)
    "implementation"(Retrofit.retrofit)
    "implementation"(Retrofit.okHttpLoggingInterceptor)
    "implementation"(Retrofit.moshiConverter)

    "kapt"(Room.roomCompiler)
    "implementation"(Room.roomKtx)
    "implementation"(Room.roomRuntime)
}