import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions")
    id("gradm.build-logic.root-project.base")
}

allprojects {
    configureDependencyUpdates(
        pinnedGroups = mapOf(
            "org.gradle.kotlin.kotlin-dsl" to versions.kotlin.dsl,
            "org.jetbrains.kotlin" to versions.kotlin.toString(),
            "org.jetbrains.kotlin.plugin.serialization" to versions.kotlin.toString(),
        ),
        pinnedModules = mapOf(
            "kotlinpoet" to versions.kotlinpoet,
            "kotlinx-coroutines-core" to versions.kotlinx.coroutines,
            "kotlinx-serialization-json" to versions.kotlinx.serialization,
            "org.gradle.kotlin.embedded-kotlin.gradle.plugin" to versions.kotlin.dsl,
        ),
    )
}

fun Project.configureDependencyUpdates(
    pinnedGroups: Map<String, String> = emptyMap(),
    pinnedModules: Map<String, String> = emptyMap(),
) {
    apply(plugin = "com.github.ben-manes.versions")
    tasks.withType<DependencyUpdatesTask> {
        rejectVersionIf {
            if (candidate.version == "") return@rejectVersionIf true
            when {
                pinnedGroups.keys.contains(candidate.group) -> candidate.version != pinnedGroups[candidate.group]
                pinnedModules.keys.contains(candidate.module) -> candidate.version != pinnedModules[candidate.module]
                else -> false
            }
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
