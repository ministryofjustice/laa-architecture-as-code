package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class ProviderCMS private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Provider CMS",
        "Provider's Content Management System. This is unique to each Provider."
      ).apply {
        OutsideLAA.addTo(this)
        Tags.CRIME.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      system.uses(
        CCCD.web,
        "Forwards the claim to",
        "REST",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
      LegalAidAgencyUsers.provider.uses(
        system, "Submits a claim", null, null, tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
