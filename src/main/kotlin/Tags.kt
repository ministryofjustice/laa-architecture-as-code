package uk.gov.justice.laa.architecture

import com.structurizr.model.Element

enum class Tags : addTo {
  DATABASE,
  WEB_BROWSER,
  PROVIDER {
    override fun addTo(element: Element) {
      super.addTo(element)
      OutsideLAA.addTo(element)
    }
  };

  // Usage example: Tags.DATABASE.addTo(any_model_element)
  override fun addTo(element: Element) {
    element.addTags(this.toString())
  }
}

fun tagsToArgument(vararg tags: Tags):Array<String> {
  val stringedTags =  arrayListOf<String>()
  tags.forEach { stringedTags.add(it.toString()) }

  return stringedTags.toTypedArray()
}
