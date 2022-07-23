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
package me.omico.gradm.integration.github

import me.omico.gradm.VersionsMeta
import me.omico.gradm.asVersionsMeta
import me.omico.gradm.path.gradmProjectPaths
import me.omico.gradm.path.integrationFolder
import me.omico.gradm.store
import java.nio.file.Path
import kotlin.io.path.createDirectories

object GradmGithubIntegrationConfigs {

    private val githubIntegrationDirPath: Path = gradmProjectPaths.integrationFolder.resolve("github")
    private val versionsMetaPath: Path = githubIntegrationDirPath.resolve("versions-meta.txt")
    private val versionsMetaHashPath: Path = githubIntegrationDirPath.resolve("versions-meta.hash")

    val localVersionsMeta: VersionsMeta?
        get() = versionsMetaPath.asVersionsMeta()

    fun updateLocalVersionsMeta(versionsMeta: VersionsMeta) {
        githubIntegrationDirPath.createDirectories()
        versionsMeta.store(versionsMetaPath, versionsMetaHashPath)
    }
}
