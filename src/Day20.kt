fun main() {
    val input = readInput("Day20")
    val broadCaster = BroadCaster(input)
    broadCaster.hitTheButton(1000).println()
    broadCaster.hitTheButtonUntilRx().println() // bruteforce cause long computing
}

class BroadCaster(configuration: List<String>) : Module(configuration, mutableMapOf(), "broadcaster", "") {
    fun hitTheButton(times: Int): Long {
        var countHigh = 0L
        var countLow = 0L
        repeat(times) {
            var events = sendPulse(false)
            while (events.isNotEmpty()) {
                val highs = events.count(Event::pulse)
                countHigh += highs
                countLow += events.size - highs
                events = events.flatMap { event ->
                    event.target.receivePulse(event.pulse, event.source)
                }
            }
        }
        return countHigh * (countLow + times)
    }

    fun hitTheButtonUntilRx(): Long {
        var countPresses = 0L
        var rxLowPulse = false
        while (!rxLowPulse) {
            countPresses++
            var events = sendPulse(false)
            while (events.isNotEmpty()) {
                rxLowPulse = events.any { event -> !event.pulse && event.target.name == "rx" }
                events = events.flatMap { event ->
                    event.target.receivePulse(event.pulse, event.source)
                }
            }
        }
        return countPresses
    }
}

class FlipFlop(configuration: List<String>, modules: MutableMap<String, Module>, name: String) :
    Module(configuration, modules, name, "%") {
    var on = false

    override fun receivePulse(pulse: Boolean, name: String): List<Event> {
        return if (!pulse) {
            on = !on
            sendPulse(on)
        } else {
            emptyList()
        }

    }
}

class Conjunction(configuration: List<String>, modules: MutableMap<String, Module>, name: String) :
    Module(configuration, modules, name, "&") {
    var state: MutableMap<String, Boolean>? = null

    override fun connect(name: String) {
        if (state == null) state = mutableMapOf()
        state!![name] = false
    }

    override fun receivePulse(pulse: Boolean, name: String): List<Event> {
        state!![name] = pulse
        return sendPulse(!state!!.all { it.value })
    }
}

class TheOminousRxMachine(name: String) : Module(emptyList(), mutableMapOf(), name, "(% || &).not()")

abstract class Module(
    configuration: List<String>,
    modules: MutableMap<String, Module>,
    val name: String,
    typePrefix: String
) {
    companion object {
        fun createModule(
            configuration: List<String>,
            modules: MutableMap<String, Module>,
            name: String
        ) = configuration.find { it.contains("$name -> ") }?.let { configLine ->
            if (configLine.startsWith("%")) FlipFlop(configuration, modules, name)
            else Conjunction(configuration, modules, name)
        } ?: TheOminousRxMachine(name)
    }

    init {
        modules[name] = this
    }

    private val connectedModules = configuration.find { it.startsWith("$typePrefix$name -> ") }?.let { configLine ->
        val moduleNames = configLine.removePrefix("$typePrefix$name -> ").split(", ")
        moduleNames.map { moduleName ->
            val module = modules[moduleName] ?: createModule(configuration, modules, moduleName)
            module.connect(name)
            module
        }
    } ?: emptyList()

    fun sendPulse(pulse: Boolean): List<Event> {
        return connectedModules.map {
            Event(name, it, pulse)
        }
    }

    open fun connect(name: String) {}

    open fun receivePulse(pulse: Boolean, name: String): List<Event> = emptyList()
}

data class Event(val source: String, val target: Module, val pulse: Boolean) {
    override fun toString() = "$source -> ${if (pulse) "HIGH" else "LOW"} -> ${target.name}"
}