package gojeck.gojecktest.com.gojecktest

import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.app.FragmentTransaction
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.lal.data.laldataexample.DashboardManager
import com.lal.data.laldataexample.R
import fragment.MapFragment
import fragment.MockRepoFragment
import staticdata.Config


@RunWith(AndroidJUnit4::class)
@LargeTest
class RecyclerViewTest{

    @get:Rule
    public val activityRule: ActivityTestRule<DashboardManager> = ActivityTestRule(DashboardManager::class.java)

    val fragmentInstance: MockRepoFragment
    get(){
        val activity = activityRule.activity as DashboardManager
        val transaction = activity.supportFragmentManager.beginTransaction()
        val mockRepoFragment = MockRepoFragment()
        transaction.replace(R.id.fragment_container, mockRepoFragment, Config.MOCK_REPO_FRAGMENT)
        transaction.commit()

        return mockRepoFragment
    }

    val fragmentInstanceNew: MapFragment
        get(){
            val activity = activityRule.activity as DashboardManager
            val transaction = activity.supportFragmentManager.beginTransaction()
            val mapFragment = MapFragment()
            transaction.replace(R.id.fragment_container, fragmentInstanceNew, Config.MOCK_REPO_FRAGMENT)
            transaction.commit()

            return mapFragment
        }

    private val rvCount: Int
    get() {
        val recyclerView = activityRule.activity.findViewById<View>(R.id.mockListRecyclerView) as RecyclerView
        return recyclerView.adapter!!.itemCount
    }

    @Test
    fun fragmentCanBeInstantiated(){
        if (rvCount > 0) {
            activityRule.activity.runOnUiThread { val trendingRepoFragment = fragmentInstance }
            // Then use Espresso to test the Fragment
            onView(withId(R.id.mockListRecyclerView)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkScrollAndClick() {
        onView(withId(R.id.mockListRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(12, click()))
    }

    @Test
    fun checkRecyclerviewData() {
        if (rvCount > 0) {
            onView(withId(R.id.mockListRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))

            activityRule.activity.runOnUiThread { val trendingRepoFragment = fragmentInstanceNew }
            // Then use Espresso to test the Fragment
            onView(withId(R.id.frg)).check(matches(isDisplayed()))
        }
    }
}