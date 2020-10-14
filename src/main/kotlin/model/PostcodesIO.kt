package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class PostcodesIO private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "postcodes.io",
        "API for looking up information about UK postcodes"
      ).apply {
        OutsideLAA.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers here
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
    }
  }
}
