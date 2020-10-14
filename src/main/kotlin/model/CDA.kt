package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CDA private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container
    lateinit var db: Container
    lateinit var sidekiq: Container
    lateinit var queue: Container
    lateinit var sqsQueue: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Court Data Adapter",
        "The laa-court-data-adaptor system is a web service that connects to LAA systems and " +
          "to HMCTS Common Platform (CP)"
      )

      api = system.addContainer(
        "Court Data Adapter API",
        "An API to allow LAA access Common Platform data",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-court-data-adaptor")
        CloudPlatform.kubernetes.add(this)
      }

      db = system.addContainer(
        "Court Data Adapter Database",
        "Stores OAuth credentials, metadata and acts a cache for some Common Platform data",
        "PostgreSQL"
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

      sqsQueue = system.addContainer(
        "SQS",
        "Used for pushing notifications from HMCTS to other LAA systems",
        "SQS"
      ).apply {
        AWSLegacy.sqs.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      api.uses(db, "Connects to")
      api.uses(sqsQueue, "Adds events to process by other systems")
      api.uses(queue, "Queues jobs to")
      sidekiq.uses(queue, "Processes queued jobs from")
    }

    override fun defineRelationships() {
      api.uses(
        MAAT.api,
        "Uses MAAT API to validate MAAT IDs before sending requests to Common Platform",
        "REST"
      )
    }

    override fun defineExternalRelationships() {
      api.uses(
        CommonPlatform.system,
        "Uses APIs to search and retreive case information and mark cases that LAA want to receive notifications for",
        "REST (w/ mTLS)"
      )
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "cda-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "cda-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
