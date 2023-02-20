/*
 * Copyright 2023 Omico
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
package me.omico.gradm.typesafe.task

import me.omico.gradm.internal.codegen.generateProjectSourceFiles
import me.omico.gradm.internal.codegen.generateTypesafePluginSourceFile
import me.omico.gradm.service.GradmBuildService
import me.omico.gradm.task.GradmTask
import me.omico.gradm.utility.clearDirectory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class GradmTypesafeGenerator : GradmTask<GradmBuildService<*>>() {

    abstract val outputDirectoryProperty: DirectoryProperty
        @OutputDirectory get

    @TaskAction
    fun generate() {
        val projectInfoFile = configFileProperty.get().asFile
        val outputDirectory = outputDirectoryProperty.asFile.get().toPath()
        outputDirectory.clearDirectory()
        generateProjectSourceFiles(
            projectPaths = projectInfoFile.readLines(),
            generatedSourcesDirectory = outputDirectory,
        )
        generateTypesafePluginSourceFile(
            generatedSourcesDirectory = outputDirectory,
        )
    }
}
