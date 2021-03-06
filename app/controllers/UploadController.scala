package controllers

import com.google.inject.Inject
import play.api.mvc._
import service.UploadService

class UploadController @Inject() (uploadService: UploadService)
  extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>

      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      if(isSupportedFormat(filename))
      {
        uploadService.readExcel(picture.ref.file);
        Ok("File uploaded")
      }
      else
      {
        Redirect(routes.UploadController.index).flashing(
          "error" -> "wrong format file")
      }
    }.getOrElse {
      Redirect(routes.UploadController.index).flashing(
        "error" -> "Missing file")
    }
  }

  def isSupportedFormat(fileName: String) : Boolean = {
    return fileName contains "xls"
    }

}
