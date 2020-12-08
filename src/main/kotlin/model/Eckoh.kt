package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class Eckoh private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Eckoh",
        "Card payment telephone contact centre"
      ).apply {
        OutsideLAA.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
      system.uses(
        Barclaycard.system,
        "Sends payment requests to"
      )
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.citizen.uses(system, "Telephone calls and makes payment by giving card details")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
