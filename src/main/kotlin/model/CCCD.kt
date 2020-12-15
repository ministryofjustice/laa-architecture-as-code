package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CCCD private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var sqsCCR: Container
    lateinit var sqsCCLF: Container
    lateinit var sqsProcessResponse: Container
    lateinit var sns: Container
    lateinit var redis: Container
    lateinit var sidekiq: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Claim for crown court defence",
        "Service for the remunerating of advocates and litigators by the Legal Aid Agency for work " +
          "done on behalf of defendants in criminal proceedings."
      ).apply {
        Tags.CRIME.addTo(this)
        Tags.GET_PAID.addTo(this)
      }

      web = system.addContainer(
        "Claim for crown court defence UI",
        "Interface for Providers to submit claims and for Billing case workers access claims",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/Claim-for-Crown-Court-Defence")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }

      db = system.addContainer(
        "Claim for crown court defence Database", "Stores claims for Crown Court cases", "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      sidekiq = system.addContainer("Sidekiq", "Listens to queued events and processes them", "Sidekiq").apply {
        CloudPlatform.kubernetes.add(this)
      }
      redis = system.addContainer(
        "In Memory Data Store", "Key-value store used for scheduling jobs via Sidekiq and Caching", "Redis"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.elasticache.add(this)
      }

      sns = system.addContainer("SNS Filter", "Simple Notification Service", "AWS").apply {
        CloudPlatform.sns.add(this)
      }
      sqsCCR = system.addContainer("SQS CCR", "Simple Queue Service", "AWS").apply {
        CloudPlatform.sqs.add(this)
      }
      sqsCCLF = system.addContainer("SQS CCLF", "Simple Queue Service", "AWS").apply {
        CloudPlatform.sqs.add(this)
      }
      sqsProcessResponse = system.addContainer("SQS Response", "Simple Queue Service", "AWS").apply {
        CloudPlatform.sqs.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      web.uses(db, "Connects to")
      web.uses(redis, "Caches API Calls")
      web.uses(redis, "Queues jobs to")
      web.uses(sns, "Pushes claim when it has been received")
      web.uses(sqsProcessResponse, "Processes job to see if claim is successful or not")

      sidekiq.uses(redis, "Processes queued jobs from")

      sns.uses(sqsCCR, "Queues job for CCR to process claim")
      sns.uses(sqsCCLF, "Queues job for CCLF to process claim")
    }

    override fun defineRelationships() {
      web.uses(
        FeeCalculator.api,
        "Uses API to calculator fees",
        "REST",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.billingCaseWorker.uses(
        web, "Starts a claim assessment", null, null, tagsToArgument(Tags.CRIME)
      )
      LegalAidAgencyUsers.provider.uses(
        web, "Submits a claim", null, null, tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "cccd-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "cccd-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
