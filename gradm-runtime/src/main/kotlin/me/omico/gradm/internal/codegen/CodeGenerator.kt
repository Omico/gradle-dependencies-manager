/*
 * Copyright 2022 Omico
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.omico.gradm.internal.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import me.omico.gradm.internal.VersionsMeta
import me.omico.gradm.internal.YamlDocument
import me.omico.gradm.internal.path.GradmPaths
import me.omico.gradm.internal.path.RootProjectPaths
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale

internal fun generateDependenciesProjectFiles(document: YamlDocument, versionsMeta: VersionsMeta) {
    RootProjectPaths.copyTo(GradmPaths.GeneratedDependenciesProject)
    generateGradleBuildScript()
    clearDir(GradmPaths.GeneratedDependenciesProject.sourceDir)
    generateDependenciesSourceFiles(document, versionsMeta)
    generateVersionsSourceFile(document)
}

internal fun clearDir(dir: Path) {
    if (Files.exists(dir)) Files.walk(dir).sorted(Comparator.reverseOrder()).forEach(Files::delete)
    Files.createDirectories(dir)
}

private val defaultSuppressWarningTypes = arrayOf(
    "MemberVisibilityCanBePrivate",
    "RedundantVisibilityModifier",
    "unused",
)

internal fun FileSpec.Builder.addSuppressWarningTypes(vararg types: String = defaultSuppressWarningTypes): FileSpec.Builder =
    AnnotationSpec.builder(Suppress::class)
        .addMember("%S,".repeat(types.count()).trimEnd(','), *types)
        .build()
        .let(::addAnnotation)

internal fun FileSpec.Builder.addGradmComment(): FileSpec.Builder =
    addComment(
        """

        Generated by Gradm, will be overwritten by every dependencies update, do not edit!!!

        """.trimIndent()
    )

internal fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
