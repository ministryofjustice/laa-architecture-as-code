package uk.gov.justice.laa.architecture

import com.structurizr.view.Shape
import com.structurizr.view.Styles

fun defineStyles(styles: Styles) {
  styles.addElementStyle("Software System").background("#aabbdd")
  styles.addElementStyle("Person").shape(Shape.Person).background("#aabbdd")

  styles.addElementStyle(Tags.DATABASE.toString()).shape(Shape.Cylinder)

  styles.addElementStyle(Tags.WEB_BROWSER.toString()).shape(Shape.WebBrowser)

  styles.addElementStyle(Tags.PROVIDER.toString()).background("#ccff99")
  styles.addElementStyle(Tags.DEPRECATED.toString()).background("#999999")
}
