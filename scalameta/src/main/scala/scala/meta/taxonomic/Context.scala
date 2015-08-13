package scala.meta
package taxonomic

import org.scalameta.annotations._
import scala.annotation._
import scala.{Seq => _}
import scala.collection.immutable.Seq
import org.apache.ivy.plugins.resolver._

@opaque
@implicitNotFound("this method requires an implicit scala.meta.taxonomic.Context")
trait Context {
  def binaries(module: Module): Seq[Path]
  def sources(module: Module): Seq[Source]
  def resources(module: Module): Seq[Resource]
  def deps(module: Module): Seq[Module]
}

// NOTE: "Ivy contexts? In the platform-independent part of scala.meta? Blasphemy!".
// If you're thinking along these lines, take a look at the comments in Taxonomy.scala.

object Context {
  // NOTE: Parts of this file are originally taken from lihaoyi/ammonite:
  // https://github.com/lihaoyi/Ammonite/blob/cd5de73b5601735093f4f80a775423b7a0102b37/repl/src/main/scala/ammonite/repl/IvyThing.scala
  private lazy val defaultResolvers: Seq[DependencyResolver] = {
    def resolver(name: String) = {
      val res = new IBiblioResolver()
      res.setUsepoms(true)
      res.setM2compatible(true)
      res.setName(name)
      res.setRoot("http://repo1.maven.org/maven2/")
      res
    }
    //add duplicate resolvers with different name to make Ivy shut up
    //and stop giving `unknown resolver null` or `unknown resolver sbt-chain`
    //errors
    Seq(resolver("central"), resolver("sbt-chain"), resolver("null"))
  }
  implicit object DefaultTaxonomy extends Taxonomy(defaultResolvers: _*)
}
