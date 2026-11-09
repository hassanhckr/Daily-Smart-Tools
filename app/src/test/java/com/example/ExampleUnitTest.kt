package com.example

import com.example.model.ToolsData
import com.example.ui.screens.tools.UnitCategory
import com.example.ui.screens.tools.convertUnits
import com.example.ui.screens.tools.evaluateStrength
import com.example.ui.screens.tools.generateSecurePassword
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testAll12ToolsIntegrity() {
    val tools = ToolsData.allTools
    assertEquals(12, tools.size)
    assertNotNull(ToolsData.getToolById("calculator"))
    assertNotNull(ToolsData.getToolById("age_calculator"))
    assertNotNull(ToolsData.getToolById("qr_scanner"))
    assertNotNull(ToolsData.getToolById("qr_generator"))
    assertNotNull(ToolsData.getToolById("discount_calc"))
    assertNotNull(ToolsData.getToolById("percent_calc"))
    assertNotNull(ToolsData.getToolById("unit_converter"))
    assertNotNull(ToolsData.getToolById("bmi_calc"))
    assertNotNull(ToolsData.getToolById("notes"))
    assertNotNull(ToolsData.getToolById("expense"))
    assertNotNull(ToolsData.getToolById("password_gen"))
    assertNotNull(ToolsData.getToolById("date_calc"))
  }

  @Test
  fun testUnitConversions() {
    // 1 km to meters
    val meters = convertUnits(1.0, "Kilometers", "Meters", UnitCategory.Length)
    assertEquals(1000.0, meters, 0.001)

    // 1 kg to grams
    val grams = convertUnits(1.0, "Kilograms", "Grams", UnitCategory.Weight)
    assertEquals(1000.0, grams, 0.001)

    // 100 Celsius to Fahrenheit
    val fahrenheit = convertUnits(100.0, "Celsius", "Fahrenheit", UnitCategory.Temperature)
    assertEquals(212.0, fahrenheit, 0.01)

    // 1 Liter to ml
    val ml = convertUnits(1.0, "Liters", "Milliliters", UnitCategory.Volume)
    assertEquals(1000.0, ml, 0.001)
  }

  @Test
  fun testPasswordGenerator() {
    val pwd = generateSecurePassword(
      length = 16,
      upper = true,
      lower = true,
      numbers = true,
      symbols = true,
      excludeConfusing = false
    )
    assertEquals(16, pwd.length)
    val strength = evaluateStrength(pwd)
    assertNotNull(strength.label)
  }

  @Test
  fun testPasswordExcludesConfusing() {
    val pwd = generateSecurePassword(
      length = 30,
      upper = true,
      lower = true,
      numbers = true,
      symbols = false,
      excludeConfusing = true
    )
    assertFalse(pwd.contains('l'))
    assertFalse(pwd.contains('1'))
    assertFalse(pwd.contains('I'))
    assertFalse(pwd.contains('0'))
    assertFalse(pwd.contains('O'))
  }
}
