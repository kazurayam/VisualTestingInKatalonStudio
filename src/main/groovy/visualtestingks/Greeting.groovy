package com.kazurayam.visualtestingks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class Greeting extends DefaultTask {
    private String message
    private String recipient

    @Input String getMessage() { return message }
    void setMessage(String message) { this.message = message }


    @Input String getRecipient() { return recipient }
    void setRecipient(String recipient) { this.recipient = recipient}

    @TaskAction
    void sayGreeting() {
        printf("%s, %s!\n", getMessage(), getRecipient())
    }

}