package com.chuckerteam.chucker.api

import android.content.Context
import android.content.Intent
import androidx.annotation.IntDef
import com.appspiriment.kashproxy.di.KashProxyLib
import com.chuckerteam.chucker.internal.support.ChuckerCrashHandler
import com.chuckerteam.chucker.internal.support.NotificationHelper
import com.chuckerteam.chucker.internal.ui.MainActivity

/**
 * Chucker methods and utilities to interact with the library.
 */
public object Chucker {

    @Deprecated("This variable will be removed in 4.x release")
    public const val SCREEN_HTTP: Int = 1

    @Deprecated("This variable will be removed in 4.x release")
    public const val SCREEN_ERROR: Int = 2

    /**
     * Check if this instance is the operation one or no-op.
     * @return `true` if this is the operation instance.
     */
    @Suppress("MayBeConst ") // https://github.com/ChuckerTeam/chucker/pull/169#discussion_r362341353
    public val isOp: Boolean = true

    /**
     * Get an Intent to launch the Chucker UI directly.
     * @param context An Android [Context].
     * @param screen The [Screen] to display: SCREEN_HTTP or SCREEN_ERROR.
     * @return An Intent for the main Chucker Activity that can be started with [Context.startActivity].
     */
    @Deprecated(
        "This fun will be removed in 4.x release",
        ReplaceWith("Chucker.getLaunchIntent(context)"),
        DeprecationLevel.WARNING
    )
    @JvmStatic
    public fun getLaunchIntent(context: Context, @Screen screen: Int): Intent {
        return getLaunchIntent(context).putExtra(MainActivity.EXTRA_SCREEN, screen)
    }

    /**
     * Get an Intent to launch the Chucker UI directly.
     * @param context An Android [Context].
     * @return An Intent for the main Chucker Activity that can be started with [Context.startActivity].
     */
    @JvmStatic
    public fun getLaunchIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * Configure the default crash handler of the JVM to report all uncaught [Throwable] to Chucker.
     * You may only use it for debugging purpose.
     *
     * @param collector the ChuckerCollector
     */
    @Deprecated(
        "This fun will be removed in 4.x release",
        ReplaceWith(""),
        DeprecationLevel.WARNING
    )
    @JvmStatic
    public fun registerDefaultCrashHandler(collector: ChuckerCollector) {
        Thread.setDefaultUncaughtExceptionHandler(ChuckerCrashHandler(collector))
    }

    /**
     * Method to dismiss the Chucker notification of HTTP Transactions
     */
    @Deprecated(
        "This fun will be removed in 4.x release",
        ReplaceWith("Chucker.dismissNotifications(context)"),
        DeprecationLevel.WARNING
    )
    @JvmStatic
    public fun dismissTransactionsNotification(context: Context) {
        NotificationHelper(context).dismissTransactionsNotification()
    }

    /**
     * Method to dismiss the Chucker notification of Uncaught Errors.
     */
    @Deprecated(
        "This fun will be removed in 4.x release",
        ReplaceWith("Chucker.dismissNotifications(context)"),
        DeprecationLevel.WARNING
    )
    @JvmStatic
    public fun dismissErrorsNotification(context: Context) {
        NotificationHelper(context).dismissErrorsNotification()
    }

    /**
     * Dismisses all previous Chucker notifications.
     */
    @JvmStatic
    public fun dismissNotifications(context: Context) {
        NotificationHelper(context).dismissNotifications()
    }

    public fun showChuckerActivity(context: Context) {
        getLaunchIntent(context).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.let{
            context.startActivity(it)
        }
    }

    public fun showMappingActivity(context: Context) {
        KashProxyLib.showMappingActivity(context)
    }

    public fun getMappingLaunchIntent(context: Context): Intent = KashProxyLib.getLaunchIntent(context)

    public fun enableKashProxyMapping(context: Context, enabled: Boolean) {
        KashProxyLib.enableKashProxyMapping(context, enabled)
    }

    public fun isKashProxyMappingEnabled(context: Context): Boolean =
        KashProxyLib.isKashProxyMapping(context)


    /**
     * Annotation used to specify which screen of Chucker should be launched.
     */
    @Deprecated("This param will be removed in 4.x release")
    @IntDef(value = [SCREEN_HTTP, SCREEN_ERROR])
    public annotation class Screen


}
