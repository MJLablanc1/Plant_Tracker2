package com.isit322.plant_tracker_test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.isit322.plant_tracker.PlantView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentTest {
    @Rule
    @JvmField
    val rule: ActivityScenarioRule<PlantView> = ActivityScenarioRule(PlantView::class.java)

    @Test
    fun enter_name_field() {

    }

    @Test
    fun enter_description_field() {

    }
}