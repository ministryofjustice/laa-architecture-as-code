package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

fun defineGlobalViews(views: ViewSet) {
  views.createSystemLandscapeView("system-overview", "All systems").apply {
    addAllSoftwareSystems()
    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
  }
}
