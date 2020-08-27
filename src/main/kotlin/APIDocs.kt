package uk.gov.justice.laa.architecture

import com.structurizr.model.Element

class APIDocs(url: String) {
  val url: String = url

  fun addTo(element: Element) {
    element.addProperty("api-docs-url", url)
  }
}
