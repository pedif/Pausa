pluginManagement {
    repositories {
//        google {
//            content {
//                includeGroupByRegex("com\\.android.*")
//                includeGroupByRegex("com\\.google.*")
//                includeGroupByRegex("androidx.*")
//            }
//        }
//        mavenCentral()
//        gradlePluginPortal()
        maven(url =  "https://maven.myket.ir" )
        maven(url = "https://archive.ito.gov.ir/gradle/maven_central/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        google()
//        mavenCentral()
        maven(url =  "https://maven.myket.ir" )
        maven(url = "https://archive.ito.gov.ir/gradle/maven_central/")
    }
}

rootProject.name = "Pausa"
include(":app")
include(":designsystem")
include(":core")
include(":domain")
include(":feature:home")
include(":settings")
include(":onboarding")
include(":feature:focus")
include(":feature:quick")
