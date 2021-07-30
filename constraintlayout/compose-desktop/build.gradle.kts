import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.compose") version "0.5.0-build270"
    id("compose.desktop.build.publishing")
}

repositories {
//    mavenCentral()
//    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("androidx.constraintlayout:constraintlayout-core:1.0.0-beta04")
}

//globalConfig.pomName = value('test')
//globalConfig.pomDescription.value = 'plop'

//globalConfig {
//    pomName = 'ConstraintLayout for Jetpack Compose'
//    pomDescription = 'ConstraintLayout for Jetpack Compose'
//}
