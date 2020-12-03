package uk.gov.justice.laa.architecture

import com.structurizr.view.AutomaticLayout
import com.structurizr.view.FilterMode
import com.structurizr.view.ViewSet

fun defineFilteredViews(tags: List<Tags>, views: ViewSet) {
  val systemLandscapeView = views.createSystemLandscapeView("System Landscape", "All systems + people").apply {
    addAllElements()
  }

  tags.forEach {
    views.createFilteredView(
      systemLandscapeView,
      it.toString().toLowerCase(),
      "All applications filtered by " + it.toString().toLowerCase(),
      FilterMode.Include,
      it.toString()
    ).apply {
      view.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 400, 400, 100, false)
    }
  }
}
