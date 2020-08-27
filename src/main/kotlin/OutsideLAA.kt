package uk.gov.justice.laa.architecture

import com.structurizr.model.Element
import com.structurizr.model.Location
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem

class OutsideLAA {
  companion object : addTo {
    override fun addTo(element: Element) {
      when (element) {
        is SoftwareSystem -> element.setLocation(Location.External)
        is Person -> element.setLocation(Location.External)
      }
    }
  }
}
