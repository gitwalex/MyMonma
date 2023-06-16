package com.gerwalex.mymonma.ext

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Regisriert ActivityForResult für StartActivityForResult
 *
 * @param onActivityResult Resulthandler
 */
fun ActivityResultCaller.registerActivityForResult(onActivityResult: ActivityForResultUtil.OnActivityResult<ActivityResult>): ActivityForResultUtil.ActivityResultUtil<Intent, ActivityResult> {
    return registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        onActivityResult
    )
}

/**
 * Regisriert ActivityForResult
 *
 * @param contract der ActivityResultContract.
 *
 * @param onActivityResult Resulthandler
 */
fun <Input, Result> ActivityResultCaller.registerForActivityResult(
    contract: ActivityResultContract<Input, Result>,
    onActivityResult: ActivityForResultUtil.OnActivityResult<Result>
): ActivityForResultUtil.ActivityResultUtil<Input, Result> {
    return ActivityForResultUtil.ActivityResultUtil(this, contract, onActivityResult)
}


class ActivityForResultUtil {
    class ActivityResultUtil<Input, Result>(
        caller: ActivityResultCaller,
        contract: ActivityResultContract<Input, Result>,
        private val onActivityResult: OnActivityResult<Result>,
    ) {

        private val launcher: ActivityResultLauncher<Input>

        init {
            launcher =
                caller.registerForActivityResult(contract) { result -> callOnActivityResult(result) }
        }

        private fun callOnActivityResult(result: Result) {
            onActivityResult.onActivityResult(result)
        }

        /**
         * Executes an {@link ActivityResultContract}.
         *
         * <p>This method throws {@link android.content.ActivityNotFoundException}
         * if there was no Activity found to run the given Intent.
         * @param input the input required to execute an {@link ActivityResultContract}.
         *
         * @throws android.content.ActivityNotFoundException
         */
        fun launch(input: Input) {
            launcher.launch(input)
        }
    }

    /**
     * Callback interface
     */
    fun interface OnActivityResult<O> {

        /**
         * Called after receiving a result from the target activity
         */
        fun onActivityResult(result: O)
    }
}