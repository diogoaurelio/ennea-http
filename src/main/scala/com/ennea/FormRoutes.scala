package com.ennea

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout


import com.ennea.FormRegistry.GetForm
import com.ennea.FormRegistry.PostForm


class FormRoutes(formRegistry: ActorRef[FormRegistry.Command])
                (implicit val system: ActorSystem[_]) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getForm(): Future[Form] = formRegistry.ask(GetForm)

  def createForm(form: Form): Future[FormResult] = formRegistry.ask(PostForm(form, _))

  val formRoutes: Route = {
    pathPrefix("form") {
        pathEnd {
          concat(
            get {
              complete(getForm())
            }
/*
            ,
            post {
              entity(as[Form]) { form =>
                onSuccess(createForm(form)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            }
*/
          )
        }
    }
  }

}
