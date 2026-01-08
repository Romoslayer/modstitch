package dev.isxander.modstitch.shadow.loom

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.isxander.modstitch.base.loom.LoomType
import dev.isxander.modstitch.shadow.ShadowCommonImpl
import dev.isxander.modstitch.shadow.devlib
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.named

class ShadowLoomImpl(private val type: LoomType) : ShadowCommonImpl<Nothing>() {
    override fun configureShadowTask(
        target: Project,
        shadowTask: TaskProvider<ShadowJar>,
        shadeConfiguration: NamedDomainObjectProvider<Configuration>,
    ) {
        super.configureShadowTask(target, shadowTask, shadeConfiguration)
        if (type == LoomType.Remap){

            target.tasks.named<RemapJarTask>("remapJar") {
                dependsOn(shadowTask)

                // change the input from jar to shadowJar
                inputFile = shadowTask.flatMap { it.archiveFile }

                // this is the final jar, so this should have no classifier
                archiveClassifier = ""
            }
        } /*else {
            target.tasks.named<Jar>("Jar") {
                dependsOn(shadowTask)

                // change the input from jar to shadowJar
                inputFile = shadowTask.flatMap { it.archiveFile }

                // this is the final jar, so this should have no classifier
                archiveClassifier = ""
            }
        }*/


        shadowTask {
            archiveClassifier = "dev-fat"
            devlib()
        }
    }
}