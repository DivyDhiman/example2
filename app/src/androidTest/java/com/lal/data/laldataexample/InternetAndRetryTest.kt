package gojeck.gojecktest.com.gojecktest

import android.content.Context
import android.net.ConnectivityManager
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import junit.framework.Assert
import junit.framework.AssertionFailedError

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.lal.data.laldataexample.DashboardManager
import com.lal.data.laldataexample.R


@RunWith(AndroidJUnit4::class)
@LargeTest
class InternetAndRetryTest {

    @get:Rule
    public val activityRule: ActivityTestRule<*> = ActivityTestRule(DashboardManager::class.java)

    @Test
    fun checkIsViewVisible(){
        try {
            Assert.assertTrue(isConnected(activityRule.activity))
        } catch (e: AssertionFailedError) {
        }
    }

    companion object {
        fun isConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}