package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class Xhibit private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "HMCTS Xhibit",
        "Legacy application that contains information about Court Cases in Crown Courts"
      ).apply {
        OutsideLAA.addTo(this)
        Tags.CRIME.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      system.uses(
        HUB.system,
        "Provides Crown Court Outcomes",
        "FTP",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.billingCaseWorker.uses(
        system, "Retrives hearing data from Live & Portal applications", null, null, tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
