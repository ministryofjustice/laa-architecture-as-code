package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class ProviderTraining private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Provider training",
        "A website with training, guidance and support for providers"
      ).apply {
        url = "https://legalaidlearning.justice.gov.uk/"
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineExternalRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.provider.uses(system, "Learns how to use CCMS")
    }

    override fun defineViews(views: ViewSet) {
    }

  }
}
