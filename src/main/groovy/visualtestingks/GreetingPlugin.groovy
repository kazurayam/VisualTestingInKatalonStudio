package com.kazurayam.visualtestingks

import org.gradle.api.Plugin
import org.gradle.api.Project

class GreetingPlugin implements Plugin<Project> {
    public void apply(Project project) {
        def extension = project.extensions.create('greeting', GreetingPluginExtension)
        project.task('hello') {
            message = 'Hello'
            recipient = 'World'
            doLast {
                println extension.message
            }
        }
    }
}