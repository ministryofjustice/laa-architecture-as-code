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
    lateinit var meansCaseWorker: Person
    lateinit var meritsCaseWorker: Person
    lateinit var directServicesTeam: Person
    lateinit var contactCentreOperator: Person

    override fun defineModelEntities(model: Model) {
      citizen = model.addPerson(Location.External, "A member of the public in England and Wales", null)
      provider = model.addPerson(Location.External, "Legal Aid Provider", null)

      meansCaseWorker = model.addPerson(Location.Internal, "Legal Aid means case worker", null)
      meritsCaseWorker = model.addPerson(Location.Internal, "Legal Aid merits case worker", null)

      crimeApplicationCaseWorker = model.addPerson(
        Location.Internal,
        "Legal Aid crime application case worker",
        "Manages applications for criminal legal aid"
      )

      billingCaseWorker = model.addPerson(
        Location.Internal,
        "Legal Aid billing case worker",
        "Verifies legal aid provider's bills"
      )

      directServicesTeam = model.addPerson(
        Location.Internal,
        "Direct Services Team",
        "Maintains the Civil Legal Aid operator relationship and performance"
      )

      contactCentreOperator = model.addPerson(
        Location.Internal,
        "Contact Centre Operator",
        "Contact centre personell who signposts members of the public in their legal help queries"
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
