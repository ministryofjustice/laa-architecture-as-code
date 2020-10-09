package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class CommonPlatform private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "HMCTS Common Platform",
        "Contains information about Court Cases in Magistrates and Crown Courts"
      ).apply {
        OutsideLAA.addTo(this)
      }
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers here
      system.uses(CDA.api, "Provides notifications when Hearings have resulted", "REST (w/ mTLS)")
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
    }
  }
}
