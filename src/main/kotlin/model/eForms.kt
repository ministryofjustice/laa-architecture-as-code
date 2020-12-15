package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class eForms private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "eForms",
        "Electronic form submission. Historical paper forms digitised and made available to legal providers."
      ).apply {
        Tags.CRIME.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      system.uses(
        BenefitChecker.api,
        "Checks if applicant receives passported benefit",
        "SOAP",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.provider.uses(
        system, "Applies for Legal Aid on behalf of a Citizen", null, null, tagsToArgument(Tags.CRIME)
      )
      LegalAidAgencyUsers.crimeApplicationCaseWorker.uses(
        system,
        "Accepts/rejects an application",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "eforms-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "eforms-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
