package com.isit322.plant_tracker_test

import com.isit322.plant_tracker.Validation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class InstrumentTest {
//    @Rule
//    @JvmField
//    val rule: ActivityScenarioRule<PlantView> = ActivityScenarioRule(PlantView::class.java)

    @Test
    fun enter_name_field() {
        val desc = "Sun flower"
        val result = Validation.returnPlantObjectName(desc)
        assertEquals(result, desc)

    }

    @Test
    fun enter_description_field() {
        val desc = "two"
        val result = Validation.returnPlantObjectDescription(desc)
        assertEquals(result, desc)
    }
}