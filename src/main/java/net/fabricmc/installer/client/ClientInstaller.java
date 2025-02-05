/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.installer.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import mjson.Json;

import net.fabricmc.installer.LoaderVersion;
import net.fabricmc.installer.util.FabricService;
import net.fabricmc.installer.util.InstallerProgress;
import net.fabricmc.installer.util.Reference;
import net.fabricmc.installer.util.Utils;

public class ClientInstaller {
	public static String install(Path mcDir, String gameVersion, LoaderVersion loaderVersion, InstallerProgress progress) throws IOException {
		System.out.println("Installing " + gameVersion + " with fabric " + loaderVersion.name);

		String profileName = String.format("%s-%s-%s", Reference.LOADER_NAME, loaderVersion.name, gameVersion);

		Path versionsDir = mcDir.resolve("versions");
		Path profileDir = versionsDir.resolve(profileName);
		Path profileJson = profileDir.resolve(profileName + ".json");

		if (!Files.exists(profileDir)) {
			Files.createDirectories(profileDir);
		}

		Path profileJar = profileDir.resolve(profileName + ".jar");
		Files.deleteIfExists(profileJar);

		Json json = FabricService.queryMetaJson(String.format("v2/versions/loader/%s/%s/profile/json", gameVersion, loaderVersion.name));
		Files.write(profileJson, json.toString().getBytes(StandardCharsets.UTF_8));

		progress.updateProgress(Utils.BUNDLE.getString("progress.done"));

		return profileName;
	}
}
