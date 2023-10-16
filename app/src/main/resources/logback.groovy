import ch.qos.logback.core.ConsoleAppender

def environment = System.getenv().getOrDefault("SENTRY_ENVIRONMENT", "dev")

def defaultLevel = INFO
def defaultTarget = "System.err"

if (environment == "dev") {
    defaultLevel = DEBUG
    defaultTarget = "System.out"

    logger("com.zaxxer.hikari", INFO)
    logger("org.eclipse.jetty", INFO)
}

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%boldGreen(%d{yyyy-MM-dd}) %boldYellow(%d{HH:mm:ss}) %gray(|) %highlight(%5level) %gray(|) %boldMagenta(%40.40logger{40}) %gray(|) %msg%n"

        withJansi = true
    }

    target = defaultTarget
}

root(defaultLevel, ["CONSOLE"])
