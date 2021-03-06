
package org.papamitra.android.dbhelper

import java.lang.reflect.Method

trait MetaMapper[A <: Mapper[A]] extends Mapper[A] {

  case class FieldHolder(name: String, method: Method, field: MappedField[_, A])

  def dbTableName:String

  def create:A = this.getClass.getSuperclass.newInstance.asInstanceOf[A]

  /**
   * Find the magic mapper fields on the superclass
   */
  def findMagicFields(onMagic: Mapper[A], staringClass: Class[_]): List[Method] = {
    // If a class name ends in $module, it's a subclass created for scala object instances
    def deMod(in: String): String =
      if (in.endsWith("$module")) in.substring(0, in.length - 7)
      else in

    // find the magic fields for the given superclass
    def findForClass(clz: Class[_]): List[Method] = clz match {
      case null => Nil
      case c =>
        // get the fields

        val fields = Map(c.getDeclaredFields.
          filter(f => classOf[MappedField[_, _]].isAssignableFrom(f.getType)).
          map(f => (deMod(f.getName), f)): _*)

        // this method will find all the super classes and super-interfaces
        def getAllSupers(clz: Class[_]): List[Class[_]] = clz match {
          case null => Nil
          case c =>
            c :: c.getInterfaces.toList.flatMap(getAllSupers) :::
              getAllSupers(c.getSuperclass)
        }

        // does the method return an actual instance of an actual class that's
        // associated with this Mapper class
        def validActualType(meth: Method): Boolean = {
          try {
            // invoke the method
            meth.invoke(onMagic) match {
              case null =>
                //                logger.debug("Not a valid mapped field: %s".format(meth.getName))
                false
              case inst =>
                // do we get a MappedField of some sort back?
                if (!classOf[MappedField[_, _]].isAssignableFrom(inst.getClass)) false
                else {
                  // find out if the class name of the actual thing starts
                  // with the name of this class or some superclass...
                  // basically, is an inner class of this class
                  getAllSupers(clz).find { c =>
                    inst.getClass.getName.startsWith(c.getName)
                  }.isDefined
                }
            }

          } catch {
            case e =>
              //              logger.debug("Not a valid mapped field: %s, got exception: %s".format(meth.getName, e))
              false
          }
        }

        // find all the declared methods
        val meths = c.getDeclaredMethods.toList.
          filter(_.getParameterTypes.length == 0). // that take no parameters
          //          filter(m => Modifier.isPublic(m.getModifiers)). // that are public
          filter(m => fields.contains(m.getName) && // that are associated with private fields
            fields(m.getName).getType == m.getReturnType).
          filter(validActualType) // and have a validated type

        meths ::: findForClass(clz.getSuperclass)
    }

    val ret = findForClass(staringClass).distinct

    ret
  }

  val mapperAccessMethods = findMagicFields(this, this.getClass.getSuperclass)

  def fieldList = mapperAccessMethods.map(v => 
    v.invoke(this) match{
    case mf: MappedField[AnyRef, A] =>
      mf.setName(v.getName)
      FieldHolder(mf.name,v,mf)
  })

  val mappedFieldList = fieldList

}
