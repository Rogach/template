package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import auth._
import sitemap._
import Loc._

import fulmar._

class Boot {
  def boot {
    LiftRules.addToPackages("fulmar")
    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))    
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    val pages = List(
      Menu("Main") / "index"
    )
    LiftRules.setSiteMap(SiteMap(pages:_*))
    
    // no-ajax mode
    // LiftRules.autoIncludeAjax = session => false

    // add some spying functions :)
    LiftRules.onBeginServicing.append { req =>
      Main.log("request: %s?%s, %s", 
        req.path.wholePath.mkString("/"),
        req.params.flatMap { case (k,v) => v.map(k + "=" + _)}.mkString("&"),
        req.headers.find(_._1 == "Range"))
    }

    Main.log("Lift is running in %s mode", Props.mode)

  }
}
