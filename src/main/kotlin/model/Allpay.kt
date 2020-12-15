package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class Allpay private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "allpay",
        "Direct Debit payment processor"
      ).apply {
        OutsideLAA.addTo(this)
        Tags.CRIME.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      system.uses(LegalAidAgencyUsers.provider, "Pays", null, null, tagsToArgument(Tags.CRIME))
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}