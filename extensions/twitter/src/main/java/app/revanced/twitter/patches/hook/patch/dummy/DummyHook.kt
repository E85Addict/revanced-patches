package app.revanced.extension.twitter.patches.hook.patch.dummy

import app.revanced.extension.twitter.patches.hook.json.BaseJsonHook
import app.revanced.extension.twitter.patches.hook.json.JsonHookPatch
import org.json.JSONObject

/**
 * Dummy hook to reserve a register in [JsonHookPatch.hooks] list.
 */
object DummyHook : BaseJsonHook() {
    override fun apply(json: JSONObject) {
        // Do nothing.
    }
}
