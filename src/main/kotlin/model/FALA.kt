package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class FALA private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var laalaa: Container
    lateinit var db: Container
    lateinit var celery: Container
    lateinit var queue: Container
    lateinit var callCentreOperator: Person
    lateinit var managementInformationTeam: Person

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Find a Legal Adviser",
        "Search for a legal adviser or family mediator with a legal aid contract in England and Wales"
      )

      web = system.addContainer(
        "Find a Legal Adviser UI",
        "Interface to search for legal advisers in an area",
        "Django"
      ).apply {
        Tags.WEB_BROWSER.addTo(this)
        setUrl("https://github.com/ministryofjustice/fala")
      }

      laalaa = system.addContainer(
        "Legal Aid Agency Legal Adviser API",
        "A JSON API for looking up legal advisers",
        "Flask"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-legal-adviser-api")
      }

      db = system.addContainer(
        "Provider address database",
        "Stores provider address details with latitude and longitude coordinates",
        "PostgreSQL with PostGIS")
      .apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      celery = system.addContainer("Celery", "Listens to queued events and processes them", "Celery").apply {
        CloudPlatform.kubernetes.add(this)
      }

      queue = system.addContainer("Queue", "Key-value store used for scheduling jobs via Celery", "Redis").apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.elasticache.add(this)
      }

      callCentreOperator = model.addPerson(
        "Call centre operator",
        "Contact centre personnel who signposts members of the public in their legal help queries"
      )
      managementInformationTeam = model.addPerson("Management Information Team")
    }

    override fun defineInternalContainerRelationships() {
      web.uses(laalaa, "Performs legal adviser searches through", "REST")
      laalaa.uses(db, "connects to")
      laalaa.uses(queue, "Queues postcode lookup jobs to")
      celery.uses(queue, "Processes queued postcode lookup jobs from")
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
      laalaa.uses(PostcodesIO.system, "Looks up postcode latitude and longitude from", "REST")
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.citizen.uses(web, "Looks for nearby legal advisers using")
      LegalAidAgencyUsers.provider.uses(web, "Looks for nearby legal advisers for citizens using")
      callCentreOperator.uses(web, "Looks for nearby legal advisers for citizens using")
      managementInformationTeam.uses(web, "Logs in every month and updates legal provider details")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "fala-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "fala-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
