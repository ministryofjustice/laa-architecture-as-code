package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class ExternalDemo private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "External Demo",
        "This system is a demonstration for things outside LAA, delete me"
      ).apply {
        OutsideLAA.addTo(this)
      }
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers here
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
    }
  }
}
