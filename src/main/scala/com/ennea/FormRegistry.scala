package com.ennea

import java.util.UUID

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

final case class Form(questions: Map[String, String] = Map.empty[String, String])
final case class FormResult(uuid: UUID = UUID.randomUUID(),
                            results: Map[String, String] = Map.empty[String, String])

object FormRegistry {

  sealed trait Command
  final case class GetForm(replyTo: ActorRef[Form]) extends Command
  final case class PostForm(form: Form, replyTo: ActorRef[FormResult]) extends Command
  final case class GetFormResult(replyTo: ActorRef[FormResult]) extends Command

  def apply(): Behavior[Command] = registry()

  private def registry(): Behavior[Command] = {
    Behaviors.receiveMessage {
      case GetForm(replyTo) =>
        replyTo ! Form()
        Behaviors.same

      case PostForm(form, replyTo) =>
        println(s"Form is ${form}")
        replyTo ! FormResult()
        Behaviors.same

      case GetFormResult(replyTo) =>
        replyTo ! FormResult()
        Behaviors.same
    }

  }

}
