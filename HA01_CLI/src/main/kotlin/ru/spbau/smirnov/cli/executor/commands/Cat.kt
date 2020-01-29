package ru.spbau.smirnov.cli.executor.commands

import ru.spbau.smirnov.cli.executor.Streams
import java.io.*

/**
 * Cat command.
 *
 * Concatenates files listed in `arguments` to `streams.outputStream`
 * If `arguments` is empty, prints only `streams.inputStream`
 */
class Cat(arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        val output = DataOutputStream(streams.outputStream)
        if (arguments.isEmpty()) {
            try {
                output.writeBytes(String(DataInputStream(streams.inputStream).readBytes()))
            } catch (e: IOException) {
                streams.errorStream.println("Error in cat while reading from inputStream!\n${e.message}")
                return 1
            }
        } else {
            for (filename in arguments) {
                try {
                    FileInputStream(filename).use {
                        output.writeBytes(String(DataInputStream(it).readBytes()))
                    }
                } catch (e: IOException) {
                    streams.errorStream.println("Error in cat while reading file $filename!\n${e.message}")
                    return 1
                }
            }
        }
        return 0
    }
}