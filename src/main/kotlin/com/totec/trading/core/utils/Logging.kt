/**
 * Created by ndrsh on 7/28/20
 */

package com.totec.trading.core.utils

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.RollingFileAppender
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.layout.PatternLayout


lateinit var logger: Logger

fun setUpLogger(production: Boolean = false, level: Level = Level.ALL, name: String = "Totec Logger") {
	val ctx = LogManager.getContext(false) as LoggerContext
	val config: Configuration = ctx.configuration
	
	val layout = PatternLayout.newBuilder()
		.withConfiguration(config)
		.withPattern("%d{HH:mm:ss.SSS} %msg %n")
		.build()
	
	val rollingFileAppender = (RollingFileAppender::class.java.getMethod("newBuilder").invoke(null) as RollingFileAppender.Builder<*>)
		.setConfiguration(config)
		.setName(name)
		.setLayout(layout)
		.withFilePattern("log/$name-%d{yy-MM-dd}.log")
		.withPolicy(TimeBasedTriggeringPolicy
			            .newBuilder()
			            .withModulate(true) // guarantees that next file starts at 00:00 and not 1 day after app start
			            .withInterval(1)
			            .build())
		.build()
	
	rollingFileAppender.start()
	config.addAppender(rollingFileAppender)
	val loggerConfig = config.getLoggerConfig(name)
	loggerConfig.addAppender(rollingFileAppender, null, null)
	loggerConfig.level = level
	
	if (production) {
		loggerConfig.removeAppender("DefaultConsole-2")
	} else {
		loggerConfig.removeAppender(name)
	}
	logger = LogManager.getLogger(name)
}
