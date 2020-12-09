package uk.gov.justice.laa.architecture

import com.structurizr.view.AutomaticLayout
import com.structurizr.view.FilterMode
import com.structurizr.view.ViewSet

fun defineFilteredViews(tags: List<Tags>, views: ViewSet) {

  tags.forEach {
    val name = it.toString()
    val landscapeView = views.createSystemLandscapeView("x-" + name.toLowerCase(), "Elements by $name").apply {
      addAllElements()
    }

    views.createFilteredView(
      landscapeView,
      name,
      "$name applications",
      FilterMode.Include,
      name
    ).apply {
      view.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 400, 400, 100, false)
    }
  }
}
