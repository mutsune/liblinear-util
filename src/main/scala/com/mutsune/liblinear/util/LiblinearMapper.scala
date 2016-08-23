package com.mutsune.liblinear.util

import java.io.File

import com.mutsune.util.io.File.FileOps

import scala.collection.mutable
import scala.io.Source

/**
  * Created by nakayama.
  */
final class LiblinearMapper(val classLabelIndex: Map[String, Int],
                            val featureNameIndex: Map[String, Int]) {

    def classIndexToClassLabel(classIndex: Int): Option[String] = {
        classLabelIndex.foreach {
            case (l, i) => if (i == classIndex) return Some(l)
        }
        None
    }

    def saveClassLabelIndex(file: File) = {
        FileOps.writeWithMakeParentDirs(file)(_.println(classLabelIndex.map {
            case (classLabel, classIndex) => classLabel + "\t" + classIndex
        }.mkString("\n")))
    }

    def saveFeatureNameIndex(file: File) = {
        FileOps.writeWithMakeParentDirs(file)(_.println(featureNameIndex.map {
            case (featureName, featureIndex) => featureName + "\t" + featureIndex
        }.mkString("\n")))
    }

}

object LiblinearMapper {

    val indexRegExp = """(.+)\t(\d+)""".r

    def apply(liblinearInstances: Seq[LiblinearInstance]): LiblinearMapper = {
        val classLabelIndex = liblinearInstances.foldLeft(mutable.Set[String]()) { (z, li) =>
            z += li.classLabel
        }.zipWithIndex.map { case (c, i) => (c, i + 1) }.toMap
        val featureNameIndex = liblinearInstances.foldLeft(mutable.Set[String]()) { (z, li) =>
            z ++= li.features.keys
        }.zipWithIndex.map { case (c, i) => (c, i + 1) }.toMap

        new LiblinearMapper(classLabelIndex, featureNameIndex)
    }

    def apply(classLabelIndexFile: File, featureNameIndexFile: File) = {
        val classLabelIndex = Source.fromFile(classLabelIndexFile).getLines.collect {
            case indexRegExp(classLabel, classIndex) => classLabel -> classIndex.toInt
        }.toMap
        val featureNameIndex = Source.fromFile(featureNameIndexFile).getLines.collect {
            case indexRegExp(featureName, featureIndex) => featureName -> featureIndex.toInt
        }.toMap

        new LiblinearMapper(classLabelIndex, featureNameIndex)
    }

}
