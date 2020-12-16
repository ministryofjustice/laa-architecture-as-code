package uk.gov.justice.laa.architecture

import com.structurizr.view.Shape
import com.structurizr.view.Border
import com.structurizr.view.Styles

fun defineStyles(styles: Styles) {
  styles.addElementStyle("Software System").background("#f3f2f1")
  styles.addElementStyle("Person").shape(Shape.Person).background("#aabbdd")

  styles.addElementStyle(Tags.EXTERNAL_USER.toString()).background("#f3f2f1")
  styles.addElementStyle(Tags.DATABASE.toString()).shape(Shape.Cylinder)
  styles.addElementStyle(Tags.WEB_BROWSER.toString()).shape(Shape.WebBrowser)
  styles.addElementStyle(Tags.LEGACY.toString()).background("#1d70b8").color("#ffffff")
  styles.addElementStyle(Tags.LIVE.toString()).background("#aabbdd")
  styles.addElementStyle(Tags.BETA.toString()).background("#aabbdd")
  styles.addElementStyle(Tags.ALPHA.toString()).background("#ffffff").border(Border.Dotted)
}
