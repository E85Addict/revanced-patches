package app.revanced.patches.music.misc.androidauto

import app.revanced.util.exception
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.music.misc.androidauto.fingerprints.CheckCertificateFingerprint


@Patch(
    name = "Bypass certificate checks",
    description = "Bypasses certificate checks which prevent YouTube Music from working on Android Auto.",
    compatiblePackages = [
        CompatiblePackage(
            "com.google.android.apps.youtube.music",
            [
                "6.45.54",
                "6.51.53",
                "7.01.53",
                "7.02.52",
                "7.03.52",
                "7.08.54",
                "7.10.52",
                "7.11.51",
                "7.13.53",
                "7.14.52",
            ]
        )
    ]
)
@Suppress("unused")
object BypassCertificateChecksPatch : BytecodePatch(setOf(CheckCertificateFingerprint)) {
    override fun execute(context: BytecodeContext) {
        CheckCertificateFingerprint.result?.apply {
            mutableMethod.addInstructions(
                0, """
                const/4 v0, 0x1
                return v0
                """
            )
        } ?: throw CheckCertificateFingerprint.exception
    }
}
