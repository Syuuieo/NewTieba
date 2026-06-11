pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "NewTieba"

include(":app")
include(":core:common")
include(":core:ui")
include(":core:testing")
include(":protocol")
include(":network")
include(":database")
include(":domain")
include(":data")
include(":feature:home")
include(":feature:forum")
include(":feature:thread")
include(":feature:search")
include(":feature:profile")
include(":feature:message")
include(":feature:login")
include(":feature:settings")
