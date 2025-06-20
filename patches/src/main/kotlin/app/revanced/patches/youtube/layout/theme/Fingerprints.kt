package app.revanced.patches.youtube.layout.theme

import app.revanced.patcher.fingerprint
import app.revanced.util.literal
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal val lithoThemeFingerprint = fingerprint {
    accessFlags(AccessFlags.PROTECTED, AccessFlags.FINAL)
    returns("V")
    parameters("Landroid/graphics/Rect;")
    opcodes(
        Opcode.IGET,
        Opcode.IF_EQZ,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IF_NEZ,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.RETURN_VOID,
    )
    custom { method, _ ->
        method.name == "onBoundsChange"
    }
}

internal const val GRADIENT_LOADING_SCREEN_AB_CONSTANT = 45412406L

internal val useGradientLoadingScreenFingerprint = fingerprint {
    literal { GRADIENT_LOADING_SCREEN_AB_CONSTANT }
}

internal const val SPLASH_SCREEN_STYLE_FEATURE_FLAG = 269032877L

internal val splashScreenStyleFingerprint = fingerprint {
    returns("V")
    parameters("Landroid/os/Bundle;")
    literal { SPLASH_SCREEN_STYLE_FEATURE_FLAG }
    custom { method, classDef ->
        method.name == "onCreate" && classDef.endsWith("/MainActivity;")
    }
}
