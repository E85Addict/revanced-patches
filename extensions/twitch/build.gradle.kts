dependencies {
    compileOnly(project(":extensions:shared:library"))
    compileOnly(project(":extensions:twitch:stub"))
    compileOnly(libs.okhttp)
    compileOnly(libs.retrofit)
    compileOnly(libs.annotation)
    compileOnly(libs.appcompat)
}