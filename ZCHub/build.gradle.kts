plugins {
    java
}

group = "lt.itsvaidas"
version = "1.0"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly(project(":ZCAPI"))
}
