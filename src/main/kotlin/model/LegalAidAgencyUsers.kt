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
      citizen = model.addPerson(
        Location.External,
        "A member of the public in England or Wales",
        null
      ).apply {
        Tags.EXTERNAL_USER.addTo(this)
      }

      provider = model.addPerson(
        Location.External,
        "Legal Aid Provider",
        null
      ).apply {
        Tags.EXTERNAL_USER.addTo(this)
        Tags.CRIME.addTo(this)
      }

      crimeApplicationCaseWorker = model.addPerson(
        Location.Internal,
        "Crime application caseworker",
        "A caseworker who manages applications for criminal legal aid"
      ).apply {
        Tags.CRIME.addTo(this)
      }

      billingCaseWorker = model.addPerson(
        Location.Internal,
        "Billing caseworker",
        "A caseworker who verifies legal aid provider's bills"
      ).apply {
        Tags.CRIME.addTo(this)
      }

      meansCaseWorker = model.addPerson(
        Location.Internal,
        "Means caseworker",
        "A caseworker who assesses legal aid applications for means"
      )

      meritsCaseWorker = model.addPerson(
        Location.Internal,
        "Merits caseworker",
        "A caseworker who assesses legal aid applications for merits"
      )

      directServicesTeam = model.addPerson(
        Location.Internal,
        "Direct Services Team",
        "Maintains the Civil Legal Aid operator relationship and performance"
      )

      contactCentreOperator = model.addPerson(
        Location.Internal,
        "Contact Centre Operator",
        "Contact centre personel who signposts members of the public in their legal help queries"
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
