import com.google.inject._

import play.api.Application

object Global extends play.api.GlobalSettings {
  var injector: Injector = null
  override def onStart(application: Application) = {
    injector = Guice.createInjector()
  }
  override def getControllerInstance[A](aClass: Class[A]) = injector.getInstance(aClass)

}

