package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ContainerView
import com.structurizr.view.ViewSet

class VCD private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var applicationCaseWorker: Person
    lateinit var billingCaseWorker: Person

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "View Court Data", 
        "The laa-court-data-ui system is a web service that Application and Billing case workers use to interact " +
          "with the Courts"
      )

      web = system.addContainer(
        "View Court Data UI",
        "Interface for Application and Billing case workers access the Court's data",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-court-data-ui")
        Tags.WEB_BROWSER.addTo(this)
      }

      val db = system.addContainer("View Court Data Database", "Stores user details for the application", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }
      web.uses(db, "connects to")

      val sidekiq = system.addContainer("Sidekiq", "Listens to queued events and processes them", "Sidekiq").apply {
        CloudPlatform.kubernetes.add(this)
      }
      val queue = system.addContainer("Queue", "Key-value store used for scheduling jobs via Sidekiq", "Redis").apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.elasticache.add(this)
      }
      sidekiq.uses(queue, "processes queued jobs from")
      web.uses(queue, "queues feedback jobs to")

      applicationCaseWorker = model.addPerson(
        "Legal aid application case worker", 
        "Manages applications for criminal legal aid"
      )
      billingCaseWorker = model.addPerson("Legal aid billing case workers", "Verifies legal aid provider's bills")
    }

    override fun defineRelationships() {
      // // user relationships
      applicationCaseWorker.uses(web, "Searches and links/unlinks defendants to MAAT")
      billingCaseWorker.uses(web, "Searches and inspects defendants' case hearing history")
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "vcd-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "vcd-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
