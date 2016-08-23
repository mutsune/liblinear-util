package com.mutsune.liblinear.util

import java.io.File

import com.mutsune.util.io.File.FileOps

/**
  * Created by nakayama.
  */
final case class LiblinearFormatter(liblinearInstances: Seq[LiblinearInstance], liblinearMapper: LiblinearMapper) {

    lazy val liblinearTrainFormatFeatures: String = liblinearInstances.map { li =>
        liblinearMapper.classLabelIndex(li.classLabel) + " " + li.features.toList.map {
            case (k, v) => (liblinearMapper.featureNameIndex(k), v)
        }.sortBy(_._1.toInt).map {
            // label feature1:true feature2:false => 1 1:1 2:0
            case (k, v) => k.toString + ":" + v
        }.mkString(" ")
    }.mkString("\n")

    def liblinearTrainFormatWithFeatureName: String = liblinearInstances.map { li =>
        li.classLabel + " " + li.features.map {
            // label feature1:1 feature2:0
            case (featureName, v) => featureName + ":" + v
        }.mkString(" ")
    }.mkString("\n")

    def saveAsLiblinearTrainFormat(file: File): Unit =
        FileOps.writeWithMakeParentDirs(file)(_.println(liblinearTrainFormatFeatures))

}
