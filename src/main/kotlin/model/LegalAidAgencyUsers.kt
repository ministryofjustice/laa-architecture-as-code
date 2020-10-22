package uk.gov.justice.laa.architecture

import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet

class LegalAidAgencyUsers private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var citizen: Person
    lateinit var provider: Person
    lateinit var crimeApplicationCaseWorker: Person
    lateinit var billingCaseWorker: Person

    override fun defineModelEntities(model: Model) {
      citizen = model.addPerson(Location.External, "A member of the public in England and Wales", null)
      provider = model.addPerson(Location.External, "Legal Aid Provider", null)

      crimeApplicationCaseWorker = model.addPerson(
        Location.Internal,
        "Legal aid crime application case worker",
        "Manages applications for criminal legal aid"
      )
      billingCaseWorker = model.addPerson(
        Location.Internal,
        "Legal aid billing case workers",
        "Verifies legal aid provider's bills"
      )
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
