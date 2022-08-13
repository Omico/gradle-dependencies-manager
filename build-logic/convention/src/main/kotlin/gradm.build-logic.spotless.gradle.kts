import me.omico.age.spotless.configureSpotless
import me.omico.age.spotless.intelliJIDEARunConfiguration
import me.omico.age.spotless.kotlin
import me.omico.age.spotless.kotlinGradle

plugins {
    id("com.diffplug.spotless")
    id("gradm.build-logic.root-project.base")
    id("me.omico.age.spotless")
}

allprojects {
    configureSpotless {
        intelliJIDEARunConfiguration()
        kotlin(
            licenseHeaderFile = rootProject.file("spotless/copyright.kt"),
            licenseHeaderConfig = {
                updateYearWithLatest(true)
                yearSeparator("-")
            },
        )
        kotlinGradle(
            additionalExcludeTargets = setOf(
                ".gradm/**/*.gradle.kts",
            ),
        )
    }
}
