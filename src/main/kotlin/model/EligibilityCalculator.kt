package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class EligibilityCalculator private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Civil Legal Aid Eligibility Calculator",
        "A service for assessing financial eligibility for civil legal aid"
      ).apply {
        Tags.GET_ACCESS.addTo(this)
      }

      web = system.addContainer(
        "Eligibility Calculator UI",
        "A web service for assesing legal aid eligibility",
        "Classic ASP (VBScript), C#"
      ).apply {
        setUrl("https://github.com/ministryofjustice/cla_eligibility_calculator")
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.provider.uses(web, "Assesses how much legal aid a citizen is eligible for using")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "eligibility-calculator-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "eligibility-calculator-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
