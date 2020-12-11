package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class VCD private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var db: Container
    lateinit var sidekiq: Container
    lateinit var queue: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "View Court Data",
        "The laa-court-data-ui system is a web service that Application and Billing case workers use to interact " +
          "with the Courts"
      ).apply {
        Tags.CRIME.addTo(this)
        Tags.GET_PAID.addTo(this)
      }

      web = system.addContainer(
        "View Court Data UI",
        "Interface for Application and Billing case workers access the Court's data",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-court-data-ui")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }

      db = system.addContainer(
        "View Court Data Database", "Stores user details for the application", "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      sidekiq = system.addContainer("Sidekiq", "Listens to queued events and processes them", "Sidekiq").apply {
        CloudPlatform.kubernetes.add(this)
      }

      queue = system.addContainer("Queue", "Key-value store used for scheduling jobs via Sidekiq", "Redis").apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.elasticache.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      web.uses(db, "Connects to")
      web.uses(queue, "Queues feedback jobs to")
      sidekiq.uses(queue, "Processes queued jobs from")
    }

    override fun defineRelationships() {
      web.uses(
        CDA.api,
        "Provides interface to HMCTS Common Platform to access Court case and hearing information",
        "REST",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.crimeApplicationCaseWorker.uses(
        web, "Searches and links/unlinks defendants to MAAT", null, null, tagsToArgument(Tags.CRIME)
      )
      LegalAidAgencyUsers.billingCaseWorker.uses(
        web, "Searches and inspects defendants' case hearing history", null, null, tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineViews(views: ViewSet) {
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
