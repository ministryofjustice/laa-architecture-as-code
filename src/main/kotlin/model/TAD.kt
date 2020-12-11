package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class TAD private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var tad: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Tender Assessment Database",
        "A system for managing tender assessments"
      ).apply {
        Tags.GET_ACCESS.addTo(this)
        Tags.LEGACY.addTo(this)
      }

      tad = system.addContainer(
        "Tender Asssessment Database",
        "Web application for managing tender assessments, replaced by the Tender Verification (TV) system",
        "Oracle Application Express (APEX)"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-apex")
        Tags.WEB_BROWSER.addTo(this)
      }

      db = system.addContainer(
        "TAD Database",
        "The TAD schema stores data for all of the Oracle APEX applications at the LAA",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      tad.uses(db, "Connects to")
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "tad-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300)
      }

      views.createContainerView(system, "tad-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
