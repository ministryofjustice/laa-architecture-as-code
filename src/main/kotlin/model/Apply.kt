package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ContainerView
import com.structurizr.view.ViewSet

class Apply private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var db: Container
    lateinit var sidekiq: Container
    lateinit var queue: Container
    lateinit var clamAV: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Apply",
        "The laa-apply-for-legal-aid system is a web service by use for solicitors providing legal aid services to " +
          "enter applications for legal aid on-line"
      ).apply {
        Tags.GET_LEGAL_AID.addTo(this)
      }

      web = system.addContainer(
        "Apply UI",
        "Interface for providers and legal aid applicants to apply for legal aid",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-apply-for-legal-aid")
        Tags.WEB_BROWSER.addTo(this)
      }

      db = system.addContainer("Apply Database", "Stores applications for legal aid", "PostgreSQL").apply {
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

      clamAV = system.addContainer("ClamAV", "Clam AntiVirus virus scanner", "ClamAV").apply {
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      web.uses(db, "Connects to")
      sidekiq.uses(queue, "Processes queued jobs from")
      web.uses(queue, "Queues feedback jobs to")
      web.uses(clamAV, "Scans attachments with")
    }

    override fun defineRelationships() {
      web.uses(CCMS.system, "Gets provider details and reference data from and submits application through")
      web.uses(CCMS.providerDetailsAPI, "Gets provider details from", "REST")
      web.uses(CCMS.soa, "Gets reference data and submits legal aid application through", "SOAP")
      web.uses(CFE.api, "Checks applicant financial eligibility through", "REST")
      web.uses(LFA.api, "Checks legal aid application requirements", "REST")
      web.uses(BenefitChecker.api, "Checks if applicant receives passported benefit through", "SOAP")
      web.uses(Portal.system, "Authenticates users through", "SAML")
    }

    override fun defineExternalRelationships() {
      web.uses(Geckoboard.system, "Sends metrics to", "REST")
      web.uses(TrueLayer.system, "Gets applicant bank information from", "REST")
      web.uses(GOVUKNotify.system, "Sends email using", "REST")
      web.uses(OSPlacesAPI.system, "Gets address data from", "REST")
      web.uses(BankHolidaysAPI.system, "Gets UK bank holiday dates from", "REST")
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.citizen.uses(web, "Applies for legal aid using")
      LegalAidAgencyUsers.citizen.uses(TrueLayer.system, "Gives bank access authorisation to")
      LegalAidAgencyUsers.provider.uses(web, "Fills legal aid application through")
      GOVUKNotify.system.delivers(LegalAidAgencyUsers.citizen, "Sends email to")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "apply-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "apply-container", null).apply {
        addDefaultElements()
        renderInternalCcmsContainers(this)
        enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 400, 100)
      }
    }

    private fun renderInternalCcmsContainers(view: ContainerView) {
      view.remove(CCMS.system)
      listOf(CCMS.soa, CCMS.providerDetailsAPI).forEach { view.add(it) }
    }
  }
}
