package app.revanced.patches.music.layout.branding

import app.revanced.patcher.patch.resourcePatch
import app.revanced.patcher.patch.stringOption
import app.revanced.util.ResourceGroup
import app.revanced.util.Utils.trimIndentMultiline
import app.revanced.util.copyResources
import java.io.File
import java.nio.file.Files

private const val REVANCED_ICON = "ReVanced*Logo" // Can never be a valid path.
private const val APP_NAME = "YT Music"

private val iconResourceFileNames = arrayOf(
    "adaptiveproduct_youtube_music_background_color_108",
    "adaptiveproduct_youtube_music_foreground_color_108",
    "ic_launcher",
    "ic_launcher_round",
).map { "$it.png" }.toTypedArray()

private val iconResourceFileNamesNew = mapOf(
    "adaptiveproduct_youtube_music_foreground_color_108" to "adaptiveproduct_youtube_music_2024_q4_foreground_color_108",
    "adaptiveproduct_youtube_music_background_color_108" to "adaptiveproduct_youtube_music_2024_q4_background_color_108",
)

private val mipmapDirectories = arrayOf(
    "xxxhdpi",
    "xxhdpi",
    "xhdpi",
    "hdpi",
    "mdpi",
).map { "mipmap-$it" }

@Suppress("unused")
val customBrandingPatch = resourcePatch(
    name = "Custom Music branding",
    description = "Applies a custom app name and icon. Defaults to \"YT Music ReVanced\" and the ReVanced logo.",
    use = true,
) {
    compatibleWith("com.google.android.apps.youtube.music")

    val appName by stringOption(
        key = "appName",
        default = APP_NAME,
        values = mapOf(
            "YouTube Music ReVanced" to APP_NAME,
            "YTM ReVanced" to "YTM ReVanced",
            "YTM" to "YTM",
            "YouTube Music" to "YouTube Music",
        ),
        title = "App name",
        description = "The name of the app.",
    )

    val icon by stringOption(
        key = "iconPath",
        default = REVANCED_ICON,
        values = mapOf("ReVanced Logo" to REVANCED_ICON),
        title = "App icon",
        description = """
            The icon to apply to the app.
            
            If a path to a folder is provided, the folder must contain the following folders:

            ${mipmapDirectories.joinToString("\n") { "- $it" }}

            Each of these folders must contain the following files:

            ${iconResourceFileNames.joinToString("\n") { "- $it" }}
        """.trimIndentMultiline(),
    )

    execute {
        icon?.let { icon ->
            // Change the app icon.
            mipmapDirectories.map { directory ->
                ResourceGroup(
                    directory,
                    *iconResourceFileNames,
                )
            }.let { resourceGroups ->
                if (icon != REVANCED_ICON) {
                    val path = File(icon)
                    val resourceDirectory = get("res")

                    resourceGroups.forEach { group ->
                        val fromDirectory = path.resolve(group.resourceDirectoryName)
                        val toDirectory = resourceDirectory.resolve(group.resourceDirectoryName)

                        group.resources.forEach { iconFileName ->
                            Files.write(
                                toDirectory.resolve(iconFileName).toPath(),
                                fromDirectory.resolve(iconFileName).readBytes(),
                            )
                        }
                    }
                } else {
                    resourceGroups.forEach { copyResources("branding/music", it) }
                }
            }

            val resourceDirectory = get("res")

            mipmapDirectories.forEach { directory ->
                val targetDirectory = resourceDirectory.resolve(directory)

                iconResourceFileNamesNew.forEach { (old, new) ->
                    val oldFile = targetDirectory.resolve("$old.png")
                    val newFile = targetDirectory.resolve("$new.png")

                    Files.write(newFile.toPath(), oldFile.readBytes())
                }
            }
        }

        appName?.let { name ->
            // Change the app name.
            val manifest = get("AndroidManifest.xml")
            manifest.writeText(
                manifest.readText()
                    .replace(
                        "android:label=\"@string/application_name",
                        "android:label=\"$name",
                    ),
            )
        }
    }
}
